package com.epam.lab.mobilepaymentsystem.model;

import javax.persistence.*;

@Entity
@Table (name = "users")
public class User extends AbstractEntity {

    @Column (name = "is_admin")
    private boolean isAdmin;

    @Column (name = "is_locked")
    private boolean isLocked;

    @Column (name = "personal_account")
    private int personalAccount;

    public String login;

    public String password;

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    public int getPersonalAccount() {
        return personalAccount;
    }

    public void setPersonalAccount(int personalAccount) {
        this.personalAccount = personalAccount;
    }
}
