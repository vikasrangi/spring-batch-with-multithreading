package com.itbility.batch.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author vikas
 *
 */
public class FileDataStore {
	
	private List<Data> fileLineList = new ArrayList<Data>();

	public List<Data> getFileLineList() {
		return fileLineList;
	}

	public void setFileLineList(List<Data> fileLineList) {
		this.fileLineList = fileLineList;
	}

}
