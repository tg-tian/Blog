package com.tg.blog.backend.common;

import lombok.Data;

/**
 * 统一响应结果封装类
 * 用于封装所有API接口的返回结果
 * 
 * @param <T> 响应数据的类型
 * @author TG
 * @since 1.0.0
 */
@Data
public class ResponseEntity<T> {
    
    /** 响应状态码 */
    private Integer code;
    
    /** 响应消息 */
    private String message;
    
    /** 响应数据 */
    private T data;

    /**
     * 创建成功响应
     * @param data 响应数据
     * @param <T> 数据类型
     * @return 成功响应对象
     */
    public static <T> ResponseEntity<T> success(T data) {
        ResponseEntity<T> result = new ResponseEntity<>();
        result.setCode(200);
        result.setMessage("success");
        result.setData(data);
        return result;
    }

    /**
     * 创建失败响应
     * @param code 错误状态码
     * @param message 错误消息
     * @param <T> 数据类型
     * @return 失败响应对象
     */
    public static <T> ResponseEntity<T> failure(Integer code, String message) {
        ResponseEntity<T> result = new ResponseEntity<>();
        result.setCode(code);
        result.setMessage(message);
        result.setData(null);
        return result;
    }
}
