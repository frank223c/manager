package com.suny.association.web.validate.code.image;

/**************************************
 *  Description  
 *  @author 孙建荣
 *  @date 18-1-28.下午3:24
 *  @version 1.0
 **************************************/
public class ImageCodeProperties {

    /**
     * 验证码图片的长度
     */
    private int width;
    /**
     * 验证码图片的高度
     */
    private int height;
    /**
     * 图形验证码的长度
     */
    private int length;

    public ImageCodeProperties() {
        this.width = 90;
        this.height = 20;
        this.length = 4;
    }

    public ImageCodeProperties(int width, int height, int length) {
        this.width = width;
        this.height = height;
        this.length = length;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}
