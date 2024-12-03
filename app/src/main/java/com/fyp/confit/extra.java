package com.fyp.confit;

import com.google.gson.JsonElement;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

public class extra {

    @SerializedName("loudness_score")
    @Expose
    private String loudness_score;

    @SerializedName("data")
    @Expose
    private JsonElement data;

    @SerializedName("clarity_score")
    @Expose
    private String clarity_score;
    @SerializedName("emotional_app_score")
    @Expose
    private String emotional_app_score;
    @SerializedName("fluency_score")
    @Expose
    private String fluency_score;

    public extra(JsonElement data) {
        this.data = data;
    }

    public JsonElement getData() {
        return data;
    }

    public void setData(JsonElement data) {
        this.data = data;
    }

    public extra(String loudness_score, String clarity_score, String emotional_app_score, String fluency_score) {
        this.loudness_score = loudness_score;
        this.clarity_score = clarity_score;
        this.emotional_app_score = emotional_app_score;
        this.fluency_score = fluency_score;
    }

    public extra() {

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
}
