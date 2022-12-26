package org.decaywood.collector.snowball;

import com.fasterxml.jackson.databind.JsonNode;
import org.decaywood.entity.Stock;
import org.decaywood.entity.enums.MarketType;
import org.decaywood.timeWaitingStrategy.TimeWaitingStrategy;
import org.decaywood.utils.RequestParaBuilder;
import org.decaywood.utils.URLMapper;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: decaywood
 * @date: 2015/11/25 13:05
 */

/**
 * 股票清单
 */
public class StockListCollector extends AbstractCollector<List<Stock>> {


    private MarketType marketType;

    public StockListCollector(MarketType marketType) {
        this(null, marketType);
    }


    /**
     * @param strategy 超时等待策略（null则设置为默认等待策略）
     * @param marketType marketType
     */
    public StockListCollector(TimeWaitingStrategy strategy, MarketType marketType) {
        super(strategy);
        this.marketType = marketType;
    }

    @Override
    public List<Stock> collectLogicByPage(int page, int pageNum) throws Exception {
        String target = URLMapper.STOCK_LIST.toString();
        RequestParaBuilder builder = new RequestParaBuilder(target)
                .addParameter("page", page)
                .addParameter("size", pageNum)
                .addParameter("order", "desc")
                .addParameter("orderby", "percent")
                .addParameter("order_by", "percent")
                .addParameter("market", marketType.getMarket())
                .addParameter("type", marketType.getType());
        URL url = new URL(builder.build());
        String json = request(url);
        JsonNode node = mapper.readTree(json);
        return processNode(node);
    }

    @Override
    public List<Stock> collectLogic() throws Exception {
        return null;
    }

    private List<Stock> processNode(JsonNode node) {

        List<Stock> stocks = new ArrayList<>();
        JsonNode data = node.get("data");
        if (data == null) return stocks;
        JsonNode stockList = data.get("list");
        if (stockList == null || stockList.size() == 0) return stocks;
        for (JsonNode jsonNode : stockList) {
            String symbol = jsonNode.get("symbol").asText();
            if (marketType == MarketType.SH_SZ || marketType == MarketType.KCB) {
                symbol = symbol.substring(2);
            }
            String name = jsonNode.get("name").asText();
            Stock stock = new Stock(name, symbol);
            stocks.add(stock);
        }
        return stocks;

    }
}
