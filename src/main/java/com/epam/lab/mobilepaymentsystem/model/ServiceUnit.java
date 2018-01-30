package com.epam.lab.mobilepaymentsystem.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import java.util.Objects;

@Entity
@Table (name = "services")
public class ServiceUnit extends AbstractEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "cost")
    @Min(1)
    private Integer cost;

    @Column (name = "duration")
    private Integer duration;

    public ServiceUnit() {
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

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    // todo: fix when we will change Set to List
    @Override
    public int hashCode() {
        return Objects.hash(name, cost, duration);
    }
}
