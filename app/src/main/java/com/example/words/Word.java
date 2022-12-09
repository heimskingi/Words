package com.example.words;

public class Word {

    public String serbianWord, englishWord;
    public int wordPoints, userID;

    public Word(String serbianWord, String englishWord, int wordPoints, int userID) {
        this.serbianWord = serbianWord;
        this.englishWord = englishWord;
        this.wordPoints = wordPoints;
        this.userID = userID;
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

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
}
