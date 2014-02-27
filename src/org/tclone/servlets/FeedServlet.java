package org.tclone.servlets;

import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.gson.Gson;
import org.apache.commons.lang3.time.StopWatch;
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
import java.util.*;

/**
 * Created by Todd on 27/02/14.
 */
@WebServlet(name = "FeedServlet", urlPatterns = {"/feed", "/feed/*"})
public class FeedServlet extends HttpServlet
{
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try
		{
			StopWatch stopWatch = new StopWatch();
			stopWatch.start();
			String[] args = request.getPathInfo().split("/");

			UserDao userDao = new UserDao();
			User user = userDao.retrieve(UUID.fromString(args[1]));
			TweetDao tweetDao = new TweetDao();
			ArrayList<Tweet> allTweets = tweetDao.retrieveAllTweets();

			ArrayList<Tweet> tweets = new ArrayList<>();

			Map<UUID, String> usernames = new HashMap<>();
			for(UUID uuid : user.following)
			{
				User u = userDao.retrieve(uuid);
				if(u != null)
					usernames.put(uuid, u.username);
			}

			for(Tweet t : allTweets)
			{
				for(UUID id : user.following)
				{
					if(t.userid.equals(id))
					{
						t.username = usernames.get(id);
						tweets.add(t);
					}
				}
			}

			Gson gson = new Gson();
			response.setStatus(HttpServletResponse.SC_OK);
			response.getOutputStream().print(gson.toJson(tweets));
		}
		catch (Exception e)
		{
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}

	}
}
