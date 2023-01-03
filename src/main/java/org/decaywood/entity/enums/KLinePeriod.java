package org.decaywood.entity.enums;

public enum KLinePeriod {
    DAY("day"),
    WEEK("week"),
    MONTH("month"),
    QUART("quart"),
    YEAR("year");
    private String desc;
    KLinePeriod(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
