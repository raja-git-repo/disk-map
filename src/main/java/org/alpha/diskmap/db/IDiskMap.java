package org.alpha.diskmap.db;

import java.io.Serializable;

public interface IDiskMap<K extends Serializable,V extends Serializable> {

	public V putObject(K k, V v);
	public V getObject(K k);
	public void close();
}
