package org.tclone.dao;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import org.tclone.CassandraDatabaseConnection;
import org.tclone.entities.Tweet;

import java.util.UUID;

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
    public Tweet retrieve(UUID id) {
		CassandraDatabaseConnection db = CassandraDatabaseConnection.getInstance();
		ResultSet resultSet = db.getSession().execute("SELECT * FROM tweetclone.tweets WHERE id = " + id + " LIMIT 1;");

		if(resultSet.getAvailableWithoutFetching() == 1)
		{
            Tweet tweet = new Tweet();
			tweet.construct(resultSet.one());
            return tweet;
		}
        else
        {
            return null;
        }

    }

    @Override
    public void update(Tweet tweet) {
		CassandraDatabaseConnection db = CassandraDatabaseConnection.getInstance();
        Statement query = QueryBuilder.update("tweetclone", "tweets")
                .with(QueryBuilder.set("tweet_contents", tweet.tweet_contents))
                .and(QueryBuilder.set("location", tweet.location))
                .where(QueryBuilder.eq("id", tweet.id));
        db.getSession().execute(query);
		/*db.getSession().execute("UPDATE tweetclone.tweets SET " +
				"tweet_contents='" + tweet.tweet_contents + "' ," +
				"location='" + tweet.location + "' " +
				"WHERE id=" +tweet.id +  " AND userid = " +tweet.userid.toString() +";");*/
    }

    @Override
    public void delete(Tweet tweet) {

		CassandraDatabaseConnection db = CassandraDatabaseConnection.getInstance();
		db.getSession().execute("DELETE FROM tweetclone.tweets WHERE " +
				"id =" + tweet.id +  ";");
    }
}
