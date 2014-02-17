package org.tclone;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Servlet implementation class DeleteSession
 */
@WebServlet("/DeleteSession")
public class DeleteSession extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	/**
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		HttpSession session = request.getSession();
		if (session != null)
		{
			response.getOutputStream().println("Invalidating session " + session.getId());
			session.invalidate();
			response.getOutputStream().println("Done");
		}
	}



}
