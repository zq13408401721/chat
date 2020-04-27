package com.mychat.module.apis;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UploadApi {

    @POST("file_upload.php")
    @Multipart
    Call<ResponseBody> uploadFile(@Part("key") RequestBody key, @Part MultipartBody.Part file);
}
