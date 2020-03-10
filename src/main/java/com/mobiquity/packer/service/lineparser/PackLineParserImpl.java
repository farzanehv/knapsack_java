package com.mobiquity.packer.service.lineparser;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.mobiquity.exception.APIException;
import com.mobiquity.packer.service.filereader.FileReaderImpl;

public class PackLineParserImpl implements LineParser {

	@Override
	public List<List<String>> getPacksDataFromLines(List<String> readFileLines) throws APIException {

		validateLines(readFileLines);
		return readFileLines.stream().map(line -> {
			try {
				return tokenizeLine(line);
			} catch (APIException e) {
				throw new RuntimeException(e);
			}
		}).collect(Collectors.toList());
	}

	public static final String LINE_PARSER_INPUT_IS_NULL = "line.parser.input.is.null";

	private void validateLines(List<String> lines) throws APIException {
		if (lines == null)
			throw new APIException(FileReaderImpl.getPropertyMessages(LINE_PARSER_INPUT_IS_NULL));
		validateLinePattern(lines);
	}

	public static final String LINE_PATTERN_IS_INCORRECT = "line.pattern.is.incorrect";

	public static final String REGEX_PATTERN = "regex.pattern";
	public static final String REGEX_PATTERN_DELIMETER = "regex.pattern.delimeters";

	// The pattern is loaded from config.properties
	private void validateLinePattern(List<String> lines) throws APIException {
		for (int i = 0; i < lines.size(); i++) {
			StringTokenizer multiTokenizer = new StringTokenizer(lines.get(i), FileReaderImpl.getConfigData(REGEX_PATTERN_DELIMETER));

			List<String> lineTokensList = new ArrayList<>();

			while (multiTokenizer.hasMoreTokens())
				lineTokensList.add(multiTokenizer.nextToken().trim());

			for (int j = 1; j < lineTokensList.size(); j++)
				if (!Pattern.matches(FileReaderImpl.getConfigData(REGEX_PATTERN), lineTokensList.get(j)))
					throw new APIException(FileReaderImpl.getPropertyMessages(LINE_PATTERN_IS_INCORRECT));
		}
	}

	public static final String DELIMETERS = "delimeters";

	// Tokenize line with config loaded delimeters
	private List<String> tokenizeLine(String line) throws APIException {

		StringTokenizer multiTokenizer = new StringTokenizer(line, FileReaderImpl.getConfigData(DELIMETERS));

		List<String> lineTokensList = new ArrayList<>();

		while (multiTokenizer.hasMoreTokens())
			lineTokensList.add(multiTokenizer.nextToken().trim());

		return lineTokensList;
	}
}
