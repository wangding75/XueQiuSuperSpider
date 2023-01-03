package org.decaywood;

import org.decaywood.collector.snowball.StockKLineCollector;
import org.decaywood.collector.snowball.StockListCollector;
import org.decaywood.entity.Stock;
import org.decaywood.entity.enums.KLinePeriod;
import org.decaywood.entity.enums.MarketType;
import org.decaywood.model.StockKlineBase;
import org.decaywood.utils.DatabaseAccessor;
import org.decaywood.wf.StockWorkFlow;

import java.sql.*;
import java.util.*;

public class StockSpiderMain {

    public static void main(String[] args) throws Exception {
//        loadAllStock();
        loadStockKline();
    }

    private static void loadStockKline() throws SQLException {
        Connection connection = DatabaseAccessor.Holder.ACCESSOR.getConnection();
        Statement statement = connection.createStatement();

//        ResultSet rs1 = statement.executeQuery("select distinct id from stock_kline_day");
//        Set<String> existStockNoList = new HashSet<>();
//        while (rs1.next()) {
//            existStockNoList.add(rs1.getString(1));
//        }
//        System.out.println("has read " + existStockNoList.size());
        ResultSet rs = statement.executeQuery("select distinct id from base_stock where market_type != 'us'");
        Set<String> stockNoList = new HashSet<>();
        while (rs.next()) {
            String no = rs.getString(1);
            stockNoList.add(no);
        }
        System.out.println("wait read " + stockNoList.size());

        stockNoList.forEach(stockNo -> {
            StockKLineCollector collector = new StockKLineCollector(stockNo, KLinePeriod.DAY);
            try {
                List<StockKlineBase> stockKlineBaseList = collector.collectLogic();
                stockKlineBaseList.forEach(base -> {
                    String sql = "insert into stock_kline_day_1 values("
                            + "'" + base.stockNo + "',"
                            + base.timestamp + ","
                            + base.volume + ","
                            + base.open + ","
                            + base.high + ","
                            + base.low + ","
                            + base.close + ","
                            + base.chg + ","
                            + base.percent + ","
                            + base.turnoverrate + ","
                            + base.amount + ","
                            + base.volume_post + ","
                            + base.amount_post + ","
                            + base.pe + ","
                            + base.pb + ","
                            + base.ps + ","
                            + base.pcf + ","
                            + base.market_capital + ","
                            + base.balance + ","
                            + base.hold_volume_cn + ","
                            + base.hold_ratio_cn + ","
                            + base.net_volume_cn + ","
                            + base.hold_volume_hk + ","
                            + base.hold_ratio_hk + ","
                            + base.net_volume_hk + ")";
                    try {
                        statement.addBatch(sql);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
                statement.executeBatch();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        DatabaseAccessor.Holder.ACCESSOR.returnConnection(connection);
    }

    public static void loadAllStock() throws Exception {
        // 创建收集器
        List<StockListCollector> collectors = new ArrayList<>();
        collectors.add(new StockListCollector(MarketType.SH_SZ));
        collectors.add(new StockListCollector(MarketType.KCB));
        collectors.add(new StockListCollector(MarketType.HK));
        collectors.add(new StockListCollector(MarketType.US));
        Connection connection = DatabaseAccessor.Holder.ACCESSOR.getConnection();
        String insertSQL = "insert base_stock (id, name, market_type, data_source, id_prefix) values";
        Statement statement = connection.createStatement();
        collectors.forEach(collector -> {
            MarketType marketType = collector.getMarketType();
            for (int i = 0; i < 9999; i++) {
                List<Stock> stocks = null;
                try {
                    stocks = collector.collectLogicByPage(i + 1, 500);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (stocks.size() == 0) return;
                stocks.forEach(stock -> {
                    String stockNo = stock.getStockNo();
                    String stockName = stock.getStockName();
                    try {
                        stockName = stockName.replace("'", "\\'");
                        String idPrefix = marketType == MarketType.HK || marketType == MarketType.US? "": stockNo.substring(0, 2);
                        List<String> params = Arrays.asList(stockNo, stockName, marketType.getType(), "xq", idPrefix);
                        String splitSql = "('" + String.join("', '", params) + "')";
                        statement.addBatch(insertSQL + splitSql);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
                try {
                    statement.executeBatch();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        DatabaseAccessor.Holder.ACCESSOR.returnConnection(connection);
    }
}
