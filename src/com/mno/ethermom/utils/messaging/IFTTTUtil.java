package com.mno.ethermom.utils.messaging;

import java.io.IOException;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import com.mno.ethermom.utils.ConfigUtil;

public class IFTTTUtil {

	private static final String API_URL = "https://maker.ifttt.com/trigger/%s/with/key";

	public static void sendMessage(String message) throws ClientProtocolException, IOException {
		String apiKey = ConfigUtil.getIFTTTApiKey();
		if (apiKey == null || apiKey.isEmpty()) {
			throw new IllegalArgumentException("IFTTT API Key is missing.");
		}

		String eventName = ConfigUtil.getIFTTTEventName();
		if (eventName == null || eventName.isEmpty()) {
			throw new IllegalArgumentException("IFTTT event name is missing.");
		}
		
		message = message.replaceAll("\n", "<br>");

		HttpClient httpClient = HttpClientBuilder.create().build();
		StringBuilder restUrl = new StringBuilder();
		restUrl.append(String.format(API_URL, eventName));
		restUrl.append(API_URL.endsWith("/") ? "" : "/");
		restUrl.append(apiKey);
		restUrl.append("?value1=" + URLEncoder.encode(message, "UTF-8"));
		HttpGet getRequest = new HttpGet(restUrl.toString());
		HttpResponse response = httpClient.execute(getRequest);

		if (response.getStatusLine().getStatusCode() != 200) {
			System.out.println("Failed to send message");
		}
	}

}
