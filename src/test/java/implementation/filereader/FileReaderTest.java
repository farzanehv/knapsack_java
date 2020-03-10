package implementation.filereader;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mobiquity.exception.APIException;
import com.mobiquity.packer.repository.PackRepositoryImpl;
import com.mobiquity.packer.service.filereader.FileReader;
import com.mobiquity.packer.service.filereader.FileReaderImpl;

public class FileReaderTest {

	FileReader fileReader;
	Properties props;

	@BeforeEach
	public void setup() throws APIException {
		fileReader = new FileReaderImpl();
		props = FileReaderImpl.loadPropertiesFile(FileReaderImpl.MESSAGES_PROPERTY_FILE);
	}

	@Test
	public void readerFileLines_testNullInput() {
		Exception exception = Assertions.assertThrows(APIException.class, () -> fileReader.readFileLines(null));
		assertEquals(props.getProperty(FileReaderImpl.FILE_PATH_ERROR), exception.getMessage());
	}

	@Test
	public void readerFileLines_testEmptyInput() {
		Exception exception = Assertions.assertThrows(APIException.class, () -> fileReader.readFileLines(""));
		assertEquals(props.getProperty(FileReaderImpl.FILE_PATH_ERROR), exception.getMessage());
	}

	@Test
	public void readerFileLines_testInvalidPath() {
		Exception exception = Assertions.assertThrows(APIException.class, () -> fileReader.readFileLines("asdf"));
		assertEquals(props.getProperty(FileReaderImpl.FILE_NOT_FOUND_ERROR), exception.getMessage());
	}

	@Test
	public void readerFileLines_ok() throws APIException {
		String path = Paths.get("").toAbsolutePath().toString();
		List<String> readFileLines = fileReader.readFileLines(path + "/src/test/java/implementation/filereader/t0.txt");

		StringBuilder b = new StringBuilder();
		readFileLines.forEach(b::append);
		assertEquals("8 : (1,15.3,€34)", b.toString());
	}

	@Test
	public void getConfigData_ok() throws APIException {
		String configData = FileReaderImpl.getConfigData(PackRepositoryImpl.MAX_NUM_OF_THINGS_TO_CHOOSE_FROM);
		assertEquals("15", configData);
	}

	@Test
	public void loadProperties_invalidFile() {
		Assertions.assertThrows(APIException.class, () -> FileReaderImpl.loadPropertiesFile("invalidfile.txt"));
	}
}
