package com.project.sample.services.info;

public class ServiceInfoMessage {
	private String hostName;
	private String timeStamp;
	private String buildNumber;
	private String baseServiceURL;
	private String baseDataBaseURL;

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getBuildNumber() {
		return buildNumber;
	}

	public void setBuildNumber(String buildNumber) {
		this.buildNumber = buildNumber;
	}

	public String getBaseServiceURL() {
		return baseServiceURL;
	}

	public void setBaseServiceURL(String baseServiceURL) {
		this.baseServiceURL = baseServiceURL;
	}

	public String getBaseDataBaseURL() {
		return baseDataBaseURL;
	}

	public void setBaseDataBaseURL(String baseDataBaseURL) {
		this.baseDataBaseURL = baseDataBaseURL;
	}

}
