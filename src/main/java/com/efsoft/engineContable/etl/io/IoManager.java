package com.efsoft.engineContable.etl.io;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.springframework.core.io.Resource;

/*
 * Inferface para definir las clases io de los archivos
 */

public interface IoManager {

	public boolean existsFile(String fileName);

	public List<String> readFile(String fileName) throws FileNotFoundException, IOException;

	public void writeFile(String fileName, byte[] content) throws IOException;

	public void appendToFile(String fileName, byte[] content) throws IOException;

	public boolean deleteFile(String fileName);

	public Resource getFileResource(String fileName) throws Exception;

}
