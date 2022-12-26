package collectTest;

import org.decaywood.collector.snowball.StockScopeHotRankCollector;
import org.decaywood.entity.Stock;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * @author: decaywood
 * @date: 2015/11/25 10:49
 */
public class StockScopeHotRankCollectorTest {


    @Test
    public void testNull() {
        StockScopeHotRankCollector collector = new StockScopeHotRankCollector(null);
        Assert.assertNotNull(collector.getScope());
    }
    
    @Test
    public void testGlobal() {
        StockScopeHotRankCollector collector1 = new StockScopeHotRankCollector(StockScopeHotRankCollector.Scope.GLOBAL_WITHIN_1_HOUR);
        StockScopeHotRankCollector collector24 = new StockScopeHotRankCollector(StockScopeHotRankCollector.Scope.GLOBAL_WITHIN_24_HOUR);
        List<Stock> stocks1 = collector1.get();
        List<Stock> stocks24 = collector24.get();
        Assert.assertTrue(stocks1.size() > 0);
        Assert.assertTrue(stocks24.size() > 0);
        for (Stock stock : stocks1) {
            Assert.assertTrue(stock.getStockNo().length() > 0);
            Assert.assertTrue(stock.getStockName().length() > 0);
        }
        for (Stock stock : stocks24) {
            Assert.assertTrue(stock.getStockNo().length() > 0);
            Assert.assertTrue(stock.getStockName().length() > 0);
        }
    }

    @Test
    public void test_SH_SZ() {
        StockScopeHotRankCollector collector1 = new StockScopeHotRankCollector(StockScopeHotRankCollector.Scope.SH_SZ_WITHIN_1_HOUR);
        StockScopeHotRankCollector collector24;
        collector24 = new StockScopeHotRankCollector(StockScopeHotRankCollector.Scope.SH_SZ_WITHIN_24_HOUR);
        List<Stock> stocks1 = collector1.get();
        List<Stock> stocks24 = collector24.get();
        Assert.assertTrue(stocks1.size() > 0);
        Assert.assertTrue(stocks24.size() > 0);
        for (Stock stock : stocks1) {
            Assert.assertTrue(stock.getStockNo().length() > 0);
            Assert.assertTrue(stock.getStockName().length() > 0);
        }
        for (Stock stock : stocks24) {
            Assert.assertTrue(stock.getStockNo().length() > 0);
            Assert.assertTrue(stock.getStockName().length() > 0);
        }
    }




    @Test
    public void test_HK() {
        StockScopeHotRankCollector collector1 = new StockScopeHotRankCollector(StockScopeHotRankCollector.Scope.HK_WITHIN_1_HOUR);
        StockScopeHotRankCollector collector24 = new StockScopeHotRankCollector(StockScopeHotRankCollector.Scope.HK_WITHIN_24_HOUR);
        List<Stock> stocks1 = collector1.get();
        List<Stock> stocks24 = collector24.get();
        Assert.assertTrue(stocks1.size() > 0);
        Assert.assertTrue(stocks24.size() > 0);
        for (Stock stock : stocks1) {
            Assert.assertTrue(stock.getStockNo().length() > 0);
            Assert.assertTrue(stock.getStockName().length() > 0);
        }
        for (Stock stock : stocks24) {
            Assert.assertTrue(stock.getStockNo().length() > 0);
            Assert.assertTrue(stock.getStockName().length() > 0);
        }
    }

    @Test
    public void test_US() {
        StockScopeHotRankCollector collector1 = new StockScopeHotRankCollector(StockScopeHotRankCollector.Scope.US_WITHIN_1_HOUR);
        StockScopeHotRankCollector collector24 = new StockScopeHotRankCollector(StockScopeHotRankCollector.Scope.US_WITHIN_24_HOUR);
        List<Stock> stocks1 = collector1.get();
        List<Stock> stocks24 = collector24.get();
        Assert.assertTrue(stocks1.size() > 0);
        Assert.assertTrue(stocks24.size() > 0);
        for (Stock stock : stocks1) {
            Assert.assertTrue(stock.getStockNo().length() > 0);
            Assert.assertTrue(stock.getStockName().length() > 0);
        }
        for (Stock stock : stocks24) {
            Assert.assertTrue(stock.getStockNo().length() > 0);
            Assert.assertTrue(stock.getStockName().length() > 0);
        }
    }



    @Test(expected = IllegalArgumentException.class)
    public void testPageSize() {
        new StockScopeHotRankCollector(-5);
    }


    @Test
    public void testOverFlowPageSize() {
        StockScopeHotRankCollector collector = new StockScopeHotRankCollector(30);
        List<Stock> stocks = collector.get();
        Assert.assertTrue(stocks.size() == StockScopeHotRankCollector.PAGE_SIZE_SHRESHOLD);
    }


}
