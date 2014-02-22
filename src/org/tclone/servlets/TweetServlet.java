package org.tclone.servlets;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.utils.UUIDs;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import org.tclone.CassandraDatabaseConnection;
import org.tclone.dao.TweetDao;
import org.tclone.dao.UserDao;
import org.tclone.entities.Tweet;
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
@WebServlet(name = "TweetServlet", urlPatterns = {"/tweet","/tweet/*"} )
public class TweetServlet extends HttpServlet
{
	// Create
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{

        try
        {
            response.setContentType("application/json");
            UserDao userDao = new UserDao();
            TweetDao tweetDao = new TweetDao();
            UUID user_id = UUID.fromString(request.getParameter("userid"));
            User user = userDao.retrieve(user_id);
            if(user !=null)
            {
                Tweet tweet = new Tweet();
                tweet.id = UUIDs.timeBased();
                tweet.userid = user.id;
                tweet.tweet_contents = request.getParameter("tweet_contents");
                tweet.location = request.getParameter("location");


                // TODO AND validate and create tweet
                if(tweetDao.create(tweet))
                {
                    if(tweetDao.retrieve(tweet.id) != null)
                    {
						Gson gson = new Gson();
                        response.setStatus(HttpServletResponse.SC_OK);
						response.getOutputStream().print(gson.toJson(tweet));

                    }
                    else
                    {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    }
                }
                else
                {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                }
            }
            else
            {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }

        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            response.getOutputStream().println(e.getMessage());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
	}

	// Update
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
        try
        {
            String[] args = request.getPathInfo().split("/");
            response.setContentType("application/json");
            UserDao userDao = new UserDao();
            TweetDao tweetDao = new TweetDao();
            //User user = userDao.retrieve(user_id);
            Tweet tweet = tweetDao.retrieve(UUID.fromString(args[1]));
            if(tweet != null)
            {
                tweet.tweet_contents = request.getParameter("tweet_contents");
                tweet.location = request.getParameter("location");
                tweetDao.update(tweet);
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            }
            else
            {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }

        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
	}


	// Delete
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		response.setContentType("application/json");
		String[] args = request.getPathInfo().split("/");
		System.out.println(args[1]);
		try
		{
            UserDao userDao = new UserDao();
            TweetDao tweetDao = new TweetDao();
			Tweet tweet = tweetDao.retrieve(UUID.fromString(args[1]));
			//get user id from auth data
			if(tweet != null)
            {
                tweetDao.delete(tweet);
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            }
            else
            {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }

		}
		catch (Exception e)
		{
			System.out.println(e.getStackTrace());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
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
			TweetDao tweetDao = new TweetDao();
			Gson gson = new Gson();

            UUID tweet_id = UUID.fromString(args[1]);
            Tweet tweet = tweetDao.retrieve(tweet_id);
            if(tweet != null)
            {
			    System.out.println(gson.toJson(tweet));
                response.setStatus(HttpServletResponse.SC_OK);
			    response.getOutputStream().print(gson.toJson(tweet));
            }
            else
            {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
		}
		catch (Exception e)
		{
			System.out.println(e.getStackTrace());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}
}
