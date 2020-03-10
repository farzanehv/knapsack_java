package com.mobiquity.packer.service.outputgenerator;

import java.util.Collections;
import java.util.List;

import com.mobiquity.exception.APIException;
import com.mobiquity.packer.service.filereader.FileReaderImpl;

public class OutputGeneratorImpl implements OutputGenerator {

	@Override
	public String generateOutput(List<Integer> resultArray) throws APIException {

		validateInput(resultArray);

		if (resultArray.isEmpty())
			return generateEmptyOutput();

		sortInputResultArray(resultArray);

		return createResultString(resultArray);
	}

	public static final String OUTPUT_GENERATED_IS_NOTHING = "output.generated.is.nothing";

	private void validateInput(List<Integer> resultArray) throws APIException {
		if (resultArray == null)
			throw new APIException(FileReaderImpl.getPropertyMessages(OUTPUT_GENERATED_IS_NOTHING));
	}

	private String generateEmptyOutput() {
		return new StringBuilder("-\n").toString();

	}

	private void sortInputResultArray(List<Integer> resultArray) {
		Collections.sort(resultArray);
	}

	private String createResultString(List<Integer> resultArray) {
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < resultArray.size() - 1; i++) {
			result.append(resultArray.get(i));
			result.append(",");
		}
		result.append(resultArray.get(resultArray.size() - 1));
		result.append("\n");
		return result.toString();
	}
}
