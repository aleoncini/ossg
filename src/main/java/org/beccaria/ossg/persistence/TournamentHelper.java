package org.beccaria.ossg.persistence;

import org.beccaria.ossg.model.*;
import org.bson.Document;

import java.util.*;

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
        System.out.println("=== " + day + "." + month + "." + year);
        Collection<Tournament> tournaments = new ArrayList<Tournament>();
        Document filter = new Document();
        if (day > 0){
            filter.append("dayOfEvent.day",day);
        }
        if (month > 0){
            filter.append("dayOfEvent.month",month);
        }
        if (year > 0){
            filter.append("dayOfEvent.year",year);
        }
        System.out.println(">>> " + filter.toJson());
        Iterator<Document> docs = DBTools.searchByFilter(COLLECTION_NAME,filter);
        while (docs.hasNext()){
            tournaments.add(new Tournament().build(docs.next()));
        }
        return tournaments;
    }

}