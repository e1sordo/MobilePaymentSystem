package com.epam.lab.mobilepaymentsystem.model;

import org.apache.tomcat.jni.Local;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Objects;

@Entity
@Table (name = "services")
public class ServiceUnit extends AbstractEntity {

    public static void main(String[] args) throws InterruptedException {
        ServiceUnit su = new ServiceUnit();
        Thread.sleep(5000);
        ServiceUnit su1 = new ServiceUnit();
        System.out.println(su.equals(su1));
    }

    private final static int DURATION = 30;

    @Column (name = "name")
    @NotNull
    @Size(min=2)
    private String name;

    @Column (name = "cost")
    @Min(1)
    private int cost;

    @Column (name = "startDate")
    private LocalDate startDate;

    @Column (name = "endDate")
    private LocalDate endDate;

    public ServiceUnit() {
        this.startDate = LocalDate.now();
        this.endDate = this.startDate.plusDays(DURATION);
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

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public LocalDate getStartDate() { return startDate; }

    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }

    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServiceUnit that = (ServiceUnit) o;
        return cost == that.cost &&
                Objects.equals(name, that.name) &&
                startDate.equals(that.startDate) &&
                endDate.equals(that.endDate);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name, cost, startDate, endDate);
    }
}
