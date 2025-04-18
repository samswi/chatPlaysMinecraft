package org.samswi.client;

import java.util.*;

public class VotesSystem {
    public static int votingTimeLeft = 0;
    public static int votingCheckTime = 0;

    public static Map<String, Integer> votesList = new LinkedHashMap<>();
    public static String[] top5votes = new String[5];


    public static void increment(String input){
        if(!votesList.containsKey(input)) {votesList.put(input, 1);}
        else {votesList.put(input, votesList.get(input) + 1);}
    }

    public static void getTop(){
        top5votes = getTop5(votesList);

    }



    //chatgpt code (i couldnt be bothered)
    public static String[] getTop5(Map<String, Integer> map) {
        return map.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(5)
                .map(Map.Entry::getKey)
                .toArray(String[]::new);
    }



}
