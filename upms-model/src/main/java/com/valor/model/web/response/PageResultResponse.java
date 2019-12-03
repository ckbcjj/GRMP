package com.valor.model.web.response;

import java.util.List;

public class PageResultResponse<T> {

    private int code; //状态码, 0表示成功

    private String msg="all";  //提示信息

    private long count; // 总数量, bootstrapTable是total

    private List<T> data; // 当前数据, bootstrapTable是rows

    public PageResultResponse(List<T> rows) {
        this.data = rows;
        this.count = rows.size();
        this.code = 0;
    }

    public PageResultResponse(long total, List<T> rows) {
        this.count = total;
        this.data = rows;
        this.code = 0;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
        this.count = data.size();
    }

    @Override
    public String toString() {
        return "PageResultResponse{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", count=" + count +
                ", dataSize=" + data.size() +
                '}';
    }
}
