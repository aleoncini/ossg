package org.beccaria.ossg.model;

import java.util.Comparator;

public class MedalResultComparator implements Comparator<Round> {
    public int compare(Round r1, Round r2) {
        int diff = r2.getScorecard().getNetMedal() - r1.getScorecard().getNetMedal();
        if (diff == 0){
            diff = r2.getScorecard().getPhcp() - r1.getScorecard().getPhcp();
        }
        return diff;
    }
}
