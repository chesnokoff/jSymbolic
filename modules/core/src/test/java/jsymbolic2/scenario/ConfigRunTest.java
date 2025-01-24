package jsymbolic2.scenario;

import ace.datatypes.DataBoard;
import jsymbolic2.Main;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ConfigRunTest {
    @Test
    public void testAllFeaturesConfigRun() throws Exception {
        Main.main(new String[]{"-configrun", "src/test/java/jsymbolic2/scenario/resources/configs/all_features_config.txt"});
        String prefix = "src/test/java/jsymbolic2/scenario/resources/values/";
        assertTrue(new File(prefix + "all_features_definitions_expected.xml").exists());
        assertTrue(new File(prefix + "all_features_values_expected.xml").exists());
        assertTrue(new File(prefix + "all_features_definitions_actual.xml").exists());
        assertTrue(new File(prefix + "all_features_values_actual.xml").exists());
        DataBoard dataBoardExpected = new DataBoard(null,
            prefix + "all_features_definitions_expected.xml",
                new String[]{prefix + "all_features_values_expected.xml"}, null);
        DataBoard dataBoardActual = new DataBoard(null,
            prefix + "all_features_definitions_actual.xml",
                new String[]{prefix + "all_features_values_actual.xml"}, null);
        for (int i = 0; i < dataBoardExpected.feature_vectors.length; i++) {
            assertTrue(Arrays.deepEquals(dataBoardExpected.feature_vectors[i].feature_values, dataBoardActual.feature_vectors[i].feature_values),
                    "Rows are not equals, expected: " + Arrays.deepToString(dataBoardExpected.feature_vectors[i].feature_values) + " but actual: " + Arrays.deepToString(dataBoardActual.feature_vectors[i].feature_values));
        }
    }

    @Test
    public void testWindowFeaturesConfigRun() throws Exception {
        Main.main(new String[]{"-configrun", "src/test/java/jsymbolic2/scenario/resources/configs/window_config.txt"});
        DataBoard dataBoardExpected = new DataBoard(null,
                "src/test/java/jsymbolic2/scenario/resources/values/window_definitions_expected.xml",
                new String[]{"src/test/java/jsymbolic2/scenario/resources/values/window_values_expected.xml"}, null);
        DataBoard dataBoardActual = new DataBoard(null,
                "src/test/java/jsymbolic2/scenario/resources/values/window_definitions_actual.xml",
                new String[]{"src/test/java/jsymbolic2/scenario/resources/values/window_values_actual.xml"}, null);
        for (int i = 0; i < dataBoardExpected.feature_vectors.length; i++) {
            assertTrue(Arrays.deepEquals(dataBoardExpected.feature_vectors[i].feature_values, dataBoardActual.feature_vectors[i].feature_values));
        }
    }

    @Test
    public void testOverallFeaturesConfigRun() throws Exception {
        Main.main(new String[]{"-configrun", "src/test/java/jsymbolic2/scenario/resources/configs/overall_config.txt"});
        DataBoard dataBoardExpected = new DataBoard(null,
                "src/test/java/jsymbolic2/scenario/resources/values/overall_definitions_expected.xml",
                new String[]{"src/test/java/jsymbolic2/scenario/resources/values/overall_values_expected.xml"}, null);
        DataBoard dataBoardActual = new DataBoard(null,
                "src/test/java/jsymbolic2/scenario/resources/values/overall_definitions_actual.xml",
                new String[]{"src/test/java/jsymbolic2/scenario/resources/values/overall_values_actual.xml"}, null);
        for (int i = 0; i < dataBoardExpected.feature_vectors.length; i++) {
            assertTrue(Arrays.deepEquals(dataBoardExpected.feature_vectors[i].feature_values, dataBoardActual.feature_vectors[i].feature_values));
        }
    }
}