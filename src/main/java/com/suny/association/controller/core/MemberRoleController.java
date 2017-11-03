package com.suny.association.controller.core;

import com.suny.association.annotation.SystemControllerLog;
import com.suny.association.controller.BaseController;
import com.suny.association.entity.dto.BootstrapTableResultDTO;
import com.suny.association.entity.dto.JsonResultDTO;
import com.suny.association.entity.po.MemberRoles;
import com.suny.association.entity.vo.ConditionMap;
import com.suny.association.enums.ResponseCodeEnum;
import com.suny.association.service.interfaces.core.IMemberRolesService;
import com.suny.association.service.interfaces.core.IMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Comments:   协会角色管理
 * @author :   孙建荣
 * Create Date: 2017/04/22 22:34
 */
@Controller
@RequestMapping("/member/role")
public class MemberRoleController extends BaseController {
    private static final Integer ROLE_NAME_MAX_LENGTH=20;
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
    public JsonResultDTO delete(@PathVariable("memberRoleId") Integer memberRoleId) {
        // 1.查询数据库中是否有这个角色
        if (memberRolesService.selectById(memberRoleId) == null) {
            return JsonResultDTO.failureResult(ResponseCodeEnum.DELETE_FAILURE);
        }
        // 2.判断数据库里面是不是有成员引用了这个角色,引用了就不能被删除
        if (!memberService.selectByMemberRoleId(memberRoleId).isEmpty()) {
            return JsonResultDTO.failureResult(ResponseCodeEnum.HAVE_QUOTE);
        }
        memberRolesService.deleteById(memberRoleId);
        return JsonResultDTO.successResult(ResponseCodeEnum.DELETE_SUCCESS);
    }

    @SystemControllerLog(description = "更新成员角色")
    @ResponseBody
    @RequestMapping(value = "/update.action", method = RequestMethod.POST)
    public JsonResultDTO update(@RequestBody MemberRoles memberRoles) {
        // 1.首先检查数据库是否存在要更新的数据记录
        if (memberRoles.getMemberRoleId() == null || memberRolesService.selectById(memberRoles.getMemberRoleId()) == null) {
            return JsonResultDTO.failureResult(ResponseCodeEnum.SELECT_FAILURE);
        }
        // 2.再检查角色名字是否有空值
        if ("".equals(memberRoles.getMemberRoleName()) || memberRoles.getMemberRoleName() == null) {
            return JsonResultDTO.failureResult(ResponseCodeEnum.FIELD_NULL);
        }
        // 3. 查询数据库中是否已经有存在的值了,如果存在则拒绝更新
        if(memberRolesService.selectByName(memberRoles.getMemberRoleName()) != null){
            return JsonResultDTO.failureResult(ResponseCodeEnum.REPEAT_ADD);
        }
        memberRolesService.update(memberRoles);
        return JsonResultDTO.successResult(ResponseCodeEnum.UPDATE_SUCCESS);
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
    public JsonResultDTO insert(@RequestBody MemberRoles memberRoles) {
        if(memberRoles.getMemberRoleName().length()>ROLE_NAME_MAX_LENGTH){
            return JsonResultDTO.failureResult(ResponseCodeEnum.FIELD_LENGTH_WRONG);
        }
        // 1.首先检查数据库是否存在要更新的数据记录
        if ("".equals(memberRoles.getMemberRoleName()) || memberRoles.getMemberRoleName() == null) {
            return JsonResultDTO.failureResult(ResponseCodeEnum.FIELD_NULL);
        }
        // 2.再检查角色名字是否有空值
        if (memberRolesService.selectByName(memberRoles.getMemberRoleName()) != null) {
            return JsonResultDTO.failureResult(ResponseCodeEnum.REPEAT_ADD);
        }
        // 3. 查询数据库中是否已经有存在的值了,如果存在则拒绝更新
        if(memberRolesService.selectByName(memberRoles.getMemberRoleName()) != null){
            return JsonResultDTO.failureResult(ResponseCodeEnum.REPEAT_ADD);
        }
        memberRolesService.insert(memberRoles);
        return JsonResultDTO.successResult(ResponseCodeEnum.ADD_SUCCESS);
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
    public BootstrapTableResultDTO selectByParam(@RequestParam(value = "memberRoleId",defaultValue = "")String memberRoleId,
                                                 @RequestParam(value = "memberRoleId",defaultValue = "") String memberRoleName,
                                                 @RequestParam(value = "offset", required = false, defaultValue = "0") int offset,
                                                 @RequestParam(value = "limit", required = false, defaultValue = "10") int limit) {
        MemberRoles memberRoles=new MemberRoles();
        if(!"".equals(memberRoleId)){
            memberRoles.setMemberRoleId(Integer.valueOf(memberRoleId));
        }
        if (!"".equals(memberRoleName)){
            memberRoles.setMemberRoleName(memberRoleName);
        }
        ConditionMap<MemberRoles> conditionMap=new ConditionMap<>(memberRoles,offset,limit);
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
