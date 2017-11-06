package com.suny.association.controller.system;

import com.suny.association.annotation.SystemControllerLog;
import com.suny.association.controller.BaseController;
import com.suny.association.entity.dto.BootstrapTableResultDTO;
import com.suny.association.entity.po.OperationLog;
import com.suny.association.entity.vo.ConditionMap;
import com.suny.association.service.interfaces.system.IOperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Comments:   操作记录控制器
 * @author :   孙建荣
 * Create Date: 2017/04/10 13:20
 */
@Controller
@RequestMapping(value = "/operations/log")
public class OperationLogController extends BaseController {

    private final IOperationLogService operationLogService;

    @Autowired
    public OperationLogController(IOperationLogService operationLogService) {
        this.operationLogService = operationLogService;
    }

    /**
     * 带查询条件的操作记录查询
     *
     * @param offset 从第几条开始查询
     * @param limit  查几个数据
     * @return 带查询条件的数据
     */
    @RequestMapping(value = "/list.action", method = RequestMethod.GET)
    @ResponseBody
    public BootstrapTableResultDTO query(@RequestParam(value = "offset", required = false, defaultValue = "0") int offset,
                                         @RequestParam(value = "limit", required = false, defaultValue = "10") int limit) {
        ConditionMap<OperationLog> conditionMap=new ConditionMap<>(new OperationLog(),offset,limit);
        List<OperationLog> operationLogList = operationLogService.selectByParam(conditionMap);
        int total = operationLogService.selectCount();
        return new BootstrapTableResultDTO(total,operationLogList);
    }

    @SystemControllerLog(description = "查看操作记录页面")
    @RequestMapping(value = "/index.html", method = RequestMethod.GET)
    public String index() {
        return "/operations/operationList";
    }
}
