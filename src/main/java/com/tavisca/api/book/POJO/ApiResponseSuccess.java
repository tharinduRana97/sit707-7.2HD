package com.tavisca.api.book.POJO;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


public class ApiResponseSuccess extends ApiResponse {
    private Object data;

    public ApiResponseSuccess(Object data) {
        this.data = data;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
