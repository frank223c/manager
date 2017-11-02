package com.suny.association.entity.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**************************************
 *  Description  封装符合BootstrapTable需要的内容
 *  @author 孙建荣
 *  @date 17-11-2.下午5:27
 *  @version 1.0
 **************************************/
public class BootstrapTableResult implements Serializable{

    /**
     * 返回给BootstrapTable的数据总数
     */
    private int total;

    /**
     * 返回给BootstrapTable的数据
     */
    private List  rows=new ArrayList(16);

    /**
     * 禁止无参构造方法
     */
    private  BootstrapTableResult() {
    }

    /**
     * 有参数的构造方法,不允许无参构造,必须要有结果
     * @param total  返回的数据总量
     * @param rows   返回的数据
     */
    public BootstrapTableResult(int total, List rows) {
        this.total = total;
        this.rows = rows;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List getRows() {
        return rows;
    }

    public void setRows(List rows) {
        this.rows = rows;
    }
}
