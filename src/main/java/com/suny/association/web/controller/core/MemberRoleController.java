package com.suny.association.web.controller.core;

import com.suny.association.annotation.SystemControllerLog;
import com.suny.association.web.controller.BaseController;
import com.suny.association.entity.dto.BootstrapTableResultDTO;
import com.suny.association.entity.dto.ResultDTO;
import com.suny.association.entity.po.MemberRoles;
import com.suny.association.entity.vo.ConditionMap;
import com.suny.association.enums.CommonEnum;
import com.suny.association.enums.FormEnum;
import com.suny.association.service.interfaces.core.IMemberRolesService;
import com.suny.association.service.interfaces.core.IMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * Comments:   协会角色管理
 *
 * @author :   孙建荣
 *         Create Date: 2017/04/22 22:34
 */
@Controller
@RequestMapping("/member/role")
public class MemberRoleController extends BaseController {
    private static final Integer ROLE_NAME_MAX_LENGTH = 30;
    private static final Integer ROLE_ID_MAX_LENGTH = 2;
    private final IMemberRolesService memberRolesService;
    private final IMemberService memberService;

    @Autowired
    public MemberRoleController(IMemberRolesService memberRolesService, IMemberService memberService) {
        this.memberRolesService = memberRolesService;
        this.memberService = memberService;
    }

    @SystemControllerLog(description = "删除成员角色")
    @RequestMapping(value = "/delete.action/{memberRoleId}", method = RequestMethod.GET)
    @ResponseBody
    public ResultDTO delete(@PathVariable("memberRoleId") Integer memberRoleId) {
        // 过滤特殊字符，防止XSS注入
        Integer escapeId = Integer.valueOf(HtmlUtils.htmlEscape(String.valueOf(memberRoleId)));
        if (escapeId.toString().length() > ROLE_ID_MAX_LENGTH) {
            return ResultDTO.failureResult(FormEnum.FIELD_LENGTH_WRONG);
        }
        // 1.查询数据库中是否有这个角色
        if (memberRolesService.selectById(escapeId) == null) {
            return ResultDTO.failureResult(CommonEnum.DELETE_FAILURE);
        }
        // 2.判断数据库里面是不是有成员引用了这个角色,引用了就不能被删除
        if (!memberService.selectByMemberRoleId(escapeId).isEmpty()) {
            return ResultDTO.failureResult(CommonEnum.HAVE_QUOTE);
        }
        memberRolesService.deleteById(escapeId);
        return ResultDTO.successResult(CommonEnum.DELETE_SUCCESS);
    }

    @SystemControllerLog(description = "更新成员角色")
    @ResponseBody
    @RequestMapping(value = "/update.action", method = RequestMethod.POST)
    public ResultDTO update(@RequestBody MemberRoles memberRoles) {
        // 过滤特殊字符，防止XSS注入
        String escapeName = HtmlUtils.htmlEscape(memberRoles.getMemberRoleName());
        Integer escapeId = Integer.valueOf(HtmlUtils.htmlEscape(String.valueOf(memberRoles.getMemberRoleId())));
        if (escapeName.length() > ROLE_NAME_MAX_LENGTH || escapeId.toString().length() > ROLE_ID_MAX_LENGTH) {
            return ResultDTO.failureResult(FormEnum.FIELD_LENGTH_WRONG);
        }
        // 1.首先检查数据库是否存在要更新的数据记录
        if (memberRolesService.selectById(escapeId) == null) {
            return ResultDTO.failureResult(CommonEnum.SELECT_FAILURE);
        }
        // 2.再检查角色名字是否有空值
        if ("".equals(escapeName)) {
            return ResultDTO.failureResult(FormEnum.FIELD_NULL);
        }
        // 3. 查询数据库中是否已经有存在的值了,如果存在则拒绝更新
        if (memberRolesService.selectByName(escapeName) != null) {
            return ResultDTO.failureResult(CommonEnum.REPEAT_ADD);
        }
        memberRoles.setMemberRoleName(escapeName);
        memberRoles.setMemberRoleId(escapeId);
        memberRolesService.update(memberRoles);
        return ResultDTO.successResult(CommonEnum.UPDATE_SUCCESS);
    }

    @RequestMapping(value = "/update.html/{memberRoleId}", method = RequestMethod.GET)
    public ModelAndView updatePage(@PathVariable int memberRoleId
            , ModelAndView modelAndView) {
        MemberRoles memberRoles = memberRolesService.selectById(memberRoleId);
        if (memberRoles == null) {
            memberRoles = memberRolesService.selectById(0);
            modelAndView.addObject("role", memberRoles);
            return modelAndView;
        }
        modelAndView.addObject("role", memberRoles);
        modelAndView.setViewName("/memberInfo/role/roleUpdate");
        return modelAndView;
    }


    /**
     * 插入数据请求
     *
     * @param memberRoles 数据
     * @return 插入数据的结果
     */
    @SystemControllerLog(description = "新增成员角色")
    @ResponseBody
    @RequestMapping(value = "/insert.action", method = RequestMethod.POST)
    public ResultDTO insert(@RequestBody MemberRoles memberRoles) {
        String escapeName = HtmlUtils.htmlEscape(memberRoles.getMemberRoleName());
        if (escapeName.length() > ROLE_NAME_MAX_LENGTH) {
            return ResultDTO.failureResult(FormEnum.FIELD_LENGTH_WRONG);
        }
        // 1.首先检查数据库是否存在要更新的数据记录
        if ("".equals(escapeName)) {
            return ResultDTO.failureResult(FormEnum.FIELD_NULL);
        }
        // 2.再检查角色名字是否有空值
        if (memberRolesService.selectByName(escapeName) != null) {
            return ResultDTO.failureResult(CommonEnum.REPEAT_ADD);
        }
        // 3. 查询数据库中是否已经有存在的值了,如果存在则拒绝更新
        if (memberRolesService.selectByName(escapeName) != null) {
            return ResultDTO.failureResult(CommonEnum.REPEAT_ADD);
        }
        memberRoles.setMemberRoleName(escapeName);
        memberRolesService.insert(memberRoles);
        return ResultDTO.successResult(CommonEnum.ADD_SUCCESS);
    }

    @RequestMapping(value = "/insert.html", method = RequestMethod.GET)
    public ModelAndView insertPage(ModelAndView modelAndView) {
        modelAndView.setViewName("/memberInfo/role/roleInsert");
        return modelAndView;
    }

    /**
     * 带查询条件的查询
     *
     * @param offset 从第几条记录开始查询
     * @param limit  查询几条数据
     * @return 带查询条件的数据
     */
    @SystemControllerLog(description = "查询成员角色")
    @RequestMapping(value = "/selectByParam.action", method = RequestMethod.GET)
    @ResponseBody
    public BootstrapTableResultDTO selectByParam(@RequestParam(value = "memberRoleId", defaultValue = "") String memberRoleId,
                                                 @RequestParam(value = "memberRoleName", defaultValue = "") String memberRoleName,
                                                 @RequestParam(value = "offset", required = false, defaultValue = "0") int offset,
                                                 @RequestParam(value = "limit", required = false, defaultValue = "10") int limit) {
        MemberRoles memberRoles = new MemberRoles();
        if (!"".equals(memberRoleId)) {
            memberRoles.setMemberRoleId(Integer.valueOf(memberRoleId));
        }
        if (!"".equals(memberRoleName)) {
            memberRoles.setMemberRoleName(memberRoleName);
        }
        ConditionMap<MemberRoles> conditionMap = new ConditionMap<>(memberRoles, offset, limit);
        List<MemberRoles> rolesList = memberRolesService.selectByParam(conditionMap);
        int totalCount = memberRolesService.selectCount();
        return new BootstrapTableResultDTO(totalCount, rolesList);
    }

    @SystemControllerLog(description = "查看成员角色页面")
    @RequestMapping(value = "/index.html", method = RequestMethod.GET)
    public String index() {
        return "/memberInfo/role/roleList";
    }
}
