package com.mobiquity.packer.service.outputgenerator;

import java.util.List;

import com.mobiquity.exception.APIException;

public interface OutputGenerator {
	
	public String generateOutput(List<Integer> resultArray) throws APIException;
}
