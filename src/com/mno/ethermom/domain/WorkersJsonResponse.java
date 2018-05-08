
package com.mno.ethermom.domain;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WorkersJsonResponse {

	@SerializedName("status")
	@Expose
	private String status;
	@SerializedName("data")
	@Expose
	private List<Worker> data = null;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<Worker> getData() {
		return data;
	}

	public void setData(List<Worker> data) {
		this.data = data;
	}

}
