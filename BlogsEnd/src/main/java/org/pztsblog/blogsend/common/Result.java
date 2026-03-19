package org.pztsblog.blogsend.common;

import lombok.Data;

@Data
public class Result<T> {
    private Integer code; // 状态码：200代表成功，400/500代表失败等
    private String message; // 提示信息
    private T data; // 真正要返回的数据，使用泛型 T 可以装入任何类型的数据

    // 成功时的快捷方法 (无数据)
    public static <T> Result<T> success() {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMessage("操作成功");
        return result;
    }

    // 成功时的快捷方法 (带数据)
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMessage("操作成功");
        result.setData(data);
        return result;
    }

    // 失败时的快捷方法
    public static <T> Result<T> error(Integer code, String message) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }
}