package com.suny.association.web.controller.core;

import com.suny.association.annotation.SystemControllerLog;
import com.suny.association.web.controller.BaseController;
import com.suny.association.entity.dto.BootstrapTableResultDTO;
import com.suny.association.entity.po.CallbackResult;
import com.suny.association.entity.vo.ConditionMap;
import com.suny.association.service.interfaces.core.ICallbackResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Comments:   审批结果控制
 * @author :   孙建荣
 * Create Date: 2017/04/19 12:50
 */
@Controller
@RequestMapping("/punchLog/applyForResult")
public class CallbackResultController extends BaseController {

    private final ICallbackResultService callbackResultService;

    @Autowired
    public CallbackResultController(ICallbackResultService callbackResultService) {
        this.callbackResultService = callbackResultService;
    }

    /**
     * 带查询条件的查询审批结果记录
     *
     * @param offset 起始行
     * @param limit  查看几条
     * @return 分页的结果
     */
    @RequestMapping(value = "/selectByParam.action", method = RequestMethod.GET)
    @ResponseBody
    public BootstrapTableResultDTO selectByParam(@RequestParam(value = "offset", required = false, defaultValue = "0") int offset,
                                         @RequestParam(value = "limit", required = false, defaultValue = "10") int limit) {
        ConditionMap<CallbackResult> conditionMap=new ConditionMap<>(new CallbackResult(),offset,limit);
        List<CallbackResult> callbackResultList = callbackResultService.selectByParam(conditionMap);
        int total = callbackResultService.selectCountByParam(new CallbackResult());
        return new BootstrapTableResultDTO(total, callbackResultList);
    }


    /**
     * 异议考勤审批结果列表
     *
     * @return 当然是页面列表
     */
    @SystemControllerLog(description = "查看异议考勤审批结果页面")
    @RequestMapping(value = "/index.html", method = RequestMethod.GET)
    public String index() {
        return "/punchLog/applyForResult/result";
    }


}
