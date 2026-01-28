package com.boot.ordercraft.dto;

import com.boot.ordercraft.model.AccountStatus;

public class AccountStatusStatsDTO {

    private AccountStatus accountStatus;
    private Long count;

    public AccountStatusStatsDTO(AccountStatus accountStatus, Long count) {
        this.accountStatus = accountStatus;
        this.count = count;
    }

    public AccountStatus getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(AccountStatus accountStatus) {
        this.accountStatus = accountStatus;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
