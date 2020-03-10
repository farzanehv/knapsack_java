package com.mobiquity.packer.service.pack;

import com.mobiquity.exception.APIException;

public interface PackService {
	public String pack(String filePath) throws APIException;
}
