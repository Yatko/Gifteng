package com.venefica.module.alert;

/**
 * @author avinash
 * Class to hold alert data
 */
public class AlertData {
	private long alertId;
	private String alertTitle;
	private String alertTime;
	private String alertDesc;
	/**
	 * Alert levels Low(0) to High(5)
	 */
	private int alertLevel = 1;
	private boolean isUnread = true;
	/**
	 * @return the alertId
	 */
	public long getAlertId() {
		return alertId;
	}
	/**
	 * @param alertId the alertId to set
	 */
	public void setAlertId(long alertId) {
		this.alertId = alertId;
	}
	/**
	 * @return the alertTitle
	 */
	public String getAlertTitle() {
		return alertTitle;
	}
	/**
	 * @param alertTitle the alertTitle to set
	 */
	public void setAlertTitle(String alertTitle) {
		this.alertTitle = alertTitle;
	}
	/**
	 * @return the alertTime
	 */
	public String getAlertTime() {
		return alertTime;
	}
	/**
	 * @param alertTime the alertTime to set
	 */
	public void setAlertTime(String alertTime) {
		this.alertTime = alertTime;
	}
	/**
	 * @return the alertDesc
	 */
	public String getAlertDesc() {
		return alertDesc;
	}
	/**
	 * @param alertDesc the alertDesc to set
	 */
	public void setAlertDesc(String alertDesc) {
		this.alertDesc = alertDesc;
	}
	/**
	 * @return the alertLevel
	 */
	public int getAlertLevel() {
		return alertLevel;
	}
	/**
	 * @param alertLevel the alertLevel to set
	 */
	public void setAlertLevel(int alertLevel) {
		this.alertLevel = alertLevel;
	}
	/**
	 * @return the isUnread
	 */
	public boolean isUnread() {
		return isUnread;
	}
	/**
	 * @param isUnread the isUnread to set
	 */
	public void setUnread(boolean isUnread) {
		this.isUnread = isUnread;
	}
}
