package com.suny.association.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Comments:   在controller中存在大量重复代码，所以抽取出来组成一个静态的公共方法，组合成符合Bootstrap-table需要的分页结果
 * @author :   孙建荣
 * Create Date: 2017/04/15 19:40
 */
public class ConversionUtil {
    private static final Logger logger = LoggerFactory.getLogger(ConversionUtil.class);

    /**
     * 把前端传来的int类型的状态码转换为boolean类型的状态
     *
     * @param status int类型状态码
     * @return boolean类型状态
     */
    private static Boolean convertToBooleanStatus(int status) {
        Boolean booleanStatus;
        if (status == 0){
            booleanStatus = false;
        }
        else if (status == 1) {
            booleanStatus = true;
        } else {
            booleanStatus = null;
        }
        return booleanStatus;
    }

    /**
     * 封装map查询条件
     *
     * @param args 查询条件的参数
     * @return map查询查询条件
     */
    public static Map<Object, Object> convertToCriteriaMap(Object... args) {
        Map<Object, Object> criteriaMap = new HashMap<>(16);
        criteriaMap.put("offset", args[0]);
        criteriaMap.put("limit", args[1]);
        if (args.length == 3) {
            criteriaMap.put("status", ConversionUtil.convertToBooleanStatus((Integer) args[2]));
            return criteriaMap;
        } else if (args.length == 4) {
            criteriaMap.put("departmentname", args[2]);
            criteriaMap.put("status", ConversionUtil.convertToBooleanStatus((Integer) args[3]));
            return criteriaMap;
        }
        return criteriaMap;
    }

    /**
     * 把查询出来的结果变成符合Bootstrap-table需要的服务器端分页数据
     *
     * @param resultList 查询出来的结果集数据
     * @param totalCount 查询出来的总行数
     * @return Mpa集合
     */
    public static Map<Object, Object> convertToBootstrapTableResult(List resultList, int totalCount) {
        Map<Object, Object> tableDate = new HashMap<>(16);
        if (!resultList.isEmpty()) {
            tableDate.put("rows", resultList);
            tableDate.put("total", totalCount);
            return tableDate;
        }
        tableDate.put("rows", null);
        tableDate.put("total", 0);
        return tableDate;
    }


}










