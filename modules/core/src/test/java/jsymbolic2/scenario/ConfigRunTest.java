package jsymbolic2.scenario;

import ace.datatypes.DataBoard;
import ace.datatypes.DataSet;
import jsymbolic2.Main;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ConfigRunTest {

    private static final double DELTA = 0.00001;

    @Test
    public void testAllFeaturesConfigRun() throws Exception {
        Main.main(new String[] {"-configrun", "src/test/java/jsymbolic2/scenario/resources/configs/all_features_config.txt"});
        String prefix = "src/test/java/jsymbolic2/scenario/resources/values/";
        assertTrue(new File(prefix + "all_features_definitions_expected.xml").exists());
        assertTrue(new File(prefix + "all_features_values_expected.xml").exists());
        assertTrue(new File(prefix + "all_features_definitions_actual.xml").exists());
        assertTrue(new File(prefix + "all_features_values_actual.xml").exists());
        DataBoard dataBoardExpected = new DataBoard(null,
            prefix + "all_features_definitions_expected.xml",
            new String[] {prefix + "all_features_values_expected.xml"}, null);
        DataBoard dataBoardActual = new DataBoard(null,
            prefix + "all_features_definitions_actual.xml",
            new String[] {prefix + "all_features_values_actual.xml"}, null);
        assertTrue(equals(dataBoardExpected, dataBoardActual));
    }

    @Test
    public void testWindowFeaturesConfigRun() throws Exception {
        Main.main(new String[] {"-configrun", "src/test/java/jsymbolic2/scenario/resources/configs/window_config.txt"});
        DataBoard dataBoardExpected = new DataBoard(null,
            "src/test/java/jsymbolic2/scenario/resources/values/window_definitions_expected.xml",
            new String[] {"src/test/java/jsymbolic2/scenario/resources/values/window_values_expected.xml"}, null);
        DataBoard dataBoardActual = new DataBoard(null,
            "src/test/java/jsymbolic2/scenario/resources/values/window_definitions_actual.xml",
            new String[] {"src/test/java/jsymbolic2/scenario/resources/values/window_values_actual.xml"}, null);
        assertTrue(equals(dataBoardExpected, dataBoardActual));
    }

    @Test
    public void testOverallFeaturesConfigRun() throws Exception {
        Main.main(new String[] {"-configrun", "src/test/java/jsymbolic2/scenario/resources/configs/overall_config.txt"});
        DataBoard dataBoardExpected = new DataBoard(null,
            "src/test/java/jsymbolic2/scenario/resources/values/overall_definitions_expected.xml",
            new String[] {"src/test/java/jsymbolic2/scenario/resources/values/overall_values_expected.xml"}, null);
        DataBoard dataBoardActual = new DataBoard(null,
            "src/test/java/jsymbolic2/scenario/resources/values/overall_definitions_actual.xml",
            new String[] {"src/test/java/jsymbolic2/scenario/resources/values/overall_values_actual.xml"}, null);
        assertTrue(equals(dataBoardExpected, dataBoardActual));
    }

    private static boolean equals(DataBoard board, DataBoard anotherBoard) {
        for (DataSet audio : board.feature_vectors) {
            Optional<DataSet> anotherAudio = Arrays.stream(anotherBoard.feature_vectors)
                .filter(a -> a.identifier.equals(audio.identifier))
                .findFirst();

            if (anotherAudio.isEmpty()) {
                return false;
            }

            if (!equals(audio, anotherAudio.get())) {
                return false;
            }
        }
        return true;
    }

    private static boolean equals(DataSet audio, DataSet anotherAudio) {
        if (audio.sub_sets != null) {
            if (anotherAudio.sub_sets == null) {
                return false;
            }
            for (int i = 0; i < audio.sub_sets.length; i++) {
                if (!equals(audio.sub_sets[i], anotherAudio.sub_sets[i])) {
                    return false;
                }
            }
        }

        if (audio.feature_values == null) {
            return anotherAudio.feature_values == null;
        }

        if (audio.feature_values.length != anotherAudio.feature_values.length) {
            return false;
        }

        Map<String, double[]> features = new HashMap<>();
        Map<String, double[]> anotherFeatures = new HashMap<>();

        for (int i = 0; i < audio.feature_values.length; i++) {
            features.put(audio.feature_names[i], audio.feature_values[i]);
            anotherFeatures.put(anotherAudio.feature_names[i], anotherAudio.feature_values[i]);
        }

        if (features.size() != anotherFeatures.size()) {
            return false;
        }

        for (Map.Entry<String, double[]> entry : features.entrySet()) {
            String key = entry.getKey();
            double[] value = entry.getValue();
            double[] anotherValue = anotherFeatures.get(key);
            if (anotherValue == null || value.length != anotherValue.length) {
                return false;
            }
            for (int i = 0; i < value.length; i++) {
                if (Math.abs(value[i] - anotherValue[i]) >= DELTA) {
                    return false;
                }
            }
        }

        return true;
    }
}