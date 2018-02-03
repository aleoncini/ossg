package org.ossg.remote.client;

import org.ossg.model.DayOfEvent;
import org.ossg.model.Tournament;
import org.ossg.remote.helpers.TournamentHelper;

public class TestConnection {

    public static void main(String[] args ) {

        System.out.println("=====================================================================");

        Configuration c = new Configuration()
                .setAuthenticated()
                .setHost("leoncini.hopto.org")
                .setPort(80)
                .setProtocol(Configuration.SECURE_HTTP)
                .setUsername("admin")
                .setPassword("ero1Tem@");

        ConnectionFactory factory = ConnectionFactory.getFactory(c);

        /*
        System.out.println("=== getting root...");
        Response response = factory.getConnection().GET("/");
        System.out.println("=== status: " + response.getStatus());

        System.out.println("=== getting players rest service...");
        response = factory.getConnection().GET("/rs/players");
        System.out.println("=== status: " + response.getStatus());

        response.close();

        Player p = new PlayerHelper().getPlayer("brankac");
        System.out.println("\n=== " + p.toString());

        Player nuovo = new Player().setName("Pinco Pallino");
        Player synced = new PlayerHelper().savePlayer(nuovo);
        System.out.println("\n=== " + synced.toString());
        */

        Tournament t = new Tournament().setTitle("La coppa del nonno").setDayOfEvent(new DayOfEvent().today());
        System.out.println("\n=== adding test tournament...");
        Tournament nt = new TournamentHelper().saveTournament(t);
        System.out.println("=== test tournament ID: " + nt.getId());


        System.out.println("\n=== done.");
        System.out.println("=====================================================================");
    }

}
