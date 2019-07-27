package com.itbility.batch.writer;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.itbility.batch.model.Data;
import com.itbility.batch.model.FileDataStore;

/**
 * @author vikas
 *
 */
public class FileDataWriter implements ItemWriter<Data> {
	
	@Autowired
	private FileDataStore dataStore;

	@Override
	public void write(List<? extends Data> lines) throws Exception {
		
		lines.forEach(item -> dataStore.getFileLineList().add(item));
      
    }
}
