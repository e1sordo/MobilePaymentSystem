package com.epam.lab.mobilepaymentsystem.model;

import javax.persistence.*;

@Entity
@Table(name = "bills")
public class Bill extends AbstractEntity {

    @Column(name = "user_id")
    private long userId;

    @Column(name = "service_id")
    private long serviceId;

    @Column(name = "is_paid")
    private boolean isPaidFor;

    @Column(name = "name")
    private String name;

    @Column(name = "total")
    private int total;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getServiceId() {
        return serviceId;
    }

    public void setServiceId(long serviceId) {
        this.serviceId = serviceId;
    }

    public boolean isPaidFor() {
        return isPaidFor;
    }

    public void setPaidFor(boolean paidFor) {
        isPaidFor = paidFor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
