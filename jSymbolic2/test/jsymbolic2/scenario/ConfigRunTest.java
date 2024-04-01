import ace.datatypes.DataBoard;
import jsymbolic2.Main;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ConfigRunTest {
    @Test
    public void testConfigRun() throws Exception {
        Main.main(new String[]{"-configrun", "./test/jSymbolic2/scenario/resources/configs/all_features_config.txt"});
        DataBoard dataBoard = new DataBoard(null,
                "test/jsymbolic2/scenario/resources/all_features_definitions_test.xml",
                new String[]{"test/jsymbolic2/scenario/resources/real_values/all_features_values.xml"}, null);
//        assertTrue(FileUtils.contentEquals(new File("./test/jSymbolic2/scenario/resources/real_values/all_features_values.xml"),
//                new File("./test/jSymbolic2/scenario/resources/all_features_values_test.xml")));
    }
}