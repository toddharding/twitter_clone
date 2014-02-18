package org.tclone.dao;

import java.io.Serializable;

/**
 * Created by Todd on 18/02/14.
 */
public interface Dao <T extends Serializable>
{
	T create (T t);
	T retrieve (T t);
	T update (T t);
	void delete (T t);
}
