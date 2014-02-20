package org.tclone.dao;

import com.datastax.driver.core.ResultSet;
import org.tclone.CassandraDatabaseConnection;
import org.tclone.entities.Tweet;

/**
 * Created by Todd on 18/02/14.
 */
public class TweetDao implements Dao<Tweet>
{

    @Override
    public boolean create(Tweet tweet)
    {
        try
        {
            CassandraDatabaseConnection.getInstance().getSession().execute("INSERT INTO tweetclone.tweets " +
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
    public Tweet retrieve(Tweet tweet) {
		CassandraDatabaseConnection db = CassandraDatabaseConnection.getInstance();
		ResultSet resultSet = db.getSession().execute("SELECT * FROM tweetclone.tweets WHERE id = " + tweet.id + " LIMIT 1;");
		if(resultSet.getAvailableWithoutFetching() == 1)
		{
			tweet.construct(resultSet.one());
		}
        return tweet;
    }

    @Override
    public Tweet update(Tweet tweet) {
		CassandraDatabaseConnection db = CassandraDatabaseConnection.getInstance();
		db.getSession().execute("UPDATE tweetclone.tweets SET " +
				"tweet_contents='" + tweet.tweet_contents + "' ," +
				"location='" + tweet.location + "' " +
				"WHERE id=" +tweet.id.toString() +  " AND userid = " +tweet.userid.toString() +";");
        return null;
    }

    @Override
    public void delete(Tweet tweet) {

		CassandraDatabaseConnection db = CassandraDatabaseConnection.getInstance();
		db.getSession().execute("DELETE FROM tweetclone.tweets WHERE " +
				"id =" + tweet.id.toString() + " AND " +
				"userid = " + tweet.userid.toString() + ";");
    }
}
