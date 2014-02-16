package org.tclone;

import com.datastax.driver.core.Row;

import java.util.UUID;

/**
 * Created by Todd on 14/02/14.
 */
public class Tweet extends Entity
{
	public UUID id;
	public UUID userid;
	public String tweet_contents;
	public String location;

	@Override
	public void construct(Row row)
	{
		id = row.getUUID("id");
		userid = row.getUUID("userid");
		tweet_contents = row.getString("tweet_contents");
		location = row.getString("location");
	}
}
