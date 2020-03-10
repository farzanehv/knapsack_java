package com.mobiquity.packer.service.filereader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.mobiquity.exception.APIException;

public class FileReaderImpl implements FileReader {

	// use try-with-resource to close, java.nio
	// Read a file lines
	public List<String> readFileLines(String filePath) throws APIException {

		validateFilePath(filePath);
		try (Stream<String> lines = Files.lines(Paths.get(filePath))) {
			return lines.collect(Collectors.toList());
		} catch (IOException e) {
			throw new APIException(getPropertyMessages(FILE_NOT_FOUND_ERROR), e);
		}
	}

	public static final String MESSAGES_PROPERTY_FILE = "messages.properties";
	public static final String CONFIG_FILE = "config.properties";
	public static final String FILE_PATH_ERROR = "file.path.error";
	public static final String FILE_NOT_FOUND_ERROR = "file.not.found.error";

	//filePath should not be null
	private void validateFilePath(String filePath) throws APIException {
		if (filePath == null || filePath.isEmpty())
			throw new APIException(getPropertyMessages(FILE_PATH_ERROR));
	}

	// get messages from messages.properties by its related key
	public static String getPropertyMessages(String key) throws APIException {
		return loadPropertiesFile(MESSAGES_PROPERTY_FILE).getProperty(key);
	}

	// get config data from config.properties by its related key
	public static String getConfigData(String key) throws APIException {
		return loadPropertiesFile(CONFIG_FILE).getProperty(key);
	}

	public static Properties loadPropertiesFile(String fileName) throws APIException {

		InputStream input;
		try {
			input = FileReaderImpl.class.getClassLoader().getResourceAsStream("implementation/messages/" + fileName);
			Properties prop = new Properties();
			prop.load(input);
			return prop;
		} catch (IOException | NullPointerException e) {
			throw new APIException(e.getMessage());
		}

	}

}
