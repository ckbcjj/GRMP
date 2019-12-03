package com.valor.manager.sdk.service;

import com.mfc.config.ConfigTools3;
import com.valor.manager.sdk.web.request.AuthRequest;
import com.valor.manager.sdk.web.response.UpmsDetailsResponse;
import com.valor.manager.sdk.web.response.UpmsResponse;
import okhttp3.OkHttpClient;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

@Service
public class UpmsService {

    private static Logger logger = LoggerFactory.getLogger(UpmsService.class);

    protected UpmsRetrofitService getApiService() {
        return apiService;
    }

    private UpmsRetrofitService apiService;

    @PostConstruct
    private void init() {
        String baseUrl = null;
        try {
            baseUrl = ConfigTools3.getConfigAsString("upms.api.base.url");
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .build();
            Retrofit retrofit = new Retrofit.Builder()
                    .client(client)
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            apiService = retrofit.create(UpmsRetrofitService.class);
        } catch (Exception ex) {
            logger.error("init error. baseUrl:[{}]", baseUrl, ex);
        }
    }

    protected <T> UpmsResponse<T> excuteReq(Call<UpmsResponse<T>> enityCall) {
        UpmsResponse<T> upmsResponse = new UpmsResponse();
        String url = enityCall.request().url().toString();
        try {
            Response<UpmsResponse<T>> response = enityCall.execute();
            if (response.isSuccessful()) {
                upmsResponse = response.body();
            } else {
                upmsResponse.setStatus(response.code() + "");
                upmsResponse.setMsg(response.message());
            }
        } catch (Exception ex) {
            upmsResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value() + "");
            upmsResponse.setMsg(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            logger.error("excuteReq error. url:{}", url, ex);
        }
        return upmsResponse;
    }

    protected String getRemoteHost(HttpServletRequest request) {
        String remoteHost = null;
        remoteHost = request.getHeader("x-forwarded-for");
        if (remoteHost == null || remoteHost.length() == 0 || "unknown".equalsIgnoreCase(remoteHost)) {
            remoteHost = request.getHeader("CF-Connecting-IP");
        }
        if (remoteHost == null || remoteHost.length() == 0 || "unknown".equalsIgnoreCase(remoteHost)) {
            remoteHost = request.getHeader("Proxy-Client-IP");
        }
        if (remoteHost == null || remoteHost.length() == 0 || "unknown".equalsIgnoreCase(remoteHost)) {
            remoteHost = request.getHeader("WL-Proxy-Client-IP");
        }
        if (remoteHost == null || remoteHost.length() == 0 || "unknown".equalsIgnoreCase(remoteHost)) {
            remoteHost = request.getRemoteAddr();
        }
        if (remoteHost != null && remoteHost.length() > 15) { //"***.***.***.***".length() = 15
            if (remoteHost.indexOf(",") > 0) {
                remoteHost = remoteHost.substring(0, remoteHost.indexOf(","));
            }
        }
        if (StringUtils.isEmpty(remoteHost)) {
            remoteHost = "unknown host";
        }
        return remoteHost;
    }
}
