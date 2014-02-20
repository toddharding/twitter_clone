package org.tclone.servlets;


import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Todd on 17/02/14.
 */
@WebServlet(name = "AuthorisationServlet",urlPatterns = {"/auth"})
public class AuthorisationServlet extends HttpServlet
{
	public class AuthObject
	{
		boolean isLoggedIn = false;
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		AuthObject authObject = new AuthObject();
		Gson gson = new Gson();

		response.setContentType("application/json");
		response.getOutputStream().print(gson.toJson(authObject));
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{

	}
}
