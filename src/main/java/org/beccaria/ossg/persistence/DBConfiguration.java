package org.beccaria.ossg.persistence;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

public class DBConfiguration {
    private static DBConfiguration theInstance;
    private MongoDatabase database;
    public final static String DB_NAME = "ossg";

    private DBConfiguration() {
        MongoClient client = new MongoClient( "localhost" , 27017 );
        database = client.getDatabase(DB_NAME);
    }

    public static DBConfiguration getInstance(){
        if( theInstance == null ){
            theInstance = new DBConfiguration();
        }
        return theInstance;
    }

    public MongoDatabase getDatabase(){
        return this.database;
    }

    public void shutdown(){
        database = null;
    }
}
