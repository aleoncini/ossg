package org.ossg.remote.helpers;

import org.bson.Document;
import org.ossg.model.Tournament;
import org.ossg.remote.client.Connection;
import org.ossg.remote.client.ConnectionFactory;

import javax.ws.rs.core.Response;
import java.util.logging.Logger;

public class TournamentHelper {
    private Logger logger = Logger.getLogger("org.ossg");
    Connection connection;

    public TournamentHelper() {
        connection = ConnectionFactory.getConnection();
    }

    public Tournament saveTournament(Tournament tournament){
        if (connection == null){
            throw new IllegalStateException("Connection Factory not initialized. Use ConnectionFactory.getInstance(<Configuration>).");
        }
        Response response = connection.POST("/rs/tournaments/ads", tournament.toString());
        if (response.getStatus() == 200){
            String entity = response.readEntity(String.class);
            Document document = Document.parse(entity);
            String id = document.getString("tournamentId");
            System.out.println("");
            tournament.setId(id);
            System.out.println("=== saved new tournament with ID: " + id);
            return tournament;
        }
        return null;
    }

    public Tournament getTournament(String id){
        if (connection == null){
            throw new IllegalStateException("Connection Factory not initialized. Use ConnectionFactory.getInstance(<Configuration>).");
        }
        Response response = connection.GET("/rs/tournaments/tournament/" + id);
        String entity = response.readEntity(String.class);
        Document document = Document.parse(entity);
        return new Tournament().build(document);
    }
}
