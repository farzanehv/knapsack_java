package implementation.lineparser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mobiquity.exception.APIException;
import com.mobiquity.packer.repository.PackRepositoryImpl;
import com.mobiquity.packer.service.filereader.FileReader;
import com.mobiquity.packer.service.filereader.FileReaderImpl;
import com.mobiquity.packer.service.lineparser.LineParser;
import com.mobiquity.packer.service.lineparser.PackLineParserImpl;

public class LineParserTest {
	private LineParser packLineParser = new PackLineParserImpl();
	List<String> readFileLines;
	List<List<String>> packDataExpected;
	Properties props;

	@BeforeEach
	public void setup() throws APIException {
		FileReader fileReader = new FileReaderImpl();
		String path = Paths.get("").toAbsolutePath().toString();
		readFileLines = fileReader.readFileLines(path + "/src/test/java/implementation/filereader/t0.txt");
		packDataExpected = new ArrayList<>();
		List<String> packData = new ArrayList<>();
		packData.add("8");
		packData.add("1");
		packData.add("15.3");
		packData.add("34");
		packDataExpected.add(packData);

		props = FileReaderImpl.loadPropertiesFile(FileReaderImpl.MESSAGES_PROPERTY_FILE);
	}

	@Test
	public void getPacksDataFromLines_null() throws APIException {
		Exception exception = Assertions.assertThrows(APIException.class,
				() -> packLineParser.getPacksDataFromLines(null));
		assertEquals(props.getProperty(PackLineParserImpl.LINE_PARSER_INPUT_IS_NULL), exception.getMessage());
	}

	@Test
	public void getPacksDataFromLines_emptyInput() throws APIException {
		List<String> emptyStream = new ArrayList<>();
		List<List<String>> packsDataFromLines = packLineParser.getPacksDataFromLines(emptyStream);
		assertEquals(new ArrayList<>(), packsDataFromLines);
	}

	@Test
	public void getPacksDataFromLines_ok() throws APIException {
		List<List<String>> packsDataFromLines = packLineParser.getPacksDataFromLines(readFileLines);
		assertEquals(packDataExpected, packsDataFromLines);
	}

	@Test
	public void getPacksDataFromLines_patternError_euro() throws APIException {
		List<String> lines = new ArrayList<>();
		StringBuilder line1 = new StringBuilder("5 : (1,12.30,20) (2,4.89,€5)");
		StringBuilder line2 = new StringBuilder("5 : (1,12.30,€40) (2,4.89,€5)");
		lines.add(line1.toString());
		lines.add(line2.toString());

		APIException exception = Assertions.assertThrows(APIException.class,
				() -> packLineParser.getPacksDataFromLines(lines));
		assertEquals(props.getProperty(PackLineParserImpl.LINE_PATTERN_IS_INCORRECT), exception.getMessage());
	}
	
	@Test
	public void getPacksDataFromLines_patternError() throws APIException {
		List<String> lines = new ArrayList<>();
		StringBuilder line1 = new StringBuilder("100 : (1,12.30,€20) (2,4.89)");
		StringBuilder line2 = new StringBuilder("5 : (1,12.30,€40) (2,4.89,€5)");
		lines.add(line1.toString());
		lines.add(line2.toString());

		APIException exception = Assertions.assertThrows(APIException.class,
				() -> packLineParser.getPacksDataFromLines(lines));
		assertEquals(props.getProperty(PackLineParserImpl.LINE_PATTERN_IS_INCORRECT), exception.getMessage());
	}
}
