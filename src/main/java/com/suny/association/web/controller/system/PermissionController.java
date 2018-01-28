package com.suny.association.web.controller.system;

import com.suny.association.annotation.SystemControllerLog;
import com.suny.association.web.controller.BaseController;
import com.suny.association.entity.dto.BootstrapTableResultDTO;
import com.suny.association.entity.po.Permission;
import com.suny.association.entity.vo.ConditionMap;
import com.suny.association.enums.CommonEnum;
import com.suny.association.enums.FormEnum;
import com.suny.association.service.interfaces.system.IPermissionService;
import com.suny.association.entity.dto.ResultDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

import static com.suny.association.entity.dto.ResultDTO.failureResult;
import static com.suny.association.entity.dto.ResultDTO.successResult;

/**
 * Comments:   权限具体的管理，不含权限的分配
 * @author :   孙建荣
 * Create Date: 2017/05/02 12:58
 */
@RequestMapping("/system/permission")
@Controller
public class PermissionController extends BaseController {

    private final IPermissionService permissionService;

    @Autowired
    public PermissionController(IPermissionService permissionService) {
        this.permissionService = permissionService;
    }

    /**
     * 插入一条账号信息
     *
     * @param permission 要插入的权限信息
     * @return 插入的json数据结果
     */
    @SystemControllerLog(description = "插入权限信息")
    @RequestMapping(value = "/insert.action", method = RequestMethod.POST)
    @ResponseBody
    public ResultDTO insert(@RequestBody Permission permission) {
        if ("".equals(permission.getpermissionName()) || permission.getpermissionName() == null) {
            return ResultDTO.failureResult(FormEnum.FIELD_NULL);
        } else if (permissionService.selectByName(permission.getpermissionName()) != null) {
            return ResultDTO.failureResult(CommonEnum.REPEAT_ADD);
        }
        permissionService.insert(permission);
        return successResult(CommonEnum.ADD_SUCCESS);
    }


    /**
     * 新增权限页面
     *
     * @param modelAndView 模型数据跟视图
     * @return 新增权限页面
     */
    @RequestMapping(value = "/insert.html", method = RequestMethod.GET)
    public ModelAndView insertPage(ModelAndView modelAndView) {
        modelAndView.setViewName("system/permission/permissionInsert");
        return modelAndView;
    }

    /**
     * 删除一条账号信息请求
     *
     * @param permissionId 账号
     * @return 操作结果
     */
    @SystemControllerLog(description = "删除权限信息")
    @RequestMapping(value = "/deleteById.action/{permissionId}", method = RequestMethod.GET)
    @ResponseBody
    public ResultDTO deleteById(@PathVariable("permissionId") int permissionId) {
        if (!permissionService.queryPermissionQuote(permissionId).isEmpty()) {
            return failureResult(CommonEnum.HAVE_QUOTE);
        }
        if (permissionService.selectById(permissionId) == null) {
            return failureResult(CommonEnum.SELECT_FAILURE);
        }
        if (permissionId <= 37){
            return failureResult(CommonEnum.SYSTEM_LIMIT);
        }
        permissionService.deleteById(permissionId);
        return successResult(CommonEnum.DELETE_SUCCESS);
    }

    /**
     * 更新权限信息
     *
     * @param permission 权限信息
     * @return 更新数据的结果
     */
    @SystemControllerLog(description = "更新权限信息")
    @RequestMapping(value = "/update.action", method = RequestMethod.POST)
    @ResponseBody
    public ResultDTO update(@RequestBody Permission permission) {
        if (permission.getpermissionId() < 37) {
            return failureResult(CommonEnum.SYSTEM_LIMIT);
        }
        permissionService.update(permission);
        return successResult(CommonEnum.UPDATE_SUCCESS);
    }


    /**
     * 请求更新一个账号页面
     *
     * @param id           要更新信息的账号
     * @param modelAndView 模型数据跟视图地址
     * @return 模型数据跟视图地址
     */
    @RequestMapping(value = "/update.html/{id}", method = RequestMethod.GET)
    public ModelAndView updatePage(@PathVariable("id") Integer id, ModelAndView modelAndView) {
        Permission permission = permissionService.selectById(id);
        modelAndView.addObject("permission", permission);
        modelAndView.setViewName("/system/permission/permissionUpdate");
        return modelAndView;
    }


    /**
     * 带查询条件的查询
     *
     * @param offset 从第几行开始查询
     * @param limit  查询几条数据
     * @param status 查询的账号状态
     * @return 带查询条件的结果集
     */
    @SystemControllerLog(description = "查询所有的权限信息")
    @RequestMapping(value = "/selectByParam.action", method = RequestMethod.GET)
    @ResponseBody
    public BootstrapTableResultDTO selectByParam(@RequestParam(value = "offset", required = false, defaultValue = "0") int offset,
                                            @RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
                                            @RequestParam(value = "status", required = false, defaultValue = "3") int status) {
        ConditionMap<Permission> conditionMap=new ConditionMap<>(new Permission(),offset,limit);
        int totalCount = permissionService.selectCount();
        List<Permission> permissionList = permissionService.selectByParam(conditionMap);
        return new BootstrapTableResultDTO(totalCount,permissionList);
    }

    @SystemControllerLog(description = "查看权限管理页面")
    @RequestMapping(value = "/index.html", method = RequestMethod.GET)
    public String index() {
        return "/system/permission/permissionList";
    }

}
