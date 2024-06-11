package com.efsoft.engineContable.etl.utils;

public class Constants {

	public final static String CSV_SEPARATOR = ",";
	public final static String CSV_SUBCOLOUMN = ";";
	public final static String BREAKLINE = "\r\n";
	public final static String CSV_QUOTE = "\"";
	public final static String SQL126 = "yyyy-MM-dd";
	public final static String PIPE_SEPARATOR = "|";
	public static final int CHUNK_SIZE = 10000;


	public enum Alignment {
		RIGHT,
		LEFT
	}

}
