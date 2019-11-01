package com.ttpsc.irrigationcasestudy.irrigation.weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;

public class WeatherClient {
	private static final String URL = "http://api.openweathermap.org/data/2.5/weather?";
	private static final String APPID = "&appid=4ee5fd31e381dfd6d0452eac3b43c942";

	public WeatherClient() {
	}

	public static Weather getWeather(double longitude, double latitude) throws IOException, JSONException {

		Weather weather = new Weather();
		
		StringBuilder sb = new StringBuilder(URL);
		sb.append("&lon=").append(longitude); //經度
		sb.append("&lat=").append(latitude);  //緯度
		sb.append(APPID);
				
		String weatherInfo = JsonParser.getAll(sb.toString());
		JSONObject obj = new JSONObject(weatherInfo);

		JSONObject main = (JSONObject) obj.getJSONObject("main");

		weather.setTemperature(main.getDouble("temp"));
		weather.setPressure(main.getDouble("pressure"));
		weather.setHumidity(main.getDouble("humidity"));
		
		JSONArray weatherStatusArray = (JSONArray) obj.getJSONArray("weather");
		JSONObject lastestWeatherStatus = (JSONObject) weatherStatusArray.get(0);
		weather.setIcon(lastestWeatherStatus.getString("icon"));
		weather.setDes(lastestWeatherStatus.getString("description"));
		//String iconInfo = lastestWeatherStatus.getString("icon");
		weather.setRain(isRain(weather.getIcon()));

		return weather;
	}

	private static boolean isRain(String iconInfo) {
		if (
				!iconInfo.equals("09d") &&!iconInfo.equals("09n") 		//shower rain
				&& !iconInfo.equals("10d") && !iconInfo.equals("10n")  	//rain
				&&!iconInfo.equals("11d") &&!iconInfo.equals("11n") 	//thunderstorm
				&& !iconInfo.equals("13d")&& !iconInfo.equals("13n") 	//snow
				&& !iconInfo.equals("50d")&& !iconInfo.equals("50n") 	//mist
				) {
			return false;
		} else
			return true;
	}
	
	public boolean getWeatherDet(double longitude, double latitude) throws IOException, JSONException {

		Weather weather = new Weather();
		
		StringBuilder sb = new StringBuilder(URL);
		sb.append("&lon=").append(longitude); 
		sb.append("&lat=").append(latitude); 
		sb.append(APPID);
				
		String weatherInfo = JsonParser.getAll(sb.toString());
		JSONObject obj = new JSONObject(weatherInfo);

		JSONObject main = (JSONObject) obj.getJSONObject("main");

		weather.setTemperature(main.getDouble("temp"));
		weather.setPressure(main.getDouble("pressure"));
		weather.setHumidity(main.getDouble("humidity"));
		
		JSONArray weatherStatusArray = (JSONArray) obj.getJSONArray("weather");
		JSONObject lastestWeatherStatus = (JSONObject) weatherStatusArray.get(0);
		weather.setIcon(lastestWeatherStatus.getString("icon"));
		weather.setDes(lastestWeatherStatus.getString("description"));

		weather.setRain(isRain(weather.getIcon()));

		return isRain(weather.getIcon());
	}

	public String getWeatherStatus(double longitude, double latitude) throws IOException, JSONException {
		try {
		StringBuilder sb = new StringBuilder(URL);
		sb.append("&lon=").append(longitude); 
		sb.append("&lat=").append(latitude); 
		sb.append(APPID);
				
		String weatherInfo = JsonParser.getAll(sb.toString());
		JSONObject obj = new JSONObject(weatherInfo);

		JSONArray weatherStatusArray = (JSONArray) obj.getJSONArray("weather");
		JSONObject lastestWeatherStatus = (JSONObject) weatherStatusArray.get(0);
		
		return lastestWeatherStatus.getString("description");
		}
		catch (java.io.IOException e) {
			return "Service unavaliable";
		}
		catch (org.json.JSONException e) {
			return "Unknow";
		}
	}
}
