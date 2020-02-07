package com.haberuygulamasi.json;

public class JSONFotografGaleriIcindekiler {
	public int num;
	public Row[] row;

	public JSONFotografGaleriIcindekiler() {
	}

	public class Row {
		public String id;
		public String spot;
		public String image;
	}
}
