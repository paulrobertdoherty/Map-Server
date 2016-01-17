package com.map.servlets;

import java.io.*;
import java.util.Base64;
import javax.servlet.http.*;
import com.map.*;

public class Add extends HttpServlet {
	private static final long serialVersionUID = -3655788427730951449L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		PrintWriter writer = response.getWriter();
		try {
			long id = Map.addResult(
					request.getParameter("title"),
					request.getParameter("description"),
					Base64.getDecoder().decode(request.getParameter("thumbnail")),
					request.getParameter("url"),
					request.getParameter("queries").split(","),
					Integer.parseInt(request.getParameter("width")),
					Integer.parseInt(request.getParameter("height")),
					//TODO Payments
					Integer.parseInt(request.getParameter("lifetime")));
			writer.println("Submmitted successfully with an ID of " + Long.toHexString(id));
		} catch (NumberFormatException e) {
			writer.println("ERROR: The number parsing failed.  In ohter words, " + e.getMessage());
		} catch (ClassNotFoundException | IOException e) {
			writer.println("ERROR: The server done goofed.  Otherwise known as " + e.getMessage());
		} catch (NullPointerException e) {
			writer.println("ERROR: One of the parameters has no value.  Otherwise known as " + e.getMessage());
		}
		writer.close();
	}
}
