package org.tclone.servlets;

import com.datastax.driver.core.utils.UUIDs;
import com.google.gson.Gson;
import org.mindrot.jbcrypt.BCrypt;
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
		try
		{
			response.setContentType("application/json");
			try
			{
				Gson gson = new Gson();
				User user = new User();
				user.generateID();
				user.username = request.getParameter("username");
				user.real_name = request.getParameter("real_name");
				user.email = request.getParameter("email");
				user.password = BCrypt.hashpw(request.getParameter("password"), BCrypt.gensalt(12));
				user.language = request.getParameter("language");
				user.timezone = request.getParameter("timezone");
				user.country = request.getParameter("country");
				user.website = request.getParameter("website");
				user.bio = request.getParameter("bio");
				user.facebook_link = request.getParameter("facebook_link");
				user.tailored_ads = Boolean.parseBoolean(request.getParameter("tailored_ads"));
				user.generateApiKey();

				UserDao userDao = new UserDao();


				if(userDao.create(user))
				{
					response.setStatus(HttpServletResponse.SC_OK);
					response.getOutputStream().print(gson.toJson(user));
				}
				else
				{
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				}

			}
			catch (Exception e)
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


	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{

	}


	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
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
						userDao.delete(user);
						response.setStatus(HttpServletResponse.SC_OK);
					}
					else
					{
						response.setStatus(HttpServletResponse.SC_NOT_FOUND);
					}

				}
				catch (Exception e)
				{
					response.setStatus(HttpServletResponse.SC_NOT_FOUND);
					System.out.println(e.getMessage());
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
					else
					{
						response.setStatus(HttpServletResponse.SC_NOT_FOUND);
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
