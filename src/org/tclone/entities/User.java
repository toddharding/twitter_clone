package org.tclone.entities;

import com.datastax.driver.core.Row;
import com.datastax.driver.core.utils.UUIDs;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.mindrot.jbcrypt.BCrypt;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Todd on 13/02/14.
 */
public class User extends Entity implements Serializable
{
	@NotNull
	public UUID id;

	@NotBlank
	@SafeHtml
	public String username;

	@NotBlank
	@SafeHtml
	public String real_name;

	@NotBlank
	@Email
	public String email;

	@NotBlank
	public String password;

	@NotBlank
	@SafeHtml
	public String language;

	@NotBlank
	@SafeHtml
	public String country;

	public Set<UUID> followers;
	public Set<UUID> following;
	public Set<UUID> favorite_tweets;

	@SafeHtml
	public String website;

	@SafeHtml
	public String bio;

	@NotNull
	public boolean tailored_ads;

	@NotNull
	public UUID api_key;

	public void generateID()
	{
		id = UUIDs.timeBased();
	}

	public void generateApiKey()
	{
		api_key = UUIDs.random();
	}

	public boolean authenticate(String password)
	{
		return BCrypt.checkpw(password, this.password);
	}
	@Override
	public void construct(Row row)
	{
		id = row.getUUID("id");
		username = row.getString("username");
		real_name = row.getString("real_name");
		email = row.getString("email");
		password = row.getString("password");
		language = row.getString("language");
		country = row.getString("country");
		followers = row.getSet("followers", UUID.class);
		following = row.getSet("following", UUID.class);
		favorite_tweets = row.getSet("favorite_tweets", UUID.class);
		website = row.getString("website");
		bio = row.getString("bio");
		tailored_ads = row.getBool("tailored_ads");
		api_key = row.getUUID("api_key");
	}
}
