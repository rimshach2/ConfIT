package com.fyp.confit;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UploadAPIs {
    @Multipart
    @POST("/api/v1/score_presentation")
    Call<extra> uploadAudio(@Part MultipartBody.Part file/*, @Part("desc") RequestBody requestBody*/);

    @Multipart
    @POST("/api/v1/score_presentation")
    Call<extra> uploadAudioAndScript(@Part MultipartBody.Part file, @Part MultipartBody.Part file1/*, @Part("desc") RequestBody requestBody*/);

}
