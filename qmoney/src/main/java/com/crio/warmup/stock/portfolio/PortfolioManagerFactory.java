
package com.crio.warmup.stock.portfolio;

import com.crio.warmup.stock.quotes.StockQuoteServiceFactory;
import org.springframework.web.client.RestTemplate;

public class PortfolioManagerFactory {

  @Deprecated
  public static PortfolioManager getPortfolioManager(RestTemplate restTemplate) {
    return new PortfolioManagerImpl(new RestTemplate());
  }

  // TODO: CRIO_TASK_MODULE_ADDITIONAL_REFACTOR
  // Implement the method to return new instance of PortfolioManager.
  // Steps:
  // 1. Create appropriate instance of StoockQuoteService using StockQuoteServiceFactory and then
  // use the same instance of StockQuoteService to create the instance of PortfolioManager.
  // 2. Mark the earlier constructor of PortfolioManager as @Deprecated.
  // 3. Make sure all of the tests pass by using the gradle command below:
  // ./gradlew test --tests PortfolioManagerFactory


  public static PortfolioManager getPortfolioManager(String provider, RestTemplate restTemplate) {
    return new PortfolioManagerImpl(
        StockQuoteServiceFactory.INSTANCE.getService(provider, restTemplate));
  }

}
