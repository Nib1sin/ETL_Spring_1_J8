package com.efsoft.engineContable.etl.pojo;

import java.util.Map;

public class GenericInputData {

	private String rawData;
	private String fileName;
	private Integer lineNumber;
	private Map<String, Object> parsedData;

	public GenericInputData(String fileName, String rawData, Integer lineNumber, Map<String, Object> parsedData) {
		this.rawData = rawData;
		this.fileName = fileName;
		this.lineNumber = lineNumber;
		this.parsedData = parsedData;
	}

	public String getRawData() {
		return rawData;
	}

	public String getFileName() {
		return fileName;
	}

	public Integer getLineNumber() {
		return lineNumber;
	}

	public Map<String, Object> getParsedData() {
		return parsedData;
	}

}
