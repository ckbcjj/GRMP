package com.valor.server.db;

import javax.persistence.*;

@Entity
@Table(name = "sys_log")
public class SysLogDao extends AbstractLMI {
    private long id;
    private String clientIp;
    private String requestUrl;
    private String operateUser;
    private long operatingTime;
    private String reqDetail;
    private String respDetail;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column(name = "client_ip", length = 32)
    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    @Column(name = "req_url") //default 255
    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    @Column(name = "opera_user", length = 32)
    public String getOperateUser() {
        return operateUser;
    }

    public void setOperateUser(String operateUser) {
        this.operateUser = operateUser;
    }

    @Column(name = "opera_time")
    public long getOperatingTime() {
        return operatingTime;
    }

    public void setOperatingTime(long operatingTime) {
        this.operatingTime = operatingTime;
    }

    @Column(name = "req_detail", length = 1024)
    public String getReqDetail() {
        return reqDetail;
    }

    public void setReqDetail(String reqDetail) {
        this.reqDetail = reqDetail;
    }

    @Column(name = "resp_detail", length = 1024)
    public String getRespDetail() {
        return respDetail;
    }

    public void setRespDetail(String respDetail) {
        this.respDetail = respDetail;
    }
}
