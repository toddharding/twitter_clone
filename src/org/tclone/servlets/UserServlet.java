package org.tclone.servlets;

import com.google.gson.Gson;
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
 * Created by Todd on 18/02/14.
 */
@WebServlet(name = "UserServlet", urlPatterns = {"/user", "/user/*"})
public class UserServlet extends HttpServlet
{
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try
		{
			response.setContentType("application/json");
			String[] args = request.getPathInfo().split("/");
			if(args.length == 2)
			{
				UUID id = UUID.fromString(args[1]);
				try
				{
					Gson gson = new Gson();
					User user = null;
					UserDao userDao = new UserDao();

					user = userDao.retrieve(id);

					if(user != null)
					{
						response.setStatus(HttpServletResponse.SC_OK);
						response.getOutputStream().print(gson.toJson(user));
					}

				}
				catch (Exception e)
				{
					response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				}
			}
			else
			{
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			}
		}
		catch (Exception e)
		{
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			System.out.println(e.getMessage());
		}
	}
}
