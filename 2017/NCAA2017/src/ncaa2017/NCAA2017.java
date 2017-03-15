/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ncaa2017;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author Timothy Smith and Blake Lasky
 */
public class NCAA2017 {
    
    static class Team implements Comparable {
        public String name;
        public int numGames;
        public double totRate;
        public double ptDiff;
        public double avgRate;
        
        public Team(String n, double pd) {
            name = n;
            numGames = 0;
            totRate = 0;
            ptDiff = pd;
        }
        
        public void calc() {
            if (numGames == 0) avgRate = -100;
            else avgRate = totRate/numGames;
            totRate = 0;
            numGames = 0;
        }

        @Override
        public int compareTo(Object t) {
            Team t2 = (Team)t;
            return Double.compare(avgRate, t2.avgRate);
        }
        
    }
    
    static class Game {
        public String t1, t2;
        public int diff;
        
        public Game(String team1, String team2, int d) {
            t1 = team1;
            t2 = team2;
            diff = d;
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException {
        // TODO code application logic here
        
        Scanner scan = new Scanner(new File("src/data.csv"));
        scan.useDelimiter(",");
        
        ArrayList<Game> games = new ArrayList<>();
        ArrayList<Team> teamList = new ArrayList<>();
        HashMap<String, Team> teamMap = new HashMap<>();
        int totDiff = 0;
        int totGames = 0;
        
        int r05 = 0;
        int r610 = 0;
        int r1115 = 0;
        int r1620 = 0;
        int over20 = 0;
        
        double max = 0;
        double min = 0;
        int max3 = 0;
        int max4 = 0;
        int max5 = 0;
        
        HashSet<String> final68 = new HashSet<>();
        for (int i = 0; i < 1; ++i) {
            Scanner s = new Scanner(new File("src/bracketteams.txt"));
            while (s.hasNextLine()) {
                final68.add(s.nextLine());
            }
        }
        
        while (scan.hasNext()) {
            String team1 = scan.next();
            String team2 = scan.next();
            int pointDiff = scan.nextInt();

            games.add(new Game(team1, team2, pointDiff));
            
            
            double t1Diff = scan.nextDouble();
            
            if (!teamMap.containsKey(team1)) {
                teamMap.put(team1, new Team(team1, t1Diff));
                teamList.add(teamMap.get(team1));
            }
            
            double t2Diff = scan.nextDouble();
            
            if (!teamMap.containsKey(team2)) {
                teamMap.put(team2, new Team(team2, t2Diff));
                teamList.add(teamMap.get(team2));
            }
            
            
            scan.nextLine();
        }
        
        for (Game game : games) {
            
            Team t1 = teamMap.get(game.t1);
            Team t2 = teamMap.get(game.t2);
            if (!final68.contains(t1.name) || !final68.contains(t2.name)) continue;
            
            int t1D =  game.diff;
            int t2D = -game.diff;
            
            double t1Rate, t2Rate;
            
            t1Rate = t1D;
            t2Rate = t2D;
            
            
            t1.totRate += t1Rate;
            t1.numGames++;
        
            t2.totRate += t2Rate;
            t2.numGames++;
        }
        
        
        for (Team team : teamList) {
            team.calc();
        }
        
        Collections.sort(teamList, Collections.reverseOrder());
        
        
        
        for (Game game : games) {

            Team t1 = teamMap.get(game.t1);
            Team t2 = teamMap.get(game.t2);
            if (!final68.contains(t1.name) && !final68.contains(t2.name)) continue;

            int t1D =  game.diff;
            int t2D = -game.diff;

            double t1Rate, t2Rate;

            t1Rate = t1D + t2.ptDiff;
            t2Rate = t2D + t1.ptDiff;


            t1.totRate += t1Rate;
            t1.numGames++;

            t2.totRate += t2Rate;
            t2.numGames++;
        }

        for (Team team : teamList) {
            team.calc();
        }
        
        
        for (int i = 0; i < 1000; ++i) {
            for (Game game : games) {

                Team t1 = teamMap.get(game.t1);
                Team t2 = teamMap.get(game.t2);
                if (!final68.contains(t1.name) && !final68.contains(t2.name)) continue;

                int t1D =  game.diff;
                int t2D = -game.diff;

                double t1Rate, t2Rate;

                t1Rate = t1D + t2.avgRate;
                t2Rate = t2D + t1.avgRate;


                t1.totRate += t1Rate;
                t1.numGames++;

                t2.totRate += t2Rate;
                t2.numGames++;
            }

            for (Team team : teamList) {
                team.calc();
            }
        }
        
        Collections.sort(teamList, Collections.reverseOrder());
        for (Team team : teamList) {
            System.out.print(team.name + ": ");
            System.out.println(((int)((team.avgRate)*100))/100.0);
        }
        
        System.out.println(r05);
        System.out.println(r610);
        System.out.println(r1115);
        System.out.println(r1620);
        System.out.println(over20);
        System.out.println(totDiff);
        System.out.println(totGames);
        System.out.println(games.size());
        System.out.println(min);
        System.out.println(max);
    }
    
}
