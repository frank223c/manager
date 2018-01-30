package com.suny.association.web.controller;

import com.suny.association.annotation.SystemControllerLog;
import com.suny.association.entity.dto.BootstrapTableResultDTO;
import com.suny.association.entity.dto.ResultDTO;
import com.suny.association.entity.po.AccountRoles;
import com.suny.association.entity.vo.ConditionMap;
import com.suny.association.enums.CommonEnum;
import com.suny.association.enums.FormEnum;
import com.suny.association.service.interfaces.IRolesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

import static com.suny.association.entity.dto.ResultDTO.failureResult;
import static com.suny.association.entity.dto.ResultDTO.successResult;

/**
 * Comments:  账号角色控制器
 * @author :   孙建荣
 * Create Date: 2017/04/11 18:15
 */
@Controller
@RequestMapping("/account/role")
public class RoleController extends BaseController {
    private final IRolesService rolesService;
    private static final Integer ACCOUNT_ROLE_ID_LENGTH=2;
    private static final Integer ACCOUNT_ROLE_NAME_LENGTH=10;
    private static final Integer ACCOUNT_ROLE_DESCRIPTION_LENGTH=200;

    @Autowired
    public RoleController(IRolesService rolesService) {
        this.rolesService = rolesService;
    }

    @SystemControllerLog(description = "删除账号角色")
    @RequestMapping(value = "/delete.action/{roleId}", method = RequestMethod.GET)
    @ResponseBody
    public ResultDTO delete(@PathVariable("roleId") Integer roleId) {
        Integer escapeId = Integer.valueOf(HtmlUtils.htmlEscape(String.valueOf(roleId)));
        if ( escapeId.toString().length() > ACCOUNT_ROLE_ID_LENGTH) {
            return ResultDTO.failureResult(FormEnum.FIELD_LENGTH_WRONG);
        }
        if (!rolesService.queryQuote(roleId).isEmpty()) {
            return failureResult(CommonEnum.HAVE_QUOTE);
        } else if (rolesService.selectById(roleId) == null) {
            return failureResult(CommonEnum.DELETE_FAILURE);
        }
        rolesService.deleteById(escapeId);
        return successResult(CommonEnum.DELETE_SUCCESS);
    }

    @SystemControllerLog(description = "更新账号角色")
    @ResponseBody
    @RequestMapping(value = "/update.action", method = RequestMethod.POST)
    public ResultDTO update(@RequestBody AccountRoles accountRoles) {
        // 过滤特殊字符，防止XSS注入
        String escapeName = HtmlUtils.htmlEscape(accountRoles.getRoleName());
        String escapeDescription = HtmlUtils.htmlEscape(accountRoles.getDescription());
        Integer escapeId = Integer.valueOf(HtmlUtils.htmlEscape(String.valueOf(accountRoles.getRoleId())));
        if (accountRoles.getRoleId() == null || rolesService.selectById(accountRoles.getRoleId()) == null) {
            return failureResult(CommonEnum.SELECT_FAILURE);
        }
        if (escapeName.length() > ACCOUNT_ROLE_NAME_LENGTH || escapeId.toString().length() > ACCOUNT_ROLE_ID_LENGTH||escapeDescription.length()>ACCOUNT_ROLE_DESCRIPTION_LENGTH) {
            return ResultDTO.failureResult(FormEnum.FIELD_LENGTH_WRONG);
        }
        if ("".equals(accountRoles.getDescription()) || accountRoles.getDescription() == null) {
            return failureResult(FormEnum.FIELD_NULL);
        }
        accountRoles.setRoleId(escapeId);
        accountRoles.setRoleName(escapeName);
        accountRoles.setDescription(escapeDescription);
        rolesService.update(accountRoles);
        return successResult(CommonEnum.UPDATE_SUCCESS);
    }

    @RequestMapping(value = "/update.html/{roleId}", method = RequestMethod.GET)
    public ModelAndView updatePage(@PathVariable int roleId
            , ModelAndView modelAndView) {
        AccountRoles role = rolesService.selectById(roleId);
        if (role == null) {
            int defaultId = 0;
            role = rolesService.selectById(defaultId);
            modelAndView.addObject("role", role);
            return modelAndView;
        }
        modelAndView.addObject("role", role);
        modelAndView.setViewName("/accountInfo/role/roleUpdate");
        return modelAndView;
    }


    /**
     * 插入数据请求
     *
     * @param accountRoles 数据
     * @return 插入数据的结果
     */
    @SystemControllerLog(description = "新增账号角色")
    @ResponseBody
    @RequestMapping(value = "/insert.action", method = RequestMethod.POST)
    public ResultDTO insert(@RequestBody AccountRoles accountRoles) {
        // 过滤特殊字符，防止XSS注入
        String escapeName = HtmlUtils.htmlEscape(accountRoles.getRoleName());
        String escapeDescription = HtmlUtils.htmlEscape(accountRoles.getDescription());
        if ("".equals(escapeDescription) || escapeDescription == null) {
            return failureResult(FormEnum.FIELD_NULL);
        }
        if (escapeName.length() > ACCOUNT_ROLE_NAME_LENGTH ||escapeDescription.length()>ACCOUNT_ROLE_DESCRIPTION_LENGTH) {
            return ResultDTO.failureResult(FormEnum.FIELD_LENGTH_WRONG);
        }
        if (rolesService.selectByName(escapeDescription) != null) {
            return failureResult(CommonEnum.REPEAT_ADD);
        }
        accountRoles.setRoleName(escapeName);
        accountRoles.setDescription(escapeDescription);
        rolesService.insert(accountRoles);
        return successResult(CommonEnum.ADD_SUCCESS);
    }

    @RequestMapping(value = "/insert.html", method = RequestMethod.GET)
    public ModelAndView insertPage(ModelAndView modelAndView) {
        modelAndView.setViewName("/accountInfo/role/roleInsert");
        return modelAndView;
    }

    /**
     * 带查询条件的查询
     *
     * @param offset 从第几条记录开始查询
     * @param limit  查询几条数据
     * @return 带查询条件的数据
     */
    @RequestMapping(value = "/selectByParam.action", method = RequestMethod.GET)
    @ResponseBody
    public BootstrapTableResultDTO selectByParam(AccountRoles accountRoles,
                                         @RequestParam(value = "offset", required = false, defaultValue = "0") int offset,
                                         @RequestParam(value = "limit", required = false, defaultValue = "10") int limit) {
        List<AccountRoles> accountRolesList;
        int total;
        if(accountRoles ==null){
            AccountRoles accountRoles1 = new AccountRoles();
            ConditionMap<AccountRoles> conditionMap=new ConditionMap<>(accountRoles1,offset,limit);
            accountRolesList = rolesService.selectByParam(conditionMap);
            total = rolesService.selectCountByParam(accountRoles1);
        }else{
            ConditionMap<AccountRoles> conditionMap=new ConditionMap<>(accountRoles,offset,limit);
            accountRolesList = rolesService.selectByParam(conditionMap);
            total = rolesService.selectCountByParam(accountRoles);
        }
        return new BootstrapTableResultDTO(total, accountRolesList);
    }

    @SystemControllerLog(description = "查看账号角色页面")
    @RequestMapping(value = "/index.html", method = RequestMethod.GET)
    public String index() {
        return "/accountInfo/role/roleList";
    }
}
