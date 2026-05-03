package main;

import java.io.*;
import java.util.*;

public class ScoreManager {
    private static final String FILE = "scores.txt";

    public static void saveScore(int score) {
        List<Integer> scores = loadScores();
        scores.add(score);
        Collections.sort(scores, Collections.reverseOrder());
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE))) {
            for (int i = 0; i < Math.min(5, scores.size()); i++)
                pw.println(scores.get(i));
        } catch (IOException e) { e.printStackTrace(); }
    }

    public static List<Integer> loadScores() {
        List<Integer> scores = new ArrayList<>();
        try (Scanner sc = new Scanner(new File(FILE))) {
            while (sc.hasNextInt()) scores.add(sc.nextInt());
        } catch (IOException e) {}
        return scores;
    }
}
