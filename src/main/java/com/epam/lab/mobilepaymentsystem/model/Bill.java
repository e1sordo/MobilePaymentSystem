package com.epam.lab.mobilepaymentsystem.model;

import javax.persistence.*;

@Entity
@Table(name = "bills")
public class Bill extends AbstractEntity {

    @OneToOne (cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private User user;

    @OneToOne (cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private ServiceUnit serviceUnit;

    @Column(name = "is_paid")
    private boolean isPaidFor;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ServiceUnit getServiceUnit() {
        return serviceUnit;
    }

    public void setServiceUnit(ServiceUnit serviceUnit) {
        this.serviceUnit = serviceUnit;
    }

    public boolean getPaidFor() {
        return isPaidFor;
    }

    public void setPaidFor(boolean paidFor) {
        isPaidFor = paidFor;
    }
}
