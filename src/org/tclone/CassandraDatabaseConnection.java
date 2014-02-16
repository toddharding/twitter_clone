package org.tclone;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

/**
 * Created by Todd on 12/02/14.
 */
public class CassandraDatabaseConnection extends DatabaseConnection
{
	private Cluster cluster;
	public Cluster getCluster()
	{
		return cluster;
	}

	private String node;
	public CassandraDatabaseConnection(String node)
	{
		this.node = node;
		connect();
	}

	private Session session;
	public Session getSession()
	{
		return session;
	}


	@Override
	public void close() throws Exception
	{
		System.out.println("Closing Connection");
		disconnect();
	}

	@Override
	protected void connect()
	{

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
