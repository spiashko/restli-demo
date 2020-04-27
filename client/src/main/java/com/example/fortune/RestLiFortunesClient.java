package com.example.fortune;

import com.linkedin.common.callback.FutureCallback;
import com.linkedin.common.util.None;
import com.linkedin.r2.transport.common.Client;
import com.linkedin.r2.transport.common.bridge.client.TransportClientAdapter;
import com.linkedin.r2.transport.http.client.HttpClientFactory;
import com.linkedin.restli.client.Request;
import com.linkedin.restli.client.Response;
import com.linkedin.restli.client.ResponseFuture;
import com.linkedin.restli.client.RestClient;

import java.util.Collections;

public class RestLiFortunesClient {
    private static final FortunesRequestBuilders fortuneBuilders = new FortunesRequestBuilders();

    /**
     * This stand-alone app demos the client-side Rest.li API.
     * To see the demo, run the server, then start the client
     */
    public static void main(String[] args) throws Exception {

        // Create an HttpClient and wrap it in an abstraction layer
        final HttpClientFactory http = new HttpClientFactory();
        final Client r2Client = new TransportClientAdapter(
                http.getClient(Collections.<String, String>emptyMap()));

        // Create a RestClient to talk to localhost:8080
        RestClient restClient = new RestClient(r2Client, "http://localhost:8080/");

        // Generate a random ID for a fortune cookie, in the range 0 - 5
        long fortuneId = (long) (Math.random() * 5);

        // Construct a request for the specified fortune
        FortunesGetRequestBuilder getBuilder = fortuneBuilders.get();
        Request<Fortune> getRequest = getBuilder.id(fortuneId).build();

        // Send the request and wait for a response
        final ResponseFuture<Fortune> getFuture = restClient.sendRequest(getRequest);
        final Response<Fortune> response = getFuture.getResponse();

        // Print the response
        System.out.println(response.getEntity().getFortune());

        // Shutdown
        restClient.shutdown(new FutureCallback<None>());
        http.shutdown(new FutureCallback<None>());
    }
}