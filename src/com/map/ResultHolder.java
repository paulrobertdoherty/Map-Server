package com.map;

import java.io.*;

public class ResultHolder implements Serializable {
	private static final long serialVersionUID = -5345652947981962510L;
	
	private Object least, greatest;
	
	/**
	 * A constructor for creating a result holder.
	 * @param least The result with the lowest quantity.  Must be a ResultHolder, Result, or String.
	 * @param greatest The result with the greatest quantity.  Must be a ResultHolder, Result, or String.
	 */
	public ResultHolder(Object least, Object greatest) {
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
	
	public void add(Result r) {
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
			} else {
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
		
		public String pointer;
		public int quality;

		public Pointer(Result r) {
			pointer = Long.toHexString(r.id);
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
				try {
					f.createNewFile();
					fo = new FileOutputStream(f);
					o = new ObjectOutputStream(fo);
					o.writeObject(r);
					o.close();
					fo.close();
				} catch (IOException e2) {
					e2.printStackTrace();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}