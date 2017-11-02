package com.suny.association.controller.system;

import com.suny.association.annotation.SystemControllerLog;
import com.suny.association.controller.BaseController;
import com.suny.association.entity.dto.BootstrapTableResult;
import com.suny.association.entity.po.LoginHistory;
import com.suny.association.entity.vo.ConditionMap;
import com.suny.association.service.interfaces.IAccountService;
import com.suny.association.service.interfaces.system.ILoginHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * Comments:    登录记录控制器
 * @author :   孙建荣
 * Create Date: 2017/04/09 14:44
 */
@Controller
@RequestMapping("/session")
public class SessionController extends BaseController {

    private final ILoginHistoryService loginHistoryService;

    @Autowired
    public SessionController(ILoginHistoryService loginHistoryService, IAccountService accountService) {
        this.loginHistoryService = loginHistoryService;
    }

    @RequestMapping(value = "/list.action", method = RequestMethod.GET)
    @ResponseBody
    public BootstrapTableResult query(@RequestParam(value = "offset", required = false, defaultValue = "0") int offset,
                                     @RequestParam(value = "limit", required = false, defaultValue = "10") int limit) {
        ConditionMap<LoginHistory> conditionMap=new ConditionMap<>(new LoginHistory(),0,10);
        List<LoginHistory> loginHistoryList = loginHistoryService.selectByParam(conditionMap);
        int total = loginHistoryService.selectCount();
        return new BootstrapTableResult(total,loginHistoryList);
    }

    @SystemControllerLog(description = "查询指定账号登录记录")
    @RequestMapping(value = "/queryByMemberId.action", method = RequestMethod.GET)
    @ResponseBody
    public Map<Object, Object> queryById(@RequestParam("memberId") int memberId) {
        return null;
    }

    @SystemControllerLog(description = "查看登录记录页面")
    @RequestMapping(value = "/index.html", method = RequestMethod.GET)
    public String index() {
        return "/session/sessionList";
    }
}



















