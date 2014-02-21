package org.tclone.servlets;

import com.datastax.driver.core.ResultSet;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import org.tclone.CassandraDatabaseConnection;
import org.tclone.dao.TweetDao;
import org.tclone.dao.UserDao;
import org.tclone.entities.Tweet;

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
@WebServlet(name = "TweetServlet", urlPatterns = {"/tweet","/tweet/*"} )
public class TweetServlet extends HttpServlet
{
	// Create
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{

	}

	// Update
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{

	}

	// Delete
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		response.setContentType("application/json");
		String[] args = request.getPathInfo().split("/");
		System.out.println(args[1]);
		try
		{

			Tweet tweet = new Tweet();
			tweet.id = UUID.fromString(args[1]);
			//get user id from auth data
			UserDao userDao = new UserDao();
			TweetDao tweetDao = new TweetDao();
			tweetDao.delete(tweet);
			Gson gson = new Gson();
			response.getOutputStream().print("{ \"id\":\"" + tweet.id +"\", \"status\":\"deleted\"}");
		}
		catch (Exception e)
		{
			System.out.println(e.getStackTrace());
		}
	}

	// Read
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		response.setContentType("application/json");
		String[] args = request.getPathInfo().split("/");
		System.out.println(args[1]);
		try
		{
            CassandraDatabaseConnection db = CassandraDatabaseConnection.getInstance();
			TweetDao tweetDao = new TweetDao();
			Gson gson = new Gson();
			Tweet tweet = new Tweet();
            tweet.id = UUID.fromString(args[1]);
			tweet = tweetDao.retrieve(tweet);
			//ResultSet resultSet = db.getSession().execute("SELECT * FROM tweetclone.tweets WHERE id = " + args[1] + " LIMIT 1;");
			//tweet.construct(resultSet.one());
			System.out.println(gson.toJson(tweet));
			response.getOutputStream().print(gson.toJson(tweet));
		}
		catch (Exception e)
		{
			System.out.println(e.getStackTrace());
		}
	}
}
