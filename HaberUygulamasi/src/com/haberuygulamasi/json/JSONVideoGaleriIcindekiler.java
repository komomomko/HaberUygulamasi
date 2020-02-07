package com.haberuygulamasi.json;

public class JSONVideoGaleriIcindekiler {
	public int num;
	public Row[] row;

	public JSONVideoGaleriIcindekiler() {

	}

	public class Row {
		public int id;
		public String title;
		public String image;
	}
}
