package org.tclone.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.tclone.dao.UserDao;
import org.tclone.entities.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;

/**
 * Created by Todd on 26/02/14.
 */
@WebServlet(name = "FollowServlet", urlPatterns = {"/follow", "/follow/*"})
public class FollowServlet extends HttpServlet
{
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		response.setContentType("application/json");

		try
		{
			UUID followerId =  UUID.fromString(request.getParameter("follower_id"));
			UUID followingId =  UUID.fromString(request.getParameter("following_id"));
			UserDao userDao = new UserDao();

			userDao.follow(followerId, followingId);
			response.setStatus(HttpServletResponse.SC_OK);

		}
		catch (Exception e)
		{
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
	}

	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		response.setContentType("application/json");

		try
		{
			UUID followerId =  UUID.fromString(request.getParameter("follower_id"));
			UUID followingId =  UUID.fromString(request.getParameter("following_id"));
			UserDao userDao = new UserDao();

			userDao.unfollow(followerId, followingId);
			response.setStatus(HttpServletResponse.SC_OK);

		}
		catch (Exception e)
		{
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
	}


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		response.setContentType("application/json");
		String[] args = request.getPathInfo().split("/");
		try
		{
			UUID id = UUID.fromString(args[1]);
			Gson gson = new Gson();
			UserDao userDao = new UserDao();
			HashSet<User> followers;
			HashSet<User> following;
			followers = userDao.getFollowers(userDao.retrieve(id));
			following = userDao.getFollowing(userDao.retrieve(id));
			response.setStatus(HttpServletResponse.SC_OK);
			JsonObject p = new JsonObject();
			p.add("followers", gson.toJsonTree(followers));
			p.add("following", gson.toJsonTree(following));
			response.getOutputStream().print(gson.toJson(p));

		}
		catch (Exception e)
		{
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
	}
}
