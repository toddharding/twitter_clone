package org.tclone.dao;

import com.datastax.driver.core.BatchStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import org.tclone.CassandraDatabaseConnection;
import org.tclone.entities.User;
import org.tclone.listeners.AppStartupListener;

import java.util.UUID;

/**
 * Created by Todd on 18/02/14.
 */
public class UserDao implements Dao<User>
{
    public static final String insertUserQuery = "INSERT INTO tweetclone.users " +
            "(id, username, bio, country, email, language, password, real_name, tailored_ads, website, api_key) " +
            "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    @Override
    public boolean create(User user)
    {
        CassandraDatabaseConnection db = CassandraDatabaseConnection.getInstance();
		if (!isUsernameUnique(user)) return false;
		if (!isEmailUnique(user)) return false;
		try
        {
            db.getSession().execute(insertUserQuery,
                    user.id, user.username, user.bio, user.country, user.email, user.language, user.password, user.real_name,
                    user.tailored_ads, user.website, user.api_key
            );
            db.getSession().execute("INSERT INTO tweetclone.usernames " +
                    "(username)" +
                    "VALUES(?)",
                    user.username
            );
            db.getSession().execute("INSERT INTO tweetclone.emails " +
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

	public boolean isEmailUnique(User user)
	{

		try
        {
			CassandraDatabaseConnection db = CassandraDatabaseConnection.getInstance();
            ResultSet resultSet = db.getSession().execute("SELECT * FROM tweetclone.emails " +
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
		return true;
	}

	public boolean isUsernameUnique(User user)
	{
		try
		{
			CassandraDatabaseConnection db = CassandraDatabaseConnection.getInstance();
			ResultSet resultSet = db.getSession().execute("SELECT * FROM tweetclone.usernames " +
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
		return true;
	}

	@Override
    public User retrieve(UUID id) {
		CassandraDatabaseConnection db = CassandraDatabaseConnection.getInstance();
		ResultSet resultSet = db.getSession().execute("SELECT * FROM tweetclone.users WHERE id=" + id + " LIMIT 1 ALLOW FILTERING;" );
		if(resultSet.getAvailableWithoutFetching() == 1)
		{
            User user = new User();
			user.construct(resultSet.one());
            return user;
		}
        else
        {
            return null;
        }

    }

	public User retrieve(String username) {
		CassandraDatabaseConnection db = CassandraDatabaseConnection.getInstance();
		Statement statement = QueryBuilder
				.select()
				.all()
				.from(AppStartupListener.keyspace, "users")
				.where(QueryBuilder.eq("username", username));

		//ResultSet resultSet = db.getSession().execute("SELECT * FROM tweetclone.users WHERE username=" + username + " ;" );
		ResultSet resultSet = db.getSession().execute(statement);
		if(resultSet.getAvailableWithoutFetching() == 1)
		{
			User user = new User();
			user.construct(resultSet.one());
			return user;
		}
		else
		{
			return null;
		}

	}

    @Override
    public void update(User user) {
		CassandraDatabaseConnection db = CassandraDatabaseConnection.getInstance();
		if(isEmailUnique(user) == true)
		{
			BatchStatement batchStatement = new BatchStatement();
			UserDao userDao = new UserDao();
			User oldUser = userDao.retrieve(user.id);
			Statement delEmailStatement = QueryBuilder
					.delete()
					.from(AppStartupListener.keyspace, "emails")
					.where(QueryBuilder.eq("email", oldUser.email));
			batchStatement.add(delEmailStatement);


			db.getSession().execute("UPDATE tweetclone.users SET " +
					//"id=" + user.id + "," +
					//"username='" + user.username + "' ," +
					"real_name='" + user.real_name + "' ," +
					"email='" + user.email + "' ," +
					"password='" + user.password + "' ," +
					"language='" + user.language + "' ," +
					"country='" + user.country + "' ," +
					//"followers=" + user.followers + "," +
					//"following=" + user.following + "," +
					//"favorite_tweets=" + user.favorite_tweets + "," +
					"website='" + user.website + "' ," +
					"bio='" + user.bio + "' ," +
					"tailored_ads=" + user.tailored_ads + " " +
					//"api_key='" + user.api_key + " " +
					"WHERE id=" + user.id  + ";");
			db.getSession().execute(batchStatement);
		}

    }

    @Override
    public void delete(User user) {
		CassandraDatabaseConnection db = CassandraDatabaseConnection.getInstance();
		BatchStatement batchStatement = new BatchStatement();
		Statement delUserStatement = QueryBuilder
				.delete()
				.from(AppStartupListener.keyspace, "users")
				.where(QueryBuilder.eq("id", user.id));
		batchStatement.add(delUserStatement);

		Statement delUsernameStatement = QueryBuilder
				.delete()
				.from(AppStartupListener.keyspace, "usernames")
				.where(QueryBuilder.eq("username", user.username));
		batchStatement.add(delUsernameStatement);

		Statement delEmailStatement = QueryBuilder
				.delete()
				.from(AppStartupListener.keyspace, "emails")
				.where(QueryBuilder.eq("email", user.email));
		batchStatement.add(delEmailStatement);

		db.getSession().execute(batchStatement);

    }
}
