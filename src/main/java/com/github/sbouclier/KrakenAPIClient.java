package com.github.sbouclier;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sbouclier.result.ServerTimeResult;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;

/**
 * Kraken API client
 *
 * @author Stéphane Bouclier
 */
public class KrakenAPIClient {

    private CloseableHttpClient client;

    public KrakenAPIClient() {
        client = HttpClients.createDefault();
    }

    /**
     * Get server time
     *
     * @return server time
     * @throws IOException
     */
    public ServerTimeResult getServerTime() throws IOException {
        HttpGet httpGet = new HttpGet("https://api.kraken.com/0/public/Time");

        CloseableHttpResponse response = client.execute(httpGet);
        String responseString = new BasicResponseHandler().handleResponse(response);
        ServerTimeResult res = new ObjectMapper().readValue(responseString, ServerTimeResult.class);

        client.close();

        return res;
    }

    public void setClient(CloseableHttpClient client) {
        this.client = client;
    }

    public static void main(String[] args) throws IOException {
        KrakenAPIClient client = new KrakenAPIClient();
        ServerTimeResult result = client.getServerTime();

        System.out.println(result);
    }
}