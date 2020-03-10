package implementation.packrepository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mobiquity.exception.APIException;
import com.mobiquity.packer.repository.PackRepository;
import com.mobiquity.packer.repository.PackRepositoryImpl;
import com.mobiquity.packer.service.filereader.FileReader;
import com.mobiquity.packer.service.filereader.FileReaderImpl;
import com.mobiquity.packer.service.lineparser.LineParser;
import com.mobiquity.packer.service.lineparser.PackLineParserImpl;

public class PackRepositoryTest {

	PackRepository packRepository = new PackRepositoryImpl();
	Properties props;
	String mainPath = Paths.get("").toAbsolutePath().toString()
			+ "/src/test/java/implementation/packrepository";

	@BeforeEach
	public void setup() throws APIException {

		props = FileReaderImpl.loadPropertiesFile(FileReaderImpl.MESSAGES_PROPERTY_FILE);
	}

	@Test
	public void loadPacks_indexOutOfRange() throws APIException {
		String path = mainPath + "/indexOutOfRange.txt";

		APIException exception = Assertions.assertThrows(APIException.class, () -> packRepository.loadPacks(path));
		assertEquals(
				APIException.class.getCanonicalName()
						+ ": " + props.getProperty(PackRepositoryImpl.NUM_OF_THINGS_IS_GREATER_THAN_ALLOWD),
				exception.getMessage());
	}
	
	@Test
	public void loadPacks_weightOutOfRange() throws APIException {
		String path = mainPath + "/weightOutOfRange.txt";

		APIException exception = Assertions.assertThrows(APIException.class, () -> packRepository.loadPacks(path));
		assertEquals(
				APIException.class.getCanonicalName()
						+ ": " + props.getProperty(PackRepositoryImpl.THING_WEIGHT_IS_GREATER_THAN_ALLOWED),
				exception.getMessage());
	}
	
	@Test
	public void loadPacks_costOutOfRange() throws APIException {
		String path = mainPath + "/costOutOfRange.txt";

		APIException exception = Assertions.assertThrows(APIException.class, () -> packRepository.loadPacks(path));
		assertEquals(
				APIException.class.getCanonicalName()
						+ ": " + props.getProperty(PackRepositoryImpl.THING_COST_IS_GREATER_THAN_ALLOWED),
				exception.getMessage());
	}
	
	@Test
	public void loadPacks_limitedWeightOutOfRange() throws APIException {
		String path = mainPath + "/limitedWeightOutOfRange.txt";

		APIException exception = Assertions.assertThrows(APIException.class, () -> packRepository.loadPacks(path));
		assertEquals(
				APIException.class.getCanonicalName()
						+ ": " + props.getProperty(PackRepositoryImpl.MAX_WEIGHT_OF_PACKAGE_IS_GREATER_THAN_ALLOWD),
				exception.getMessage());
	}
	
}
