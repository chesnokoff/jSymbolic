package jsymbolic2.processing;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import jsymbolic2.processing.SymbolicMusicFileUtilities;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for the FileValidator class.
 * 
 * @author Tristano Tenaglia
 */
public class FileValidatorTest {

    /**
     * Test of getMEISequence from the FileValidator class.
     */
    @Test
    public void testGetMEISequence() {
        File invalid = new File("mei-test/Invalid_Altenburg.mei");
        List<String> errorLog = new ArrayList<>();
        
        Assertions.assertThrows(Exception.class, () -> {
            SymbolicMusicFileUtilities.getMidiSequenceFromMidiOrMeiFile(invalid, errorLog);
        });
        String errorFile = errorLog.get(0);
        assertEquals("The specified file, mei-test/Invalid_Altenburg.mei, is not a valid MEI file.",
                        errorFile);
    }
    
    /**
     * Test of getMIDISequence from the FileValidator class.
     * @throws Exception test
     */
    @Test
    public void testGetMIDISequence() 
            throws Exception
    {
        File dne = new File("dne.midi");
        List<String> errorLog = new ArrayList<>();
        
        Assertions.assertThrows(Exception.class, () -> {
            SymbolicMusicFileUtilities.getMidiSequenceFromMidiOrMeiFile(dne, errorLog);
        });

        String errorFile = errorLog.get(0);
        assertEquals("The specified path, dne.midi, does not refer to a valid file.",
                        errorFile);
    }

    /**
     * Test of correctFileExtension from the FileValidator class.
     */
    @Test
    public void testCorrectFileExtension() {
        String fileExtension = "xml";

        String noDot = "test";
        String expectedNoDot = "test.xml";
        assertEquals(expectedNoDot,SymbolicMusicFileUtilities.correctFileExtension(noDot,fileExtension));

        String withDot = "test.two";
        String expectedWithDot = "test.two.xml";
        assertEquals(expectedWithDot,SymbolicMusicFileUtilities.correctFileExtension(withDot,fileExtension));

        String noProblem = "test.Xml";
        String expectedNoProblem = "test.Xml";
        assertEquals(expectedNoProblem,SymbolicMusicFileUtilities.correctFileExtension(noProblem,fileExtension));
    }
}
