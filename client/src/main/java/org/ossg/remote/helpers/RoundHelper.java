package org.ossg.remote.helpers;

import org.bson.Document;
import org.ossg.model.DayOfEvent;
import org.ossg.model.Round;
import org.ossg.remote.client.Connection;
import org.ossg.remote.client.ConnectionFactory;

import javax.ws.rs.core.Response;
import java.util.logging.Logger;

public class RoundHelper {
    private Logger logger = Logger.getLogger("org.ossg");
    Connection connection;

    public RoundHelper() {
        connection = ConnectionFactory.getConnection();
    }

    public Round saveRound(Round round){
        if (connection == null){
            throw new IllegalStateException("Connection Factory not initialized. Use ConnectionFactory.getInstance(<Configuration>).");
        }
        Response response = connection.POST("/rs/rounds/ads", round.toString());
        if (response.getStatus() == 200){
            String entity = response.readEntity(String.class);
            Document document = Document.parse(entity);
            String id = document.getString("roundId");
            System.out.println("=== saved new round with ID: " + id);
            round.setId(id);
            return round;
        }
        return null;
    }

    public boolean roundExist(String playerId, String courseId, DayOfEvent dayOfEvent){
        if (connection == null){
            throw new IllegalStateException("Connection Factory not initialized. Use ConnectionFactory.getInstance(<Configuration>).");
        }
        StringBuffer sb = new StringBuffer("/rs/rounds/search?");
        sb.append("playerid=").append(playerId);
        sb.append("&courseid=").append(courseId);
        sb.append("&year=").append(dayOfEvent.getYear());
        sb.append("&month=").append(dayOfEvent.getMonth());
        sb.append("&day=").append(dayOfEvent.getDay());
        Response response = connection.GET(sb.toString());
        String entity = response.readEntity(String.class);
        if (response.getStatus() == 404){
            return false;
        }
        return true;
    }

    public boolean setTournament(String roundid, String tournamentid){
        if (connection == null){
            throw new IllegalStateException("Connection Factory not initialized. Use ConnectionFactory.getInstance(<Configuration>).");
        }
        Response response = connection.POST("/rs/rounds/set/" + roundid + "?tournamentid=" + tournamentid, null);
        if (response.getStatus() == 200){
            String entity = response.readEntity(String.class);
            Document document = Document.parse(entity);
            logger.info(document.toJson());
            String status = document.getString("status");
            if (status.equalsIgnoreCase("success")){
                logger.info("=== round " + roundid + " added to the tournament: " + tournamentid);
                return true;
            }
        }
        return false;
    }
}