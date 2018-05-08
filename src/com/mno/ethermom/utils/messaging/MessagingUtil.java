package com.mno.ethermom.utils.messaging;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import com.mno.ethermom.utils.ConfigUtil;

public class MessagingUtil {

	public static void sendMessage(String message) throws ClientProtocolException, IOException {
		
		if (ConfigUtil.isTelegramEnabled()) {
			TelegramUtil.sendMessage(message);
		}
		
		if (ConfigUtil.isIFTTTEnabled()) {
			IFTTTUtil.sendMessage(message);
		}
	}

}
