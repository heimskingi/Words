package com.example.words;

public class Word {

    public String serbianWord, englishWord;
    public int wordPoints, level;

    public Word(){}

    public Word(String serbianWord, String englishWord, int wordPoints, int level) {
        this.serbianWord = serbianWord;
        this.englishWord = englishWord;
        this.wordPoints = wordPoints;
        this.level = level;
    }

    @Override
    public String toString() {
        return "Word{" +
                "serbianWord='" + serbianWord + '\'' +
                ", englishWord='" + englishWord + '\'' +
                ", wordPoints=" + wordPoints +
                ", level=" + level +
                '}';
    }

    public String getSerbianWord() {
        return serbianWord;
    }

    public void setSerbianWord(String serbianWord) {
        this.serbianWord = serbianWord;
    }

    public String getEnglishWord() {
        return englishWord;
    }

    public void setEnglishWord(String englishWord) {
        this.englishWord = englishWord;
    }

    public int getWordPoints() {
        return wordPoints;
    }

    public void setWordPoints(int wordPoints) {
        this.wordPoints = wordPoints;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
