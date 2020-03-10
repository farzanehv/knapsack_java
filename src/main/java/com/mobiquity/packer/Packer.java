package com.mobiquity.packer;

import com.mobiquity.exception.APIException;
import com.mobiquity.packer.service.pack.PackService;
import com.mobiquity.packer.service.pack.PackServiceImpl;

public class Packer {

	private Packer() {
	}

	public static String pack(String filePath) throws APIException {
		try {
			PackService pack = new PackServiceImpl();
			return pack.pack(filePath);
		} catch (Exception ex) {
			throw new APIException(ex.getMessage());
		}
	}
}
