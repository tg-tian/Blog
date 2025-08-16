package com.tg.blog.backend.common;

import lombok.Data;

@Data
public class ResponseEntity<T> {
    private Integer code;
    private String message;
    private T data;

    public static <T> ResponseEntity<T> success(T data) {
        ResponseEntity<T> result = new ResponseEntity<>();
        result.setCode(200);
        result.setMessage("success");
        result.setData(data);
        return result;
    }

    public static <T> ResponseEntity<T> failure(Integer code, String message) {
        ResponseEntity<T> result = new ResponseEntity<>();
        result.setCode(code);
        result.setMessage(message);
        result.setData(null);
        return result;
    }
}
