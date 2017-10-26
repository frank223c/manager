package com.suny.association.service.impl.core;

import com.suny.association.annotation.SystemControllerLog;
import com.suny.association.annotation.SystemServiceLog;
import com.suny.association.enums.BaseEnum;
import com.suny.association.exception.BusinessException;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
     * 在插入一条成员信息后自动创建一个简单的账号
     *
     * @param memberId 成员id
     */
    @SystemServiceLog(description = "自动创建一条账号信息失败")
    @Transactional(rollbackFor = {Exception.class})
    public void generationAccount1(Integer memberId) {
        Account autoAccount = new Account();
        Member member = new Member();
        member.setMemberId(memberId);
        String memberIdString = String.valueOf(memberId);
        //设置账号名字
        autoAccount.setAccountName(memberIdString);
        //设置对应的管理员账号
        autoAccount.setAccountMember(member);
        accountMapper.insert(autoAccount);
        logger.info("{}", autoAccount.getAccountMember().getMemberId());

    }

    /*  通过社团成员id查询是否被用户账号存在引用    */
    @Override
    public Member selectMemberReference(int memberId) {
        Account account = accountMapper.selectMemberReference(memberId);
        return memberMapper.selectById(account.getAccountMember().getMemberId());
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
    public int insertReturnCount(Member member) {
        return memberMapper.insertAndReturnId(member);
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
        AtomicReference<List<Member>> failList = new AtomicReference<>(new ArrayList<>());
        //   Member member;   // 定义一个成员实体变量
        // 成功插入的行数
        int successNum = ZERO;
        List<String[]> memberList = ExcelUtils.parseExcel(file, fileExtension, 0, 0);// 包含成员信息跟账号信息的一个集合，原子操作
        int memberListSize = memberList.size();
        if (memberListSize > 0) {
            for (String[] memberArray : memberList) {
                /* 按照模板约定，必须是6组数据，分别是名字，班级，性别，电话号码，入学年份，部门，如果不满足直接拒绝写入*/
                if (memberArray.length >= 6) {
                    /* 把数据传过去组成一个Member信息跟Account信息   */
                    Member member = parseMemberInformation(memberArray);
                    Account account = parseAccountInformation(memberArray);
                    //  首先通过成员的名字跟年级去判断是否是同一个成员，一般情况下同一个年级同一个名字的人概率比较的低    //
                    Member queryMember = memberMapper.selectByName(member.getMemberName());
                    if (queryMember != null && Objects.equals(queryMember.getMemberGradeNumber(), member.getMemberGradeNumber())) {
                        logger.error("数据库中存在这个成员,成员名字:【{}】,班级【{}】,年级【{}】", queryMember.getMemberName(), member.getMemberClassName(), member.getMemberGradeNumber());
                        failList.get().add(member);
                    } else {
                        //   获取插入后返回的Member信息   //
                        Member insertMember = insertBatch(member);
                        //   首先判断插入成员是否成功，成功的话会返回一个带自增主键的Member实体类,判断Id是否为空就可以知道是否插入成功了
                        if (insertMember != null && insertMember.getMemberId() != null) {
                            //   给当前的账号信息设置一个对应的成员信息    //
                            account.setAccountMember(insertMember);
                            //    开始插入账号信息      //
                            boolean successStatus = insertBatch(account);
                            if (successStatus) {
                                logger.info("成功插入信息{}", member.toString());
                                successNum++;
                            }
                        } else {
                            logger.error("插入成员信息失败{}", member.toString());
                        }
                    }
                } else {
                    logger.info("数据不符合约定，当前数据个数为{}", memberArray.length);
                }

            }
            logger.info("成功插入的行数为:{}，失败的行数为{}", successNum, memberListSize - successNum);
        } else {
            logger.warn("读取出来的数据是空的，无法进行插入");
            throw new BusinessException(BaseEnum.ADD_FAIL_ALL_NULL);
        }
        return failList;
    }


    /**
     * 根据传过来的数组读取出来,然后组成一个Account实体信息.
     *
     * @param memberArray 数组数据
     * @return 组合成的Account数据
     */
    private Account parseAccountInformation(String[] memberArray) {
        Account account = new Account();
        //   因为要自动产生一个账号，所以默认拉取电话号码作为登录账号   //
        Long phoneNumber = Long.valueOf(memberArray[3]);
        String phoneString = String.valueOf(phoneNumber);
        account.setAccountPhone(phoneNumber);
        account.setAccountName(phoneString);
        return account;
    }

    /**
     * 根据传过来的数组读取出,然后组成一个Member实体信息.
     *
     * @param memberArray 数组数据
     * @return 组合成的Member数据
     */
    private Member parseMemberInformation(String[] memberArray) {
        Member member = new Member();
        Department department = new Department();
        //  创建一个List来存放读取的每一行数据     //
        member.setMemberName(memberArray[0]);
        member.setMemberClassName(memberArray[1]);
        member.setMemberSex(convertSexToBoolean(memberArray[2]));
        member.setMemberGradeNumber(Integer.valueOf(memberArray[4]));
        department.setDepartmentId(convertDepartId(memberArray[5]));
        member.setMemberDepartment(department);
        return member;
    }

    /**
     * 根据文本判断性别，如果没有获取到就默认是男
     *
     * @param sexText 可能是性别的文字
     * @return 用boolean表示的性别，true为男，false为女
     */
    private static boolean convertSexToBoolean(String sexText) {
        switch (sexText) {
            case "男":
                return true;
            case "女":
                return false;
            default:
                return true;
        }
    }

    /**
     * 通过部门的名字转换成部门ID，从数据库读取部门id
     *
     * @param departName 部门的名字
     * @return 部门对应的ID
     */
    private Integer convertDepartId(String departName) {
        Department department = departmentMapper.selectByName(departName);
        return department.getDepartmentId() != null ? department.getDepartmentId() : 0;
    }


    /**
     * 往数据库里面插入Member的信息
     *
     * @param member Member实体信息
     * @return 如果插入成功就返回带自增主键Id的Member实体信息，否则就返回一个NULL对象
     */
    @SystemControllerLog(description = "批量插入成员信息失败")
    @Transactional(rollbackFor = Exception.class)
    public Member insertBatch(Member member) {
        try {
            memberMapper.insertAndReturnId(member);
            return member;
        } catch (Exception e) {
            logger.error("插入成员信息发生了异常，信息为{}", member);
            logger.error("{}", e.getMessage());
        }
        return null;
    }

    /**
     * 往数据库里面插入账号信息
     *
     * @param account 账号实体信息
     * @return 是否插入成功的布尔值
     */
    @SystemControllerLog(description = "批量自动产生账号信息失败")
    @Transactional(rollbackFor = Exception.class)
    public boolean insertBatch(Account account) {
        try {
            /* 获取插入成功的行数  */
            int successRow = accountMapper.insertAndReturnId(account);
            if (successRow != 0) {
                logger.info("成功插入一条账号信息,信息为{}", account);
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /*   查询正常的成员信息    */
    @Override
    public List<Member> selectNormalMember() {
        return memberMapper.selectNormalMember();
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
