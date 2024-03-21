
package com.crio.warmup.stock.portfolio;

import com.crio.warmup.stock.dto.AnnualizedReturn;
import com.crio.warmup.stock.dto.Candle;
import com.crio.warmup.stock.dto.PortfolioTrade;
import com.crio.warmup.stock.exception.StockQuoteServiceException;
import com.crio.warmup.stock.quotes.StockQuotesService;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.springframework.web.client.RestTemplate;

public class PortfolioManagerImpl implements PortfolioManager {

  private RestTemplate restTemplate;
  private StockQuotesService stockQuotesService;
  String APIKEY = "fc9725b00d3352ba451e2632f673ef0384ec5c6d";
  String avAPIKEY = "NHW5ABM6IDMJW8I4";


  // Caution: Do not delete or modify the constructor, or else your build will break!
  // This is absolutely necessary for backward compatibility
  @Deprecated
  protected PortfolioManagerImpl(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  // TODO: CRIO_TASK_MODULE_REFACTOR
  // 1. Now we want to convert our code into a module, so we will not call it from main anymore.
  // Copy your code from Module#3 PortfolioManagerApplication#calculateAnnualizedReturn
  // into #calculateAnnualizedReturn function here and ensure it follows the method signature.
  // 2. Logic to read Json file and convert them into Objects will not be required further as our
  // clients will take care of it, going forward.

  // Note:
  // Make sure to exercise the tests inside PortfolioManagerTest using command below:
  // ./gradlew test --tests PortfolioManagerTest


  public PortfolioManagerImpl(StockQuotesService stockQuotesService) {
    this.stockQuotesService = stockQuotesService;
  }

  @Override
  public List<AnnualizedReturn> calculateAnnualizedReturn(List<PortfolioTrade> portfolioTrades,
      LocalDate endDate) throws StockQuoteServiceException {
    List<AnnualizedReturn> annualizedReturns = new ArrayList<>();
    for (PortfolioTrade portfolioTrade : portfolioTrades) {
      List<Candle> candles;
      try {
        candles =
            getStockQuote(portfolioTrade.getSymbol(), portfolioTrade.getPurchaseDate(), endDate);
        annualizedReturns.add(calculateAnnualizedReturns(endDate, portfolioTrade,
            candles.get(0).getOpen(), candles.get(candles.size() - 1).getClose()));
      } catch (JsonProcessingException e) {

      }

    }
    Collections.sort(annualizedReturns, getComparator());
    return annualizedReturns;
  }

  public AnnualizedReturn calculateAnnualizedReturns(LocalDate endDate, PortfolioTrade trade,
      Double buyPrice, Double sellPrice) {
    double totalReturn = (sellPrice - buyPrice) / buyPrice;
    long diff = ChronoUnit.DAYS.between(trade.getPurchaseDate(), endDate);
    double yearSpan = (double) diff / 365;
    double annualizedReturn = Math.pow((1 + totalReturn), (1 / yearSpan)) - 1;
    return new AnnualizedReturn(trade.getSymbol(), annualizedReturn, totalReturn);
  }

  private Comparator<AnnualizedReturn> getComparator() {
    return Comparator.comparing(AnnualizedReturn::getAnnualizedReturn).reversed();
  }

  // CHECKSTYLE:OFF

  // TODO: CRIO_TASK_MODULE_REFACTOR
  // Extract the logic to call Tiingo third-party APIs to a separate function.
  // Remember to fill out the buildUri function and use that.

  public List<Candle> getStockQuote(String symbol, LocalDate from, LocalDate to)
      throws JsonProcessingException, StockQuoteServiceException {
    return stockQuotesService.getStockQuote(symbol, from, to);
  }

  @Override
  public List<AnnualizedReturn> calculateAnnualizedReturnParallel(
      List<PortfolioTrade> portfolioTrades, LocalDate endDate, int numThreads)
      throws InterruptedException, StockQuoteServiceException {

    // Create a ThreadPool with a configurable size
    ExecutorService executorService = Executors.newFixedThreadPool(numThreads);

    // List to store Future objects
    List<Future<AnnualizedReturn>> futures = new ArrayList<>();

    try {
      // Submit tasks to the ThreadPool
      for (PortfolioTrade portfolioTrade : portfolioTrades) {
        Callable<AnnualizedReturn> callableTask = () -> {
          List<Candle> candles;
          try {
            candles = getStockQuote(portfolioTrade.getSymbol(), portfolioTrade.getPurchaseDate(),
                endDate);
            return calculateAnnualizedReturns(endDate, portfolioTrade, candles.get(0).getOpen(),
                candles.get(candles.size() - 1).getClose());
          } catch (JsonProcessingException e) {
            // Handle exception or log if needed
            return null;
          }
        };

        // Submit the task and store the Future object
        futures.add(executorService.submit(callableTask));
      }

      // Collect results from Future objects
      List<AnnualizedReturn> annualizedReturns = new ArrayList<>();
      for (Future<AnnualizedReturn> future : futures) {
        try {
          AnnualizedReturn result = future.get();
          annualizedReturns.add(result);

        } catch (ExecutionException e) {
          // Handle exception or log if needed
          throw new StockQuoteServiceException("Error when calling the API", e);
        }
      }

      // Sort the results
      Collections.sort(annualizedReturns, getComparator());

      return annualizedReturns;

    } finally {
      // Shutdown the ThreadPool
      executorService.shutdown();
    }
  }

}
