package com.mobiquity.packer.repository;

import java.util.List;
import java.util.stream.Collectors;

import com.mobiquity.exception.APIException;
import com.mobiquity.packer.entity.pack.Pack;
import com.mobiquity.packer.entity.pack.PackData;
import com.mobiquity.packer.service.filereader.FileReader;
import com.mobiquity.packer.service.filereader.FileReaderImpl;
import com.mobiquity.packer.service.lineparser.LineParser;
import com.mobiquity.packer.service.lineparser.PackLineParserImpl;

public class PackRepositoryImpl implements PackRepository {

	// load all packs from a file
	public List<Pack> loadPacks(String packFilePath) throws APIException {

		List<String> packDataList = loadPackDataFromFile(packFilePath);

		List<List<String>> tokenizedPackData = getTokenizedPackData(packDataList);

		try {
			return loadPacksFromTokenizedPackData(tokenizedPackData);
		} catch (RuntimeException e) {
			throw new APIException(e.getLocalizedMessage(), e);
		}
	}

	// Use FileReader to load file lines
	private List<String> loadPackDataFromFile(String packFilePath) throws APIException {

		FileReader packFileReader = new FileReaderImpl();
		return packFileReader.readFileLines(packFilePath);
	}

	// Tokenize lines with PackLineParser
	private List<List<String>> getTokenizedPackData(List<String> packDataList) throws APIException {

		LineParser packLineParser = new PackLineParserImpl();
		return packLineParser.getPacksDataFromLines(packDataList);

	}

	// Load pack according to the tokenized line
	private List<Pack> loadPacksFromTokenizedPackData(List<List<String>> tokenizedPackData) {

		return (List<Pack>) tokenizedPackData.stream().map(packData -> {
			try {
				return createPackFromTokenizedLine(packData);
			} catch (APIException e) {
				throw new RuntimeException(e);
			}
		}).collect(Collectors.toList());
	}

	public static final String NUM_OF_EXTRA_DATA_IN_LINE = "num.of.extra.data.in.line";
	public static final String NUM_OF_DATA_FOR_EACH_PACK = "num.of.data.for.each.pack";
	public static final String NUMBER_FORMAT_IN_CONFIG_FILE = "number.format.in.config.file";

	private int getDataFromConfigFile(String key) throws APIException {
		try {
			return Integer.parseInt(FileReaderImpl.getConfigData(key));
		} catch (NumberFormatException e) {
			throw new APIException(FileReaderImpl.getPropertyMessages(NUMBER_FORMAT_IN_CONFIG_FILE), e);
		} catch (APIException e) {
			throw e;
		}
	}

	// The count of things in each pack, here it is 1 that is limitedWeight
	private int getNumOfExtraDataInLine() throws APIException {
		return getDataFromConfigFile(NUM_OF_EXTRA_DATA_IN_LINE);
	}

	// The count of data related to each Pack, here is 3, (1,12.3,30)
	private int getNumOfDataForEachPack() throws APIException {
		return getDataFromConfigFile(NUM_OF_DATA_FOR_EACH_PACK);
	}

	// How many things are there in each line for the corresponding pack
	private int getNumOfThings(int sizeOfTokensInLine) throws APIException {

		return (sizeOfTokensInLine - getNumOfExtraDataInLine()) / getNumOfDataForEachPack();
	}

	private Pack createPackFromTokenizedLine(List<String> tokenizedLine) throws APIException {

		return new Pack(getLimitedWeightFromTokenizedLine(tokenizedLine, 0),
				createPackDataArrayFromTokenizedLine(tokenizedLine));
	}

	private PackData createpackDataFromTokeniezeLine(List<String> tokenizedLine, int i) throws APIException {
		return new PackData(getIndexFromTokenizedLine(tokenizedLine, i),
				getWeightFromTokenizedLine(tokenizedLine, i + 1), getCostFromTokenizedLine(tokenizedLine, i + 2));
	}

	// Create an array of things related to each pack
	private PackData[] createPackDataArrayFromTokenizedLine(List<String> tokenizedLine) throws APIException {

		int numOfThings = getNumOfThings(tokenizedLine.size());

		int index = 0;

		PackData[] packDataList = new PackData[numOfThings];
		int numOfDataForEachPack = getNumOfDataForEachPack();

		for (int i = 1; i < tokenizedLine.size(); i = i + numOfDataForEachPack)
			packDataList[index++] = createpackDataFromTokeniezeLine(tokenizedLine, i);

		return packDataList;
	}

	private int getLimitedWeightFromTokenizedLine(List<String> tokenizedLine, int index) throws APIException {
		int limitedWeight = Integer.parseInt(tokenizedLine.get(index));
		validateLimitedWeight(limitedWeight);
		return limitedWeight;
	}

	public static final String MAX_OF_WEIGHT_OF_PACKAGE = "max.of.weight.of.package";
	public static final String MAX_WEIGHT_OF_PACKAGE_IS_GREATER_THAN_ALLOWD = "max.weight.of.package.is.greater.than.allowed";

	private void validateLimitedWeight(int limitedWeight) throws APIException {
		if (limitedWeight > getDataFromConfigFile(MAX_OF_WEIGHT_OF_PACKAGE))
			throw new APIException(FileReaderImpl.getPropertyMessages(MAX_WEIGHT_OF_PACKAGE_IS_GREATER_THAN_ALLOWD));
	}

	private int getIndexFromTokenizedLine(List<String> tokenizedLine, int index) throws APIException {
		int packIndex = Integer.parseInt(tokenizedLine.get(index));
		validatePackIndex(packIndex);
		return packIndex;
	}

	public static final String MAX_NUM_OF_THINGS_TO_CHOOSE_FROM = "max.num.of.things.to.choose.from";
	public static final String NUM_OF_THINGS_IS_GREATER_THAN_ALLOWD = "num.of.things.is.greater.than.allowed";

	private void validatePackIndex(int packIndex) throws APIException {
		if (packIndex > getDataFromConfigFile(MAX_NUM_OF_THINGS_TO_CHOOSE_FROM))
			throw new APIException(FileReaderImpl.getPropertyMessages(NUM_OF_THINGS_IS_GREATER_THAN_ALLOWD));
	}

	private float getWeightFromTokenizedLine(List<String> tokenizedLine, int index) throws APIException {
		float packWeight = Float.parseFloat(tokenizedLine.get(index));
		validatePackWeight(packWeight);
		return packWeight;
	}

	public static final String MAX_WEIGHT_OF_EACH_THING = "max.weight.of.each.thing";
	public static final String THING_WEIGHT_IS_GREATER_THAN_ALLOWED = "thing.weight.is.greater.than.allowed";

	private void validatePackWeight(float packWeight) throws APIException {
		if (packWeight > getDataFromConfigFile(MAX_WEIGHT_OF_EACH_THING))
			throw new APIException(FileReaderImpl.getPropertyMessages(THING_WEIGHT_IS_GREATER_THAN_ALLOWED));
	}

	private int getCostFromTokenizedLine(List<String> tokenizedLine, int index) throws APIException {
		int packCost = Integer.parseInt(tokenizedLine.get(index));
		validatePackCost(packCost);
		return packCost;
	}

	public static final String MAX_COST_OF_EACH_THING = "max.cost.of.each.thing";
	public static final String THING_COST_IS_GREATER_THAN_ALLOWED = "thing.cost.is.greater.than.allowed";

	private void validatePackCost(int packCost) throws APIException {
		if (packCost > getDataFromConfigFile(MAX_COST_OF_EACH_THING))
			throw new APIException(FileReaderImpl.getPropertyMessages(THING_COST_IS_GREATER_THAN_ALLOWED));
	}
}
