package com.fyp.confit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PresentationClass implements Serializable{
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("genre")
    @Expose
    private String genre;
    @SerializedName("env")
    @Expose
    private String env;
    @SerializedName("audience")
    @Expose
    private String audience;
    @SerializedName("loudness_score")
    @Expose
    private String loudness_score;
    @SerializedName("clarity_score")
    @Expose
    private String clarity_score;
    @SerializedName("emotional_app_score")
    @Expose
    private String emotional_app_score;
    @SerializedName("fluency_score")
    @Expose
    private String fluency_score;
    @SerializedName("total_score")
    @Expose
    private String total_score;

    String avg_loudness;
    String percentage_mistakes;
    String fearful;
    String serious;
    String happy;
    String sad;
    String speechrate;
    String noOfPauses;
    String pauseDuration;
    String totalDuration;


    public PresentationClass(int id, String title, String genre, String env, String audience, String loudness_score, String clarity_score, String emotional_app_score, String fluency_score, String total_score, String avg_loudness, String percentage_mistakes, String fearful, String serious, String happy, String sad, String speechrate, String noOfPauses, String pauseDuration, String totalDuration) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.env = env;
        this.audience = audience;
        this.loudness_score = loudness_score;
        this.clarity_score = clarity_score;
        this.emotional_app_score = emotional_app_score;
        this.fluency_score = fluency_score;
        this.total_score = total_score;
        this.avg_loudness = avg_loudness;
        this.percentage_mistakes = percentage_mistakes;
        this.fearful = fearful;
        this.serious = serious;
        this.happy = happy;
        this.sad = sad;
        this.speechrate = speechrate;
        this.noOfPauses = noOfPauses;
        this.pauseDuration = pauseDuration;
        this.totalDuration = totalDuration;
    }

    public PresentationClass(int id, String title, String genre, String env, String audience, String loudness_score, String clarity_score, String emotional_app_score, String fluency_score, String total_score) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.env = env;
        this.audience = audience;
        this.loudness_score = loudness_score;
        this.clarity_score = clarity_score;
        this.emotional_app_score = emotional_app_score;
        this.fluency_score = fluency_score;
        this.total_score = total_score;
    }

    public PresentationClass(String title, String genre, String env, String audience, String loudness_score, String clarity_score, String emotional_app_score, String fluency_score, String total_score) {
        this.title = title;
        this.genre = genre;
        this.env = env;
        this.audience = audience;
        this.loudness_score = loudness_score;
        this.clarity_score = clarity_score;
        this.emotional_app_score = emotional_app_score;
        this.fluency_score = fluency_score;
        this.total_score = total_score;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public String getAudience() {
        return audience;
    }

    public void setAudience(String audience) {
        this.audience = audience;
    }

    public String getLoudness_score() {
        return loudness_score;
    }

    public void setLoudness_score(String loudness_score) {
        this.loudness_score = loudness_score;
    }

    public String getClarity_score() {
        return clarity_score;
    }

    public void setClarity_score(String clarity_score) {
        this.clarity_score = clarity_score;
    }

    public String getEmotional_app_score() {
        return emotional_app_score;
    }

    public void setEmotional_app_score(String emotional_app_score) {
        this.emotional_app_score = emotional_app_score;
    }

    public String getFluency_score() {
        return fluency_score;
    }

    public void setFluency_score(String fluency_score) {
        this.fluency_score = fluency_score;
    }

    public String getTotal_score() {
        return total_score;
    }

    public void setTotal_score(String total_score) {
        this.total_score = total_score;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getFearful() {
        return fearful;
    }

    public void setFearful(String fearful) {
        this.fearful = fearful;
    }

    public String getSerious() {
        return serious;
    }

    public void setSerious(String serious) {
        this.serious = serious;
    }

    public String getHappy() {
        return happy;
    }

    public void setHappy(String happy) {
        this.happy = happy;
    }

    public String getSad() {
        return sad;
    }

    public void setSad(String sad) {
        this.sad = sad;
    }

    public String getAvg_loudness() {
        return avg_loudness;
    }

    public void setAvg_loudness(String avg_loudness) {
        this.avg_loudness = avg_loudness;
    }

    public String getPercentage_mistakes() {
        return percentage_mistakes;
    }

    public void setPercentage_mistakes(String percentage_mistakes) {
        this.percentage_mistakes = percentage_mistakes;
    }

    public String getSpeechrate() {
        return speechrate;
    }

    public void setSpeechrate(String speechrate) {
        this.speechrate = speechrate;
    }

    public String getNoOfPauses() {
        return noOfPauses;
    }

    public void setNoOfPauses(String noOfPauses) {
        this.noOfPauses = noOfPauses;
    }

    public String getPauseDuration() {
        return pauseDuration;
    }

    public void setPauseDuration(String pauseDuration) {
        this.pauseDuration = pauseDuration;
    }

    public String getTotalDuration() {
        return totalDuration;
    }

    public void setTotalDuration(String totalDuration) {
        this.totalDuration = totalDuration;
    }
}
