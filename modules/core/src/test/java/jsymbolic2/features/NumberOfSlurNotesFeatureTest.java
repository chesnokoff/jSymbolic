package jsymbolic2.features;

import jsymbolic2.featureutils.MEIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;
import org.ddmal.jmei2midi.MeiSequence;
import org.ddmal.jmei2midi.meielements.meispecific.MeiSpecificStorage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * Created by dinamix on 7/18/16.
 */
public class NumberOfSlurNotesFeatureTest {
    File saint_saens;
    MeiSequence sequence;
    MeiSpecificStorage specificStorage;
    @BeforeEach
    public void setUp() throws Exception {
        saint_saens = new File("src/test/java/jsymbolic2/features/resources/Saint-Saens_LeCarnevalDesAnimmaux.mei");
        sequence = new MeiSequence(saint_saens);
        specificStorage = sequence.getNonMidiStorage();
    }

    @AfterEach
    public void tearDown() throws Exception {

    }

    @Test
    public void extractMEIFeature() throws Exception {
        MEIFeatureExtractor slurNotesFeature = new NumberOfSlursMeiFeature();
        MIDIIntermediateRepresentations minter = new MIDIIntermediateRepresentations(sequence.getSequence());
        double[] actualSlurNotes = slurNotesFeature.extractMEIFeature(specificStorage, sequence.getSequence(), minter, null);
        double[] expectedSlurNotes = new double[]{0.1154};
        assertArrayEquals(expectedSlurNotes, actualSlurNotes, 0.001);
    }

}