package implementation;

import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.runner.RunWith;

import implementation.filereader.FileReaderTest;
import implementation.lineparser.LineParserTest;
import implementation.outputgenerator.OutputGeneratorTest;
import implementation.packrepository.PackRepositoryTest;
import implementation.packservice.PackServiceTest;

@RunWith(JUnitPlatform.class)
@SelectClasses({ FileReaderTest.class, PackRepositoryTest.class, LineParserTest.class, PackServiceTest.class,
		OutputGeneratorTest.class, PackRepositoryTest.class })
public class AllTestSuite {

}
