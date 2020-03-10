package implementation.outputgenerator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.mobiquity.exception.APIException;
import com.mobiquity.packer.service.filereader.FileReaderImpl;
import com.mobiquity.packer.service.outputgenerator.OutputGenerator;
import com.mobiquity.packer.service.outputgenerator.OutputGeneratorImpl;

public class OutputGeneratorTest {

	OutputGenerator outputGenerator = new OutputGeneratorImpl();
	Properties props;
	
	@Test
	public void generateOutput_nullInput() throws APIException {
		props = FileReaderImpl.loadPropertiesFile(FileReaderImpl.MESSAGES_PROPERTY_FILE);
		Exception exception = Assertions.assertThrows(APIException.class, () -> outputGenerator.generateOutput(null));
		assertEquals(props.getProperty(OutputGeneratorImpl.OUTPUT_GENERATED_IS_NOTHING), exception.getMessage());
	}
	
	@Test
	public void generateOutput_ok() throws APIException {
		List<Integer> resultArray = new ArrayList<>();
		resultArray.add(8);
		resultArray.add(9);
		String actualOutput = outputGenerator.generateOutput(resultArray);
		assertEquals("8,9\n", actualOutput);
	}
	
	@Test
	public void generateOutput_emptyList() throws APIException {
		List<Integer> resultArray = new ArrayList<>();
		String actualOutput = outputGenerator.generateOutput(resultArray);
		assertEquals("-\n", actualOutput);
	}
}
