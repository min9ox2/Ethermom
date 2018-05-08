package com.mno.ethermom.utils.messaging;

import java.io.IOException;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import com.mno.ethermom.utils.ConfigUtil;

public class TelegramUtil {
	
	private static final String API_URL = "https://api.telegram.org/bot";
	private static final String API_FUNCTION_SENDMSG = "sendMessage";

	public static void sendMessage(String message) throws ClientProtocolException, IOException {
		String apiKey = ConfigUtil.getTelegramApiKey();
		if (apiKey == null || apiKey.isEmpty()) {
			throw new IllegalArgumentException("Telegram API Key is missing.");
		}
		
		String chatId = ConfigUtil.getTelegramChatId();
		if (chatId == null || chatId.isEmpty()) {
			throw new IllegalArgumentException("Telegram channel chat Id is missing.");
		}
		
		HttpClient httpClient = HttpClientBuilder.create().build();
		StringBuilder restUrl = new StringBuilder();
		restUrl.append(API_URL);
		restUrl.append(apiKey);
		restUrl.append("/" + API_FUNCTION_SENDMSG);
		restUrl.append("?chat_id=" + chatId);
		restUrl.append("&text=" + URLEncoder.encode(message, "UTF-8"));
		HttpGet getRequest = new HttpGet(restUrl.toString());
		HttpResponse response = httpClient.execute(getRequest);
		
		if (response.getStatusLine().getStatusCode() != 200) {
			System.out.println("Failed to send message");
		}
	}

}
