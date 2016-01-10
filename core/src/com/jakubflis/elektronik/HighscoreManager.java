package com.jakubflis.elektronik;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class HighscoreManager {
    public static String username;
    public static float totalTime;
    public static float totalBlowingTime;
    public static Long tempBlowingTime;

    public static void saveHighscoreFile(float percentageScore) {
        String filename = "Highscore_" + username + "_" + getCurrentDate("yyyy_MM_dd_HH_mm_ss") + ".txt";
        float score = mapPercentageScore(percentageScore);

        try {
            PrintWriter writer = new PrintWriter(filename, "UTF-8");
            writer.println("" + totalTime + "," + totalBlowingTime + "," + username + "," + getCurrentDate("yyyy/MM/dd HH:mm:ss") + "," + score);
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private static String getCurrentDate(String format) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        Calendar cal = Calendar.getInstance();

        return dateFormat.format(cal.getTime());
    }

    private static float mapPercentageScore(float percentageScore) {
        return (percentageScore / 100.0f) * Constants.MAX_POINTS;
    }
}
