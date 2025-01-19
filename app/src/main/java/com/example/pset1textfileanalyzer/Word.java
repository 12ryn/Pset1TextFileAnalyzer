package com.example.pset1textfileanalyzer;

public class Word {
    private String word;
    private int amount;

    public Word(String word){

        amount = 1;
        this.word = word;
    }

    public void increment() {

        amount++;
    }

    public void setCount(int count) {

        this.amount = count;
    }

    public int getCount() {

        return amount;
    }

    public String getWord() {

        return word;
    }

    public void setWord(String word) {

        this.word = word;
    }


}
