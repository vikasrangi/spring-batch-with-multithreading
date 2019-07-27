package com.itbility.batch.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.itbility.batch.model.Data;

/**
 * @author vikas
 *
 */
public class FileDataEncryptionProcessor implements ItemProcessor<Data, Data> {

	private static final Logger log = LoggerFactory.getLogger(FileDataEncryptionProcessor.class);

	@Override
	public Data process(final Data item) throws Exception {
		// TODO Auto-generated method stub
		Data encryptedData = new Data();

		encryptedData.setLine(encrypt(item.getLine(), 10));
		return encryptedData;
	}

	public static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";

	public static String encrypt(String plainText, int shiftKey) {
		plainText = plainText.toLowerCase();
		String cipherText = "";
		for (int i = 0; i < plainText.length(); i++) {
			int charPosition = ALPHABET.indexOf(plainText.charAt(i));
			int keyVal = (shiftKey + charPosition) % 26;
			char replaceVal = ALPHABET.charAt(keyVal);
			cipherText += replaceVal;
		}
		return cipherText;
	}

}
