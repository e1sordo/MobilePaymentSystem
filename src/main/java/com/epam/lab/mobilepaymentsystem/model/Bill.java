package com.epam.lab.mobilepaymentsystem.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "bills")
public class Bill extends AbstractEntity {

    @ManyToOne (cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private User user;

    @ManyToOne (cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private ServiceUnit serviceUnit;

    @Column(name = "is_paid")
    private boolean paidFor;

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
        return paidFor;
    }

    public void setPaidFor(boolean paidFor) {
        this.paidFor = paidFor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, serviceUnit, paidFor);
    }
}
