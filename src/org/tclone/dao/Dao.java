package org.tclone.dao;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by Todd on 18/02/14.
 */
public interface Dao <T extends Serializable>
{
	boolean create (T t);
	T retrieve (UUID id);
	void update (T t);
	void delete (T t);
}
