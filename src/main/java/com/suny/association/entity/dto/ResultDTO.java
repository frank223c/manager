package com.suny.association.entity.dto;

/**
 * Comments:   封装自己的返回JSON数据格式
 * @author :   孙建荣
 * Create Date: 2017/03/17 16:01
 */
public class ResultDTO {


    /**
     *返回的状态
     */
    private Integer status;

    /**
     *给前端返回的内容
     */
    private String message;

    /**
     *返回给前端的数据
     */
    private Object data;

    /**
     * 发送成功的结果还有返回数据
     *
     * @param enumObject 传过来的枚举值
     * @param resultData 返回的数据
     * @return 结果与及数据
     */
    public static ResultDTO successResultAndData(Object enumObject, Object resultData) {
        ResultDTO jsonResult = setStatusAndMessage(enumObject);
        jsonResult.setData(resultData);
        return jsonResult;
    }

    /**
     * 就返回一个成功的结果
     *
     * @param enumObject 传过来的枚举值
     * @return 仅仅只有成功的结果
     */
    public static ResultDTO successResult(Object enumObject) {
        return setStatusAndMessage(enumObject);
    }

    /**
     * 就返回一个成功的结果
     *
     * @param enumObject 传过来的枚举值
     * @return 仅仅只有成功的结果
     */
    public static ResultDTO failureResult(Object enumObject) {
        return setStatusAndMessage(enumObject);
    }


    /**
     * 一个抽取出来的公共操作方法，用于设置响应的status跟message,依赖于枚举类的toString()方法
     *
     * @param enumObject 传过来的枚举值
     * @return JSONResponseUtil实体
     */
    private static ResultDTO setStatusAndMessage(Object enumObject) {
        ResultDTO jsonResult = new ResultDTO();
        //获取枚举值的toString方法后的字符值
        String message = enumObject.toString();
        // 返回的toString字符值中有一个状态码，下面把这个状态码抓取出来返回给前端
        int afterCode = message.lastIndexOf(',');
        String codeString = message.substring(0, afterCode);
        int codeNumber = Integer.parseInt(codeString);
        // 设置返回给前端的状态码与及消息
        jsonResult.setStatus(codeNumber);
        jsonResult.setMessage(message);
        return jsonResult;
    }



    private ResultDTO() {
    }


    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResultDTO{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
