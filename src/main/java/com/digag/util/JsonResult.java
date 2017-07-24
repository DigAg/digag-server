package com.digag.util;

/**
 * Created by Yuicon on 2017/7/2.
 * https://segmentfault.com/u/yuicon
 */
public class JsonResult<T> {

    private final boolean success;

    private final T data;

    private final String error;

    private JsonResult(JsonResultBuilder<T> builder) {
        this.success = builder.success;
        this.data = builder.data;
        this.error = builder.error;
    }

    public static <T>JsonResult.JsonResultBuilder<T> builder(){
        return new JsonResultBuilder<>();
    }

    public boolean isSuccess() {
        return success;
    }

    public T getData() {
        return data;
    }

    public String getError() {
        return error;
    }

    @Override
    public String toString() {
        return "JsonResult{" +
                "success=" + success +
                ", data=" + data +
                ", error='" + error + '\'' +
                '}';
    }

    public static final class JsonResultBuilder<T> {

        private boolean success;

        private T data;

        private String error;

        private JsonResultBuilder() {

        }

        public JsonResultBuilder error(String error) {
            this.error = error;
            this.success = false;
            return this;
        }

        public JsonResultBuilder data(T data) {
            this.data = data;
            this.success = true;
            return this;
        }

        public JsonResult build() {
            return new JsonResult<>(this);
        }
    }

    public static void main(String[] args) {
        System.out.print(JsonResult.<String>builder().data("asd").build().toString());
    }

}
