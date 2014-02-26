package org.tclone.servlets;

import com.datastax.driver.core.utils.UUIDs;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.mindrot.jbcrypt.BCrypt;
import org.tclone.dao.UserDao;
import org.tclone.entities.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.IOException;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Todd on 18/02/14.
 */
@WebServlet(name = "UserServlet", urlPatterns = {"/user", "/user/*"})
public class UserServlet extends HttpServlet
{

	private static Validator validator;

	@Override
	public void init() throws ServletException
	{
		ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
		validator = validatorFactory.getValidator();
		super.init();
	}

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
				user.country = request.getParameter("country");
				user.website = request.getParameter("website");
				user.bio = request.getParameter("bio");
				user.tailored_ads = Boolean.parseBoolean(request.getParameter("tailored_ads"));
				user.generateApiKey();

				Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);
				UserDao userDao = new UserDao();
				boolean isEmailUnique = userDao.isEmailUnique(user);
				JsonObject errors = new JsonObject();
				JsonObject userJson = new JsonObject();
				if(constraintViolations.size() > 0 || isEmailUnique == false)
				{

					if(isEmailUnique == false)
					{
						errors.addProperty("email", "email is not unique");
					}
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

					for(ConstraintViolation<User> c : constraintViolations)
					{
						errors.addProperty(c.getPropertyPath().toString(), c.getMessage());
					}
					JsonObject responseJson = new JsonObject();
					responseJson.add("validation_errors", errors);
					response.getOutputStream().print(responseJson.toString());
				}
				else
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
		try
		{
			response.setContentType("application/json");
			String[] args = request.getPathInfo().split("/");
			if(args.length == 2)
			{
				UUID id = UUID.fromString(args[1]);
				try
				{
					UserDao userDao = new UserDao();
					Gson gson = new Gson();
					User userOld = userDao.retrieve(id);
					User user = new User();
					if(userOld == null)
					{
						response.setStatus(HttpServletResponse.SC_NOT_FOUND);
					}
					else
					{
						user.id = userOld.id;
						user.username = userOld.username;
						user.api_key = userOld.api_key;
						user.real_name = request.getParameter("real_name");
						user.email = request.getParameter("email");
						user.password = BCrypt.hashpw(request.getParameter("password"), BCrypt.gensalt(12));
						user.language = request.getParameter("language");
						user.country = request.getParameter("country");
						user.website = request.getParameter("website");
						user.bio = request.getParameter("bio");
						user.tailored_ads = Boolean.parseBoolean(request.getParameter("tailored_ads"));

						Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);

						//user.generateApiKey();
						boolean isEmailUnique = userDao.isEmailUnique(user);
						JsonObject errors = new JsonObject();
						JsonObject userJson = new JsonObject();
						if(constraintViolations.size() > 0 || isEmailUnique == false)
						{

							if(isEmailUnique == false)
							{
								errors.addProperty("email", "email is not unique");
							}
							response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

							for(ConstraintViolation<User> c : constraintViolations)
							{
								errors.addProperty(c.getPropertyPath().toString(), c.getMessage());
							}
							JsonObject responseJson = new JsonObject();
							responseJson.add("validation_errors", errors);
							response.getOutputStream().print(responseJson.toString());
						}
						else
						{
							JsonObject responseJson = new JsonObject();
							userDao.update(user);

							responseJson.add("user", gson.toJsonTree(userDao.retrieve(user.id)));
							response.setStatus(HttpServletResponse.SC_OK);
							response.getOutputStream().print(responseJson.toString());
						}

					}
				}
				catch (Exception e)
				{
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
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
				boolean isUUID = false;
				UUID id = null;
				try
				{
					id = UUID.fromString(args[1]);
					isUUID = true;
				}
				catch (Exception e)
				{
					isUUID = false;
				}
				if(isUUID)
				{
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
					try
					{

						Gson gson = new Gson();
						User user = null;
						UserDao userDao = new UserDao();

						user = userDao.retrieve(args[1]);

						if(user != null)
						{
							user.password = null;
							user.email = null;
							user.api_key = null;
							user.id = null;
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
