package com.valor.manager.sdk.service;

import com.valor.manager.sdk.web.request.AuthRequest;
import com.valor.manager.sdk.web.response.UpmsDetailsResponse;
import com.valor.manager.sdk.web.response.UpmsResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UpmsRetrofitService {

    @POST("/api/upms/upmsDetails/upmsAuths/v1")
    Call<UpmsResponse<UpmsDetailsResponse>> upmsAuths(@Body AuthRequest args);
}
