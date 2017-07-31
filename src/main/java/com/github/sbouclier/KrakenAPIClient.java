package com.github.sbouclier;

import com.github.sbouclier.result.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Kraken API client
 *
 * @author Stéphane Bouclier
 */
public class KrakenAPIClient {

    public static String BASE_URL = "https://api.kraken.com";

    private HttpApiClientFactory clientFactory;

    private String apiKey;
    private String apiSecret;

    // ----------------
    // - CONSTRUCTORS -
    // ----------------

    /**
     * Default constructor to call public API requests
     */
    public KrakenAPIClient() {
        this.clientFactory = new HttpApiClientFactory();
    }

    /**
     * Secure constructor to call private API requests
     *
     * @param apiKey
     * @param apiSecret
     */
    public KrakenAPIClient(String apiKey, String apiSecret) {
        this();
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
    }

    /**
     * Constructor injecting {@link com.github.sbouclier.HttpApiClientFactory}
     *
     * @param clientFactory
     */
    public KrakenAPIClient(HttpApiClientFactory clientFactory) {
        this.clientFactory = clientFactory;
    }

    // ---------------
    // - INNER ENUMS -
    // ---------------

    public enum Interval {

        ONE_MINUTE(1),
        FIVE_MINUTES(5),
        FIFTEEN_MINUTES(15),
        THIRTY_MINUTES(30),
        ONE_HOUR(60),
        FOUR_HOURS(240),
        ONE_DAY(1440),
        ONE_WEEK(10080),
        FIFTEEN_DAYS(21600);

        private int minutes;

        Interval(int minutes) {
            this.minutes = minutes;
        }

        @Override
        public String toString() {
            return "Interval{" +
                    "minutes=" + minutes +
                    '}';
        }
    }

    // -----------
    // - METHODS -
    // -----------

    /**
     * Get server time
     *
     * @return server time
     * @throws KrakenApiException
     */
    public ServerTimeResult getServerTime() throws KrakenApiException {
        HttpApiClient<ServerTimeResult> client = (HttpApiClient<ServerTimeResult>) this.clientFactory.getHttpApiClient(KrakenApiMethod.SERVER_TIME);
        return client.callPublic(BASE_URL, KrakenApiMethod.SERVER_TIME, ServerTimeResult.class);
    }

    /**
     * Get asset information
     *
     * @return asset information
     * @throws KrakenApiException
     */
    public AssetInformationResult getAssetInformation() throws KrakenApiException {
        HttpApiClient<AssetInformationResult> client = (HttpApiClient<AssetInformationResult>) this.clientFactory.getHttpApiClient(KrakenApiMethod.ASSET_INFORMATION);
        return client.callPublic(BASE_URL, KrakenApiMethod.ASSET_INFORMATION, AssetInformationResult.class);
    }

    /**
     * Get tradable asset pairs
     *
     * @return asset pairs
     * @throws KrakenApiException
     */
    public AssetPairsResult getAssetPairs() throws KrakenApiException {
        HttpApiClient<AssetPairsResult> client = (HttpApiClient<AssetPairsResult>) this.clientFactory.getHttpApiClient(KrakenApiMethod.ASSET_PAIRS);
        return client.callPublic(BASE_URL, KrakenApiMethod.ASSET_PAIRS, AssetPairsResult.class);
    }

    /**
     * Get ticker information of pairs
     *
     * @param pairs list of pair
     * @return ticker information
     * @throws KrakenApiException
     */
    public TickerInformationResult getTickerInformation(List<String> pairs) throws KrakenApiException {
        HttpApiClient<TickerInformationResult> client = (HttpApiClient<TickerInformationResult>) this.clientFactory.getHttpApiClient(KrakenApiMethod.TICKER_INFORMATION);

        Map<String, String> params = new HashMap<>();
        params.put("pair", String.join(",", pairs));

        return client.callPublic(BASE_URL, KrakenApiMethod.TICKER_INFORMATION, TickerInformationResult.class, params);
    }


    /**
     * Get OHLC data
     *
     * @param pair     currency pair
     * @param interval interval of time
     * @param since    data since given id
     * @return data (OHLC + last id)
     * @throws KrakenApiException
     */
    public OHLCResult getOHLC(String pair, Interval interval, Integer since) throws KrakenApiException {
        HttpApiClient<OHLCResult> client = (HttpApiClient<OHLCResult>) this.clientFactory.getHttpApiClient(KrakenApiMethod.OHLC);

        Map<String, String> params = new HashMap<>();
        params.put("pair", pair);
        params.put("interval", String.valueOf(interval.minutes));
        params.put("since", String.valueOf(since));

        return client.callPublic(BASE_URL, KrakenApiMethod.OHLC, OHLCResult.class, params);
    }

    /**
     * Get OHLC data
     *
     * @param pair     currency pair
     * @param interval interval of time
     * @return data (OHLC + last id)
     * @throws KrakenApiException
     */
    public OHLCResult getOHLC(String pair, Interval interval) throws KrakenApiException {
        HttpApiClient<OHLCResult> client = (HttpApiClient<OHLCResult>) this.clientFactory.getHttpApiClient(KrakenApiMethod.OHLC);

        Map<String, String> params = new HashMap<>();
        params.put("pair", pair);
        params.put("interval", String.valueOf(interval.minutes));

        return client.callPublic(BASE_URL, KrakenApiMethod.OHLC, OHLCResult.class, params);
    }

    /**
     * Get order book
     *
     * @param pair  asset pair
     * @param count maximum number of asks/bids
     * @return order book
     * @throws KrakenApiException
     */
    public OrderBookResult getOrderBook(String pair, Integer count) throws KrakenApiException {
        HttpApiClient<OrderBookResult> client = (HttpApiClient<OrderBookResult>) this.clientFactory.getHttpApiClient(KrakenApiMethod.ORDER_BOOK);

        Map<String, String> params = new HashMap<>();
        params.put("pair", pair);
        params.put("count", String.valueOf(count));

        return client.callPublic(BASE_URL, KrakenApiMethod.ORDER_BOOK, OrderBookResult.class, params);
    }

    /**
     * Get order book
     *
     * @param pair asset pair
     * @return order book
     * @throws KrakenApiException
     */
    public OrderBookResult getOrderBook(String pair) throws KrakenApiException {
        HttpApiClient<OrderBookResult> client = (HttpApiClient<OrderBookResult>) this.clientFactory.getHttpApiClient(KrakenApiMethod.ORDER_BOOK);

        Map<String, String> params = new HashMap<>();
        params.put("pair", pair);

        return client.callPublic(BASE_URL, KrakenApiMethod.ORDER_BOOK, OrderBookResult.class, params);
    }

    /**
     * Get recent trades
     *
     * @param pair asset pair
     * @return recent trades
     * @throws KrakenApiException
     */
    public RecentTradeResult getRecentTrades(String pair) throws KrakenApiException {
        HttpApiClient<RecentTradeResult> client = (HttpApiClient<RecentTradeResult>) this.clientFactory.getHttpApiClient(KrakenApiMethod.RECENT_TRADES);

        Map<String, String> params = new HashMap<>();
        params.put("pair", pair);

        return client.callPublicWithLastId(BASE_URL, KrakenApiMethod.RECENT_TRADES, RecentTradeResult.class, params);
    }

    /**
     * Get recent trades
     *
     * @param pair  asset pair
     * @param since return trade data since given id
     * @return recent trades
     * @throws KrakenApiException
     */
    public RecentTradeResult getRecentTrades(String pair, Integer since) throws KrakenApiException {
        HttpApiClient<RecentTradeResult> client = (HttpApiClient<RecentTradeResult>) this.clientFactory.getHttpApiClient(KrakenApiMethod.RECENT_TRADES);

        Map<String, String> params = new HashMap<>();
        params.put("pair", pair);
        params.put("since", String.valueOf(since));

        return client.callPublicWithLastId(BASE_URL, KrakenApiMethod.RECENT_TRADES, RecentTradeResult.class, params);
    }

    /**
     * Get recent spreads
     *
     * @param pair asset pair
     * @return recent spreads
     * @throws KrakenApiException
     */
    public RecentSpreadResult getRecentSpreads(String pair) throws KrakenApiException {
        HttpApiClient<RecentSpreadResult> client = (HttpApiClient<RecentSpreadResult>) this.clientFactory.getHttpApiClient(KrakenApiMethod.RECENT_SPREADS);

        Map<String, String> params = new HashMap<>();
        params.put("pair", pair);

        return client.callPublicWithLastId(BASE_URL, KrakenApiMethod.RECENT_SPREADS, RecentSpreadResult.class, params);
    }

    /**
     * Get recent spreads
     *
     * @param pair  asset pair
     * @param since return spreads since given id
     * @return recent spreads
     * @throws KrakenApiException
     */
    public RecentSpreadResult getRecentSpreads(String pair, Integer since) throws KrakenApiException {
        HttpApiClient<RecentSpreadResult> client = (HttpApiClient<RecentSpreadResult>) this.clientFactory.getHttpApiClient(KrakenApiMethod.RECENT_SPREADS);

        Map<String, String> params = new HashMap<>();
        params.put("pair", pair);
        params.put("since", String.valueOf(since));

        return client.callPublicWithLastId(BASE_URL, KrakenApiMethod.RECENT_SPREADS, RecentSpreadResult.class, params);
    }

    /**
     * Get account balance
     *
     * @return map of pair/balance
     * @throws KrakenApiException
     */
    public AccountBalanceResult getAccountBalance() throws KrakenApiException {
        HttpApiClient<AccountBalanceResult> client = (HttpApiClient<AccountBalanceResult>) this.clientFactory.getHttpApiClient(apiKey, apiSecret, KrakenApiMethod.ACCOUNT_BALANCE);
        return client.callPrivate(BASE_URL, KrakenApiMethod.ACCOUNT_BALANCE, AccountBalanceResult.class);
    }

    /**
     * Get tradable balance
     *
     * @return trade balance
     * @throws KrakenApiException
     */
    public TradeBalanceResult getTradeBalance() throws KrakenApiException {
        HttpApiClient<TradeBalanceResult> client = (HttpApiClient<TradeBalanceResult>) this.clientFactory.getHttpApiClient(apiKey, apiSecret, KrakenApiMethod.TRADE_BALANCE);
        return client.callPrivate(BASE_URL, KrakenApiMethod.TRADE_BALANCE, TradeBalanceResult.class);
    }

    /**
     * Get open orders
     *
     * @return open orders
     * @throws KrakenApiException
     */
    public OpenOrdersResult getOpenOrdersResult() throws KrakenApiException {
        HttpApiClient<OpenOrdersResult> client = (HttpApiClient<OpenOrdersResult>) this.clientFactory.getHttpApiClient(apiKey, apiSecret, KrakenApiMethod.OPEN_ORDERS);
        return client.callPrivate(BASE_URL, KrakenApiMethod.OPEN_ORDERS, OpenOrdersResult.class);
    }

    /**
     * Get closed orders
     *
     * @return closed orders
     * @throws KrakenApiException
     */
    public ClosedOrdersResult getClosedOrdersResult() throws KrakenApiException {
        return new HttpApiClient<ClosedOrdersResult>(this.apiKey, this.apiSecret)
                .callPrivate(BASE_URL, KrakenApiMethod.CLOSED_ORDERS, ClosedOrdersResult.class);
    }

    /**
     * Get orders information
     *
     * @return orders information
     * @throws KrakenApiException
     */
    public OrdersInformationResult getOrdersInformationResult(List<String> transactions) throws KrakenApiException {
        Map<String, String> params = new HashMap<>();
        params.put("txid", transactions.stream().collect(Collectors.joining(",")));

        return new HttpApiClient<OrdersInformationResult>(this.apiKey, this.apiSecret)
                .callPrivate(BASE_URL, KrakenApiMethod.ORDERS_INFORMATION, OrdersInformationResult.class, params);
    }

    public static void main(String[] args) throws KrakenApiException {
        KrakenAPIClient client = new KrakenAPIClient(
                "",
                "");

        //OHLCResult resultOHLC = client.getOHLC("XXBTZEUR", Interval.ONE_DAY);
        //System.out.println("resultOHLC:"+resultOHLC);

        //OrderBookResult orderBookResult = client.getOrderBook("BTCEUR");
        //System.out.println(orderBookResult);

        //RecentTradeResult result = client.getRecentTrades("BTCEUR");
        //System.out.println(result.getResult());
        //System.out.println("last id: "+result.getLastId());

        //RecentSpreadResult recentSpreadResult = client.getRecentSpreads("BTCEUR");
        //System.out.println(recentSpreadResult.getResult());
        //System.out.println("last id: " + recentSpreadResult.getLastId());

        //AccountBalanceResult accountBalanceResult = client.getAccountBalance();
        //accountBalanceResult.getResult().forEach((currency, balance) -> System.out.println(currency + " = " + balance));
        //System.out.println(accountBalanceResult.getResult());

        TradeBalanceResult tradeBalanceResult = client.getTradeBalance();
        System.out.println(tradeBalanceResult.getResult());

        OpenOrdersResult openOrders = client.getOpenOrdersResult();
        System.out.println(openOrders.getResult());

        ClosedOrdersResult closedOrders = client.getClosedOrdersResult();
        System.out.println(closedOrders.getResult());

        OrdersInformationResult ordersInformationResult = client.getOrdersInformationResult(Arrays.asList("OGRQC4-Q5C5N-2EYZDP"));
        System.out.println(ordersInformationResult.getResult());
        ordersInformationResult.getResult().forEach((txid, order) -> System.out.println(txid + " = " + order.description.type));
    }
}
