package com.itbility.batch.reader;

import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

import com.itbility.batch.model.Data;

/**
 * @author vikas
 *
 */
public class FileDataReader extends FlatFileItemReader<Data>{
	
	public FileDataReader() {
		
	    this.setLineMapper(new DefaultLineMapper<Data>() {
            {
                setLineTokenizer(new DelimitedLineTokenizer() {
                    {
                        setNames(new String[] { "\r\n" });
                    }
                });
                setFieldSetMapper(new BeanWrapperFieldSetMapper<Data>() {
                    {
                        setTargetType(Data.class);
                    }
                });
            }
        });
      
	}
	
}
