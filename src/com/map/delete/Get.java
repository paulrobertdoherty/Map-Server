package com.map.delete;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

import com.map.*;

public class Get extends HttpServlet {
	private static final long serialVersionUID = 67476219933239534L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		PrintWriter writer = response.getWriter();
		ResultBundle[] results = Map.getResults(request.getParameter("q"));
		for (ResultBundle rb : results) {
			writer.append("");
		}
		writer.close();
	}
}
