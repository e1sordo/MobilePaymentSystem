package com.epam.lab.mobilepaymentsystem.wrapper;

import javax.validation.constraints.Min;

public class IntegerWrapper {

    @Min(1)
    private int tranche = 0;

    public int getTranche() {
        return tranche;
    }

    public void setTranche(int tranche) {
        this.tranche = tranche;
    }
}
