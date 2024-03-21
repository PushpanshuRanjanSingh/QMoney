
package com.crio.warmup.stock.quotes;

import com.crio.warmup.stock.dto.Candle;
import com.crio.warmup.stock.dto.TiingoCandle;
import com.crio.warmup.stock.exception.StockQuoteServiceException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import org.springframework.web.client.RestTemplate;


public class TiingoService implements StockQuotesService {

  private RestTemplate restTemplate;
  String APIKEY = "fc9725b00d3352ba451e2632f673ef0384ec5c6d";

  protected TiingoService(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  // TODO: CRIO_TASK_MODULE_ADDITIONAL_REFACTOR
  // Implement getStockQuote method below that was also declared in the interface.

  // Note:
  // 1. You can move the code from PortfolioManagerImpl#getStockQuote inside newly created method.
  // 2. Run the tests using command below and make sure it passes.
  // ./gradlew test --tests TiingoServiceTest

  @Override
  public List<Candle> getStockQuote(String symbol, LocalDate from, LocalDate to)
      throws JsonProcessingException, StockQuoteServiceException {

    if (from.compareTo(to) >= 0) {
      throw new RuntimeException();
    }
    List<Candle> candle;
    String uri = buildUri(symbol, from,to);
    String response = restTemplate.getForObject(uri, String.class);
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    TiingoCandle[] tiingoCandles = objectMapper.readValue(response, TiingoCandle[].class);
    if (tiingoCandles != null) {
      Arrays.sort(tiingoCandles, TiingoCandle.dateComparator);
      candle = Arrays.asList(tiingoCandles);
    } else {
      candle = Arrays.asList(new TiingoCandle[0]);
    }
    return candle;
  }


  // CHECKSTYLE:OFF

  // TODO: CRIO_TASK_MODULE_ADDITIONAL_REFACTOR
  // Write a method to create appropriate url to call the Tiingo API.
  protected String buildUri(String symbol, LocalDate startDate, LocalDate endDate) {
    String uriTemplate = "https://api.tiingo.com/tiingo/daily/" + symbol + "/prices?" + "startDate="
        + startDate + "&endDate=" + endDate + "&token=" + APIKEY;
    System.out.println("URITEMPLETE:" + uriTemplate);

    return uriTemplate;
  }


}
