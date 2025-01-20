package com.example.pset1textfileanalyzer;

import java.util.*;
import java.io.*;

public class TextAnalyze {

    private ArrayList<String> commonWords;
    private Map<String, Integer> map;

    private String fileName;

    public TextAnalyze(ArrayList<String> commonWords, Map<String, Integer> map, String fileName) {
        this.commonWords = commonWords;
        this.map = map;
        this.fileName = fileName;
    }
    // New method to read files
    private void readFiles() {
        readCommonWords();
        readMainTextFile();
    }

    private void readCommonWords() {
        try (InputStream inputStream = getClass().getResourceAsStream("/commonWords.txt");
             Scanner common = new Scanner(inputStream)) {
            while (common.hasNext()) {
                String commonWord = common.next().toLowerCase();
                commonWords.add(commonWord);
            }
        } catch (NullPointerException e) {
            System.err.println("Error: Common words file not found.");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Error reading common words file.");
            e.printStackTrace();
        }
    }

    private void readMainTextFile() {
        try (InputStream inputStream = getClass().getResourceAsStream(fileName);
             Scanner scanner = new Scanner(inputStream)) {
            while (scanner.hasNext()) {
                String word = scanner.next().toLowerCase().replaceAll("[^a-z]", ""); // Remove non-alphabetic characters
                if (!commonWords.contains(word) && !word.isEmpty()) {
                    map.put(word, map.getOrDefault(word, 0) + 1); // Increment word count
                }
            }
        } catch (NullPointerException e) {
            System.err.println("Error: Text file not found.");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Error reading text file.");
            e.printStackTrace();
        }
    }

    public String getTop5() {
        readFiles();

        // entries by value (word frequency) in descending order
        List<Map.Entry<String, Integer>> wordList = new ArrayList<>(map.entrySet());
        wordList.sort((a, b) -> b.getValue().compareTo(a.getValue())); // Compare by word frequency

        if (wordList.isEmpty()) { // case where no significant words are found

            return "No significant words found in the text.";
        }

        // Build the result string
        StringBuilder result = new StringBuilder("Top 5 words with the most occurrences:\n");
        for (int i = 0; i < Math.min(5, wordList.size()); i++) {
            result.append(wordList.get(i).getKey());
            result.append(": ");
            result.append(wordList.get(i).getValue());
            result.append("\n");
        }

        return result.toString();
    }

    // Method to count total words
    public int countTotalWords() {
        readFiles(); // Read files before counting
        return map.values().stream().mapToInt(Integer::intValue).sum();
    }

    // Method to count total sentences
    public int countTotalSentences() {
        int sentenceCount = 0;
        try (InputStream inputStream = getClass().getResourceAsStream(fileName);
             Scanner scanner = new Scanner(inputStream)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                // Consider a sentence to end with '.', '!', or '?'
                sentenceCount += line.split("[.!?]").length;
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            System.out.println("Error: Text file not found.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return sentenceCount;
    }

    // Method to count unique words and display some of them
    public String countUniqueWords() {
        readFiles(); // Read files before counting unique words
        Set<String> uniqueWords = map.keySet();
        StringBuilder result = new StringBuilder("Total unique words: " + uniqueWords.size() + "\nSome unique words:\n");

        int count = 0;
        for (String word : uniqueWords) {
            result.append(word).append("\n");
            count++;
            if (count >= 5) break; // Display up to 5 unique words
        }

        return result.toString();
    }
}