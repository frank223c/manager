package com.suny.association.entity.po;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author sunjianrong
 */
public class Account implements Serializable {


    private static final long serialVersionUID = 8319454659149015617L;
    private Long accountId;

    private String accountName;

    private String accountPassword;

    private Long accountPhone;

    private String accountEmail;

    private Boolean accountStatus;

    private transient List<AccountRoles> accountRolesList = new ArrayList<>();

    private Member accountMember;


    public Account() {
    }


    public Account(Long accountId, String accountName, String accountPassword, Long accountPhone, String accountEmail, Boolean accountStatus, List<AccountRoles> accountRolesList, Member accountMember) {
        this.accountId = accountId;
        this.accountName = accountName;
        this.accountPassword = accountPassword;
        this.accountPhone = accountPhone;
        this.accountEmail = accountEmail;
        this.accountStatus = accountStatus;
        this.accountRolesList = accountRolesList;
        this.accountMember = accountMember;
    }


    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountPassword() {
        return accountPassword;
    }

    public void setAccountPassword(String accountPassword) {
        this.accountPassword = accountPassword;
    }

    public Long getAccountPhone() {
        return accountPhone;
    }

    public void setAccountPhone(Long accountPhone) {
        this.accountPhone = accountPhone;
    }

    public String getAccountEmail() {
        return accountEmail;
    }

    public void setAccountEmail(String accountEmail) {
        this.accountEmail = accountEmail;
    }

    public Boolean getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(Boolean accountStatus) {
        this.accountStatus = accountStatus;
    }

    public List<AccountRoles> getAccountRolesList() {
        return accountRolesList;
    }

    public void setAccountRolesList(List<AccountRoles> accountRolesList) {
        this.accountRolesList = accountRolesList;
    }

    public Member getAccountMember() {
        return accountMember;
    }

    public void setAccountMember(Member accountMember) {
        this.accountMember = accountMember;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Account account = (Account) o;

        if (accountId != null ? !accountId.equals(account.accountId) : account.accountId != null) {
            return false;
        }
        if (accountName != null ? !accountName.equals(account.accountName) : account.accountName != null) {
            return false;
        }
        if (accountPassword != null ? !accountPassword.equals(account.accountPassword) : account.accountPassword != null) {
            return false;
        }
        if (accountPhone != null ? !accountPhone.equals(account.accountPhone) : account.accountPhone != null) {
            return false;
        }
        if (accountEmail != null ? !accountEmail.equals(account.accountEmail) : account.accountEmail != null) {
            return false;
        }
        if (accountStatus != null ? !accountStatus.equals(account.accountStatus) : account.accountStatus != null) {
            return false;
        }
        if (accountRolesList != null ? !accountRolesList.equals(account.accountRolesList) : account.accountRolesList != null) {
            return false;
        }
        return accountMember != null ? accountMember.equals(account.accountMember) : account.accountMember == null;

    }

    @Override
    public int hashCode() {
        int result = accountId != null ? accountId.hashCode() : 0;
        result = 31 * result + (accountName != null ? accountName.hashCode() : 0);
        result = 31 * result + (accountPassword != null ? accountPassword.hashCode() : 0);
        result = 31 * result + (accountPhone != null ? accountPhone.hashCode() : 0);
        result = 31 * result + (accountEmail != null ? accountEmail.hashCode() : 0);
        result = 31 * result + (accountStatus != null ? accountStatus.hashCode() : 0);
        result = 31 * result + (accountRolesList != null ? accountRolesList.hashCode() : 0);
        result = 31 * result + (accountMember != null ? accountMember.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Account{" +
                "账号Id=" + accountId +
                ", 账号名字='" + accountName + '\'' +
                ", 账号密码='" + accountPassword + '\'' +
                ", 手机号码=" + accountPhone +
                ", 邮箱='" + accountEmail + '\'' +
                ", 账号状态=" + accountStatus +
                ", 账号角色=" + accountRolesList +
                ", 对应成员=" + accountMember +
                '}';
    }
}
