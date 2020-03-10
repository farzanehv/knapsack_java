package implementation.packservice;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mobiquity.exception.APIException;
import com.mobiquity.packer.service.pack.PackService;
import com.mobiquity.packer.service.pack.PackServiceImpl;

public class PackServiceTest {

	PackService packService = new PackServiceImpl();
	String path;

	@BeforeEach
	public void setup() {
		path = Paths.get("").toAbsolutePath().toString() + "/src/test/java/implementation/packservice/t1.txt";

	}

	@Test
	public void pack_ok() throws APIException {
		String pack = packService.pack(path);
		StringBuilder expected = new StringBuilder("4\n" + "-\n" + "2,7\n" + "8,9\n");
		assertEquals(expected.toString(), pack);
	}
}
