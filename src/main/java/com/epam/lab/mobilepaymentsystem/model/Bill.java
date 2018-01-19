package com.epam.lab.mobilepaymentsystem.model;

import javax.persistence.*;

@Entity
@Table(name = "bills")
public class Bill extends AbstractEntity {

    @Column(name = "user")
    private String user;

    @Column(name = "service")
    private String service;

    @Column(name = "is_paid")
    private Boolean isPaidFor;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public Boolean getPaidFor() {
        return isPaidFor;
    }

    public void setPaidFor(Boolean paidFor) {
        isPaidFor = paidFor;
    }
}
