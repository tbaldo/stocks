package br.com.baldo.stocks.service;

import br.com.baldo.stocks.model.DetailStock;
import br.com.baldo.stocks.model.Stocks;
import org.json.JSONException;

import java.util.List;

public interface StockService {
    DetailStock getTickerBySymbol(String ticker);
    DetailStock getTickers(List<Stocks> listTicker);

}
