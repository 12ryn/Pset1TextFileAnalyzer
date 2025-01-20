package com.example.pset1textfileanalyzer;

import java.util.*;
import java.io.*;

public class TextAnalyze {
    private List<String> words;
    private List<Integer> counts;

    // Constructor to load common words and analyze the text
    public TextAnalyze(String commonWordsFilePath, String textFilePath) {
        words = new ArrayList<>();
        counts = new ArrayList<>();
        List<String> commonWords = loadCommonWords(commonWordsFilePath);
        countWords(textFilePath, commonWords);
    }

    // Load common words from a file
    private List<String> loadCommonWords(String filePath) {
        List<String> commonWords = new ArrayList<>();
        try (Scanner common = new Scanner(new File(filePath))) {
            while (common.hasNext()) {
                String commonWord = common.next().toLowerCase();
                commonWords.add(commonWord);
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error: Common words file not found at " + filePath);
        }
        return commonWords;
    }

    // Count words in the target text file, excluding common words
    private void countWords(String filePath, List<String> commonWords) {
        Map<String, Integer> wordFrequencyMap = new HashMap<>();
        try (Scanner scanner = new Scanner(new File(filePath))) {
            while (scanner.hasNext()) {
                // Extract words, keeping valid characters like apostrophes for contraction handling
                String word = scanner.next().toLowerCase().replaceAll("[^a-z']", "");
                if (!commonWords.contains(word) && !word.isEmpty()) {
                    wordFrequencyMap.put(word, wordFrequencyMap.getOrDefault(word, 0) + 1);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error: Text file not found at " + filePath);
        }

        // Convert the map to lists for sorting
        List<Map.Entry<String, Integer>> entryList = new ArrayList<>(wordFrequencyMap.entrySet());

        // Sort the entries by their frequencies in descending order
        entryList.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));

        // Populate the words and counts lists
        for (Map.Entry<String, Integer> entry : entryList) {
            words.add(entry.getKey());
            counts.add(entry.getValue());
        }
    }

    // Method to get the top 'n' words with the most occurrences
    public String printTopWords(int n) {
        StringBuilder result = new StringBuilder();
        result.append("Top ").append(n).append(" words with the most occurrences:\n");
        for (int i = 0; i < Math.min(n, words.size()); i++) {
            result.append(words.get(i)).append(": ").append(counts.get(i)).append("\n");
        }
        return result.toString();
    }

    // Getter for word list (optional, for further processing)
    public List<String> getWords() {
        return words;
    }

    // Getter for counts list (optional, for further processing)
    public List<Integer> getCounts() {
        return counts;
    }
}