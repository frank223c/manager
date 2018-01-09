package com.suny.association.entity.dto;

import java.io.Serializable;
import java.util.List;

/**************************************
 *  Description  封装符合BootstrapTable需要的内容
 *  @author 孙建荣
 *  @date 17-11-2.下午5:27
 *  @version 1.0
 **************************************/
public class BootstrapTableResultDTO implements Serializable{

    /**
     * 返回给BootstrapTable的数据总数
     */
    private Integer total;

    /**
     * 返回给BootstrapTable的数据
     */
    private List  rows;

    /**
     * 有参数的构造方法,不允许无参构造,必须要有结果
     * @param total  返回的数据总量
     * @param rows   返回的数据
     */
    public BootstrapTableResultDTO(Integer total, List rows) {
        this.total = total;
        this.rows = rows;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "BootstrapTableResultDTO{" +
                "total=" + total +
                ", rows=" + rows +
                '}';
    }
}
