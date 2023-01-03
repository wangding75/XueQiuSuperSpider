package org.decaywood.model;

import lombok.Data;

@Data
public class StockKlineBase {
    public String stockNo;
    public long timestamp;
    public long volume;
    public double open;
    public double high;
    public double low;
    public double close;
    public double chg;
    public double percent;
    public double turnoverrate; // 周转率
    public long amount;
    public long volume_post;
    public long amount_post;
    public long pe;
    public long pb;
    public long ps;
    public long pcf;
    public long market_capital;
    public long balance;
    public long hold_volume_cn;
    public long hold_ratio_cn;
    public long net_volume_cn;
    public long hold_volume_hk;
    public long hold_ratio_hk;
    public long net_volume_hk;
}
