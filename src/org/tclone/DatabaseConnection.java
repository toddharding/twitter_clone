package org.tclone;

/**
 * Created by Todd on 12/02/14.
 */
public abstract class DatabaseConnection implements AutoCloseable
{
	@Override
	public abstract void close() throws Exception;

	protected abstract void connect();
	protected abstract void disconnect();
}
