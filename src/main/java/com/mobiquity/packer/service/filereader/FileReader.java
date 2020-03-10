package com.mobiquity.packer.service.filereader;

import java.util.List;

import com.mobiquity.exception.APIException;

public interface FileReader {
	public List<String> readFileLines(String filePath) throws APIException;
}
