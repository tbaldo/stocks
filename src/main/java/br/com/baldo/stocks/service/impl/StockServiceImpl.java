package br.com.baldo.stocks.service.impl;

import br.com.baldo.stocks.model.DetailStock;
import br.com.baldo.stocks.model.ErrorModel;
import br.com.baldo.stocks.model.Stocks;
import br.com.baldo.stocks.model.Ticker;
import br.com.baldo.stocks.service.StockService;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Service
public class StockServiceImpl implements StockService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${api.hg-brazil.url}")
    private String apiHost;

    @Value("${api.hg-brazil.key}")
    private String apiKey;



    @Override
    public DetailStock getTickerBySymbol(String ticker) {
        String result = restTemplate.getForObject(apiHost +"?key=" + apiKey +"&symbol="+ ticker, String.class);
        DetailStock dt = new DetailStock();
        DecimalFormat formatoDois = new DecimalFormat("R$ ##,###,###,##0.00", new DecimalFormatSymbols(new Locale("pt", "BR")));
        formatoDois.setMinimumFractionDigits(2);
        formatoDois.setParseBigDecimal (true);
        try {
            final JSONObject response = new JSONObject(result);
            final JSONObject resultResponse = new JSONObject(response.getString("results"));
            final JSONObject tickerObj = new JSONObject(resultResponse.getString(ticker.toUpperCase()));
            dt.setFromCache(response.getBoolean("from_cache"));
            Ticker tck = new Ticker();
            tck.setSimbol(tickerObj.getString("symbol"));
            tck.setPrice(formatoDois.format(BigDecimal.valueOf(tickerObj.getDouble("price"))));
            tck.setUpdatedAt(tickerObj.getString("updated_at"));
            dt.setResults(Arrays.asList(tck));
            return dt;
        } catch (JSONException e) {
            return null;
        }
    }

    @Override
    public DetailStock getTickers(List<Stocks> listTicker) {
        DetailStock dt = new DetailStock();
        List<Ticker> listTck = new ArrayList<>();
        DecimalFormat formatoDois = new DecimalFormat("R$ ##,###,###,##0.00", new DecimalFormatSymbols(new Locale("pt", "BR")));
        formatoDois.setMinimumFractionDigits(2);
        formatoDois.setParseBigDecimal (true);
        for (Stocks ticker : listTicker) {
            String result = restTemplate.getForObject(apiHost +"?key=" + apiKey +"&symbol="+ ticker.getTicker(), String.class);
            final JSONObject response;
            final JSONObject resultResponse;
            final JSONObject tickerObj;
            try {
                response = new JSONObject(result);
                resultResponse = new JSONObject(response.getString("results"));
                tickerObj = new JSONObject(resultResponse.getString(ticker.getTicker().toUpperCase()));
                dt.setFromCache(response.getBoolean("from_cache"));
                Ticker tck = new Ticker();
                if (tickerObj.has("symbol")) {
                    tck.setSimbol(tickerObj.getString("symbol"));
                    tck.setPrice(formatoDois.format(BigDecimal.valueOf(tickerObj.getDouble("price"))));
                    tck.setUpdatedAt(tickerObj.getString("updated_at"));
                } else {
                    tck.setSimbol(ticker.getTicker().toUpperCase());
                    tck.setPrice(tickerObj.getString("message"));
                }
                listTck.add(tck);

            } catch (JSONException e) {
                e.printStackTrace();

            }
        }
        dt.setResults(listTck);
        return dt;
    }
}
