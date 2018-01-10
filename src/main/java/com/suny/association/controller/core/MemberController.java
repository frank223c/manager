package com.suny.association.controller.core;

import com.suny.association.annotation.SystemControllerLog;
import com.suny.association.controller.BaseController;
import com.suny.association.entity.dto.ResultDTO;
import com.suny.association.enums.FileOperateEnum;
import com.suny.association.enums.CommonEnum;
import com.suny.association.entity.po.Department;
import com.suny.association.entity.po.Member;
import com.suny.association.entity.po.MemberRoles;
import com.suny.association.entity.vo.ConditionMap;
import com.suny.association.enums.FormEnum;
import com.suny.association.service.interfaces.IAccountService;
import com.suny.association.service.interfaces.core.IDepartmentService;
import com.suny.association.service.interfaces.core.IMemberRolesService;
import com.suny.association.service.interfaces.core.IMemberService;
import com.suny.association.utils.*;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.suny.association.entity.dto.ResultDTO.successResult;

/**
 * Comments:   成员信息管理类Controller
 *
 * @author :   孙建荣
 *         Create Date: 2017/03/15 20:46
 */
@Controller
@RequestMapping("/member")
public class MemberController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(MemberController.class);
    private final IMemberService memberService;
    private final IDepartmentService departmentService;
    private final IMemberRolesService memberRolesService;
    private final IAccountService accountService;

    @Autowired
    public MemberController(IDepartmentService departmentService, IMemberService memberService, IMemberRolesService memberRolesService, IAccountService accountService) {
        this.departmentService = departmentService;
        this.memberService = memberService;
        this.memberRolesService = memberRolesService;
        this.accountService = accountService;
    }


    /**
     * 插入成员信息操作
     *
     * @param member 成员信息
     * @return 插入的结果
     */
    @SystemControllerLog(description = "插入成员信息")
    @RequestMapping(value = "/insert.action", method = RequestMethod.POST)
    @ResponseBody
    public ResultDTO insert(@RequestBody Member member) {
        if (member.getMemberName() == null || "".equals(member.getMemberName())) {
            return ResultDTO.failureResult(FormEnum.FIELD_NULL);
        }
        if ("".equals(member.getMemberClassName()) || member.getMemberClassName() == null) {
            return ResultDTO.failureResult(FormEnum.FIELD_NULL);
        }
        if (!(ValidActionUtil.isContainChinese(member.getMemberName()))) {
            return ResultDTO.failureResult(FormEnum.MUST_CHINESE);
        }
        memberService.insert(member);
        return successResult(CommonEnum.ADD_SUCCESS);
    }


    /**
     * 进行新增成员信息页面
     *
     * @param modelAndView 带有数据库的数据模型
     * @return 数据跟视图地址
     */
    @SystemControllerLog(description = "新增成员页面")
    @RequestMapping(value = "/insert.html", method = RequestMethod.GET)
    public ModelAndView insertPage(ModelAndView modelAndView) {
        List<Member> managerList = memberService.selectNormalManager();
        List<Department> departmentList = departmentService.selectAll();
        List<MemberRoles> memberRolesList = memberRolesService.selectAll();
        modelAndView.addObject("departmentList", departmentList);
        modelAndView.addObject("memberRolesList", memberRolesList);
        modelAndView.addObject("managerList", managerList);
        modelAndView.addObject("memberGradeList", CustomDate.getLastYearAndThisYears());
        modelAndView.setViewName("memberInfo/memberInsert");
        return modelAndView;
    }

    @SystemControllerLog(description = "上传协会成员数据页面")
    @RequestMapping(value = "/uploadMemberInfo.html", method = RequestMethod.GET)
    public String uploadMemberInfoPage() {
        return "memberInfo/memberInfoUpdate";
    }

    @SystemControllerLog(description = "通过Excel文件批量新增数据")
    @RequestMapping(value = "/uploadMemberInfo.action", method = RequestMethod.POST)
    @ResponseBody
    public ResultDTO uploadMemberInfo(@RequestParam("excelFile") MultipartFile excelFile) throws IOException {
        String fileType = excelFile.getContentType();
        /*  获取上传文件名 */
        String fileName = ((CommonsMultipartFile) excelFile).getFileItem().getName();
        /* 获取上传文件名的文件后缀名    */
        String fileExtension = fileName.lastIndexOf(".") == -1 ? "" : fileName.substring(fileName.lastIndexOf(".") + 1);
        CommonsMultipartFile commonsMultipartFile = (CommonsMultipartFile) excelFile;
        DiskFileItem diskFileItem = (DiskFileItem) commonsMultipartFile.getFileItem();
        File file = diskFileItem.getStoreLocation();
        /* 获取文件名的后缀名，检查是否存在欺骗   */
        if (!ExcelUtils.parseExcelFileType(fileType, fileExtension)) {
            logger.warn("上传的文件貌似有点小问题，可能是后缀名欺骗");
            return ResultDTO.failureResult(FileOperateEnum.FILE_EXTENSION_WARN);
        }
        /* 查看成功插入的行数  */
        Map<String,List<Member>> listAtomicReference = memberService.insertBatchFormFile(file, fileExtension);
        int size = listAtomicReference.size();
        if (size == 0) {
            return successResult(CommonEnum.ADD_SUCCESS_ALL);
        } else {
            return ResultDTO.successResultAndData(CommonEnum.ADD_SUCCESS_PART_OF, listAtomicReference);
        }
    }


    /**
     * 下载成员信息模板，上传者要按照模板的要求进行修改Excel文档，否则系统将忽略上传请求
     *
     * @param request  request请求
     * @param response response请求
     */
    @RequestMapping(value = "downloadMemberTemplate.action", method = RequestMethod.GET)
    public void downloadMemberTemplate(HttpServletRequest request, HttpServletResponse response) {
        String fileName = "memberTemplate.xlsx";
        ServletContext servletContext = request.getServletContext();
        /*  获取放置模板文件的目录    */
        String realPath = servletContext.getRealPath("/WEB-INF/template");
        /*  得到一个真实的文件存放地址，代表一个文件 */
        Path file = Paths.get(realPath, fileName);
        /* 判断文件是否存在，否则会发生异常   */
        if (Files.exists(file)) {
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            try {
                logger.info("正在向客户端输出成员信息Excel模板");
                Files.copy(file, response.getOutputStream());
            } catch (IOException e) {
                logger.error("捕获了异常，向客户端发送Excel文件时发生错误{}", e.getMessage());
            }
        }
        logger.warn("要下载的文件{}不存在", fileName);
    }


    @SystemControllerLog(description = "删除成员信息")
    @RequestMapping(value = "/deleteById.action/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResultDTO deleteById(@PathVariable("id") Long id) {
        if (memberService.selectById(id) == null) {
            return ResultDTO.failureResult(CommonEnum.SELECT_FAILURE);
        }
        if (accountService.queryQuoteByMemberId(id) != null) {
            return ResultDTO.failureResult(CommonEnum.HAVE_QUOTE);
        }
        memberService.deleteById(id);
        return successResult(CommonEnum.DELETE_SUCCESS);
    }

    @SystemControllerLog(description = "更新协会成员信息")
    @RequestMapping(value = "/update.action", method = RequestMethod.POST)
    @ResponseBody
    public ResultDTO update(@RequestBody Member member) {
        if (member.getMemberName() == null || "".equals(member.getMemberName())) {
            return ResultDTO.failureResult(FormEnum.FIELD_NULL);
        }
        if (!member.getMemberStatus()) {
            return ResultDTO.failureResult(FormEnum.FIELD_NULL);
        }
        memberService.update(member);
        return successResult(CommonEnum.UPDATE_SUCCESS);
    }

    @RequestMapping(value = "/update.html/{id}", method = RequestMethod.GET)
    public ModelAndView update(@PathVariable("id") Integer id, ModelAndView modelAndView) {
        Member member = memberService.selectById(id);
        List<Member> managerList = memberService.selectNormalManager();
        List<Department> departmentList = departmentService.selectAll();
        List<MemberRoles> memberRolesList = memberRolesService.selectAll();
        modelAndView.addObject("member", member);
        modelAndView.addObject("departmentList", departmentList);
        modelAndView.addObject("memberRolesList", memberRolesList);
        modelAndView.addObject("managerList", managerList);
        modelAndView.setViewName("memberInfo/memberUpdate");
        return modelAndView;
    }

    @SystemControllerLog(description = "查询冻结的成员")
    @RequestMapping(value = "/selectFreezeMember.action", method = RequestMethod.POST)
    @ResponseBody
    public ResultDTO selectFreezeMember() {
        List<Member> memberList = memberService.selectFreezeMember();
        if (memberList != null) {
            return ResultDTO.successResultAndData(CommonEnum.SELECT_SUCCESS, memberList);
        }
        return ResultDTO.failureResult(CommonEnum.SELECT_FAILURE);
    }

    @SystemControllerLog(description = "查询冻结的管理员")
    @RequestMapping(value = "/selectFreezeManager.action", method = RequestMethod.POST)
    @ResponseBody
    public ResultDTO selectFreezeManager() {
        List<Member> memberList = memberService.selectFreezeManager();
        if (memberList != null) {
            return ResultDTO.successResultAndData(CommonEnum.SELECT_SUCCESS, memberList);
        }
        return ResultDTO.failureResult(CommonEnum.SELECT_FAILURE);
    }

    @SystemControllerLog(description = "查询正常的成员")
    @RequestMapping(value = "/selectNormalMember.action", method = RequestMethod.POST)
    @ResponseBody
    public ResultDTO selectNormalMember() {
        List<Member> memberList = memberService.selectNormalMember();
        if (memberList != null) {
            return ResultDTO.successResultAndData(CommonEnum.SELECT_SUCCESS, memberList);
        }
        return ResultDTO.failureResult(CommonEnum.SELECT_FAILURE);
    }

    @SystemControllerLog(description = "查询正常的管理员")
    @RequestMapping(value = "/selectNormalManager.action", method = RequestMethod.POST)
    @ResponseBody
    public ResultDTO selectNormalManager() {
        List<Member> memberList = memberService.selectNormalManager();
        if (memberList != null) {
            return ResultDTO.successResultAndData(CommonEnum.SELECT_SUCCESS, memberList);
        }
        return ResultDTO.failureResult(CommonEnum.SELECT_FAILURE);
    }

    @RequestMapping(value = "/selectAll.action", method = RequestMethod.GET)
    @ResponseBody
    public Map<Object, Object> selectAll(@RequestParam(value = "offset", required = false, defaultValue = "0") int offset,
                                         @RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
                                         @RequestParam(value = "departmentname", required = false) String departmentname,
                                         @RequestParam(value = "status", required = false) int status) {
        Boolean memberStatus = true;
        Map<Object, Object> tableDate = new HashMap<>(16);
        Member member = new Member();
        Department department = new Department();
        department.setDepartmentName(departmentname);
        member.setMemberDepartment(department);
        member.setMemberDepartment(department);
        // 前端传过来的是数字,所以需要根据状态进行转换
        // 0 冻结状态   1 正常状态   2 不设置状态条件
        if (status == 0) {
            memberStatus = false;
        } else if (status == 1) {
            memberStatus = true;
        } else {
            // 不设置状态查询条件
        }
        member.setMemberStatus(memberStatus);
        ConditionMap<Member> conditionMap = new ConditionMap<>(member, offset, limit);
        List<Member> memberList = memberService.selectByParam(conditionMap);
        if (memberList.size() != 0 && !memberList.isEmpty()) {
            int total = memberService.selectCount();
            tableDate.put("rows", memberList);
            tableDate.put("total", total);
            return tableDate;
        }
        tableDate.put("rows", null);
        tableDate.put("total", 0);
        return tableDate;
    }

    @SystemControllerLog(description = "查询指定成员")
    @RequestMapping(value = "/selectById.action/{memberId}", method = RequestMethod.GET)
    public ResultDTO selectById(@PathVariable("memberId") Integer memberId) {
        Member member = memberService.selectById(memberId);
        return ResultDTO.successResultAndData(CommonEnum.SELECT_SUCCESS, member);
    }

    @SystemControllerLog(description = "查看成员管理页面")
    @RequestMapping(value = "/memberManager.html", method = RequestMethod.GET)
    public String managerPage() {
        return "memberInfo/memberManager";
    }

}
