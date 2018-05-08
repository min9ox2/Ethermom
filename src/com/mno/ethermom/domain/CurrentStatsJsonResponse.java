
package com.mno.ethermom.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CurrentStatsJsonResponse {

	@SerializedName("status")
	@Expose
	private String status;
	@SerializedName("data")
	@Expose
	private CurrentStats data;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public CurrentStats getData() {
		return data;
	}

	public void setData(CurrentStats data) {
		this.data = data;
	}

}
