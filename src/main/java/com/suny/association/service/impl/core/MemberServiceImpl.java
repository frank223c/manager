package com.suny.association.service.impl.core;

import com.suny.association.annotation.SystemControllerLog;
import com.suny.association.annotation.SystemServiceLog;
import com.suny.association.enums.BaseEnum;
import com.suny.association.exception.BusinessException;
import com.suny.association.exception.ExcelInfoFormatWrongException;
import com.suny.association.mapper.AccountMapper;
import com.suny.association.mapper.DepartmentMapper;
import com.suny.association.mapper.MemberMapper;
import com.suny.association.pojo.po.Account;
import com.suny.association.pojo.po.Department;
import com.suny.association.pojo.po.Member;
import com.suny.association.pojo.vo.ConditionMap;
import com.suny.association.service.AbstractBaseServiceImpl;
import com.suny.association.service.interfaces.core.IMemberService;
import com.suny.association.utils.ExcelUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Comments:  成员逻辑层类
 *
 * @author :   孙建荣
 *         Create Date: 2017/03/07 22:35
 */
@Service
public class MemberServiceImpl extends AbstractBaseServiceImpl<Member> implements IMemberService {
    /**
     * 协会名字前缀
     */
    private static final String ASSOCIATION_NAME_PREFIX = "rjxh";
    private static final Logger logger = LoggerFactory.getLogger(MemberServiceImpl.class);
    private static final Integer ZERO = 0;
    private final MemberMapper memberMapper;
    private final AccountMapper accountMapper;
    private final DepartmentMapper departmentMapper;

    @Autowired
    public MemberServiceImpl(MemberMapper memberMapper, AccountMapper accountMapper, DepartmentMapper departmentMapper) {
        this.memberMapper = memberMapper;
        this.accountMapper = accountMapper;
        this.departmentMapper = departmentMapper;
    }

    /**
     * 插入一条新的成员信息
     *
     * @param member 成员信息
     */
    @SystemServiceLog(description = "插入一条成员信息失败")
    @Transactional(rollbackFor = {Exception.class})
    @Override
    public void insert(Member member) {
        memberMapper.insertAndReturnId(member);
        // 这里会自动填充ID
        Integer memberId = member.getMemberId();
        if (memberId != null) {
            Account account = generateSimpleAccount(memberId);
            logger.info("自动生成的协会成员账号登录名为:{}", account.getAccountName());
            accountMapper.insert(account);
        } else {
            logger.error("自动产生用户账号失败,产生账号失败的信息为:成员ID{},届级{},班级{},名字{}", member.getMemberId(), member.getMemberGradeNumber(), member.getMemberClassName(), member.getMemberName());
        }
    }


    /**
     * 根据成员的ID自动产生一个用户账号,成员的ID用于关联用户账号跟协会成员信息
     *
     * @param memberId 协会成员的ID
     */
    private Account generateSimpleAccount(Integer memberId) {
        Account account = new Account();
        Member member = new Member();
        member.setMemberId(memberId);
        //设置账号名字
        account.setAccountName(ASSOCIATION_NAME_PREFIX + memberId);
        //设置对应的成员账号
        account.setAccountMember(member);
        return account;
    }

    /**
     * 根据传进来的批量成员的ID自动产生批量用户账号,成员的ID用于关联用户账号跟协会成员信息
     *
     * @param memberList 批量的协会成员的ID
     */
    private List<Account> generateBatchSimpleAccount(List<Member> memberList) {
        List<Account> simpleAccountList = new ArrayList<>();
        for (Member aMemberList : memberList) {
            Integer memberId = aMemberList.getMemberId();
            Account account = new Account();
            Member member = new Member();
            member.setMemberId(memberId);
            //设置账号名字
            account.setAccountName(ASSOCIATION_NAME_PREFIX + memberId);
            //设置对应的成员账号
            account.setAccountMember(member);
            simpleAccountList.add(account);
        }
        return simpleAccountList;
    }


    /*  通过社团成员id查询是否被用户账号存在引用    */
    @Override
    public Member selectMemberReference(int memberId) {
        Account account = accountMapper.selectMemberReference(memberId);
        if (account == null) {
            logger.warn("成员ID{}没有绑定用户账号,请立即绑定!", memberId);
            return null;
        }
        Member member = account.getAccountMember();
        if (member != null) {
            logger.info("获取到的Account账号ID:{},登录名:{},对应的成员ID:{},届级:{},班级:{},姓名:{}", account.getAccountId(), account.getAccountName(), member.getMemberId(), member.getMemberGradeNumber(), member.getMemberClassName(), member.getMemberName());
            return account.getAccountMember();
        }
        logger.warn("成员ID{}没有绑定用户账号,请立即绑定!", memberId);
        return null;
    }

    /*  查询成员表里面的总记录数    */
    @Override
    public int selectCount() {
        return memberMapper.selectCount();
    }

    /*   插入一条成员信息并返回插入的行数   */
    @Override
    @SystemServiceLog(description = "插入成员信息失败")
    @Transactional(rollbackFor = {Exception.class})
    public int insertAndReturnId(Member member) {
        memberMapper.insertAndReturnId(member);
        return member.getMemberId();
    }

    /*  通过int类型的id删除一条成员信息    */
    @SystemServiceLog(description = "删除成员信息记录失败")
    @Transactional(rollbackFor = {Exception.class})
    @Override
    public void deleteById(long id) {
        memberMapper.deleteById(id);
    }


    /*  更新一条成员信息    */
    @SystemServiceLog(description = "更新成员信息失败")
    @Transactional(rollbackFor = {Exception.class})
    @Override
    public void update(Member member) {
        memberMapper.update(member);
    }

    /*   查询冻结的管理员信息   */
    @Override
    public List<Member> selectFreezeManager() {
        return memberMapper.selectFreezeManager();
    }

    /*   查询正常的管理员信息   */
    @Override
    public List<Member> selectNormalManager() {
        return memberMapper.selectNormalManager();
    }

    /*  查询冻结的成员信息    */
    @Override
    public List<Member> selectFreezeMember() {
        return memberMapper.selectFreezeMember();
    }

    /*   查询正常的成员信息    */
    @Override
    public List<Member> selectNormalMember() {
        return memberMapper.selectNormalMember();
    }


    /*   通过成员角色id查询有哪些成员引用着这个角色   */
    @Override
    public List<Member> selectByMemberRoleId(Integer memberRoleId) {
        return memberMapper.selectByMemberRoleId(memberRoleId);
    }

    @SystemControllerLog(description = "批量插入成员信息失败")
    @Transactional(rollbackFor = Exception.class)
    @Override
    public AtomicReference<List<Member>> insertBatchFormFile(File file, String fileExtension) {
        // 定义一个插入失败的列表，返回给前端
        // 成功插入的行数
        int successNum = ZERO;
        List<String[]> arrayMemberList = ExcelUtils.parseExcel(file, fileExtension, 0, 0);// 包含成员信息跟账号信息的一个集合，原子操作
        List<Member> processMemberList = processArrayToMember(arrayMemberList);
        AtomicReference<List<Member>> repetitionMemberList = new AtomicReference<>(new ArrayList<>(16));
        //  根据填入的成员信息查询数据库中是否有相同的对象
        for (Member pendingMember : processMemberList) {
            if (selectEqualsMember(pendingMember)) {
                logger.error("数据库中存在这个成员,成员名字:【{}】,班级【{}】,年级【{}】", pendingMember.getMemberName(), pendingMember.getMemberClassName(), pendingMember.getMemberGradeNumber());
                repetitionMemberList.get().add(pendingMember);
            } else {
                // 到达这里就说明数据正常并且在数据库红没有重复
                processMemberList.add(pendingMember);
            }
        }
        // 到达这里就已经分清楚了重复的账号跟正常的账号,然后需要进行账号自动生成
        List<Member> successMemberList = insertBatch(processMemberList);
        logger.info("成功插入成员信息的的行数为:{}，失败的行数为{}", successMemberList.size());
        List<Account> batchSimpleAccount = generateBatchSimpleAccount(successMemberList);
        return null;
    }

    @Override
    public Boolean selectEqualsMember(Member pendingMember) {
        ConditionMap<Member> conditionMap = new ConditionMap<>(pendingMember, 0, 10);
        List<Member> members = memberMapper.selectByParam(conditionMap);
        return !members.isEmpty();
    }


    /**
     * 通过传入约定好的数组类型数据转换为Member类型的数据
     *
     * @param memberList 转换好的Member集合数据
     * @return 转换好的Member集合数据
     */
    private List<Member> processArrayToMember(List<String[]> memberList) {
        List<Member> processMemberList = new ArrayList<>();
        if (memberList.isEmpty()) {
            logger.warn("传过来的协会成员数据【数据类型为数组】解析失败，无法进行处理");
            throw new ExcelInfoFormatWrongException("上传的Excel文件数据解析成功的数据量为0,请重新修改后重试!");
        }
        for (String[] memberArray : memberList) {
                /* 按照模板约定，必须是6组数据，分别是名字，班级，性别，电话号码，入学年份，部门，如果不满足直接拒绝写入*/
            if (memberArray.length != 6) {
                logger.info("数据不符合约定，当前数据个数为{},出错信息为{}", memberArray.length, memberArray);
                throw new ExcelInfoFormatWrongException("上传的Excel文件数据不符合约定,请重新修改后重试!出错处:" + Arrays.toString(memberArray));
            }
                /* 把对应的信息复制到一个Member对象中 */
            Member member = new Member();
            member.setMemberName(memberArray[0]);
            member.setMemberClassName(memberArray[1]);
            String female = "女";
            if (memberArray[2].equals(female)) {
                member.setMemberSex(false);
            } else {
                member.setMemberSex(true);
            }
            member.setMemberGradeNumber(Integer.valueOf(memberArray[4]));
            Department department = departmentMapper.selectByName(memberArray[5]);
            department.setDepartmentId(department.getDepartmentId() != null ? department.getDepartmentId() : 0);
            member.setMemberDepartment(department);
            processMemberList.add(member);
        }
        return processMemberList;
    }


    /**
     * 往数据库里面插入Member的信息
     *
     * @return 如果插入成功就返回带自增主键Id的Member实体信息，否则就返回一个NULL对象
     */
    @SystemControllerLog(description = "批量插入成员信息失败")
    @Transactional(rollbackFor = Exception.class)
    private List<Member> insertBatch(List<Member> memberList) {
        int i = memberMapper.insertBatch(memberList);
        logger.info("成功插入的数量为:{}", i);
        return memberList;
    }


    /*  通过id查询一条成员信息    */
    @Override
    public Member selectById(long id) {
        return memberMapper.selectById(id);
    }

    /*  通过成员名字查询一条成员信息    */
    @Override
    public Member selectByName(String name) {
        return memberMapper.selectByName(name);
    }


    /*   查询所有的成员记录   */
    @Override
    public List<Member> selectAll() {
        return memberMapper.selectAll();
    }

    /*   通过查询条件查询成员记录   */
    @Override
    public List<Member> selectByParam(ConditionMap<Member> conditionMap) {
        return memberMapper.selectByParam(conditionMap);
    }


}
