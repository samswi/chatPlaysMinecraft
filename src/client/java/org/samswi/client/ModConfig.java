package org.samswi.client;

import java.io.*;
import java.util.*;

public class ModConfig {
    public static Map<String, Integer> options = new LinkedHashMap<>();

    public static void setDefaults(){
        ModConfig.options.put("wtime", 20);
        ModConfig.options.put("stime", 20);
        ModConfig.options.put("atime", 20);
        ModConfig.options.put("dtime", 20);
        ModConfig.options.put("sprinttime", 20);
        ModConfig.options.put("breaktime", 200);
        ModConfig.options.put("usetime", 40);
        ModConfig.options.put("sneaktime", 100);
        ModConfig.options.put("allowCustomTimings", 1);
        ModConfig.options.put("votesmode", 1);
        ModConfig.options.put("votingtime", 60);
        ModConfig.options.put("votingcountingcd", 10);
    }

    public static void createFile() {
        File myConfigFile = new File("config/chatplaysmc.txt");
        System.out.println("Wow the config file was created at" + myConfigFile.getAbsolutePath());
        try {
            if(myConfigFile.createNewFile()){
                System.out.println("The config file was saved!");
            }
            setDefaults();
            saveOptionsToFile();

        } catch (Exception e) {
            System.out.println("Could not create the config file :(");
            throw new RuntimeException(e);
        }
    }

    public static void readFile(){
        File configFile = new File("config/chatplaysmc.txt");
        try {
            setDefaults();
            Scanner configReader = new Scanner(configFile);
            while (configReader.hasNextLine()) {
                String data = configReader.nextLine();
                System.out.println(data);
                if (!data.startsWith("#") && data.contains("=")) {
                    String[] splitted = data.split("=", 2);
                    ModConfig.options.put(splitted[0].trim(), Integer.valueOf(splitted[1].trim()));
                }
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void saveOptionsToFile(){
        try {
            BufferedWriter configWriter = new BufferedWriter(new FileWriter("config/chatplaysmc.txt"));
            configWriter.write("# Use this as a boolean (1 = true, 0 = false) to allow usage of custom timings i.e. 'w 50' to hold w for 50 ticks"); configWriter.newLine();
            configWriter.write("allowCustomTimings=" + ModConfig.options.get("allowCustomTimings")); configWriter.newLine(); configWriter.newLine();
            configWriter.write("wtime=" + ModConfig.options.get("wtime")); configWriter.newLine();
            configWriter.write("atime=" + ModConfig.options.get("atime")); configWriter.newLine();
            configWriter.write("stime=" + ModConfig.options.get("stime")); configWriter.newLine();
            configWriter.write("dtime=" + ModConfig.options.get("dtime")); configWriter.newLine();
            configWriter.write("sprinttime=" + ModConfig.options.get("sprinttime")); configWriter.newLine();
            configWriter.write("sneaktime=" + ModConfig.options.get("sneaktime")); configWriter.newLine();
            configWriter.write("breaktime=" + ModConfig.options.get("breaktime")); configWriter.newLine();
            configWriter.write("usetime=" + ModConfig.options.get("usetime")); configWriter.newLine(); configWriter.newLine();
            configWriter.write("votesmode=" + ModConfig.options.get("votesmode")); configWriter.newLine();
            configWriter.write("votingtime=" + ModConfig.options.get("votingtime")); configWriter.newLine();
            configWriter.write("votingcountingcd=" + ModConfig.options.get("votingcountingcd")); configWriter.newLine();
            configWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void updateValue(String key, int value){
        ModConfig.options.put(key, value);
        ModConfig.saveOptionsToFile();
    }

}
