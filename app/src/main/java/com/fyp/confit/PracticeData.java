package com.fyp.confit;
public class PracticeData {
    String id;
    String loudnessScore;
    String fluencyScore;
    String clarityScore;
    String emotionScore;

    public PracticeData(){
    }

    public PracticeData(String id, String loudnessScore, String fluencyScore, String clarityScore, String emotionScore) {
        this.id = id;
        this.loudnessScore = loudnessScore;
        this.fluencyScore = fluencyScore;
        this.clarityScore = clarityScore;
        this.emotionScore = emotionScore;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLoudnessScore() {
        return loudnessScore;
    }

    public void setLoudnessScore(String loudnessScore) {
        this.loudnessScore = loudnessScore;
    }

    public String getFluencyScore() {
        return fluencyScore;
    }

    public void setFluencyScore(String fluencyScore) {
        this.fluencyScore = fluencyScore;
    }

    public String getClarityScore() {
        return clarityScore;
    }

    public void setClarityScore(String clarityScore) {
        this.clarityScore = clarityScore;
    }

    public String getEmotionScore() {
        return emotionScore;
    }

    public void setEmotionScore(String emotionScore) {
        this.emotionScore = emotionScore;
    }
}
