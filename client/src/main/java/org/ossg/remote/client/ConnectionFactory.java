package org.ossg.remote.client;

import org.ossg.remote.client.impl.DefaultConnection;

public class ConnectionFactory {

    private static ConnectionFactory instance;
    private Configuration configuration;

    private ConnectionFactory(Configuration configuration){
        this.configuration = configuration;
    }

    public static ConnectionFactory getFactory() {
        return getFactory(new Configuration());
    }

    public static ConnectionFactory getFactory(Configuration configuration) {
        if (instance == null) {
            instance = new ConnectionFactory(configuration);
        }
        return instance;
    }

    public static Connection getConnection(){
        if (instance == null) {
            return null;
        }
        return new DefaultConnection(instance.configuration);
    }
}
