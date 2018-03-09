package org.alpha.diskmap;

import java.io.Serializable;

import org.alpha.diskmap.db.DiskMapStore;
import org.alpha.diskmap.db.IDiskMap;

public class MapStoreTest {

	
	public static void main(String[] args) {
		IDiskMap<Serializable, Serializable> db = DiskMapStore.getDB();
		/*db.putObject(1, "one");
		db.putObject(2, "two");
		db.putObject(3, "three");
		System.exit(0);
		db.putObject(4, "four");
		db.putObject(5, "five");*/
		
		System.out.println(db.getObject(1));
		System.out.println(db.getObject(4));
		db.close();
	}
}
