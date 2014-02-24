package org.tclone.servlets;


import com.google.gson.Gson;
import com.sun.xml.internal.messaging.saaj.util.Base64;
import org.tclone.dao.UserDao;
import org.tclone.entities.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by Todd on 17/02/14.
 */
@WebServlet(name = "AuthorisationServlet",urlPatterns = {"/auth"})
public class AuthorisationServlet extends HttpServlet
{
	public class AuthObject
	{
		boolean isValidUser = false;
		UUID	id;
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String auth = request.getHeader("Authorization");
		if(auth == null)
		{
			// not auth header redirect
			HttpServletResponse httpResponse = (HttpServletResponse) response;
			//httpResponse.sendRedirect("/index.jsp");
			httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
		}
		else
		{
			String[] authHeader = auth.split(" ");
			String encodedValue = auth.split(" ")[1];
			System.out.println("authHeader Length: " + authHeader.length );
			for(String s : authHeader)
			{
				System.out.println("authHeader Contents: " + s );
			}
			if(authHeader.length > 1 || authHeader.length < 2 )
			{
				if(authHeader[0].toLowerCase().contains("basic"))
				{
					// do basic authentication
					System.out.println("Base64-encoded Authorization Value: <em>" + encodedValue);
					String decodedValue = Base64.base64Decode(encodedValue);
					System.out.println("</em><br/>Base64-decoded Authorization Value: <em>" + decodedValue);
					System.out.println("</em>");
					String username = decodedValue.split(":")[0];
					String password = decodedValue.split(":")[1];

					UserDao userDao = new UserDao();
					User user = userDao.retrieve(username);
					if(user == null)
					{
						response.setStatus(HttpServletResponse.SC_NOT_FOUND);
					}
					else
					{
						AuthObject authObject = new AuthObject();
						Gson gson = new Gson();
						response.setContentType("application/json");
						if(user.authenticate(password))
						{
							response.setStatus(HttpServletResponse.SC_OK);
							authObject.isValidUser = true;
							authObject.id = user.id;
							response.getOutputStream().print(gson.toJson(authObject));
						}
						else
						{
							authObject.isValidUser = false;
							response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
							response.getOutputStream().print(gson.toJson(authObject));
						}
					}
				}
			}
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{

	}
}
