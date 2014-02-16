package org.tclone;

import com.datastax.driver.core.Row;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Todd on 13/02/14.
 */
public class User extends Entity implements Serializable
{
	public UUID id;
	public String username;
	public String real_name;
	public String email;
	public String password;
	public String language;
	public String timezone;
	public String country;
	public ArrayList<UUID> followers;
	public ArrayList<UUID> following;
	public ArrayList<UUID> favorite_tweets;
	public String website;
	public String bio;
	public String facebook_link;
	public boolean tailored_ads;
	public String api_key;


	@Override
	public void construct(Row row)
	{
		id = row.getUUID("id");
		username = row.getString("username");
		real_name = row.getString("real_name");
		email = row.getString("email");
		password = row.getString("password");
		language = row.getString("language");
		timezone = row.getString("timezone");
		country = row.getString("country");
		followers = new ArrayList<>(row.getSet("followers", UUID.class));
		following = new ArrayList<>(row.getSet("following", UUID.class));
		favorite_tweets = new ArrayList<>(row.getSet("favorite_tweets", UUID.class));
		website = row.getString("website");
		bio = row.getString("bio");
		facebook_link = row.getString("facebook_link");
		tailored_ads = row.getBool("tailored_ads");
		api_key = row.getString("api_key");
	}
}
