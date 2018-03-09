package org.alpha.diskmap.db;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public enum DiskMapStore implements IDiskMap<Serializable,Serializable>{
	INSTANCE;
	private ConcurrentMap<Serializable, Serializable> mapDB = null;
	private static final String DB_NAME ="data.ser";
	ExecutorService executor = Executors.newSingleThreadExecutor();
	volatile boolean persistDB = true;
	private DiskMapStore(){
		deSerializeDB();
		if(mapDB == null) {
			mapDB = new ConcurrentHashMap<>();
		}
		executor.execute(new Runnable() {			
			@Override
			public void run() {	
				while(persistDB) {				
					serializeDB();
				}
			}
		});
		executor.shutdown();
		Runtime.getRuntime().addShutdownHook(new Thread() {
			
			@Override
			public void run() {
				serializeDB();
			}
		});
	}
	public static IDiskMap<Serializable,Serializable> getDB() {		
        return (IDiskMap<Serializable,Serializable>) INSTANCE;
    }

	public Serializable putObject(Serializable k, Serializable v) {
		return mapDB.put(k, v);
	}

	public Serializable getObject(Serializable k) {
		return mapDB.get(k);
	}
	
	private void serializeDB() {
		try {
			FileOutputStream fos =
                    new FileOutputStream(DB_NAME);
                 ObjectOutputStream oos = new ObjectOutputStream(fos);
                 oos.writeObject(mapDB);
                 oos.close();
                 fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	private void deSerializeDB() {
		try {
			 FileInputStream fis = new FileInputStream(DB_NAME);
	         ObjectInputStream ois = new ObjectInputStream(fis);
	         mapDB = (ConcurrentMap<Serializable, Serializable>) ois.readObject();
	         ois.close();
	         fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void close() {
		persistDB= false;
		executor.shutdownNow();
	}
}
