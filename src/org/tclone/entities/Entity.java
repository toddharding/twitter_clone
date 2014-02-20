package org.tclone.entities;

import com.datastax.driver.core.Row;

import java.io.Serializable;

/**
 * Created by Todd on 13/02/14.
 */
public abstract class Entity implements Serializable
{
	public abstract void construct(Row row);
}
