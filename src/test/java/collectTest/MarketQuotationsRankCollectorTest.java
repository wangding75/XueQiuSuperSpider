package collectTest;

import org.decaywood.collector.MarketQuotationsRankCollector;
import org.decaywood.collector.MarketQuotationsRankCollector.StockType;
import org.decaywood.collector.StockListCollector;
import org.decaywood.entity.Stock;
import org.decaywood.entity.enums.MarketType;
import org.decaywood.utils.DatabaseAccessor;
import org.junit.Assert;
import org.junit.Test;

import javax.xml.crypto.Data;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * @author: decaywood
 * @date: 2015/11/25 14:55
 */
public class MarketQuotationsRankCollectorTest {

    @Test
    public void test2k(){
        System.out.println("CWT INT'L".replace("'", "\'"));
    }
    @Test
    public void testGetAllStock() throws Exception {

        DatabaseAccessor dbAcc = new DatabaseAccessor();
        Connection connection = dbAcc.getConnection();
        Statement statement = connection.createStatement();
        int sum = 0;

        StockListCollector SH_SZ_collector =
                new StockListCollector(MarketType.US);
        for (int i = 0; i < 9999; i++) {
            List<Stock> stocks = SH_SZ_collector.collectLogicByPage(i + 1, 500);
            if (stocks.size() == 0) break;
            System.out.println(stocks.size());
            stocks.forEach(stock -> {
                String stockNo = stock.getStockNo();
                String stockName = stock.getStockName();
                try {
                    stockName = stockName.replace("'", "\\'");
                    String s = "insert base_stock values('" + stockNo + "', '" + stockName + "', '" + MarketType.US.getType() +"')";
                    System.out.println(s);
                    statement.addBatch(s);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
            statement.executeBatch();
            sum += stocks.size();
        } 

//        for (int i = 0; i < 9999; i++) {
//            List<Stock> stocks = SH_B_collector.collectLogicByPage(i + 1, 500);
//            if (stocks.size() == 0) break;
//            System.out.println(stocks.size());
//            sum += stocks.size();
//        }
        System.out.println("sum : " + sum);
        sum = 0;
//        MarketQuotationsRankCollector SZ_A_collector =
//                new MarketQuotationsRankCollector(StockType.SZ_A,
//                        MarketQuotationsRankCollector.ORDER_BY_TURNOVER_RATE,
//                        500);
//        for (int i = 0; i < 9999; i++) {
//            List<Stock> stocks = SZ_A_collector.collectLogicByPage(i + 1, 500);
//            if (stocks.size() == 0) break;
//            System.out.println(stocks.size());
//            sum += stocks.size();
//        }
//        System.out.println("sum : " + sum);
//        sum = 0;
//        MarketQuotationsRankCollector GROWTH_ENTERPRISE_BOARD_collector =
//                new MarketQuotationsRankCollector(StockType.GROWTH_ENTERPRISE_BOARD,
//                        MarketQuotationsRankCollector.ORDER_BY_TURNOVER_RATE,
//                        500);
//        for (int i = 0; i < 9999; i++) {
//            List<Stock> stocks = GROWTH_ENTERPRISE_BOARD_collector.collectLogicByPage(i + 1, 500);
//            if (stocks.size() == 0) break;
//            System.out.println(stocks.size());
//            sum += stocks.size();
//        }
//        System.out.println("sum : " + sum);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNull() {

        MarketQuotationsRankCollector collector =
                new MarketQuotationsRankCollector(null,
                        MarketQuotationsRankCollector.ORDER_BY_TURNOVER_RATE,
                        -5);
        Assert.assertNotNull(collector.getStockType());
        MarketQuotationsRankCollector collector1 =
                new MarketQuotationsRankCollector(StockType.GROWTH_ENTERPRISE_BOARD,
                        null,
                        -5);
        Assert.assertNotNull(collector1.getOrderPattern());
    }


    @Test(expected = IllegalArgumentException.class)
    public void testOverFlowTopK() {
        MarketQuotationsRankCollector collector =
                new MarketQuotationsRankCollector(StockType.GROWTH_ENTERPRISE_BOARD,
                        MarketQuotationsRankCollector.ORDER_BY_TURNOVER_RATE,
                        -5);
    }


    @Test
    public void testTopKMaxSize() throws Exception {
        MarketQuotationsRankCollector collector =
                new MarketQuotationsRankCollector(StockType.GROWTH_ENTERPRISE_BOARD,
                        MarketQuotationsRankCollector.ORDER_BY_TURNOVER_RATE,
                        100);
        List<Stock> stocks = collector.get();
        Assert.assertTrue(stocks.size() <= MarketQuotationsRankCollector.TOPK_MAX_SHRESHOLD);
    }

    @Test
    public void testTopKSize() throws Exception {
        int orderSize = 3;
        MarketQuotationsRankCollector collector =
                new MarketQuotationsRankCollector(StockType.GROWTH_ENTERPRISE_BOARD,
                        MarketQuotationsRankCollector.ORDER_BY_TURNOVER_RATE,
                        orderSize);
        List<Stock> stocks = collector.get();
        Assert.assertTrue(stocks.size() == orderSize);
    }

    @Test
    public void testStockType() {
        doTestStockType(StockType.GROWTH_ENTERPRISE_BOARD);
        doTestStockType(StockType.HK);
        doTestStockType(StockType.SH_A);
        doTestStockType(StockType.SH_B);
        doTestStockType(StockType.SMALL_MEDIUM_ENTERPRISE_BOARD);
        doTestStockType(StockType.SZ_A);
        doTestStockType(StockType.SZ_B);
        doTestStockType(StockType.US);
    }

    private void doTestStockType(StockType type) {
        MarketQuotationsRankCollector collector =
                new MarketQuotationsRankCollector(type,
                        MarketQuotationsRankCollector.ORDER_BY_TURNOVER_RATE);
        List<Stock> stocks = collector.get();
        Assert.assertTrue(stocks.size() > 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWrongOrderBy() {
        doTestOrderBy("wrong");
    }


    @Test
    public void testOrderBy() {
        doTestOrderBy(MarketQuotationsRankCollector.ORDER_BY_AMOUNT);
        doTestOrderBy(MarketQuotationsRankCollector.ORDER_BY_PERCENT);
        doTestOrderBy(MarketQuotationsRankCollector.ORDER_BY_TURNOVER_RATE);
        doTestOrderBy(MarketQuotationsRankCollector.ORDER_BY_VOLUME);
    }


    private void doTestOrderBy(String orderBy) {
        MarketQuotationsRankCollector collector =
                new MarketQuotationsRankCollector(StockType.HK, orderBy);
        List<Stock> stocks = collector.get();
        Assert.assertTrue(stocks.size() > 0);
    }


}
