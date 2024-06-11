package com.efsoft.engineContable.etl.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class JavaIoManager implements IoManager{

	private static final Logger appLogger = LoggerFactory.getLogger(JavaIoManager.class);

	@Override
	public boolean existsFile(String fileName) {
		return (new File(fileName)).exists();
	}

	@Override
	public List<String> readFile(String fileName) {
		List<String> result = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
			String line;
		    while ((line = br.readLine()) != null) {
		    	result.add(line);
		    }
		} catch (FileNotFoundException e) {
			appLogger.warn("fileName = {} does not exists",fileName);
		} catch (IOException e) {
			appLogger.error("Error trying to read file {} - {}", fileName,e.getMessage());
		}
		return result;
	}

	@Override
	public void writeFile(String fileName, byte[] content) {
		try {
			deleteFile(fileName);
			if (content != null && content.length > 0){
				appLogger.info("Writing file {}",fileName);
				Files.write(Paths.get(fileName), content, StandardOpenOption.CREATE_NEW);
			}
		} catch (IOException e) {
			appLogger.error("Error trying to write file {} - {}", fileName,e.getMessage());
		}
	}

	@Override
	public void appendToFile(String fileName, byte[] content) {
		if (content != null && content.length > 0){
			try {
				Files.write(Paths.get(fileName), content, existsFile(fileName) ? StandardOpenOption.APPEND : StandardOpenOption.CREATE_NEW);
			} catch (IOException e) {
				appLogger.error("Error trying to append write file {} - {}", fileName,e.getMessage());
			}
		}
	}

	@Override
	public boolean deleteFile(String fileName) {
		try {
			return Files.deleteIfExists(Paths.get(fileName));
		} catch (IOException e) {
			appLogger.error("Error trying to cancel file {} - {}", fileName,e.getMessage());
			return false;
		}
	}

	@Override
	public Resource getFileResource(String fileName) throws Exception {
		if (existsFile(fileName)){
			return new FileSystemResource(fileName);
		} else {
			throw new Exception(String.format("fileName = %s does not exists",fileName));
		}
	}

}
