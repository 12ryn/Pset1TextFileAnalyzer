package com.example.pset1textfileanalyzer;

import android.annotation.SuppressLint;
import android.content.Intent;
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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

        top5WordsButton = (Button) findViewById(R.id.topFiveButton);

        top5WordsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                nameOfFile = String.valueOf(fileName.getText());
                ArrayList<String> cmWords = new ArrayList<>();
                Map<String, Integer> map = new HashMap<>();

                TextAnalyze analysis = new TextAnalyze(cmWords, map, nameOfFile);

                String str = analysis.getTop5();

                answer.setText(str);

//                Top5Analyzer topFive = new Top5Analyzer(nameOfFile, "commonWords.txt");
//
//                String[] topFiveWords = topFive.topFiveWords();
//                int[] topFiveFrequencies = topFive.topFiveWordsFrequency();
//
//                displayString = "The top " + topFiveWords.length + " words in "+ fileName +" are:\n";
//                printTopFive(topFiveWords, topFiveFrequencies, 0);
//
//                answer.setText(displayString);

            }
        });

        topUniqueWordsButton = (Button) findViewById(R.id.topUniqueButton);

        topUniqueWordsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                nameOfFile = String.valueOf(fileName.getText());
                ArrayList<String> cmWords = new ArrayList<>();
                Map<String, Integer> map = new HashMap<>();

                TextAnalyze analysis = new TextAnalyze(cmWords, map, nameOfFile);

                answer.setText(analysis.countUniqueWords());
            }
        });

        sentenceCountButton = (Button) findViewById(R.id.sentenceCountButton);

        sentenceCountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                nameOfFile = String.valueOf(fileName.getText());
                ArrayList<String> cmWords = new ArrayList<>();
                Map<String, Integer> map = new HashMap<>();

                TextAnalyze analysis = new TextAnalyze(cmWords, map, nameOfFile);

                answer.setText(analysis.countTotalSentences());
            }
        });

        wordCountButton = (Button) findViewById(R.id.wordCountButton);

        wordCountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                nameOfFile = String.valueOf(fileName.getText());
                ArrayList<String> cmWords = new ArrayList<>();
                Map<String, Integer> map = new HashMap<>();

                TextAnalyze analysis = new TextAnalyze(cmWords, map, nameOfFile);

                answer.setText(analysis.countTotalWords());

            }
        });

    }
}