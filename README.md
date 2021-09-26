## Stock Exchange Application

This application provides an order matching engine, stores the orders and generates trades.

It uses priority blocking queue to handle the buy orders and the sell orders sorted by price, time and id priority.

It uses spring boot server to allow connections from multiple clients.

### Input:

* POST - /api/tradeOrder - TEXT: It accepts input as text format as "buy 250 shares at 20.35"

### Output:

* GET /getOrderBook: Order book and Trade History are displayed in HTML format
* GET /api/getOrderBook: Order book is returned in json format
* GET /api/getTrades: Trade history is returned in json format

### Quick Start
* Run the jar file - java -jar OrderEngine-0.0.1-SNAPSHOT.jar
* To view the initial orders - http://localhost:8080/getOrderBook
* To test the test case - http://localhost:8080/api/tradeOrder - POST - buy 250 shares at 20.35
