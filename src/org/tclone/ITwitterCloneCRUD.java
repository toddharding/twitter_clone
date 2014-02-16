package org.tclone;

/**
 * Created by Todd on 14/02/14.
 */
public interface ITwitterCloneCRUD
{
	public boolean createUser(User user);
	public boolean createTweet(Tweet tweet);

	public boolean retrieveUser(User user);
	public boolean retrieveTweet(Tweet tweet);

	public boolean updateUser(User user);
	public boolean updateTweet(Tweet tweet);

	public boolean deleteUser(User user);
	public boolean deleteTweet(Tweet tweet);
}
