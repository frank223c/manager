package com.suny.association.controller.core;

import com.suny.association.annotation.SystemControllerLog;
import com.suny.association.controller.BaseController;
import com.suny.association.entity.dto.BootstrapTableResultDTO;
import com.suny.association.entity.dto.JsonResultDTO;
import com.suny.association.entity.po.ApplicationMessage;
import com.suny.association.entity.po.CallbackResult;
import com.suny.association.entity.vo.ConditionMap;
import com.suny.association.enums.ResponseCodeEnum;
import com.suny.association.service.interfaces.core.IApplicationMessageService;
import com.suny.association.service.interfaces.core.ICallbackResultService;
import com.suny.association.service.interfaces.core.IMemberService;
import com.suny.association.service.interfaces.core.IPunchRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Comments:   异议考勤结果控制器
 * @author :   孙建荣
 * Create Date: 2017/04/16 20:55
 */
@Controller
@RequestMapping("/punchLog/applicationMessage")
public class ApplicationController extends BaseController {

    private final IApplicationMessageService applicationMessageService;
    private final IMemberService memberService;
    private final ICallbackResultService callbackResultService;
    private final IPunchRecordService punchRecordService;


    @Autowired
    public ApplicationController(IApplicationMessageService applicationMessageService, IMemberService memberService, ICallbackResultService callbackResultService, IPunchRecordService punchRecordService) {
        this.applicationMessageService = applicationMessageService;
        this.memberService = memberService;
        this.callbackResultService = callbackResultService;
        this.punchRecordService = punchRecordService;
    }

    /**
     * 对异议考勤进行审批
     *
     * @param memberId      审批的管理员
     * @param applicationId 对应的异议考勤记录
     * @param resultStatus  管理员审批的结果状态
     * @return 对应的操作结果json数据
     */
    @SystemControllerLog(description = "审批异议考勤记录")
    @RequestMapping(value = "/setResult.action", method = RequestMethod.POST)
    @ResponseBody
    public JsonResultDTO setResult(@RequestParam(value = "memberId") Integer memberId,
                                   @RequestParam(value = "applicationId") Integer applicationId,
                                   @RequestParam(value = "result") Boolean resultStatus) {
        if (memberId == 0 || applicationId == 0 || resultStatus == null) {
            return JsonResultDTO.failureResult(ResponseCodeEnum.FIELD_NULL);
        }
        // 这里判断是否有这个管理员，再判断这个管理员的角色是否大于一个可以操作考勤的角色
        if (memberService.selectById(memberId) == null && memberService.selectById(memberId).getMemberRoles().getMemberRoleId() < 3) {
            return JsonResultDTO.failureResult(ResponseCodeEnum.LIMIT_MEMBER_MANAGER);
        }
        // 检查是否有要审批的异议考勤记录，再判断这条异议考勤记录是否已经有了结果
        if (applicationMessageService.selectById(applicationId) == null || applicationMessageService.selectById(applicationId).getApplicationResult() != null) {
            return JsonResultDTO.failureResult(ResponseCodeEnum.SELECT_FAILURE);
        }
        // 获取对应的那条异议申请记录
        ApplicationMessage applicationMessage = applicationMessageService.selectById(applicationId);
        // 判断审批结果表里面是否有这条异议考勤的结果，如果有就说明已经审批过了
        if (callbackResultService.selectById(applicationMessage.getApplicationId()) != null) {
            return JsonResultDTO.successResult(ResponseCodeEnum.REPEAT_ADD);
        }
        if (!resultStatus) {
            // 插入一条失败的反馈结果
            CallbackResult falseResult = callbackResultService.makeUpCallBackResult(applicationMessage, memberId, false);
            callbackResultService.insert(falseResult);
            // 设置申请表中的审批结果
            applicationMessageService.updateApplyForResult(applicationMessage, falseResult);
            return JsonResultDTO.successResult(ResponseCodeEnum.UPDATE_SUCCESS);
        }
        // 新增一条成功反馈结果记录
        CallbackResult trueResult = callbackResultService.makeUpCallBackResult(applicationMessage, memberId, true);
        callbackResultService.insert(trueResult);
        //  获取对应的考勤记录，准备更新考勤类型
        punchRecordService.updatePunchType(applicationMessage.getPunchRecordId(), applicationMessage.getChangePunchType());
        // 给申请记录设置申请结果
        applicationMessageService.updateApplyForResult(applicationMessage, trueResult);
        return JsonResultDTO.successResult(ResponseCodeEnum.UPDATE_SUCCESS);
    }


    /**
     * 根据查询条件去查询记录
     *
     * @param offset 从第几条开始查询
     * @param limit  查询几条记录数
     * @return 查询出来的数据
     */
    @RequestMapping(value = "/selectByParam.action", method = RequestMethod.GET)
    @ResponseBody
    public BootstrapTableResultDTO selectByParam(@RequestParam(value = "offset", required = false, defaultValue = "0") int offset,
                                         @RequestParam(value = "limit", required = false, defaultValue = "10") int limit) {
        ApplicationMessage applicationMessage = new ApplicationMessage();
        ConditionMap<ApplicationMessage> conditionMap=new ConditionMap<>(applicationMessage,offset,limit);
        List<ApplicationMessage> punchRecordList = applicationMessageService.selectByParam(conditionMap);
        int total = applicationMessageService.selectCountByParam(applicationMessage);
        return new BootstrapTableResultDTO(total, punchRecordList);
    }

    /**
     * 审批考勤记录主页面
     *
     * @return 主页面
     */
    @SystemControllerLog(description = "查看异议考勤页面")
    @RequestMapping(value = "/index.html", method = RequestMethod.GET)
    public String index() {
        return "/punchLog/application/messageList";
    }
}
