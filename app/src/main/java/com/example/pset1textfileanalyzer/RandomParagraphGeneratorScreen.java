package com.example.pset1textfileanalyzer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class RandomParagraphGeneratorScreen extends AppCompatActivity {

    private Button backButton;
    private Button generate;
    private EditText temperature;
    private EditText fileName;

    private TextView answer;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_random_paragraph_generator_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        backButton = (Button) findViewById(R.id.button3);
        generate = (Button) findViewById(R.id.generationTrigger);
        temperature = (EditText) findViewById(R.id.temperatureInput);
        fileName = (EditText) findViewById(R.id.fileName);
        answer = (TextView) findViewById(R.id.outputText);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RandomParagraphGeneratorScreen.this, MainActivity.class);
                startActivity(intent);
            }
        });

        generate.setOnClickListener(new View.OnClickListener() {
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
                    int temp = Integer.parseInt(temperature.getText().toString().trim());
                    answer.setText(randomParagraph(textContent, commonWordSet, temp));

                }

            }
        });

    }

    public String readAsset(String fileName){

        AssetManager assetManager = getAssets();
        StringBuilder content = new StringBuilder(); // sb is used to accumulate content

        if(fileName.toLowerCase().endsWith(".txt")){

            try (InputStream inputStream = assetManager.open(fileName);
                 BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }

            } catch (IOException e) { // IO exception will occur if text file is not read
                return "Error reading text file: " + e.getMessage(); // get the message from the error and display it to user
            }

            return content.toString();

        } else if (fileName.toLowerCase().endsWith(".pdf")){

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

            return "Please only input .txt or .pdf files!";

        }

    }

    public String randomParagraph(String text, Set<String> commonWords, int temperature) {

        if(temperature > 100 || temperature < 0){

            return "Invalid Input!";

        }

        // make text lowercase for ease of identification
        text = text.toLowerCase().replaceAll("[^a-zA-Z0-9.\'\\s]", "");

        String[] words = text.split("\\s+"); // put words into array

        // prepare unique words
        Set<String> uniqueWords = Arrays.stream(words) // convert array into stream
                .filter(word -> !commonWords.contains(word)) // prevent commonWords from entering stream
                .collect(Collectors.toSet());


        StringBuilder paragraph = new StringBuilder();
        Random random = new Random();
        int wordCount = 250;

        for (int i = 0; i < wordCount; i++) {

            boolean useUniqueWord = random.nextInt(100) < temperature; // whether a unique or common word is used is based on temperature

            String word = "";
            if (useUniqueWord && !uniqueWords.isEmpty()) {
                // random unique word
                word = uniqueWords.toArray(new String[0])[random.nextInt(uniqueWords.size())]; // logic is derived from src: GPT 3.5 on POE
            } else {
                // random common word
                word = commonWords.stream().skip(random.nextInt(commonWords.size())).findFirst().orElse("the"); // if common words are empty, "the" will be used by default
            }

            paragraph.append(word).append(" ");
        }

        return paragraph.toString().trim();
    }



}