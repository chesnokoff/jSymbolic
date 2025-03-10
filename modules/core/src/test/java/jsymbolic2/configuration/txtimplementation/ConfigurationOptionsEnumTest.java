package jsymbolic2.configuration.txtimplementation;

import jsymbolic2.configuration.OptionsEnum;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Created by dinamix on 5/17/16.
 */
public class ConfigurationOptionsEnumTest {
    @BeforeEach
    public void setUp() throws Exception {

    }

    @AfterEach
    public void tearDown() throws Exception {

    }

    @Test
    public void contains() throws Exception {
        assertTrue(OptionsEnum.contains("window_size"));
        assertTrue(OptionsEnum.contains("window_overlap"));
        assertTrue(OptionsEnum.contains("save_features_for_each_window"));
        assertTrue(OptionsEnum.contains("save_overall_recording_features"));
        assertTrue(OptionsEnum.contains("convert_to_arff"));
        assertTrue(OptionsEnum.contains("convert_to_csv"));
    }

    @Test
    public void checkValue() throws Exception {
        assertTrue(OptionsEnum.window_size.checkValue("5"));
        assertTrue(OptionsEnum.window_size.checkValue("0"));
        assertTrue(OptionsEnum.window_overlap.checkValue("0.1"));
        assertTrue(OptionsEnum.window_overlap.checkValue("0"));
        assertFalse(OptionsEnum.window_overlap.checkValue("1.1"));
        assertTrue(OptionsEnum.convert_to_arff.checkValue("false"));
        assertFalse(OptionsEnum.window_size.checkValue("-1"));
    }

}