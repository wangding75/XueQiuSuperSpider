package collectTest;

import org.decaywood.collector.snowball.MostProfitableCubeCollector;
import org.decaywood.entity.Cube;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * @author: decaywood
 * @date: 2015/11/25 22:46.
 */
public class MostProfitableCubeCollectorTest {


    @Test
    public void testMarket() {
        doTest(MostProfitableCubeCollector.Market.CN);
        doTest(MostProfitableCubeCollector.Market.HK);
        doTest(MostProfitableCubeCollector.Market.US);
    }

    @Test
    public void testORDER_BY() {
        doTest(MostProfitableCubeCollector.ORDER_BY.DAILY);
        doTest(MostProfitableCubeCollector.ORDER_BY.MONTHLY);
        doTest(MostProfitableCubeCollector.ORDER_BY.YEARLY);
    }

    @Test
    public void testNull() {
        MostProfitableCubeCollector collector =
                new MostProfitableCubeCollector(null, null
                        , null, 1);
        Assert.assertNotNull(collector.getMarket());
        Assert.assertNotNull(collector.getOrder_by());
    }


    @Test(expected = IllegalArgumentException.class)
    public void testIllegal() {
        new MostProfitableCubeCollector(null, MostProfitableCubeCollector.Market.CN
                , MostProfitableCubeCollector.ORDER_BY.DAILY, -500);
    }

    @Test
    public void testTopKMaxSize() {

        MostProfitableCubeCollector collector =
                new MostProfitableCubeCollector(null, MostProfitableCubeCollector.Market.CN
                        , MostProfitableCubeCollector.ORDER_BY.DAILY, 1000);
        List<Cube> cubes = collector.get();
        Assert.assertTrue(cubes.size() <= MostProfitableCubeCollector.CUBE_SIZE_SHRESHOLD);

    }

    @Test
    public void testTopKSize() throws Exception {
        int orderSize = 10;
        MostProfitableCubeCollector collector =
                new MostProfitableCubeCollector(null, MostProfitableCubeCollector.Market.CN
                        , MostProfitableCubeCollector.ORDER_BY.DAILY, orderSize);
        List<Cube> cubes = collector.get();
        Assert.assertTrue(cubes.size() == orderSize);
    }
    private void doTest(MostProfitableCubeCollector.Market market) {
        MostProfitableCubeCollector collector = new MostProfitableCubeCollector(market);
        List<Cube> cubes = collector.get();
        Assert.assertFalse(cubes.isEmpty());
    }

    private void doTest(MostProfitableCubeCollector.ORDER_BY order_by) {
        MostProfitableCubeCollector collector = new MostProfitableCubeCollector(
                MostProfitableCubeCollector.Market.CN,
                order_by);
        List<Cube> cubes = collector.get();
        Assert.assertFalse(cubes.isEmpty());
    }
}
