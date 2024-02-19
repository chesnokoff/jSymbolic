package jsymbolic2.api;

import ace.datatypes.DataSet;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static org.junit.Assert.*;

/**
 * Unit tests for the jSymbolic API.
 *
 * @author Tristano Tenaglia
 */
public class jSymbolicProcessorTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    //Try testing with both raw data processor and configuration file processor
    private JsymbolicProcessor processor;
    private JsymbolicProcessor processorConfig;
    private JsymbolicProcessor processorConvert;
    private String valuesPath = "./test/jsymbolic2/api/resources/feature_values.xml";
    private String definitionsPath = "./test/jsymbolic2/api/resources/feature_definitions.xml";

    @Before
    public void setUp() throws Exception {
        List<String> featureNames = Arrays.asList("Acoustic Guitar Prevalence", "Beat Histogram");
        processor = new JsymbolicProcessor(valuesPath, definitionsPath, false, false, featureNames, false, true, 10, 0.1, System.out, System.err);
        processorConfig = new JsymbolicProcessor("./test/jsymbolic2/api/resources/sampleConfiguration.txt", System.out, System.err);
        processorConvert = new JsymbolicProcessor("./test/jsymbolic2/api/resources/sampleConfigConvert.txt", System.out, System.err);
    }

    @After
    public void tearDown() throws Exception {
        File valuesFile = new File(valuesPath);
        File definitionsFile = new File(definitionsPath);
        if(valuesFile.exists()) Files.delete(Paths.get(valuesPath));
        if(definitionsFile.exists()) Files.delete(Paths.get(definitionsPath));
    }

    @Test
    public void getJsymbolicData() throws Exception {
        File saintSaensTest = new File("./test/jsymbolic2/api/resources/Saint-Saens_LeCarnevalDesAnimmaux.mei");
        List<String> errorLog = new ArrayList<>();
        processor.extractAndSaveFeaturesFromFileOrDirectory(saintSaensTest.getPath());
        DataSet[] dataSets = processor.getExtractedFeatureValues();
        DataSet dataSet = dataSets[0].sub_sets[0];
        assertEquals("Beat Histogram", dataSet.feature_names[0]);
        assertEquals("Acoustic Guitar Prevalence", dataSet.feature_names[1]);

        //Do it a 2nd time to test 1 processor can work on more than one file
        processor.extractAndSaveFeaturesFromFileOrDirectory(saintSaensTest.getPath());
        DataSet[] dataSets2 = processor.getExtractedFeatureValues();
        DataSet dataSet2 = dataSets2[0].sub_sets[0];
        assertEquals("Beat Histogram", dataSet.feature_names[0]);
        assertEquals("Acoustic Guitar Prevalence", dataSet.feature_names[1]);

        //Test that CSV and ARFF conversion is successful
        processorConvert.extractAndSaveFeaturesFromFileOrDirectory(saintSaensTest.getPath());
        String csvFileName = valuesPath.replaceAll(".xml",".csv");
        String arffFileName = valuesPath.replaceAll(".xml",".arff");
        File csvFile = new File(csvFileName);
        File arffFile = new File(arffFileName);
        assertTrue(csvFile.exists());
        assertTrue(arffFile.exists());

        //Remove the new resources
        if(csvFile.exists()) Files.delete(Paths.get(csvFileName));
        if(arffFile.exists()) Files.delete(Paths.get(arffFileName));

        //Check for non existing files
        exception.expect(Exception.class);
        processor.extractAndSaveFeaturesFromFileOrDirectory(new File("dne").getPath());
        exception.expect(Exception.class);
        processor.extractAndSaveFeaturesFromFileOrDirectory((String) null);
    }

    @Test
    public void getJsymbolicDataDirectory() throws Exception {
        File dir = new File("./test/jsymbolic2/api/resources/");
        List<String> errorLog = new ArrayList<>();
        processorConfig.extractAndSaveFeaturesFromFileOrDirectory(dir.getPath());
        DataSet[] dataSets = processorConfig.getExtractedFeatureValues();

        DataSet dataSetSaint = dataSets[1].sub_sets[0];
        assertEquals("Beat Histogram", dataSetSaint.feature_names[0]);
        assertEquals("Acoustic Guitar Prevalence", dataSetSaint.feature_names[1]);

        DataSet dataSetChopin = dataSets[0].sub_sets[0];
        assertEquals("Beat Histogram", dataSetChopin.feature_names[0]);
        assertEquals("Acoustic Guitar Prevalence", dataSetChopin.feature_names[1]);

        //Check for non existing files
        exception.expect(IllegalArgumentException.class);
        var answer = processorConfig.extractAndSaveFeaturesFromFileOrDirectory(new File("dne").getPath());
//        exception.expect(IllegalArgumentException.class);
//        processorConfig.extractAndSaveFeaturesFromFileOrDirectory((String) null);
    }

}