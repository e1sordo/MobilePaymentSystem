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
     * Actual cost is the cost of a service at the moment of subscribing
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bill bill = (Bill) o;
        return paidFor == bill.paidFor &&
                user.getId() == bill.user.getId() &&
                serviceUnit.getId() == bill.serviceUnit.getId()
                && actualCost == bill.actualCost;
    }

    @Override
    public int hashCode() {

        return Objects.hash(user, serviceUnit, paidFor);
    }
}
