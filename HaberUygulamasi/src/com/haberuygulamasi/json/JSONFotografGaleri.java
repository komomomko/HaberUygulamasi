package com.haberuygulamasi.json;

public class JSONFotografGaleri {
	public int num;
	public Row[] row;

	public JSONFotografGaleri() {
	}

	public class Row {
		public String id;
		public String title;
		public String image;
	}
}
