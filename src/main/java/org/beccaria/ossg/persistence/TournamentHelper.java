package org.beccaria.ossg.persistence;

import org.beccaria.ossg.model.Course;
import org.beccaria.ossg.model.Tournament;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.UUID;

public class TournamentHelper {
    public static String COLLECTION_NAME = "tournaments";

    public boolean save(Tournament tournament){
        if (tournament.getId() == null || tournament.getId().length() == 0){
            tournament.setId(UUID.randomUUID().toString().replace("-", ""));
        }
        return DBTools.save(COLLECTION_NAME, tournament.getDocument());
    }

    public Tournament get(String id){
        Document tournamentDocument = DBTools.getById(COLLECTION_NAME,id);
        if (tournamentDocument == null){
            return null;
        }
        return new Tournament().build(tournamentDocument);
    }

    public Collection<Tournament> search(int day, int month, int year){
        Collection<Tournament> tournaments = new ArrayList<Tournament>();
        Document filter = new Document()
                .append("dayOfEvent.day",day)
                .append("dayOfEvent.month",month)
                .append("dayOfEvent.year",year);
        Iterator<Document> docs = DBTools.searchByFilter(COLLECTION_NAME,filter);
        while (docs.hasNext()){
            tournaments.add(new Tournament().build(docs.next()));
        }
        return tournaments;
    }

    public Collection<Tournament> searchByName(String name){
        Collection<Tournament> tournaments = new ArrayList<Tournament>();
        Iterator<Document> docs = DBTools.search(COLLECTION_NAME,"name",name);
        while (docs.hasNext()){
            tournaments.add(new Tournament().build(docs.next()));
        }
        return tournaments;
    }
}
