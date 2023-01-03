package org.decaywood.collector.snowball;

import com.fasterxml.jackson.databind.JsonNode;
import org.decaywood.collector.AbstractCollector;
import org.decaywood.entity.Stock;
import org.decaywood.entity.enums.KLinePeriod;
import org.decaywood.entity.enums.MarketType;
import org.decaywood.model.StockKlineBase;
import org.decaywood.timeWaitingStrategy.TimeWaitingStrategy;
import org.decaywood.utils.RequestParaBuilder;
import org.decaywood.utils.URLMapper;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: decaywood
 * @date: 2015/11/25 13:05
 */

/**
 * 股票清单
 */
public class StockKLineCollector extends AbstractCollector<List<StockKlineBase>> {

    private String symbol;
    private KLinePeriod period;


    public StockKLineCollector(String symbol, KLinePeriod period) {
        this(null, symbol, period);
    }

    /**
     * @param strategy 超时等待策略（null则设置为默认等待策略）
     */
    public StockKLineCollector(TimeWaitingStrategy strategy, String symbol, KLinePeriod period) {
        super(strategy);
        this.symbol = symbol;
        this.period = period;
    }

    @Override
    public List<StockKlineBase> collectLogic() throws Exception {
        String target = URLMapper.STOCK_K_LINE.toString();
        RequestParaBuilder builder = new RequestParaBuilder(target)
                .addParameter("symbol", symbol)
                .addParameter("begin", System.currentTimeMillis())
                .addParameter("period", period.getDesc())
                .addParameter("type", "before")
                .addParameter("count", -1)
                .addParameter("indicator", "kline,pe,pb,ps,pcf,market_capital,agt,ggt,balance");
        URL url = new URL(builder.build());
        String json = request(url);
        JsonNode node = mapper.readTree(json);
        return processNode(node);
    }

    private List<StockKlineBase> processNode(JsonNode node) {

        List<StockKlineBase> stockKlineBaseList = new ArrayList<>();
        JsonNode data = node.get("data");
        if (data == null) return stockKlineBaseList;
        JsonNode stockList = data.get("item");
        if (stockList == null || stockList.size() == 0) return stockKlineBaseList;
        for (JsonNode jsonNode : stockList) {
            StockKlineBase base = new StockKlineBase();
            base.setStockNo(symbol);
            long timestamp = jsonNode.get(0).asLong();
            long volume = jsonNode.get(1).asLong();
            double open = jsonNode.get(2).asDouble();
            double high = jsonNode.get(3).asDouble();
            double low = jsonNode.get(4).asDouble();
            double close = jsonNode.get(5).asDouble();
            double chg = jsonNode.get(6).asDouble();
            double percent = jsonNode.get(7).asDouble();
            double turnoverrate = jsonNode.get(8).asDouble();
            long amount = jsonNode.get(9).asLong();
            long volume_post = jsonNode.get(10).asLong();
            long amount_post = jsonNode.get(11).asLong();
            long pe = jsonNode.get(12).asLong();
            long pb = jsonNode.get(13).asLong();
            long ps = jsonNode.get(14).asLong();
            long pcf = jsonNode.get(15).asLong();
            long market_capital = jsonNode.get(16).asLong();
            long balance = jsonNode.get(17).asLong();
            long hold_volume_cn = jsonNode.get(18).asLong();
            long hold_ratio_cn = jsonNode.get(19).asLong();
            long net_volume_cn = jsonNode.get(20).asLong();
            long hold_volume_hk = jsonNode.get(21).asLong();
            long hold_ratio_hk = jsonNode.get(22).asLong();
            long net_volume_hk = jsonNode.get(23).asLong();
            base.setTimestamp(timestamp);
            base.setVolume(volume);
            base.setOpen(open);
            base.setHigh(high);
            base.setLow(low);
            base.setClose(close);
            base.setChg(chg);
            base.setPercent(percent);
            base.setTurnoverrate(turnoverrate);
            base.setAmount(amount);
            base.setVolume_post(volume_post);
            base.setAmount_post(amount_post);
            base.setPe(pe);
            base.setPb(pb);
            base.setPs(ps);
            base.setPcf(pcf);
            base.setMarket_capital(market_capital);
            base.setBalance(balance);
            base.setHold_volume_cn(hold_volume_cn);
            base.setHold_ratio_cn(hold_ratio_cn);
            base.setNet_volume_cn(net_volume_cn);
            base.setHold_volume_hk(hold_volume_hk);
            base.setHold_ratio_hk(hold_ratio_hk);
            base.setNet_volume_hk(net_volume_hk);
            stockKlineBaseList.add(base);
        }
        return stockKlineBaseList;

    }
}
