package com.ttpsc.irrigationcasestudy.irrigation.weather;

public class Weather {
	private double longitude;
	private double latitude;

	private double temperature;
	private double pressure;
	private double humidity;
	private String des;
	private String icon;

	private boolean isRain;

	public Weather() {
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getTemperature() {
		return temperature;
	}

	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}

	public double getPressure() {
		return pressure;
	}

	public void setPressure(double pressure) {
		this.pressure = pressure;
	}

	public double getHumidity() {
		return humidity;
	}

	public void setHumidity(double humidity) {
		this.humidity = humidity;
	}

	public boolean isRain() {
		return isRain;
	}

	public void setRain(boolean isRain) {
		this.isRain = isRain;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	public String getDes() {
		return des;
	}
	
	public void setDes(String des) {
		this.des = des;
	}
}
