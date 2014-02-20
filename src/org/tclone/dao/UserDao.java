package org.tclone.dao;

import com.datastax.driver.core.ResultSet;
import org.tclone.CassandraDatabaseConnection;
import org.tclone.entities.User;

/**
 * Created by Todd on 18/02/14.
 */
public class UserDao implements Dao<User>
{
    public static final String insertUserQuery = "INSERT INTO tweetclone.users " +
            "(id, username, bio, country, email, language, password, real_name, tailored_ads, website) " +
            "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    @Override
    public boolean create(User user)
    {
        CassandraDatabaseConnection db = CassandraDatabaseConnection.getInstance();
        try
        {
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
        try
        {
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
        try
        {
            db.getSession().execute(insertUserQuery,
                    user.id, user.username, user.bio, user.country, user.email, user.language, user.password, user.real_name,
                    user.tailored_ads, user.website
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

    @Override
    public User retrieve(User user) {
        return null;
    }

    @Override
    public User update(User user) {
        return null;
    }

    @Override
    public void delete(User user) {

    }
}
