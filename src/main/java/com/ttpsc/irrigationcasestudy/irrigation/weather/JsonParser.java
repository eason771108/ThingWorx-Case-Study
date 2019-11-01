package com.ttpsc.irrigationcasestudy.irrigation.weather;

import java.io.BufferedReader;
import java.io.IOException;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class JsonParser {
	public static String getAll(String urlStr) throws IOException {
			
			URL url = new URL(urlStr);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			try{
					if (conn.getResponseCode() != 200) {
					throw new IOException(conn.getResponseMessage());
				}
				return getJson(rd);	
			}
			finally{
				rd.close();		
				conn.disconnect();
			}
		}	
		
		private static String getJson(BufferedReader br) throws IOException{
			StringBuilder sb = new StringBuilder();
			String line;
			while((line = br.readLine()) != null){
				sb.append(line);
			}
			return sb.toString();
		}
}
