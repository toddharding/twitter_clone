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
	protected void connect(String node)
	{
        this.node = node;
		cluster = Cluster.builder()
				.addContactPoint(node)
				.build();
		session = cluster.connect();

	}


    @Override
	protected void disconnect()
	{
		session.shutdown();
		cluster.shutdown();
    }
}


