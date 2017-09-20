package com.suny.association.pojo.po;

import org.springframework.stereotype.Component;

/**
 * Created by 孙建荣 on 17-9-20.上午9:34
 */
@Component
public class HostHolder {

    private static ThreadLocal<Account> accounts = new ThreadLocal<>();

    public Account getAccount() {
        return accounts.get();
    }

    public void setAccounts(Account account) {
        accounts.set(account);
    }

    public void clear() {
        accounts.remove();
    }
}
