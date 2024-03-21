
package com.crio.warmup.stock.dto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PortfolioTrade {

  public PortfolioTrade() {}

  public static enum TradeType {
    BUY, SELL
  }

  private String symbol;
  private int quantity;
  private TradeType tradeType;
  private LocalDate purchaseDate;
  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  public PortfolioTrade(String symbol, int quantity, LocalDate purchaseDate) {
    this.symbol = symbol;
    this.quantity = quantity;
    this.purchaseDate = purchaseDate;
    this.tradeType = TradeType.BUY;
  }

  public String getSymbol() {
    return symbol;
  }

  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  public TradeType getTradeType() {
    return tradeType;
  }

  public void setTradeType(TradeType tradeType) {
    this.tradeType = tradeType;
  }

  public LocalDate getPurchaseDate() {
    return purchaseDate;
  }

  public void setPurchaseDate(Object purchaseDate) {
    if (purchaseDate instanceof CharSequence) {
      this.purchaseDate = LocalDate.parse((CharSequence) purchaseDate, formatter);
    } else {
      this.purchaseDate = (LocalDate) purchaseDate;
    }
  }


  @Override
  public String toString() {
    return "PortfolioTrade [purchaseDate=" + purchaseDate + " quantity=" + quantity + ", symbol="
        + symbol + ", tradeType=" + tradeType + "]";
  }
}


