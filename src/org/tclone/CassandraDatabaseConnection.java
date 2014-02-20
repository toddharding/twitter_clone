package org.tclone;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

/**
 * Created by Todd on 12/02/14.
 */
public class CassandraDatabaseConnection extends DatabaseConnection
{


	private static CassandraDatabaseConnection instance = new CassandraDatabaseConnection();
	private static Cluster cluster;
	public static Cluster getCluster()
	{
		return cluster;
	}

	private String node;
    private CassandraDatabaseConnection()
    {

    }

	private Session session;
	public Session getSession()
	{
        return session;
	}

    public static CassandraDatabaseConnection getInstance()
    {
        return instance;
    }



	@Override
	public void connect(String node)
	{
		buildCluster(node);
		session = cluster.connect();

	}

	private void buildCluster(String node)
	{
		this.node = node;
		cluster = Cluster.builder()
				.addContactPoint(node)
				.build();
	}

	@Override
	public void connect(String node, String keyspace)
	{
		buildCluster(node);
		session = cluster.connect(keyspace);
	}


    @Override
	public void disconnect()
	{
		session.shutdown();
		cluster.shutdown();
    }
}


