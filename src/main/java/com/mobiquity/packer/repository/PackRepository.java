package com.mobiquity.packer.repository;

import java.util.List;

import com.mobiquity.exception.APIException;
import com.mobiquity.packer.entity.pack.Pack;

public interface PackRepository {
	public List<Pack> loadPacks(String packFilePath) throws APIException;
}
