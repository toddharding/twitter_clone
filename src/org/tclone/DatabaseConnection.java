package org.tclone;

/**
 * Created by Todd on 12/02/14.
 */
public abstract class DatabaseConnection
{

	public abstract void connect(String connectionString);
	public abstract void connect(String connectionString, String schema);
	public abstract void disconnect();

}
