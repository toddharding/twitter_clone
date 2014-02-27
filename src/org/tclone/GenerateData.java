package org.tclone;

import com.datastax.driver.core.*;
import com.datastax.driver.core.querybuilder.QueryBuilder;
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
	private int numberOfUsersToGenerate = 1000;
	private int numberOfTweetsToGenerate = 10000;
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
			")";

	final String createTweetIndex = "CREATE INDEX ON " + AppStartupListener.keyspace + ".tweets (userid)";

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
			")";


	final String createUsernameIndex =	"CREATE INDEX ON tweetclone.users (username)";

	final String createUsernamesTable =
			"CREATE TABLE " + keyspaceName + "." + "usernames" + " " +
					"(" +
					"username text PRIMARY KEY" +
					")";

	final String createEmailsTable =
			"CREATE TABLE " + keyspaceName + "." + "emails" + " " +
					"(" +
					"email text PRIMARY KEY" +
					")";

	final String dropKeyspace = "DROP KEYSPACE IF EXISTS " + AppStartupListener.keyspace;

	final String createKeyspace = "CREATE KEYSPACE " + keyspaceName +
			" WITH REPLICATION = {'class': 'SimpleStrategy', " +
			"'replication_factor' : 2}";

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
				ArrayList<Statement> batchStatement = new ArrayList<>();

				Statement dropKeyspaceStatement = new SimpleStatement(dropKeyspace);
				batchStatement.add(dropKeyspaceStatement);

				Statement createKeyspaceStatement = new SimpleStatement(createKeyspace);
				batchStatement.add(createKeyspaceStatement);

				Statement createTweetTableStatement = new SimpleStatement(createTweetTable);
				batchStatement.add(createTweetTableStatement);

				Statement createUserTableStatement = new SimpleStatement(createUserTable);
				batchStatement.add(createUserTableStatement);


				Statement createUsernamesTableStatement = new SimpleStatement(createUsernamesTable);
				batchStatement.add(createUsernamesTableStatement);

				Statement createEmailsTableStatement = new SimpleStatement(createEmailsTable);
				batchStatement.add(createEmailsTableStatement);


				for(Statement s : batchStatement)
				{
					s.setConsistencyLevel(ConsistencyLevel.ALL);
					System.out.println("executing statement");
					db.getSession().execute(s);
					Thread.sleep(5000);
				}

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
		createSchema();
		try
		{
			System.out.println("waiting");
			Thread.sleep(5000);
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}

		try
		{
			ArrayList<Statement> batchStatement = new ArrayList<>();
			Statement createTweetIndexStatement = new SimpleStatement(createTweetIndex);
			batchStatement.add(createTweetIndexStatement);

			Statement createUsernameIndexStatement = new SimpleStatement(createUsernameIndex);
			batchStatement.add(createUsernameIndexStatement);

			CassandraDatabaseConnection db = CassandraDatabaseConnection.getInstance();

			for(Statement s : batchStatement)
			{
				s.setConsistencyLevel(ConsistencyLevel.ALL);
				db.getSession().execute(s);
			}

			generateUsers();
			generateRelationships();
			generateTweets();



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
			response.getOutputStream().println(e.getMessage());
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
			System.out.println(e.getMessage());
		}
	}

	public void generateRelationships()
	{
		try
		{
			UserDao userDao = new UserDao();
			ArrayList<User> users = userDao.getAllUsers();
			Random random = new Random();
			BatchStatement batchStatement = new BatchStatement();
			for(int i = 0; i < numberOfRelationshipsToGenerate; ++i)
			{
				System.out.println("Generating relationship " + i + " of " + numberOfRelationshipsToGenerate);
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
					User follower = users.get(follower_index);
					User following = users.get(following_index);
					//userDao.follow(users.get(follower_index), users.get(following_index));
					if(!follower.id.equals(following.id))
					{
						if(!follower.following.contains(following.id) && !following.followers.contains(follower.id))
						{
							Statement s1 = QueryBuilder
									.update(AppStartupListener.keyspace, "users")
									.with(QueryBuilder.add("followers", follower.id))
									.where(QueryBuilder.eq("id", following.id));

							batchStatement.add(s1);

							Statement s2 = QueryBuilder
									.update(AppStartupListener.keyspace, "users")
									.with(QueryBuilder.add("following", following.id))
									.where(QueryBuilder.eq("id", follower.id));
							batchStatement.add(s2);
						}
					}
				}
				catch (Exception e)
				{
					System.out.println(e.getMessage());
				}
			}
			CassandraDatabaseConnection db = CassandraDatabaseConnection.getInstance();
			db.getSession().execute(batchStatement);
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
			ArrayList<Tweet> tweets = new ArrayList<>();
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
				tweets.add(tweet);
				//tweetDao.create(tweet);
			}
			tweetDao.batchAdd(tweets);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
