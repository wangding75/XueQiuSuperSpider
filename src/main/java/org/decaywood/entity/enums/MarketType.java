package org.decaywood.entity.enums;

/**
 * 市场类型
 */
public enum MarketType {
    SH_SZ("CN", "sh_sz"), // 沪深
    KCB("CN", "kcb"), // 科创
    HK("HK", "hk"),   //港股
    US("US", "us");   //美股
    private String market;
    private String type;
    MarketType(String market, String type) {
        this.market = market;
        this.type = type;
    }

    public String getMarket() {
        return market;
    }

    public String getType() {
        return type;
    }
}
