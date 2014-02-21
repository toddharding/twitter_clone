package org.tclone.entities;

import com.datastax.driver.core.Row;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by Todd on 14/02/14.
 */
public class Tweet extends Entity implements Serializable
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
		System.out.println("id" + id);
		System.out.println("userid" + userid);
		System.out.println("conts" + tweet_contents);
	}
}