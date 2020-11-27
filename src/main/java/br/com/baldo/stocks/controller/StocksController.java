package br.com.baldo.stocks.controller;

import br.com.baldo.stocks.api.StockApi;
import br.com.baldo.stocks.model.DetailStock;
import br.com.baldo.stocks.model.ErrorModel;
import br.com.baldo.stocks.model.Stocks;
import br.com.baldo.stocks.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

import static org.springframework.http.HttpStatus.*;

@RestController
public class StocksController extends BaseController implements StockApi {

    @Autowired
    private StockService stockService;

    @Override
    public ResponseEntity<DetailStock> stocks(@Valid List<Stocks> body) {
        DetailStock detail = stockService.getTickers(body);
        if (Objects.isNull(detail)) {
            return new ResponseEntity<>(NOT_FOUND);
        }
        return new ResponseEntity<>(detail, OK);
    }

    @Override
    public ResponseEntity<DetailStock> stocksById(String ticker) {
        DetailStock detail = stockService.getTickerBySymbol(ticker);
        if (Objects.isNull(detail)) {
            return new ResponseEntity<>(NOT_FOUND);
        }
        return new ResponseEntity<>(detail, OK);
    }
}
