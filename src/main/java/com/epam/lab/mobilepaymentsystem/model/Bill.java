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

    /**
     * Cost of a service at the moment of subscribing
     */
    @Column(name = "actual_cost")
    private int actualCost;

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

    public int getActualCost() {
        return actualCost;
    }

    public void setActualCost(int actualCost) {
        this.actualCost = actualCost;
    }

    // TODO: fix when we will change Set to List
    @Override
    public int hashCode() {
        return Objects.hash(user.getId(), serviceUnit.getId(), paidFor, actualCost);
    }
}
