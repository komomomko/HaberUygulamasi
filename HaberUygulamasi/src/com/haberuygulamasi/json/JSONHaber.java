package com.haberuygulamasi.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class JSONHaber {
	public int num;
	public JSONHaberRow[] row;

	public JSONHaber() {

	}

	public class JSONHaberRow {
		public String id;
		public String title;
		public String publish_date;
		public String category_name;
		public String image;
		public String url;
		public String mobil_detail_url;
	}

	public static String jsonCoord(String address) throws IOException {
		URL url = new URL(address);
		URLConnection connection = url.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(
				connection.getInputStream()));
		String inputLine;
		String jsonResult = "";
		while ((inputLine = in.readLine()) != null) {
			jsonResult += inputLine;
		}
		in.close();
		return jsonResult;
	}
}
