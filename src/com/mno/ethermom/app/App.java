package com.mno.ethermom.app;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.mno.ethermom.domain.CurrentStatsJsonResponse;
import com.mno.ethermom.domain.Worker;
import com.mno.ethermom.domain.WorkersJsonResponse;
import com.mno.ethermom.utils.ConfigUtil;
import com.mno.ethermom.utils.ConversionUtil;
import com.mno.ethermom.utils.HttpUtil;
import com.mno.ethermom.utils.messaging.MessagingUtil;

/**
 * Ethermine.org monitoring job.
 * Sends out alert to IFTTT or Telegram channel when hashrate dropped. 
 * 
 * @author Min Naing Oo
 * @version 1.0
 *
 */
public class App {
	
	private static Logger logger = Logger.getLogger(App.class);

	public static void main(String[] args) {
		logger.info("Job started.");

		try {
			ConfigUtil.loadConfigs();

			if (!ConfigUtil.isEnabled()) {
				System.exit(0);
			}

			String walletAddr = ConfigUtil.getWalletAddress();
			if (walletAddr == null || walletAddr.isEmpty()) {
				throw new IllegalArgumentException("Wallet not set.");
			}

			if (ConfigUtil.getMode() == ConfigUtil.CONFIG_MODE_TOTAL) {

				double expectedHash = ConfigUtil.getExpectedHash(null);
				if (expectedHash <= 0) {
					throw new IllegalArgumentException("Invalid expected hashrate. Please set 'expectedHash' property");
				}

				CurrentStatsJsonResponse currentStatsRes = (CurrentStatsJsonResponse) HttpUtil.getJsonFromUrl(
						HttpUtil.getApiUrl(walletAddr, HttpUtil.API_FUNCTION_CURRENTSTATS),
						CurrentStatsJsonResponse.class);

				if (currentStatsRes == null || !HttpUtil.STATUS_OK.equals(currentStatsRes.getStatus())) {
					throw new Exception("API is not working.");
				}

				if (currentStatsRes.getData().getReportedHashrate() < expectedHash) {
					MessagingUtil.sendMessage("Reported hashrate is lower than expected @"
							+ ConversionUtil.convertToMHs(currentStatsRes.getData().getReportedHashrate()) + "MH/s.");
				}

			} else if (ConfigUtil.getMode() == ConfigUtil.CONFIG_MODE_INDIVIDUAL) {

				WorkersJsonResponse workersRes = (WorkersJsonResponse) HttpUtil.getJsonFromUrl(
						HttpUtil.getApiUrl(walletAddr, HttpUtil.API_FUNCTION_WORKERS), WorkersJsonResponse.class);

				if (workersRes == null || !HttpUtil.STATUS_OK.equals(workersRes.getStatus())) {
					throw new Exception("API is not working.");
				}

				List<Worker> workers = workersRes.getData();
				if (workers == null || workers.isEmpty()) {
					throw new IllegalArgumentException("No active workers");
				}

				Map<String, Double> problemWorkers = new LinkedHashMap<>();
				for (Worker worker : workers) {
					double expectedHash = ConfigUtil.getExpectedHash(worker.getWorker());
					if (expectedHash == 0) {
						throw new IllegalArgumentException(
								"Invalid expected hashrate. Please set workers 'expectedHash' property");
					}

					if (worker.getReportedHashrate() < expectedHash) {
						problemWorkers.put(worker.getWorker(),
								ConversionUtil.convertToMHs(worker.getReportedHashrate()));
					}
				}

				if (!problemWorkers.isEmpty()) {
					StringBuilder message = new StringBuilder();
					message.append("Reported hashrate is lower than expected for following worker(s).");
					for (String key : problemWorkers.keySet()) {
						message.append("\n" + key + " @" + problemWorkers.get(key) + "MH/s");
					}

					MessagingUtil.sendMessage(message.toString());
				}

			} else if (ConfigUtil.getMode() == ConfigUtil.CONFIG_MODE_MIX) {

				double totalExpectedHash = ConfigUtil.getExpectedHash(null);
				if (totalExpectedHash <= 0) {
					throw new IllegalArgumentException("Invalid expected hashrate. Please set 'expectedHash' property");
				}

				CurrentStatsJsonResponse currentStatsRes = (CurrentStatsJsonResponse) HttpUtil.getJsonFromUrl(
						HttpUtil.getApiUrl(walletAddr, HttpUtil.API_FUNCTION_CURRENTSTATS),
						CurrentStatsJsonResponse.class);

				if (currentStatsRes == null || !HttpUtil.STATUS_OK.equals(currentStatsRes.getStatus())) {
					throw new Exception("API is not working.");
				}

				if (currentStatsRes.getData().getReportedHashrate() < totalExpectedHash) {

					WorkersJsonResponse workersRes = (WorkersJsonResponse) HttpUtil.getJsonFromUrl(
							HttpUtil.getApiUrl(walletAddr, HttpUtil.API_FUNCTION_WORKERS), WorkersJsonResponse.class);

					if (workersRes == null || !HttpUtil.STATUS_OK.equals(workersRes.getStatus())) {
						throw new Exception("API is not working.");
					}

					List<Worker> workers = workersRes.getData();
					if (workers == null || workers.isEmpty()) {
						throw new IllegalArgumentException("No active workers");
					}

					Map<String, Double> problemWorkers = new LinkedHashMap<>();
					for (Worker worker : workers) {
						double expectedHash = ConfigUtil.getExpectedHash(worker.getWorker());
						if (expectedHash <= 0) {
							throw new IllegalArgumentException(
									"Invalid expected hashrate. Please set workers 'expectedHash' property");
						}

						if (worker.getReportedHashrate() < expectedHash) {
							problemWorkers.put(worker.getWorker(),
									ConversionUtil.convertToMHs(worker.getReportedHashrate()));
						}
					}

					if (!problemWorkers.isEmpty()) {
						StringBuilder message = new StringBuilder();
						message.append("Reported hashrate is lower than expected for following worker(s).");
						for (String key : problemWorkers.keySet()) {
							message.append("\n" + key + " @" + problemWorkers.get(key) + "MH/s");
						}

						MessagingUtil.sendMessage(message.toString());
					}
				}

			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			logger.info("Job ended.");
			System.exit(0);
		}

	}

}
