
package com.mno.ethermom.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Worker {

	@SerializedName("worker")
	@Expose
	private String worker;
	@SerializedName("time")
	@Expose
	private Integer time;
	@SerializedName("lastSeen")
	@Expose
	private Integer lastSeen;
	@SerializedName("reportedHashrate")
	@Expose
	private Integer reportedHashrate;
	@SerializedName("currentHashrate")
	@Expose
	private Double currentHashrate;
	@SerializedName("validShares")
	@Expose
	private Integer validShares;
	@SerializedName("invalidShares")
	@Expose
	private Integer invalidShares;
	@SerializedName("staleShares")
	@Expose
	private Integer staleShares;
	@SerializedName("averageHashrate")
	@Expose
	private Double averageHashrate;

	public String getWorker() {
		return worker;
	}

	public void setWorker(String worker) {
		this.worker = worker;
	}

	public Integer getTime() {
		return time;
	}

	public void setTime(Integer time) {
		this.time = time;
	}

	public Integer getLastSeen() {
		return lastSeen;
	}

	public void setLastSeen(Integer lastSeen) {
		this.lastSeen = lastSeen;
	}

	public Integer getReportedHashrate() {
		return reportedHashrate;
	}

	public void setReportedHashrate(Integer reportedHashrate) {
		this.reportedHashrate = reportedHashrate;
	}

	public Double getCurrentHashrate() {
		return currentHashrate;
	}

	public void setCurrentHashrate(Double currentHashrate) {
		this.currentHashrate = currentHashrate;
	}

	public Integer getValidShares() {
		return validShares;
	}

	public void setValidShares(Integer validShares) {
		this.validShares = validShares;
	}

	public Integer getInvalidShares() {
		return invalidShares;
	}

	public void setInvalidShares(Integer invalidShares) {
		this.invalidShares = invalidShares;
	}

	public Integer getStaleShares() {
		return staleShares;
	}

	public void setStaleShares(Integer staleShares) {
		this.staleShares = staleShares;
	}

	public Double getAverageHashrate() {
		return averageHashrate;
	}

	public void setAverageHashrate(Double averageHashrate) {
		this.averageHashrate = averageHashrate;
	}

}
