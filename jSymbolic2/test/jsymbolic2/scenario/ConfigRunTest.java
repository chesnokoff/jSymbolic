import ace.datatypes.DataBoard;
import jsymbolic2.Main;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ConfigRunTest {
    @Test
    public void testAllFeaturesConfigRun() throws Exception {
        Main.main(new String[]{"-configrun", "./test/jsymbolic2/scenario/resources/configs/all_features_config.txt"});
        DataBoard dataBoardExpected = new DataBoard(null,
                "test/jsymbolic2/scenario/resources/values/all_features_definitions_expected.xml",
                new String[]{"test/jsymbolic2/scenario/resources/values/all_features_values_expected.xml"}, null);
        DataBoard dataBoardActual = new DataBoard(null,
                "test/jsymbolic2/scenario/resources/values/all_features_definitions_actual.xml",
                new String[]{"test/jsymbolic2/scenario/resources/values/all_features_values_actual.xml"}, null);
        for (int i = 0; i < dataBoardExpected.feature_vectors.length; i++) {
            assertTrue(Arrays.deepEquals(dataBoardExpected.feature_vectors[i].feature_values, dataBoardActual.feature_vectors[i].feature_values));
        }
    }

    @Test
    public void testWindowFeaturesConfigRun() throws Exception {
        Main.main(new String[]{"-configrun", "./test/jsymbolic2/scenario/resources/configs/window_config.txt"});
        DataBoard dataBoardExpected = new DataBoard(null,
                "test/jsymbolic2/scenario/resources/values/window_definitions_expected.xml",
                new String[]{"test/jsymbolic2/scenario/resources/values/window_values_expected.xml"}, null);
        DataBoard dataBoardActual = new DataBoard(null,
                "test/jsymbolic2/scenario/resources/values/window_definitions_actual.xml",
                new String[]{"test/jsymbolic2/scenario/resources/values/window_values_actual.xml"}, null);
        for (int i = 0; i < dataBoardExpected.feature_vectors.length; i++) {
            assertTrue(Arrays.deepEquals(dataBoardExpected.feature_vectors[i].feature_values, dataBoardActual.feature_vectors[i].feature_values));
        }
    }

    @Test
    public void testOverallFeaturesConfigRun() throws Exception {
        Main.main(new String[]{"-configrun", "./test/jsymbolic2/scenario/resources/configs/overall_config.txt"});
        DataBoard dataBoardExpected = new DataBoard(null,
                "test/jsymbolic2/scenario/resources/values/overall_definitions_expected.xml",
                new String[]{"test/jsymbolic2/scenario/resources/values/overall_values_expected.xml"}, null);
        DataBoard dataBoardActual = new DataBoard(null,
                "test/jsymbolic2/scenario/resources/values/overall_definitions_actual.xml",
                new String[]{"test/jsymbolic2/scenario/resources/values/overall_values_actual.xml"}, null);
        for (int i = 0; i < dataBoardExpected.feature_vectors.length; i++) {
            assertTrue(Arrays.deepEquals(dataBoardExpected.feature_vectors[i].feature_values, dataBoardActual.feature_vectors[i].feature_values));
        }
    }
}