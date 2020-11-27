package br.com.baldo.stocks.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetailStockResponse {
    public String by;
    public String valid_key;
    public ResultsResponse results;
    public String execution_time;
    public String from_cache;

}
