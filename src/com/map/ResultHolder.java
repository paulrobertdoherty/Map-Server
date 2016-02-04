package com.map;

import java.io.*;
import java.util.*;

public class ResultHolder implements Serializable {
	private static final long serialVersionUID = -5345652947981962510L;
	
	private Object least, greatest;
	
	/**
	 * A constructor for creating a result holder.
	 * @param least The result with the lowest quantity.  Must be a ResultHolder, Result, or String.
	 * @param greatest The result with the greatest quantity.  Must be a ResultHolder, Result, or String.
	 */
	public ResultHolder(Object least, Object greatest) throws IOException {
		if (least == null || least instanceof ResultHolder) {
			this.least = least;
		} else if (least instanceof Result) {
			this.least = new Pointer((Result)least);
		} else {
			throw new IllegalArgumentException("The least value was not a Result or a ResultHolder.");
		}
		
		if (greatest == null || greatest instanceof ResultHolder) {
			this.greatest = greatest;
		} else if (greatest instanceof Result) {
			this.greatest = new Pointer((Result)greatest);
		} else {
			throw new IllegalArgumentException("The greatest value was not a Result or a ResultHolder.");
		}
	}
	
	public ResultHolder(Object least, Object greatest, boolean lowLevel) {
		this.least = least;
		this.greatest = greatest;
	}
	
	@Override
	public String toString() {
		return "[" + least + ", " + greatest + "]";
	}

	public void add(Result r) throws IOException {
		//Just occurred to me that I didn't comment any of this..
		if (least == null) {
			if (greatest == null) {
				greatest = new Pointer(r);
			} else {
				if (((Pointer)greatest).quality > r.quality) {
					least = new Pointer(r);
				} else {
					least = greatest;
					greatest = new Pointer(r);
				}
			}
		} else if (greatest == null) {
			if (((Pointer)least).quality < r.quality) {
				greatest = new Pointer(r);
			} {
				greatest = least;
				least = new Pointer(r);
			}
		} else {
			if (least instanceof ResultHolder) {
				if (greatest instanceof ResultHolder) {
					if (((ResultHolder)least).getGreatest().quality < ((ResultHolder)greatest).getLeast().quality) {
						((ResultHolder)greatest).add(r);
					} else {
						((ResultHolder)least).add(r);
					}
				} else {
					if (((Pointer)greatest).quality > r.quality) {
						((ResultHolder)least).add(r);
					} else {
						greatest = new ResultHolder(greatest, r);
					}
				}
			} else {
				if (greatest instanceof ResultHolder) {
					if (((ResultHolder)greatest).getLeast().quality < r.quality) {
						((ResultHolder)greatest).add(r);
					} else {
						((ResultHolder)least).add(r);
					}
				} else {
					if (r.quality > ((Pointer)least).quality) {
						if (r.quality > ((Pointer)greatest).quality) {
							greatest = new ResultHolder(greatest, r);
						} else {
							least = new ResultHolder(least, r);
						}
					} else {
						least = new ResultHolder(r, least);
					}
				}
			}
		}
	}
	
	private static boolean hasId(long id, Result[] blacklist) {
		for (Result r : blacklist) {
			if (id == r.id) {
				return true;
			}
		}
		return false;
	}
	
	public static Result[] getRandom(int size, Result[] blacklist, String currentQuery, ResultHolder rh) throws IOException, ClassNotFoundException {
		Result[] r = new Result[size];
		long gId = Result.getId();
		long maxId = gId <= 0 ? 1 : gId;
		Random random = new Random();
		for (int i = 0; i < size; i++) {
			while (true) {
				long id = random.nextLong() % maxId;
				if (!hasId(id, blacklist)) {
					r[i] = rh.getResult(id, rh, false);
					Map.addResult(r[i], currentQuery);
					break;
				}
			}
		}
		return r;
	}
	
	public List<Result> get(List<Result> l, int size, Object result) throws IOException, ClassNotFoundException {
		if (result instanceof ResultHolder) {
			ResultHolder r = (ResultHolder)result;
			if (r.greatest() == null) {
				if (r.least() == null) {
					return l;
				}
				
				l.add(getResult((Pointer)r.least(), r, true));
				return l;
			} else if (r.greatest() instanceof Pointer) {
				l.add(getResult((Pointer)r.greatest(), r, true));
				if (size > 1) {
					Object p = r.least();
					if (p instanceof Pointer) {
						l.add(getResult((Pointer)p, r, false));
						return l;
					}
					return get(l, size - 1, r.least());
				}
				return l;
			} else {
				if (size > 1) {
					return get(l, size - 1, r.greatest());
				}
				return l;
			}
		} else {
			Pointer p = (Pointer)result;
			l.add(getResult(p, null, false));
			return l;
		}
	}
	
	public void setLeast(Object least) {
		this.least = least;
	}

	public void setGreatest(Object greatest) {
		this.greatest = greatest;
	}
	
	private static Result getResult(long pointer) throws IOException, ClassNotFoundException {
		FileInputStream fileIn = new FileInputStream(Result.RESULT_PATH + pointer + ".ser");
        ObjectInputStream in = new ObjectInputStream(fileIn);
        Result r = (Result) in.readObject();
        in.close();
        fileIn.close();
        return r;
	}
	
	private static ArrayList<Pointer> getList(ResultHolder rh, ArrayList<Pointer> l) {
		ArrayList<Pointer> list = l;
		if (rh.least() != null) {
			if (rh.least() instanceof Pointer) {
				list.add((Pointer)rh.least());
			} else {
				list = getList((ResultHolder)rh.least(), list);
			}
			if (rh.greatest() instanceof Pointer) {
				list.add((Pointer)rh.greatest());
			} else {
				list = getList((ResultHolder)rh.greatest(), list);
			}
		} else if (rh.greatest() != null) {
			if (rh.greatest() instanceof Pointer) {
				list.add((Pointer)rh.greatest());
			} else {
				list = getList((ResultHolder)rh.greatest(), list);
			}
		}
		return list;
	}
	
	private static ArrayList<Pointer> getList(ResultHolder rh) {
		return getList(rh, new ArrayList<Pointer>());
	}
	
	private Object clean(ArrayList<Pointer> array, int start, int end) {
		int dif = end - start;
		int halfEnd = ((end - start) / 2) + start;
		if (dif == array.size()) {
			least = clean(array, start, end / 2);
			greatest = clean(array, end / 2, end);
		} else if (dif >= 4) {
			return new ResultHolder(
					clean(array, start, halfEnd),
					clean(array, halfEnd, end), true);
		} else if (dif == 3) {
			return new ResultHolder(
					clean(array, start, halfEnd),
					array.get(end - 1), true);
		} else if (dif == 2) {
			return new ResultHolder(array.get(start), array.get(end - 1), true);
		} else {
			return array.get(start);
		}
		return null;
	}
	
	/**
	 * Changes the ResultHolder and cleans it.  Be warned, it's slow and should be run in a separate thread whenever possible.
	 * @param id
	 * @param r
	 * @param rh
	 * @throws IOException 
	 */
	public void set(long id, Result r, ResultHolder rh) throws IOException {
		ArrayList<Pointer> list = getList(rh);
		if (r != null) {
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).pointer == id) {
					list.set(i, new Pointer(r));
				}
			}
		} else {
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).pointer == id) {
					list.remove(i);
				}
			}
		}
		clean(list, 0, list.size());
	}
	
	private Result getResult(long pointer, ResultHolder rh, boolean greatest) throws IOException, ClassNotFoundException {
		Result r = getResult(pointer);
		//Do when click on link
		/*
        r.quality++;
		r.lifetime--;
		if (r.lifetime > 0)
			set(r.id, r, rh);
		*/
		return r;
	}

	/**
	 * Gets a Result object from a pointer object.
	 * @param p
	 * @param rh
	 * @param greatest False for least, true for greatest
	 * @return Result
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private Result getResult(Pointer p, ResultHolder rh, boolean greatest) throws IOException, ClassNotFoundException {
		return getResult(p.pointer, rh, greatest);
	}
	
	public Object least() {
		return least;
	}
	
	public Object greatest() {
		return greatest;
	}

	private Pointer getLeast() {
		if (least instanceof Pointer) {
			return (Pointer)least;
		}
		return ((ResultHolder)least).getLeast();
	}

	private Pointer getGreatest() {
		if (greatest instanceof Pointer) {
			return (Pointer)greatest;
		}
		return ((ResultHolder)greatest).getGreatest();
	}
	
	public class Pointer implements Serializable {
		private static final long serialVersionUID = -8801937352742752935L;
		
		public long pointer;
		public int quality;

		public Pointer(Result r) throws IOException {
			pointer = r.id;
			quality = r.quality;
			File f = new File(Result.RESULT_PATH + pointer + ".ser");
			ObjectOutputStream o;
			FileOutputStream fo;
			try {
				fo = new FileOutputStream(f);
				o = new ObjectOutputStream(fo);
				o.writeObject(r);
				o.close();
				fo.close();
			} catch (FileNotFoundException e) {
				f.createNewFile();
				fo = new FileOutputStream(f);
				o = new ObjectOutputStream(fo);
				o.writeObject(r);
				o.close();
				fo.close();
			}
		}
		
		public String toString() {
			return "(" + pointer + ")";
		}
	}
}