package org.tclone.dao;

import org.tclone.CassandraDatabaseConnection;
import org.tclone.Tweet;

import java.io.Serializable;

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
        return null;
    }

    @Override
    public Tweet update(Tweet tweet) {
        return null;
    }

    @Override
    public void delete(Tweet tweet) {

    }
}
