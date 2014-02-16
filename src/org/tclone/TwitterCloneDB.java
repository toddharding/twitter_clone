package org.tclone;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.querybuilder.QueryBuilder;

import java.util.ArrayList;

/**
 * Created by Todd on 14/02/14.
 */
public class TwitterCloneDB extends CassandraDatabaseConnection implements ITwitterCloneCRUD
{

	public static final String insertUserQuery = "INSERT INTO tweetclone.users " +
			"(id, username, bio, country, email, language, password, real_name, tailored_ads, website) " +
			"VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	public TwitterCloneDB(String node)
	{
		super(node);
	}

	@Override
	public boolean createUser(User user)
	{
		//check that username is unique
		try
		{
			ResultSet resultSet = getSession().execute("SELECT * FROM tweetclone.usernames " +
				"WHERE username = '" + user.username + "';");

			if(resultSet.all().size() != 0)
			{
				System.out.println("username is not unique: " + user.username);
				return false;
			}

		} catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		try
		{
			ResultSet resultSet = getSession().execute("SELECT * FROM tweetclone.emails " +
					"WHERE email = '" + user.email + "';");

			if(resultSet.all().size() != 0)
			{
				System.out.println("email is not unique: " + user.username);
				return false;
			}

		} catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		try
		{
			getSession().execute(insertUserQuery,
					user.id, user.username, user.bio, user.country, user.email, user.language, user.password, user.real_name,
					user.tailored_ads, user.website
			);
			getSession().execute("INSERT INTO tweetclone.usernames " +
					"(username)" +
					"VALUES(?)",
					user.username
			);
			getSession().execute("INSERT INTO tweetclone.emails " +
					"(email)" +
					"VALUES(?)",
					user.email
			);
		} catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public boolean createTweet(Tweet tweet)
	{
		try
		{
			getSession().execute("INSERT INTO tweetclone.tweets " +
					"(id, userid, tweet_contents, location) " +
					"VALUES(?, ?, ?, ?)",
					tweet.id, tweet.userid, tweet.tweet_contents, tweet.location);

			return true;
		} catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}

	}

	@Override
	public boolean retrieveUser(User user)
	{
		return false;
	}

	@Override
	public boolean retrieveTweet(Tweet tweet)
	{
		return false;
	}

	@Override
	public boolean updateUser(User user)
	{
		return false;
	}

	@Override
	public boolean updateTweet(Tweet tweet)
	{
		return false;
	}

	@Override
	public boolean deleteUser(User user)
	{
		return false;
	}

	@Override
	public boolean deleteTweet(Tweet tweet)
	{
		return false;
	}

	public ArrayList<User> getAllUsers()
	{
		ArrayList<User> users = new ArrayList<>();

		return users;
	}
}
