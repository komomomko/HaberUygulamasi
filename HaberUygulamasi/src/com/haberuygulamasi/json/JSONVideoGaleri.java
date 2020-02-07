package com.haberuygulamasi.json;

public class JSONVideoGaleri {
	public int num;
	public Row[] row;

	public JSONVideoGaleri() {

	}

	public class Row {
		public int id;
		public String title;
		public String image;
		public String orginal_link;
	}
}
