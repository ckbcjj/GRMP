package com.valor.manager.api;

import com.mfc.config.ConfigTools3;
import com.valor.manager.common.HttpTools;
import com.valor.manager.sdk.security.UserAccess;
import com.valor.model.exception.UpmsHttpCode;
import com.valor.model.web.response.JsonResultResponse;
import com.valor.model.web.response.PageResultResponse;
import com.valor.model.web.response.UpmsResponse;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class ManagerWebRetrofit {

    private static Logger logger = LoggerFactory.getLogger(ManagerWebRetrofit.class);
    private static ManagerWebRetrofit managerWebRetrofit = null;
    private Retrofit retrofit = null;
    private UpmsManagerWebRequest upmsManagerWebRequest = null;
    private String remoteUrl = "";

    private ManagerWebRetrofit(String remoteUrl) {
        this.remoteUrl = remoteUrl;
        logger.info("upms valor remote valor address: {}", remoteUrl);
        initRtrofitClient();
    }

    public void initRtrofitClient() {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();
                        Request.Builder requestBuilder = original.newBuilder();
                        String user = UserAccess.getLoginUser().getUserName();
                        if (user != null) {
                            requestBuilder.header("reqUserName", user);
                        }
                        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
                        String clientIp = HttpTools.getRemoteHost(req);
                        requestBuilder.header("clientIp", clientIp);
                        Request request = requestBuilder.build();
                        return chain.proceed(request);
                    }
                }).build();

        // 初始化Retrofit
        retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(remoteUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        if (retrofit != null) {
            upmsManagerWebRequest = retrofit.create(UpmsManagerWebRequest.class);
        } else {
            logger.error("init retrofit error");
        }
    }

    public static ManagerWebRetrofit getInstance() {
        if (managerWebRetrofit == null) {
            synchronized (ManagerWebRetrofit.class) {
                if (managerWebRetrofit == null) {
                    managerWebRetrofit = new ManagerWebRetrofit(ConfigTools3.getConfigAsString("upms.api.base.url"));
                }
            }
        }
        return managerWebRetrofit;
    }

    public UpmsManagerWebRequest getWebRequest() {
        if (upmsManagerWebRequest == null) {
            logger.error("upms web request init error");
        }
        return upmsManagerWebRequest;
    }

    public JsonResultResponse excuteReqJson(Call<UpmsResponse<Boolean>> enityCall) {
        String url = enityCall.request().url().toString();
        try {
            Response<UpmsResponse<Boolean>> response = enityCall.execute();
            if (response.isSuccessful()) {
                UpmsResponse<Boolean> adResponse = response.body();
                if (String.valueOf(UpmsHttpCode.OK).equals(adResponse.getStatus())) {
                    return JsonResultResponse.ok();
                } else {
                    int code = UpmsHttpCode.SERVER_ERROR;
                    try {
                        code = Integer.parseInt(adResponse.getStatus().replace("_", ""));
                    } catch (Exception ex) {
                    }
                    return JsonResultResponse.error(code, adResponse.getMsg());
                }
            } else {
                return JsonResultResponse.error(response.code(), response.message());
            }
        } catch (Exception e) {
            logger.error("request operate url: {} error", url, e);
            return JsonResultResponse.error(UpmsHttpCode.SERVER_ERROR_MSG);
        }
    }

    public <T> PageResultResponse<T> excuteQuryReq(Call<UpmsResponse<PageResultResponse<T>>> enityCall) {
        PageResultResponse<T> resultResponse = new PageResultResponse(new ArrayList());
        String url = enityCall.request().url().toString();
        try {
            Response<UpmsResponse<PageResultResponse<T>>> response = enityCall.execute();
            if (response.isSuccessful()) {
                UpmsResponse<PageResultResponse<T>> adResponse = response.body();
                if (String.valueOf(UpmsHttpCode.OK).equals(adResponse.getStatus())) {
                    resultResponse = adResponse.getResult();
                } else {
                    int code = UpmsHttpCode.SERVER_ERROR;
                    try {
                        code = Integer.parseInt(adResponse.getStatus().replace("_", ""));
                    } catch (Exception ex) {
                    }
                    resultResponse.setCode(code); //service error
                    resultResponse.setMsg(adResponse.getMsg());
                }
            } else {
                resultResponse.setCode(response.code()); //okhttp error
                resultResponse.setMsg(response.message());
            }
        } catch (Exception e) {
            logger.error("request manage url: {} error", url, e);
            resultResponse.setCode(UpmsHttpCode.WEB_ERROR); //web error
            resultResponse.setMsg(UpmsHttpCode.SERVER_ERROR_MSG);
        }
        return resultResponse;
    }
}
