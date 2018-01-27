package org.ossg.remote.helpers;

import org.bson.Document;
import org.ossg.model.Player;
import org.ossg.remote.client.Connection;
import org.ossg.remote.client.ConnectionFactory;

import javax.ws.rs.core.Response;
import java.util.logging.Logger;

public class PlayerHelper {
    private Logger logger = Logger.getLogger("org.ossg");
    Connection connection;

    public PlayerHelper(){
        connection = ConnectionFactory.getConnection();
    }

    public Player savePlayer(Player player){
        if (connection == null){
            throw new IllegalStateException("Connection Factory not initialized. Use ConnectionFactory.getInstance(<Configuration>).");
        }
        Response response = connection.POST("/rs/players/add", player.toString());
        if (response.getStatus() == 200){
            String entity = response.readEntity(String.class);
            Document document = Document.parse(entity);
            String id = document.getString("id");
            player.setId(id);
            return player;
        }
        return null;
    }

    public Player getPlayer(String id){
        if (connection == null){
            throw new IllegalStateException("Connection Factory not initialized. Use ConnectionFactory.getInstance(<Configuration>).");
        }
        Response response = connection.GET("/rs/players/player/" + id);
        String entity = response.readEntity(String.class);
        Document document = Document.parse(entity);
        Player player = new Player().build(document);
        return player;
    }
}