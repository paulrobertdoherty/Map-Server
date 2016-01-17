package com.map.servlets;

import java.io.*;
import java.util.Base64;
import javax.servlet.http.*;
import org.json.*;
import com.map.*;

public class Get extends HttpServlet {
	private static final long serialVersionUID = 67476219933239534L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		PrintWriter writer = response.getWriter();
		String q = request.getParameter("q");
		if (q == null) {
			writer.println("ERROR: q has no value.");
			writer.close();
			return;
		}
		ResultBundle[] results = Map.getResults(q.toLowerCase());
		writer.println("STATUS: " + results.length + " results!");
		for (ResultBundle rb : results) {
			JSONObject j = new JSONObject();
			j.put("thumbnail", Base64.getEncoder().encodeToString(rb.getThumbnail()));
			j.put("width", rb.getWidth());
			j.put("height", rb.getHeight());
			j.put("title", rb.getTitle());
			j.put("description", rb.getDescription());
			j.put("url", rb.getUrl());
			writer.println(j.toString());
		}
		writer.close();
	}
}
