package com.mobiquity.packer.service.lineparser;

import java.util.List;

import com.mobiquity.exception.APIException;

public interface LineParser {
	public List<List<String>> getPacksDataFromLines(List<String> readFileLines) throws APIException;
}
