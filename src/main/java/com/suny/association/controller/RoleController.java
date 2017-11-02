package com.suny.association.controller;

import com.suny.association.annotation.SystemControllerLog;
import com.suny.association.entity.dto.BootstrapTableResultDTO;
import com.suny.association.entity.dto.JsonResultDTO;
import com.suny.association.entity.po.Roles;
import com.suny.association.entity.vo.ConditionMap;
import com.suny.association.enums.ResponseCodeEnum;
import com.suny.association.service.interfaces.IRolesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

import static com.suny.association.entity.dto.JsonResultDTO.failureResult;
import static com.suny.association.entity.dto.JsonResultDTO.successResult;

/**
 * Comments:  账号角色控制器
 * @author :   孙建荣
 * Create Date: 2017/04/11 18:15
 */
@Controller
@RequestMapping("/account/role")
public class RoleController extends BaseController {

    private final IRolesService rolesService;

    @Autowired
    public RoleController(IRolesService rolesService) {
        this.rolesService = rolesService;
    }

    @SystemControllerLog(description = "删除账号角色")
    @RequestMapping(value = "/delete.action/{roleId}", method = RequestMethod.GET)
    @ResponseBody
    public JsonResultDTO delete(@PathVariable("roleId") Integer roleId) {
        if (rolesService.queryQuote(roleId).size() >= 1) {
            return failureResult(ResponseCodeEnum.HAVE_QUOTE);
        } else if (rolesService.selectById(roleId) == null) {
            return failureResult(ResponseCodeEnum.DELETE_FAILURE);
        }
        rolesService.deleteById(roleId);
        return successResult(ResponseCodeEnum.DELETE_SUCCESS);
    }

    @SystemControllerLog(description = "更新账号角色")
    @ResponseBody
    @RequestMapping(value = "/update.json", method = RequestMethod.POST)
    public JsonResultDTO update(@RequestBody Roles roles) {
        if (roles.getRoleId() == null || rolesService.selectById(roles.getRoleId()) == null) {
            return failureResult(ResponseCodeEnum.SELECT_FAILURE);
        }
        if ("".equals(roles.getDescription()) || roles.getDescription() == null) {
            return failureResult(ResponseCodeEnum.FIELD_NULL);
        }
        rolesService.update(roles);
        return successResult(ResponseCodeEnum.UPDATE_SUCCESS);
    }

    @RequestMapping(value = "/update.html/{roleId}", method = RequestMethod.GET)
    public ModelAndView updatePage(@PathVariable int roleId
            , ModelAndView modelAndView) {
        Roles role = rolesService.selectById(roleId);
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
     * @param roles 数据
     * @return 插入数据的结果
     */
    @SystemControllerLog(description = "新增账号角色")
    @ResponseBody
    @RequestMapping(value = "/insert.action", method = RequestMethod.POST)
    public JsonResultDTO insert(@RequestBody Roles roles) {
        if ("".equals(roles.getDescription()) || roles.getDescription() == null) {
            return failureResult(ResponseCodeEnum.FIELD_NULL);
        }
        if (rolesService.selectByName(roles.getDescription()) != null) {
            return failureResult(ResponseCodeEnum.REPEAT_ADD);
        }
        rolesService.insert(roles);
        return successResult(ResponseCodeEnum.ADD_SUCCESS);
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
    @RequestMapping(value = "/list.action", method = RequestMethod.GET)
    @ResponseBody
    public BootstrapTableResultDTO query(@RequestParam(value = "offset", required = false, defaultValue = "0") int offset,
                                         @RequestParam(value = "limit", required = false, defaultValue = "10") int limit) {
        ConditionMap<Roles> conditionMap=new ConditionMap<>(new Roles(),0,10);
        List<Roles> rolesList = rolesService.selectByParam(conditionMap);
        int total = rolesService.selectCount();
        return new BootstrapTableResultDTO(total, rolesList);
    }

    @SystemControllerLog(description = "查看账号角色页面")
    @RequestMapping(value = "/index.html", method = RequestMethod.GET)
    public String index() {
        return "/accountInfo/role/roleList";
    }
}
