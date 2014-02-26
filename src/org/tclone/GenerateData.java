package org.tclone;

import com.datastax.driver.core.Host;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.utils.UUIDs;
import org.jfairy.Fairy;
import org.jfairy.producer.person.Person;
import org.mindrot.jbcrypt.BCrypt;
import org.tclone.dao.TweetDao;
import org.tclone.dao.UserDao;
import org.tclone.entities.Tweet;
import org.tclone.entities.User;
import org.tclone.listeners.AppStartupListener;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@WebServlet(urlPatterns = {"/gendata"}, name = "GenerateDataServlet")
public class GenerateData extends HttpServlet
{
	private int numberOfUsersToGenerate = 100;
	private int numberOfTweetsToGenerate = 1000;
	private int numberOfRelationshipsToGenerate = 10000;
	final String keyspaceName = "tweetclone";

	final String tweetTableName = "tweets";
	final String createTweetTable =
		"CREATE TABLE " + AppStartupListener.keyspace + "." + tweetTableName + " " +
			"(" +
			"id timeuuid PRIMARY KEY," +
			"userid timeuuid," +
			"tweet_contents text," +
			"location text" +
			");";

	final String createTweetIndex = "CREATE INDEX tweets_userid ON tweetclone.tweets (userid);";

	final String userTableName = "users";
	final String createUserTable =
		"CREATE TABLE " + keyspaceName + "." + userTableName + " " +
			"(" +
			"id timeuuid PRIMARY KEY," +
			"username text," +
			"real_name text," +
			"email text," +
			"password text," +
			"language text," +
			"country text," +
			"followers set<timeuuid>," +
			"following set<timeuuid>," +
			"favorite_tweets set<timeuuid>," +
			"website text," +
			"bio text," +
			"tailored_ads boolean," +
			"api_key UUID" +
			");";


	final String createUsernameIndex =	"CREATE INDEX users_username ON tweetclone.users (username);";

	final String createUsernamesTable =
			"CREATE TABLE " + keyspaceName + "." + "usernames" + " " +
					"(" +
					"username text PRIMARY KEY" +
					");";

	final String createEmailsTable =
			"CREATE TABLE " + keyspaceName + "." + "emails" + " " +
					"(" +
					"email text PRIMARY KEY" +
					");";

	final String dropKeyspace = "DROP KEYSPACE IF EXISTS " + keyspaceName + " ;";

	final String createKeyspace = "CREATE KEYSPACE IF NOT EXISTS " + keyspaceName +
			" WITH REPLICATION = {'class': 'SimpleStrategy', " +
			"'replication_factor' : 3};";

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{

	}

	public void createSchema()
	{
		try
		{
            CassandraDatabaseConnection db = CassandraDatabaseConnection.getInstance();
			if (db.getSession() != null)
			{
				db.getSession().execute(dropKeyspace);
				db.getSession().execute(createKeyspace);
				db.getSession().execute(createTweetTable);
				db.getSession().execute(createTweetIndex);
				db.getSession().execute(createUserTable);
				db.getSession().execute(createUsernameIndex);
				db.getSession().execute(createUsernamesTable);
				db.getSession().execute(createEmailsTable);
			} else
			{
				System.out.println("Session is null");
			}


		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		Fairy fairy = Fairy.create(Locale.forLanguageTag("en"));
		for (int i = 0; i < 1; i++)
		{
			response.getOutputStream().println("Person: " + i + " = " + fairy.person().firstName() + " " + fairy.person().lastName() +
					" " + fairy.person().username() + " " + fairy.dateProducer().randomDateBetweenYearAndNow(2005));
		}
		createSchema();
		generateUsers();
		generateRelationships();
		generateTweets();
		try
		{
            CassandraDatabaseConnection db = CassandraDatabaseConnection.getInstance();
			Metadata metadata = db.getCluster().getMetadata();
			System.out.printf("Connected to cluster: %s\n",
					metadata.getClusterName());
			for (Host host : metadata.getAllHosts())
			{
				response.getOutputStream().println("Datacenter: " + host.getDatacenter() + " Host: " + host.getAddress() + " Rack: " +
						host.getRack());
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void generateUsers()
	{
		try
		{
            CassandraDatabaseConnection db = CassandraDatabaseConnection.getInstance();
            UserDao userDao = new UserDao();
			Fairy fairy = Fairy.create(Locale.ENGLISH);
			ArrayList<User> users = new ArrayList<>();
			String password = BCrypt.hashpw("test", BCrypt.gensalt(12));
			for (int i = 0; i < numberOfUsersToGenerate; ++i)
			{
				int failcount = 0;
				User user = new User();
				do
				{
					System.out.println("Generating user: " + i);
					Person person = fairy.person();
					user.generateID();
					user.generateApiKey();
					user.username = person.username();
					user.country = "UK";
					user.bio = fairy.text().paragraph();
					user.email = person.email();
					user.language = "English";
					user.real_name = person.fullName();
					user.password = password;
					user.tailored_ads = true;
					user.website = fairy.company().url();
					System.out.println("id: " + user.id);
					System.out.println("api_key: " + user.api_key);
					System.out.println("user: " + user.username);
					System.out.println("name: " + user.real_name);
					System.out.println("email: " + user.email);
					System.out.println("url: " + user.website);
					System.out.println("password: " + user.password);
					System.out.println("");
					users.add(user);
				}
				while(userDao.create(user) == false && ++failcount < 100);
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void generateRelationships()
	{
		try
		{
			UserDao userDao = new UserDao();
			ArrayList<User> users = userDao.getAllUsers();
			Random random = new Random();

			for(int i = 0; i < numberOfRelationshipsToGenerate; ++i)
			{
				int follower_index;
				int following_index;
				int counter = 0;
				do
				{
					follower_index = random.nextInt(users.size() - 1);
					following_index = random.nextInt(users.size() - 1);
					counter++;
				}while (follower_index == following_index && counter < 10);


				try
				{
					userDao.follow(users.get(follower_index), users.get(following_index));
				}
				catch (Exception e)
				{
					System.out.println(e.getMessage());
				}


			}
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
	}

	public void generateTweets()
	{
		try
		{
            CassandraDatabaseConnection db = CassandraDatabaseConnection.getInstance();
			Fairy fairy = Fairy.create(Locale.ENGLISH);
			ArrayList<User> users = new ArrayList<>();
			ResultSet user_results = db.getSession().execute("SELECT * FROM tweetclone.users;");
			for(Row row : user_results)
			{
				User u = new User();
				u.construct(row);
				users.add(u);
			}

			Random random = new Random();
            TweetDao tweetDao = new TweetDao();

			for (int i = 0; i < numberOfTweetsToGenerate; ++i)
			{
				System.out.println("Generating Tweet: " + i);
				Tweet tweet = new Tweet();
				User user = users.get(random.nextInt(users.size() -1));
				tweet.id = UUIDs.timeBased();
				tweet.tweet_contents = fairy.text().paragraph();
				tweet.tweet_contents = tweet.tweet_contents.substring(0, Math.min(tweet.tweet_contents.length(), 140));
				tweet.userid = user.id;
				tweet.location = user.country;
				System.out.println("id: " + tweet.id);
				System.out.println("userid: " + tweet.userid);
				System.out.println("username: " + user.username);
				System.out.println("tweet_contents: " + tweet.tweet_contents);
				System.out.println("location: " + tweet.location);
				tweetDao.create(tweet);
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
