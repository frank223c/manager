package com.suny.association.controller.core;

import com.suny.association.annotation.SystemControllerLog;
import com.suny.association.controller.BaseController;
import com.suny.association.entity.dto.BootstrapTableResultDTO;
import com.suny.association.entity.dto.ResultDTO;
import com.suny.association.entity.po.Member;
import com.suny.association.entity.po.PunchRecord;
import com.suny.association.entity.vo.ConditionMap;
import com.suny.association.enums.ResponseCodeEnum;
import com.suny.association.service.interfaces.core.IMemberService;
import com.suny.association.service.interfaces.core.IPunchRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

/**
 * Comments:  考勤记录控制器
 *
 * @author :   孙建荣
 *         Create Date: 2017/04/11 13:16
 */
@RequestMapping("/punchLog")
@Controller
public class PunchRecordController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(PunchRecordController.class);
    private final IPunchRecordService punchRecordService;
    private final IMemberService memberService;

    @Autowired
    public PunchRecordController(IPunchRecordService punchRecordService, IMemberService memberService) {
        this.punchRecordService = punchRecordService;
        this.memberService = memberService;
    }

    @SystemControllerLog(description = "考勤操作")
    @RequestMapping(value = "/update.action", method = RequestMethod.POST)
    @ResponseBody
    public ResultDTO update(HttpServletRequest request, @RequestParam("punchMemberId") Integer punchMemberId) {
        /*   获取session中的Member信息，用来判断用户是否恶意操作   */
        Member member = (Member) request.getSession().getAttribute("member");
        if (member == null) {
            logger.error("987，没有登录，无法操作");
            return ResultDTO.failureResult(ResponseCodeEnum.NO_LOGIN_IN);
        }
        if (!Objects.equals(member.getMemberId(), punchMemberId)) {
            logger.info("211,要操作的Member主键Id与Session中保存的Member主键Id不同，属于恶意操作");
            return ResultDTO.failureResult(ResponseCodeEnum.MALICIOUS_OPERATION);
        } else {
            Member databaseMember = memberService.selectById(punchMemberId);
            if (databaseMember == null) {
                logger.info("005，数据库没有查询考勤的成员Id，恶意操作");
                return ResultDTO.failureResult(ResponseCodeEnum.SELECT_FAILURE);
            } else {
                PunchRecord punchRecord = punchRecordService.queryByMemberIdAndDate(punchMemberId);
                if (punchRecord != null) {
                    if (!punchRecord.getPunchTypeId().getPunchTypeId().equals(0)) {
                        logger.warn("212,{}今天已经签到过了", member.getMemberName());
                        return ResultDTO.failureResult(ResponseCodeEnum.REPEAT_PUNCH);
                    } else {
                        logger.info("{}开始进行考勤", member.getMemberName());
                        int successRow = punchRecordService.updatePunch(punchMemberId, punchRecord.getPunchRecordId());
                        if (successRow == 0) {
                            logger.info("215,{}考勤失败", member.getMemberName());
                            return ResultDTO.successResult(ResponseCodeEnum.PUNCH_FAIL);
                        }
                    }
                } else {
                        /*   系统设计是考勤当天首先由管理员开始考勤，然后系统自动给每一个成员添加一条缺勤记录   */
                    logger.warn("213,管理员还没有开启签到！");
                    return ResultDTO.failureResult(ResponseCodeEnum.TIME_NOT_REACH);
                }
            }
        }
        logger.info("214,{}考勤成功", member.getMemberName());
        return ResultDTO.successResult(ResponseCodeEnum.PUNCH_SUCCESS);
    }


    /**
     * 开启今天的签到，给每一个符合规则的成员添加一条缺勤记录.
     * 这里开启签到必须要在协会的角色大于一个数值，这里我们先固定一个数值，
     * 就是对应我数据库里面的值，这个角色不能够小于2，也就是至少是部长级别才能开始，
     * 因为在我数据库里面角色小于2的都是新成员，新成员肯定不能具备开启签到功能
     *
     * @param memberId 开启今日考勤的管理员姓名
     * @return 开启今日签到的操作结果
     */
    @SystemControllerLog(description = "管理员开启今日考勤操作")
    @RequestMapping(value = "/insert.action", method = RequestMethod.POST)
    @ResponseBody
    public ResultDTO insert(HttpServletRequest request, @RequestParam("memberId") Long memberId) {

        Member sessionMember = (Member) request.getSession().getAttribute("member");
        if (sessionMember == null) {
            logger.warn("987,用户没有登录进行操作");
            return ResultDTO.failureResult(ResponseCodeEnum.NO_LOGIN_IN);
        } else {
            Member dataBaseMember = memberService.selectById(memberId);
            if (dataBaseMember == null) {
                logger.info("005，数据库没有查询到开启签到的管理员信息，恶意操作");
                return ResultDTO.failureResult(ResponseCodeEnum.SELECT_FAILURE);
            } else if (!Objects.equals(dataBaseMember.getMemberId(), sessionMember.getMemberId())) {
                logger.info("211,要操作的Member主键Id与Session中保存的Member主键Id不同，属于恶意操作");
                return ResultDTO.failureResult(ResponseCodeEnum.MALICIOUS_OPERATION);
            } else if (dataBaseMember.getMemberRoles().getMemberRoleId() <= 2) {
                logger.warn("206,部门角色太低,没有权限进行开启签到功能");
                return ResultDTO.failureResult(ResponseCodeEnum.LIMIT_MEMBER_MANAGER);
            } else if (!punchRecordService.queryByPunchDate().isEmpty()) {
                logger.warn("212,重复签到，今天已经开启签到过了");
                return ResultDTO.failureResult(ResponseCodeEnum.REPEAT_PUNCH);
            } else {
                int successRow = punchRecordService.batchInsertsPunchRecord();
                if (successRow == 0) {
                    logger.error("009,开启考勤失败");
                    return ResultDTO.failureResult(ResponseCodeEnum.ADD_FAIL_ALL);
                }
                return ResultDTO.successResult(ResponseCodeEnum.ADD_SUCCESS_ALL);
            }
        }
    }


    @RequestMapping(value = "/selectByParam.action", method = RequestMethod.GET)
    @ResponseBody
    public BootstrapTableResultDTO selectByParam(@RequestParam(value = "offset", required = false, defaultValue = "0") int offset,
                                         @RequestParam(value = "limit", required = false, defaultValue = "10") int limit) {
        ConditionMap<PunchRecord> conditionMap = new ConditionMap<>(new PunchRecord(), offset, limit);
        List<PunchRecord> punchRecordList = punchRecordService.selectByParam(conditionMap);
        int total = punchRecordService.selectCount();
        return new BootstrapTableResultDTO(total, punchRecordList);
    }

    @SystemControllerLog(description = "查看考勤记录页面")
    @RequestMapping(value = "/index.html", method = RequestMethod.GET)
    public String index() {
        return "/punchLog/punchLogList";
    }
}
