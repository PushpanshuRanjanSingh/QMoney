#!/bin/bash
curl -H "Content-Type: application/json" -H "Authorization: Token c3c30fdd7e60340bfbc6cc05a4ba45fec46fcefd" https://api.tiingo.com/tiingo/daily/aapl/prices
echo
echo
echo
curl -H "Content-Type: application/json" -H "Authorization: Token c3c30fdd7e60340bfbc6cc05a4ba45fec46fcefd" https://api.tiingo.com/tiingo/daily/googl/prices
echo
echo
echo
echo "Downloading Apple Stock Json"
curl -H "Content-Type: application/json" -H "Authorization: Token c3c30fdd7e60340bfbc6cc05a4ba45fec46fcefd" https://api.tiingo.com/tiingo/daily/aapl/prices -o aapl_prices.json
echo
echo
echo





