package org.beccaria.ossg.persistence;

import org.beccaria.ossg.model.Player;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.UUID;

public class PlayerHelper {
    public static String COLLECTION_NAME = "players";

    public boolean save(Player player){
        if (player.getId() == null || player.getId().length() == 0){
            player.setId(UUID.randomUUID().toString().replace("-", ""));
        }
        return DBTools.save(COLLECTION_NAME, player.getDocument());
    }

    public Player getById(String id){
        Document pDocument = DBTools.getById(COLLECTION_NAME,id);
        if (pDocument == null){
            return null;
        }
        return new Player().build(pDocument);
    }

    public Player getByEmail(String email){
        Document pDocument = DBTools.getByField(COLLECTION_NAME,"email", email);
        if (pDocument == null){
            return null;
        }
        return new Player().build(pDocument);
    }

    public Collection<Player> searchByName(String name){
        Collection<Player> players = new ArrayList<Player>();
        Iterator<Document> docs = DBTools.search(COLLECTION_NAME,"name",name);
        while (docs.hasNext()){
            players.add(new Player().build(docs.next()));
        }
        return players;
    }
}
