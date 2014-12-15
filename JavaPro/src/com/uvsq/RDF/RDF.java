package com.uvsq.RDF;

public class RDF {
	private String FileName;	
	
	public RDF(String fileName) {
		this.setFileName(fileName);
	}
	
	public RDF() {
	}

	public String getFileName() {
		return FileName;
	}

	public void setFileName(String fileName) {
		this.FileName = fileName;
	}
}
