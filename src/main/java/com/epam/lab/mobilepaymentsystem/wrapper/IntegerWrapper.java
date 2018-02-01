package com.epam.lab.mobilepaymentsystem.wrapper;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

public class IntegerWrapper {

    @Min(50)
    @Max(1000)
    private int tranche;

    public int getTranche() {
        return tranche;
    }

    public void setTranche(int tranche) {
        this.tranche = tranche;
    }
}
