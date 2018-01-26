package com.epam.lab.mobilepaymentsystem.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table (name = "services")
public class ServiceUnit extends AbstractEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "cost")
    private int cost;

    public ServiceUnit() {
    }

    public ServiceUnit(String name, int cost) {
        this.name = name;
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, cost);
    }
}
