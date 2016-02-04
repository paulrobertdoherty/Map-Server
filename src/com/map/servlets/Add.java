package com.map.servlets;

import java.io.*;
import javax.servlet.http.*;
import com.map.*;

public class Add extends HttpServlet {
	private static final long serialVersionUID = -3655788427730951449L;
	
	private String removeScript(String s) {
		return s.replace("<script>", "").replace("</script>", "").replace("<iframe>", "").replace("</iframe>", "").replace("<frame>", "").replace("</frame>", "");
	}
	
	private String getStackTrace(Exception e) {
		StringWriter s = new StringWriter();
		e.printStackTrace(new PrintWriter(s));
		return s.toString();
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/plain");
		PrintWriter writer = response.getWriter();
		try {
			long id = Map.addResult(
					removeScript(request.getParameter("title")),
					removeScript(request.getParameter("description")),
					request.getParameter("thumbnail"),
					removeScript(request.getParameter("url")),
					request.getParameter("queries").split("##"),
					//TODO Payments
					Integer.parseInt(request.getParameter("lifetime")));
			writer.println("Submmitted successfully with an ID of " + Long.toHexString(id));
		} catch (NumberFormatException e) {
			writer.println("ERROR: The number parsing failed.  Otherwise known as \n" + getStackTrace(e));
		} catch (ClassNotFoundException | IOException e) {
			writer.println("ERROR: The server done goofed.  Otherwise known as \n" + getStackTrace(e));
		} catch (NullPointerException e) {
			writer.println("ERROR: One of the parameters has no value.  Otherwise known as \n" + getStackTrace(e));
		}
		writer.close();
	}
}
