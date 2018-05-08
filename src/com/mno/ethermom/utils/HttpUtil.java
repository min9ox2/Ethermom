package com.mno.ethermom.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class HttpUtil {

	public static final String STATUS_OK = "OK";

	private static final String API_URL = "https://api.ethermine.org/miner";
	public static final String API_FUNCTION_CURRENTSTATS = "currentStats";
	public static final String API_FUNCTION_WORKERS = "workers";

	public static Object getJsonFromUrl(String restUrl, Class<?> clazz) throws ClientProtocolException, IOException {

		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpGet getRequest = new HttpGet(restUrl);
		HttpResponse response = httpClient.execute(getRequest);
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			InputStream instream = entity.getContent();
			String result = convertStreamToString(instream);
			instream.close();
			if (result != null && response.getStatusLine().getStatusCode() == 200) {
				Gson jsonRes = new GsonBuilder().create();
				Object obj = jsonRes.fromJson(result, clazz);
				return obj;
			}
		}
		return null;
	}

	private static String convertStreamToString(InputStream is) {

		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	public static String getApiUrl(String walletAddr, String apiFunction) {
		return API_URL + (API_URL.endsWith("/") ? "" : "/") + walletAddr + "/" + apiFunction;
	}

}
