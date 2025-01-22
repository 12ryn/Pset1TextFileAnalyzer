package com.example.pset1textfileanalyzer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Intent;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public class TextFileAnalyzerScreen extends AppCompatActivity {

    private String nameOfFile;
    private static String displayString;
    private Button backButton;
    private Button pieChartButton;
    private Button top5WordsButton;
    private Button topUniqueWordsButton;
    private Button sentenceCountButton;
    private Button wordCountButton;

    private EditText fileName;

    private TextView answer;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_text_file_analyzer_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        answer = (TextView) findViewById(R.id.textView3);

        fileName = (EditText) findViewById(R.id.editTextText);

        backButton = (Button) findViewById(R.id.button4);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TextFileAnalyzerScreen.this, MainActivity.class);
                startActivity(intent);
            }
        });

        pieChartButton = (Button) findViewById(R.id.pieChartButton);

        pieChartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(TextFileAnalyzerScreen.this, PieChart.class);
                startActivity(intent);
            }
        });

        top5WordsButton = (Button) findViewById(R.id.topFiveButton); // flag  1

        top5WordsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String file = fileName.getText().toString().trim();

                if(file.isEmpty()){

                    answer.setText("No file found!");

                } else {

                    String textContent = readAsset(file);
                    String commonWordsContent = readAsset("commonWords.txt");

                    List<String> commonWords = Arrays.asList(commonWordsContent.split("\\s+"));
                    Set<String> commonWordSet = new HashSet<>(commonWords);
                    answer.setText(analyzer(textContent, commonWordSet, 1));

                }

            }
        });

        topUniqueWordsButton = (Button) findViewById(R.id.topUniqueButton); // flag  2

        topUniqueWordsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String file = fileName.getText().toString().trim();

                if(file.isEmpty()){

                    answer.setText("No file found!");

                } else {

                    String textContent = readAsset(file);
                    String commonWordsContent = readAsset("commonWords.txt");

                    List<String> commonWords = Arrays.asList(commonWordsContent.split("\\s+"));
                    Set<String> commonWordSet = new HashSet<>(commonWords);
                    answer.setText(analyzer(textContent, commonWordSet, 2));

                }

            }
        });

        sentenceCountButton = (Button) findViewById(R.id.sentenceCountButton); // flag 3

        sentenceCountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String file = fileName.getText().toString().trim();

                if(file.isEmpty()){

                    answer.setText("No file found!");

                } else {

                    String textContent = readAsset(file);
                    String commonWordsContent = readAsset("commonWords.txt");

                    List<String> commonWords = Arrays.asList(commonWordsContent.split("\\s+"));
                    Set<String> commonWordSet = new HashSet<>(commonWords);
                    answer.setText(analyzer(textContent, commonWordSet, 3));

                }

            }
        });

        wordCountButton = (Button) findViewById(R.id.wordCountButton); // flag 4

        wordCountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String file = fileName.getText().toString().trim();

                if(file.isEmpty()){

                    answer.setText("No file found!");

                } else {

                    String textContent = readAsset(file);
                    String commonWordsContent = readAsset("commonWords.txt");

                    List<String> commonWords = Arrays.asList(commonWordsContent.split("\\s+"));
                    Set<String> commonWordSet = new HashSet<>(commonWords);
                    answer.setText(analyzer(textContent, commonWordSet, 4));

                }

            }
        });

    }

    public String readAsset(String fileName){

        if(fileName.toLowerCase().endsWith(".txt")){

            AssetManager assetManager = getAssets();
            StringBuilder content = new StringBuilder();

            try (InputStream inputStream = assetManager.open(fileName);
                 BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }
            } catch (IOException e) {
                return "Error reading text file: " + e.getMessage();
            }

            return content.toString();

        } else if (fileName.toLowerCase().endsWith(".pdf")){

            AssetManager assetManager = getAssets();
            StringBuilder content = new StringBuilder();

            try (InputStream inputStream = assetManager.open(fileName)){

                PDDocument document = new PDDocument();
                document.load(inputStream);
                PDFTextStripper stripper = new PDFTextStripper();
                content.append(stripper.getText(document));
                document.close();

            } catch (IOException e) {
                return "Error reading PDF file: " + e.getMessage();
            }

            return content.toString();

        } else {

            return "Error: File format unsupported";

        }

    }

    public String analyzer(String text, Set<String> commonWords, int flag) {

        // make text lowercase for the ease of identification

        text = text.toLowerCase().replaceAll("[^a-zA-Z0-9.\'\\s]", "");

        // insert words into arrays for easy implementation

        String[] words = text.split("\\s+");
        String[] sentences = text.split("[.!?]\\s*");
        int wordCount = words.length;
        int sentenceCount = sentences.length;

        // unique words

        Set<String> uniqueWords = Arrays.stream(words).filter(word -> !commonWords.contains(word)).collect(Collectors.toSet());
        int uniqueWordCount = uniqueWords.size();
        String uniqueWordOutput = "These include: " + String.join(", ", uniqueWords);

        // word frequencies

        Map<String, Integer> wordFrequencies = new HashMap<>();
        for (String word : words) {
            if (!commonWords.contains(word)) {
                wordFrequencies.put(word, wordFrequencies.getOrDefault(word, 0) + 1);
            }
        }

        List<Map.Entry<String, Integer>> topFiveWords = wordFrequencies.entrySet().stream().sorted((e1, e2) -> {
            int freqCompare = e2.getValue().compareTo(e1.getValue());
            return (freqCompare != 0) ? freqCompare : e1.getKey().compareTo(e2.getKey());
        }).limit(5).collect(Collectors.toList());

        StringBuilder result = new StringBuilder();

        if (flag == 0) { // pie chart flag



        } else if (flag == 1) { // top 5 words flag

            result.append("Top 5 Words: \n");
            for (Map.Entry<String, Integer> entry : topFiveWords) {
                result.append(entry.getKey()).append(": ").append(entry.getValue()).append(" instances\n");
            }

        } else if (flag == 2) { // unique word flag

            result.append("There are ").append(uniqueWordCount).append(" unique words in this file.\n");
            result.append(uniqueWordOutput);

        } else if (flag == 3) { // sentence count flag

            result.append("There are ").append(sentenceCount).append(" sentences in this file.");

        } else if (flag == 4) { // word count flag

            result.append("There are ").append(wordCount).append(" total words in this file.");

        }

        return String.valueOf(result);

    }
}