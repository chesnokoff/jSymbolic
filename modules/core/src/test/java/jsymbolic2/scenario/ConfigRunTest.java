package jsymbolic2.scenario;

import ace.datatypes.DataBoard;
import ace.datatypes.DataSet;
import jsymbolic2.Main;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
            for (int j = 0; j < dataBoardExpected.feature_vectors[i].feature_values.length; j++) {
                assertArrayEquals(dataBoardExpected.feature_vectors[i].feature_values[j], dataBoardActual.feature_vectors[i].feature_values[j], 0.001,
                        dataBoardExpected.feature_vectors[i].feature_names[j] + " feature is not the same.");
            }
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
            for (int j = 0; j < dataBoardExpected.feature_vectors[i].sub_sets.length; j++) {
                for (int k = 0; k < dataBoardExpected.feature_vectors[i].sub_sets[j].feature_values.length; k++) {
                        assertArrayEquals(dataBoardExpected.feature_vectors[i].sub_sets[j].feature_values[k],
                            dataBoardExpected.feature_vectors[i].sub_sets[j].feature_values[k], 0.001,
                            dataBoardExpected.feature_vectors[i].sub_sets[j].feature_names[k] + " feature is not the same.");
                    }
                }
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
            for (int j = 0; j < dataBoardExpected.feature_vectors[i].feature_values.length; j++) {
                assertArrayEquals(dataBoardExpected.feature_vectors[i].feature_values[j], dataBoardActual.feature_vectors[i].feature_values[j], 0.001,
                    dataBoardExpected.feature_vectors[i].feature_names[j] + " feature is not the same.");
            }
        }
    }
}