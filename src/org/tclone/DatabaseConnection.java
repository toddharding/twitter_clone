package org.tclone;

/**
 * Created by Todd on 12/02/14.
 */
public abstract class DatabaseConnection
{

	protected abstract void connect(String connectionString);
	protected abstract void disconnect();

}
