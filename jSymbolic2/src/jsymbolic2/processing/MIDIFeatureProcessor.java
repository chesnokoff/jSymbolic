package jsymbolic2.processing;

import java.io.File;
import java.util.ArrayList;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.Sequence;

import ace.datatypes.DataBoard;
import ace.datatypes.DataSet;
import jsymbolic2.featureutils.FeatureExtractorAccess;
import jsymbolic2.featureutils.MEIFeatureExtractor;
import mckay.utilities.sound.midi.MIDIMethods;
import ace.datatypes.FeatureDefinition;
import ca.mcgill.music.ddmal.mei.MeiXmlReader.MeiXmlReadException;

import java.util.Arrays;
import java.util.List;

import jsymbolic2.featureutils.MIDIFeatureExtractor;
import org.apache.commons.lang3.ArrayUtils;
import org.ddmal.jmei2midi.MeiSequence;
import org.ddmal.jmei2midi.meielements.meispecific.MeiSpecificStorage;


/**
 * This class is used to pre-process and extract features from MIDI recordings.
 * An object of this class should be instantiated with parameters indicating
 * the details of how features are to be extracted.
 *
 * <p>The extractFeatures method should be called whenever recordings are
 * available to be analyzed. This method should be called once for each
 * recording. It will generate DataSet for each recording and add it in List of DataSets
 *
 * <p>When you finish with pocessing the records, call generateDataBoard to get DataBoard
 * that contains all DataSets.
 *
 * <p>Features are extracted for each window and, when appropriate, the average
 * and standard deviation of each of these features is extracted for each
 * recording.
 *
 * @author Cory McKay and Tristano Tenaglia
 */
public class MIDIFeatureProcessor {
    /* FIELDS ****************************************************************/

    /**
     * The window size in seconds used for dividing up the recordings to
     * classify.
     */
    private final double windowSize;

    /**
     * The number of seconds that windows are offset by. A value of zero means
     * that there is no window overlap.
     */
    private final double windowOverlapOffset;

    /**
     * The features that are to be extracted (including dependencies of features to be saved, not just the
     * features to be saved themselves).
     */
    private MIDIFeatureExtractor[] midiFeatureExtractors;

    /**
     * The dependencies of the features in the feature_extractors field.
     * The first indice corresponds to the feature_extractors indice
     * and the second identifies the number of the dependent feature.
     * The entry identifies the indice of the feature in feature_extractors
     * that corresponds to a dependant feature. The first dimension will be
     * null if there are no dependent features.
     */
    private int[][] featureExtractorDependencies;

    /**
     * The longest number of windows of previous features that each feature
     * must have before it can be extracted. The indice corresponds to that of
     * feature_extractors.
     */
    private int[] maxFeatureOffsets;

    /**
     * Which features are to be saved after processing. Entries correspond to
     * the feature_extractors field.
     */
    private boolean[] featuresToSaveMask;

    /**
     * Whether or not to save features individually for each window.
     */
    private final boolean saveFeaturesForEachWindow;

    /**
     * Whether or not to save the average and standard deviation of each
     * feature across all windows.
     */
    private final boolean saveOverallRecordingFeatures;

    /**
     * List of DataSets. Each DataSet corresponds to record that was processed.
     */
    private final List<DataSet> dataSets = new ArrayList<>();

    /**
     * Array of overall FeatureDefinitions.
     */
    private final FeatureDefinition[] overallFeatureDefinitions;

    /**
     * Array of FeatureDefinitions of extractors.
     */
    private final FeatureDefinition[] featureExtractorsDefinitions;

    /**
     * Array of overall FeatureDefinitions' names.
     */
    private final String[] overallFeatureNames;

    /**
     * Array of extractors' FeatureDefinitions names.
     */
    private final String[] featureExtractorsNames;

    /* CONSTRUCTORS **********************************************************/

    /**
     * Validates and stores the configuration to use for extracting features
     * from MIDI recordings.
     *
     * @param windowSize                   The size of the windows in
     *                                     seconds that the MIDI
     *                                     recordings are to be broken
     *                                     into.
     * @param windowOverlap                The fraction of overlap
     *                                     between adjacent windows. Must
     *                                     be between 0.0 and less than
     *                                     1.0, with a value of 0.0
     *                                     meaning no overlap.
     * @param allFeatureExtractors         All features that can be
     *                                     extracted.
     * @param featuresToSaveAmongAll       Which features are to be
     *                                     saved. Entries correspond to
     *                                     the allFeatureExtractors
     *                                     parameter.
     * @param saveFeaturesForEachWindow    Whether or not to save
     *                                     features individually for each
     *                                     window.
     * @param saveOverallRecordingFeatures Whetehr or not to save the
     *                                     average and standard deviation
     *                                     of each feature across all
     *                                     windows.
     * @throws Exception Throws an informative
     *                   exception if the input
     *                   parameters are invalid, including if any of the feature
     *                   in allFeatureExtractors have dependencies that do not
     *                   exist in allFeatureExtractors.
     */
    public MIDIFeatureProcessor(double windowSize,
                                double windowOverlap,
                                MIDIFeatureExtractor[] allFeatureExtractors,
                                boolean[] featuresToSaveAmongAll,
                                boolean saveFeaturesForEachWindow,
                                boolean saveOverallRecordingFeatures) throws Exception {
        checkWindowFlags(windowSize, windowOverlap, saveFeaturesForEachWindow, saveOverallRecordingFeatures);
        checkAtLeastOneFlagToSaveWasSelected(featuresToSaveAmongAll);
        checkExtractors(allFeatureExtractors);
        // Save parameters as fields
        this.windowSize = windowSize;
        this.saveFeaturesForEachWindow = saveFeaturesForEachWindow;
        this.saveOverallRecordingFeatures = saveOverallRecordingFeatures;

        // Calculate the window offset
        windowOverlapOffset = windowOverlap * windowSize;
        if (windowOverlapOffset > windowSize)
            throw new Exception("Window overlap offset is greater than window size, this is not possible.");
        // Find which features need to be extracted and in what order. Also find
        // the indices of dependencies and the maximum offsets for each feature.
        findAndOrderFeaturesToExtract(allFeatureExtractors, featuresToSaveAmongAll);

        featureExtractorsDefinitions = new FeatureDefinition[allFeatureExtractors.length];
        featureExtractorsNames = new String[allFeatureExtractors.length];
        for (int feat = 0; feat < featureExtractorsDefinitions.length; ++feat) {
            featureExtractorsDefinitions[feat] = allFeatureExtractors[feat].getFeatureDefinition();
            featureExtractorsNames[feat] = featureExtractorsDefinitions[feat].name;
        }
        overallFeatureDefinitions = generateOverallFeatureDefinitions();
        overallFeatureNames = new String[overallFeatureDefinitions.length];
        for (int feat = 0; feat < overallFeatureDefinitions.length; ++feat) {
            overallFeatureNames[feat] = overallFeatureDefinitions[feat].name;
        }
    }

    private void checkExtractors(MIDIFeatureExtractor[] allFeatureExtractors) throws Exception {
        // Verify that feature names referred to by all dependencies actually exist.
        for (MIDIFeatureExtractor featureExtractor : allFeatureExtractors) {
            String[] thisFeatureDependencies = featureExtractor.getDepenedencies();
            if (thisFeatureDependencies == null) {
                continue;
            }
            boolean foundDependency = false;
            for (String dependency : thisFeatureDependencies) {
                for (int i = 0; i < allFeatureExtractors.length; i++) {
                    if (dependency.equals(allFeatureExtractors[i].getFeatureDefinition().name)) {
                        {
                            foundDependency = true;
                            break;
                        }
                    }
                }
                if (!foundDependency) {
                    throw new Exception("The " +
                            featureExtractor.getFeatureDefinition().name +
                            " feature needs the " + dependency +
                            " feature in order to be calculated, yet no feature with the latter name could be found.");
                }
            }
        }
    }

    private void checkAtLeastOneFlagToSaveWasSelected(boolean[] featuresToSaveAmongAll) throws Exception {
        boolean one_selected = false;
        for (boolean featureToSaveFlag : featuresToSaveAmongAll) {
            if (featureToSaveFlag) {
                one_selected = true;
                break;
            }
        }
        if (!one_selected) {
            throw new Exception("No features have been set to be saved.");
        }
    }

    private void checkWindowFlags(double windowSize, double windowOverlap, boolean saveFeaturesForEachWindow, boolean saveOverallRecordingFeatures) throws Exception {
        // Throw an exception if the control parameters are invalid
        if (!saveFeaturesForEachWindow && !saveOverallRecordingFeatures)
            throw new Exception("You must save at least one of the windows-based\n" + "features and the overall file-based features if\n" + "windows are to be used.");
        if (windowOverlap < 0.0 || windowOverlap >= 1.0)
            throw new Exception("Window overlap fraction is " + windowOverlap + ".\n" + "This value must be 0.0 or above and less than 1.0.");
        if (windowSize < 0.0)
            throw new Exception("Window size is " + windowSize + ".\n" + "This value must be at or above 0.0 seconds.");
    }

    /*PUBLIC METHODS ********************************************************/
    /**
     * @return The features that are to be extracted (including dependencies of features to be saved, not
     * just the features to be saved themselves).
     */
    public MIDIFeatureExtractor[] getFinalFeaturesToBeExtracted() {
        return midiFeatureExtractors;
    }

    /**
     * @return True if it has features for MEI files.
     */
    public boolean containsMeiFeatures() {
        List<String> all_mei_specific_features = FeatureExtractorAccess.getNamesOfMeiSpecificFeatures();
        for (MIDIFeatureExtractor feature : midiFeatureExtractors) {
            if (all_mei_specific_features.contains(feature.getFeatureDefinition().name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Extract the features from the provided MIDI or MEI file. This may involve
     * windowing, depending on the instantiation parameters of this object.
     * @param recordingFile The music file to extract features from.
     * @param errorLog      A List(String) that holds all the files with errors.
     * @throws InvalidMidiDataException Thrown if the MIDI data is invalid.
     * @throws MeiXmlReadException      Thrown if there is a problem reading in the MEI XML from the inputted file.
     * @throws Exception                When an unforeseen runtime exception occurs.
     */
    public void extractFeatures(File recordingFile, List<String> errorLog)
            throws InvalidMidiDataException, MeiXmlReadException, Exception {
        if (windowOverlapOffset > windowSize)
            throw new Exception("Window overlap offset is greater than window size, this is not possible.");
        // Extract the data from the file and check for exceptions
        Sequence fullSequence = null;
        MeiSequence meiSequence = null;
        if (SymbolicMusicFileUtilities.isValidMidiFile(recordingFile)) {
            fullSequence = SymbolicMusicFileUtilities.getMidiSequenceFromMidiOrMeiFile(recordingFile, errorLog);
        } else {
            if (SymbolicMusicFileUtilities.isValidMeiFile(recordingFile)) {
                meiSequence = SymbolicMusicFileUtilities.getMeiSequenceFromMeiFile(recordingFile, errorLog);
                fullSequence = meiSequence.getSequence();
            }
        }

        //Mei Specific Storage added here and null is set if the file is not an mei file
        MeiSpecificStorage meiSpecificStorage = null;
        if (meiSequence != null) {
            meiSpecificStorage = meiSequence.getNonMidiStorage();
        }

        // Prepare the windows for feature extraction with correct times
        // Tick arrays have been added to account for multiple windows
        Sequence[] windows;
        double[] secondsPerTick = MIDIMethods.getSecondsPerTick(fullSequence);
        List<int[]> startEndTickArrays;
        int[] startTicks;
        int[] endTicks;
        try {
            if (!saveFeaturesForEachWindow) {
                startEndTickArrays = MIDIMethods.getStartEndTickArrays(fullSequence, fullSequence.getMicrosecondLength() / 1000000.0, 0.0, secondsPerTick);
                startTicks = startEndTickArrays.get(0);
                endTicks = startEndTickArrays.get(1);
                windows = new Sequence[1];
                windows[0] = fullSequence;
            } else {
                startEndTickArrays = MIDIMethods.getStartEndTickArrays(fullSequence, windowSize, windowOverlapOffset, secondsPerTick);
                startTicks = startEndTickArrays.get(0);
                endTicks = startEndTickArrays.get(1);
                windows = MIDIMethods.breakSequenceIntoWindows(fullSequence, windowSize, windowOverlapOffset, startTicks, endTicks);
            }
        } catch (RuntimeException e) {
            throw new Exception("An error occurred while processing the following file: " + recordingFile + ".\n");
        }

        // Extract the feature values from the samples
        double[][][] windowFeatureValues = getFeatures(windows, meiSpecificStorage);

        // Find the feature averages and standard deviations if appropriate
        double[][] overallFeatureValues = null;
        if (saveOverallRecordingFeatures) {
            overallFeatureValues = generateOverallRecordingFeatures(windowFeatureValues);
        }
        addDataSet(windowFeatureValues, recordingFile.getPath(), overallFeatureValues, startTicks, endTicks, secondsPerTick);
    }

    /**
     * Extract the features from the provided Sequence.
     *
     * @param sequence Sequence to process.
     * @param name     How to name sequence in DataBoard.
     * @throws Exception When an unforeseen runtime exception occurs.
     */
    public void extractFeaturesFromSequence(Sequence sequence, String name)
            throws Exception {
        // Prepare the windows for feature extraction with correct times
        // Tick arrays have been added to account for multiple windows
        double[] secondsPerTick = MIDIMethods.getSecondsPerTick(sequence);
        int[] startTicks;
        int[] endTicks;
        Sequence[] windows;
        if (!saveFeaturesForEachWindow) {
            List<int[]> startEndTickArrays = MIDIMethods.getStartEndTickArrays(sequence, sequence.getMicrosecondLength() / 1000000.0, 0.0, secondsPerTick);
            startTicks = startEndTickArrays.get(0);
            endTicks = startEndTickArrays.get(1);
            windows = new Sequence[1];
            windows[0] = sequence;
        } else {
            List<int[]> startEndTickArrays = MIDIMethods.getStartEndTickArrays(sequence, windowSize, windowOverlapOffset, secondsPerTick);
            startTicks = startEndTickArrays.get(0);
            endTicks = startEndTickArrays.get(1);
            windows = MIDIMethods.breakSequenceIntoWindows(sequence, windowSize, windowOverlapOffset, startTicks, endTicks);
        }

        // Extract the feature values from the samples
        double[][][] windowFeatureValues = getFeatures(windows, null);

        // Find the feature averages and standard deviations if appropriate
        double[][] overallFeatureValues = null;
        if (saveOverallRecordingFeatures) {
            overallFeatureValues = generateOverallRecordingFeatures(windowFeatureValues);
        }
        addDataSet(windowFeatureValues, name, overallFeatureValues, startTicks, endTicks, secondsPerTick);
    }

    /**
     * Extract the features from the provided MeiSequence.
     *
     * @param meiSequence MeiSequence to process.
     * @param name        How to name sequence in DataBoard.
     * @throws Exception When an unforeseen runtime exception occurs.
     */
    public void extractFeaturesFromMeiSequence(MeiSequence meiSequence, String name)
            throws Exception {
        // Extract the data from the file and check for exceptions
        Sequence sequence = meiSequence.getSequence();
        // Prepare the windows for feature extraction with correct times
        // Tick arrays have been added to account for multiple windows
        double[] secondsPerTick = MIDIMethods.getSecondsPerTick(sequence);
        int[] startTicks;
        int[] endTicks;
        Sequence[] windows;
        if (!saveFeaturesForEachWindow) {
            List<int[]> startEndTickArrays = MIDIMethods.getStartEndTickArrays(sequence, sequence.getMicrosecondLength() / 1000000.0, 0.0, secondsPerTick);
            startTicks = startEndTickArrays.get(0);
            endTicks = startEndTickArrays.get(1);
            windows = new Sequence[1];
            windows[0] = sequence;
        } else {
            List<int[]> startEndTickArrays = MIDIMethods.getStartEndTickArrays(sequence, windowSize, windowOverlapOffset, secondsPerTick);
            startTicks = startEndTickArrays.get(0);
            endTicks = startEndTickArrays.get(1);
            windows = MIDIMethods.breakSequenceIntoWindows(sequence, windowSize, windowOverlapOffset, startTicks, endTicks);
        }
        //Mei Specific Storage added here and null is set if the file is not an mei file
        // Extract the feature values from the samples
        MeiSpecificStorage meiSpecificStorage = meiSequence.getNonMidiStorage();
        double[][][] windowFeatureValues = getFeatures(windows, meiSpecificStorage);
        // Find the feature averages and standard deviations if appropriate
        double[][] overallFeatureValues = null;
        if (saveOverallRecordingFeatures) {
            overallFeatureValues = generateOverallRecordingFeatures(windowFeatureValues);
        }
        addDataSet(windowFeatureValues, name, overallFeatureValues, startTicks, endTicks, secondsPerTick);
    }

    /**
     * Extracts features from each window of the given MIDI sequences. If the
     * passed windows parameter consists of only one window, then this could
     * be a whole unwindowed MIDI file.
     *
     * @param meiSpecificStorage The mei specific data storage used to extract
     *                           mei specific features from the sequence windows.
     *                           This will be null if the file is not an mei file
     *                           and otherwise it will contain all mei specific data
     *                           extracted by jMei2Midi.
     * @param windows            The ordered MIDI windows to extract features from.
     * @return The extracted feature values for this recording.
     * The first indice identifies the window, the second
     * identifies the feature and the third identifies
     * the feature value. The third dimension will be
     * null if the given feature could not be extracted
     * for the given window.
     * @throws Exception Throws an exception if a problem occurs.
     */
    public double[][][] getFeatures(Sequence[] windows, MeiSpecificStorage meiSpecificStorage) throws Exception {
        // The extracted feature values for this recording. The first indice
        // identifies the window, the second identifies the feature and the
        // third identifies the feature value.
        double[][][] results = new double[windows.length][midiFeatureExtractors.length][];

        // Extract features from each window one by one and add save the results.
        // The last window is zero-padded at the end if it falls off the edge of the
        // provided samples.
        for (int win = 0; win < windows.length; win++) {
            // Extract information from sequence that is needed to extract features
            MIDIIntermediateRepresentations intermediate = new MIDIIntermediateRepresentations(windows[win]);

            // Extract the features one by one
            for (int feat = 0; feat < midiFeatureExtractors.length; feat++) {
                // Only extract this feature if enough previous information
                // is available to extract this feature
                if (win >= maxFeatureOffsets[feat]) {
                    // Find the correct feature
                    MIDIFeatureExtractor feature = midiFeatureExtractors[feat];

                    // Find previously extracted feature values that this feature
                    // needs
                    double[][] otherFeatureValues = null;
                    if (featureExtractorDependencies[feat] != null) {
                        otherFeatureValues = new double[featureExtractorDependencies[feat].length][];
                        for (int i = 0; i < featureExtractorDependencies[feat].length; i++) {
                            int featureIndice = featureExtractorDependencies[feat][i];
                            /* TODO Check if this is a correct bug fix */
                            if (feature.getDepenedencyOffsets() == null) {
                                otherFeatureValues[i] = results[win][featureIndice];
                            } else {
                                int offset = feature.getDepenedencyOffsets()[i];
                                otherFeatureValues[i] = results[win + offset][featureIndice];
                            }
                        }
                    }

                    //Check here if the file is an MEI file and if the feature is an MEI feature
                    //Otherwise just extract the midi feature data
                    if (meiSpecificStorage != null && feature instanceof MEIFeatureExtractor) {
                        results[win][feat] = ((MEIFeatureExtractor) feature).extractMEIFeature(meiSpecificStorage, windows[win], intermediate, otherFeatureValues);
                    } else if (meiSpecificStorage == null && feature instanceof MEIFeatureExtractor) {
                        //Skip if this is a non-mei file as mei features are not valid
                        continue;
                    } else {
                        // Store the extracted feature values
                        results[win][feat] = feature.extractFeature(windows[win], intermediate, otherFeatureValues);
                    }
                } else results[win][feat] = null;
            }
        }

        for (int win = 0; win < results.length; ++win) {
            for (int feat = 0; feat < midiFeatureExtractors.length; ++feat) {
                if (!featuresToSaveMask[feat]) {
                    results[win][feat] = null;
                }
            }
        }
        // Return the results
        return results;
    }

    /**
     * Generates DataBoard based in list of DataSets that were constructed during features extractions
     * @return DataBoard of processed recordings
     * @throws Exception Throws an exception if it cannot create DataBoard object.
     */
    public DataBoard generateDataBoard() throws Exception {
        DataBoard dataBoard = new DataBoard(null,
                null,
                dataSets.toArray(new DataSet[0]),
                null);
        if (saveOverallRecordingFeatures) {
            dataBoard.feature_definitions = ArrayUtils.addAll(featureExtractorsDefinitions, overallFeatureDefinitions);
        } else {
            dataBoard.feature_definitions = featureExtractorsDefinitions;
        }
        return dataBoard;
    }

    /* PRIVATE METHODS *******************************************************/

    /**
     * Fills the feature_extractors, feature_extractor_dependencies,
     * max_feature_offsets and features_to_save fields. This involves finding
     * which features need to be extracted and in what order and finding
     * the indices of dependencies and the maximum offsets for each feature.
     *
     * @param allFeatureExtractors   All features that can be extracted.
     * @param featuresToSaveAmongAll Which features are to be saved.
     *                               Entries correspond to the
     *                               allFeatureExtractors parameter.
     */
    private void findAndOrderFeaturesToExtract(MIDIFeatureExtractor[] allFeatureExtractors,
                                               boolean[] featuresToSaveAmongAll) {
        // Find the names of all features
        String[] allFeatureNames = new String[allFeatureExtractors.length];
        for (int feat = 0; feat < allFeatureExtractors.length; feat++)
            allFeatureNames[feat] = allFeatureExtractors[feat].getFeatureDefinition().name;

        // Find the dependencies of each feature marked to be extracted.
        // Mark an entry as null if that entry's matching feature is not set to be extracted.
        // Note that an entry will also be null if the corresponding feature has no dependencies.
        String[][] dependencies = new String[allFeatureExtractors.length][];
        for (int feat = 0; feat < allFeatureExtractors.length; feat++) {
            dependencies[feat] = null;
            if (featuresToSaveAmongAll[feat])
                dependencies[feat] = allFeatureExtractors[feat].getDepenedencies();
        }

        // Start off the array of which features to extract by making sure to extract all those features
        // whose values are marked to be saved.
        boolean[] featuresToExtractIncludingDependencies = new boolean[allFeatureExtractors.length];
        System.arraycopy(featuresToSaveAmongAll, 0, featuresToExtractIncludingDependencies,
                0, allFeatureExtractors.length);

        // Update featuresToExtractIncludingDependencies to ALSO include those features that are not
        // marked to be saved, but are needed as dependencies in order to calculate features that are
        // marked to be saved. Also update dependencies to include any new dependencies that are introduced
        // by scheduling new features to be extracted because they themselves are dependencies of other
        // features.
        boolean done = false;
        while (!done) {
            done = true;
            for (int feat = 0; feat < allFeatureExtractors.length; feat++) {
                if (dependencies[feat] != null) {
                    for (int dep = 0; dep < dependencies[feat].length; dep++) {
                        String thisDependencyName = dependencies[feat][dep];
                        for (int j = 0; j < allFeatureExtractors.length; j++) {
                            if (thisDependencyName.equals(allFeatureNames[j])) {
                                if (!featuresToExtractIncludingDependencies[j]) {
                                    featuresToExtractIncludingDependencies[j] = true;
                                    dependencies[j] = allFeatureExtractors[j].getDepenedencies();
                                    if (dependencies[j] != null) done = false;
                                }
                                j = allFeatureExtractors.length;
                            }
                        }
                    }
                }
            }
        }

        // Begin the process of finding the correct order to extract features in by filling the
        // feature_extractors field with all features that are to be extracted (i.e. the combination of
        // those features whose values are marked to be saved and those features that are needed in order
        // to calculate those features marked to be saved). The ordering consists of the originally
        // specified feature order, with dependent features added in before they are needed.
        // Also note which of these have values that are actually to be saved by filling in
        // features_to_save.
        int numberFeaturesToExtract = 0;
        for (int i = 0; i < featuresToExtractIncludingDependencies.length; i++)
            if (featuresToExtractIncludingDependencies[i])
                numberFeaturesToExtract++;
        midiFeatureExtractors = new MIDIFeatureExtractor[numberFeaturesToExtract];
        featuresToSaveMask = new boolean[numberFeaturesToExtract];
        Arrays.fill(featuresToSaveMask, false);
        boolean[] featureAdded = new boolean[allFeatureExtractors.length];
        for (int i = 0; i < featureAdded.length; i++)
            featureAdded[i] = false;
        int currentPosition = 0;
        done = false;
        while (!done) {
            done = true;

            // Add all features that have no remaining dependencies and remove
            // their dependencies from all unadded features
            for (int feat = 0; feat < allFeatureExtractors.length; feat++) {
                if (featuresToExtractIncludingDependencies[feat] && !featureAdded[feat]) {
                    if (dependencies[feat] == null) // add feature if it has no dependencies
                    {
                        featureAdded[feat] = true;
                        midiFeatureExtractors[currentPosition] = allFeatureExtractors[feat];
                        featuresToSaveMask[currentPosition] = featuresToSaveAmongAll[feat];
                        currentPosition++;
                        done = false;

                        // Remove this dependency from all features that have
                        // it as a dependency and are marked to be extracted
                        for (int i = 0; i < allFeatureExtractors.length; i++) {
                            if (featuresToExtractIncludingDependencies[i] && dependencies[i] != null) {
                                int numDefs = dependencies[i].length;
                                for (int j = 0; j < numDefs; j++) {
                                    if (dependencies[i][j].equals(allFeatureNames[feat])) {
                                        if (dependencies[i].length == 1) {
                                            dependencies[i] = null;
                                            j = numDefs;
                                        } else {
                                            String[] temp = new String[dependencies[i].length - 1];
                                            int m = 0;
                                            for (int k = 0; k < dependencies[i].length; k++) {
                                                if (k != j) {
                                                    temp[m] = dependencies[i][k];
                                                    m++;
                                                }
                                            }
                                            dependencies[i] = temp;
                                            j--;
                                            numDefs--;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Find the indices of the feature extractor dependencies for each feature
        // extractor
        featureExtractorDependencies = new int[midiFeatureExtractors.length][];
        String[] feature_names = new String[midiFeatureExtractors.length];
        for (int feat = 0; feat < feature_names.length; feat++)
            feature_names[feat] = midiFeatureExtractors[feat].getFeatureDefinition().name;
        String[][] featureDependenciesStr = new String[midiFeatureExtractors.length][];
        for (int feat = 0; feat < featureDependenciesStr.length; feat++)
            featureDependenciesStr[feat] = midiFeatureExtractors[feat].getDepenedencies();
        for (int i = 0; i < featureDependenciesStr.length; i++)
            if (featureDependenciesStr[i] != null) {
                featureExtractorDependencies[i] = new int[featureDependenciesStr[i].length];
                for (int j = 0; j < featureDependenciesStr[i].length; j++)
                    for (int k = 0; k < feature_names.length; k++)
                        if (featureDependenciesStr[i][j].equals(feature_names[k]))
                            featureExtractorDependencies[i][j] = k;
            }

        // Find the maximum offset for each feature
        maxFeatureOffsets = new int[midiFeatureExtractors.length];
        for (int feat = 0; feat < maxFeatureOffsets.length; feat++) {
            if (midiFeatureExtractors[feat].getDepenedencyOffsets() == null) {
                maxFeatureOffsets[feat] = 0;
                continue;
            }
            int[] offsetsOfCurrentFeature = midiFeatureExtractors[feat].getDepenedencyOffsets();
            maxFeatureOffsets[feat] = Math.abs(offsetsOfCurrentFeature[0]);
            for (int offsetOfCurrentFeature : offsetsOfCurrentFeature) {
                maxFeatureOffsets[feat] = Math.max(Math.abs(offsetOfCurrentFeature), offsetOfCurrentFeature);
            }
        }
    }


    /**
     * Calculates the averages and standard deviations over a whole recording
     * of each of the windows-based features. Generates a feature definition
     * for each such feature. If only one value is present (dep.e. only one
     * window) then this value is stored without any standard deviation.
     *
     * @param windowFeatureValues The extracted window feature values
     *                            for this recording. The first
     *                            indice identifies the window, the
     *                            second identifies the feature and
     *                            the third identifies the feature
     *                            value. The third dimension will
     *                            be null if the given feature could
     *                            not be extracted for the given
     *                            window.
     * @return The extracted overall average and
     * standard deviations of the window
     * feature values that were passed to
     * this method. The first indice
     * identifies the feature and the
     * second identifies the feature
     * value. The order of the features
     * correspond to the
     * FeatureDefinitions that the
     * overallFeatureDefinitions
     * parameter is filled with.
     */
    private double[][] generateOverallRecordingFeatures(double[][][] windowFeatureValues) {
        double[][] featureOverallValues;
        if (windowFeatureValues.length == 1) {
            featureOverallValues = windowFeatureValues[0];
            return featureOverallValues;
        }
        featureOverallValues = new double[midiFeatureExtractors.length * 2][];
        for (int feat = 0; feat < midiFeatureExtractors.length; feat++) {
            if (windowFeatureValues[windowFeatureValues.length - 1][feat] == null || !featuresToSaveMask[feat]) {
                featureOverallValues[2 * feat] = null;
                featureOverallValues[2 * feat + 1] = null;
                continue;
            }
            // Find the averages and standard deviations
            double[] averages = new double[windowFeatureValues[windowFeatureValues.length - 1][feat].length];
            double[] stdvs = new double[windowFeatureValues[windowFeatureValues.length - 1][feat].length];
            for (int val = 0; val < windowFeatureValues[windowFeatureValues.length - 1][feat].length; val++) {
                // Find the number of windows that have featureOverallValues for this value feature
                int count = 0;
                for (double[][] windowFeaturesValues : windowFeatureValues) {
                    if (windowFeaturesValues[feat] != null) count++;
                }

                // Find the featureOverallValues to find the average and standard deviations of
                double[] valuesToProcess = new double[count];
                int current = 0;
                for (double[][] windowFeaturesValues : windowFeatureValues)
                    if (windowFeaturesValues[feat] != null) {
                        valuesToProcess[current] = windowFeaturesValues[feat][val];
                        current++;
                    }
                // Calculate the averages and standard deviations
                averages[val] = mckay.utilities.staticlibraries.MathAndStatsMethods.getAverage(valuesToProcess);
                stdvs[val] = mckay.utilities.staticlibraries.MathAndStatsMethods.getStandardDeviation(valuesToProcess);
            }
            // Store the results
            featureOverallValues[2 * feat] = averages;
            featureOverallValues[2 * feat + 1] = stdvs;
        }
        return featureOverallValues;
    }

    /**
     * Generates FeatureDefinitions for overall features
     * @return array of overall FeatureDefinitions
     */
    private FeatureDefinition[] generateOverallFeatureDefinitions() {
        FeatureDefinition[] definitions = new FeatureDefinition[midiFeatureExtractors.length * 2];
        for (int feat = 0; feat < midiFeatureExtractors.length; feat++) {
            // Make the definitions
            FeatureDefinition thisDef = midiFeatureExtractors[feat].getFeatureDefinition();
            FeatureDefinition averageDefinition = new FeatureDefinition(thisDef.name + " Overall Average", thisDef.description + "\nThis is the overall average over all windows.", thisDef.is_sequential, thisDef.dimensions);
            FeatureDefinition stdvDefinition = new FeatureDefinition(thisDef.name + " Overall Standard Deviation", thisDef.description + "\nThis is the overall standard deviation over all windows.", thisDef.is_sequential, thisDef.dimensions);
            // Store the results
            definitions[2 * feat] = averageDefinition;
            definitions[2 * feat + 1] = stdvDefinition;
        }
        // Finalize the values
        return definitions;
    }

    /**
     * Generates DataSet for current recording and adds it in list of DataSets
     * @param windowFeatureValues The extracted window feature values
     *                            for this recording. The first
     *                            indice identifies the window, the
     *                            second identifies the feature and
     *                            the third identifies the feature
     *                            value. The third dimension will
     *                            be null if the given feature could
     *                            not be extracted for the given
     *                            window.
     * @param overallFeatureValues The extracted overall average and
     *                             standard deviations of the window
     *                             feature values. The first indice
     *                             identifies the feature and the
     *                             second identifies the feature
     *                             value. The order of the features
     *                             correspond to the
     *                             overall_feature_definitions
     *                             parameter. This value is null if
     *                             overall feature values were not
     *                             extracted.
     * @param startTicks The start ticks that correspond to each MIDI window.
     * @param endTicks The end ticks that correspond to each MIDI window.
     * @param secondsPerTick The number of seconds in a MIDI tick given by the sequence.
     */
    private void addDataSet(double[][][] windowFeatureValues, String identifier,
                            double[][] overallFeatureValues,
                            int[] startTicks,
                            int[] endTicks,
                            double[] secondsPerTick) {
        DataSet rootDataSet = new DataSet(identifier, null, Double.NaN, Double.NaN,
                null, null, null);
        if (windowFeatureValues.length == 1) {
            rootDataSet.feature_values = windowFeatureValues[0];
            rootDataSet.feature_names = featureExtractorsNames;
            dataSets.add(rootDataSet);
            return;
        }
        rootDataSet.sub_sets = new DataSet[windowFeatureValues.length];
        for (int win = 0; win < windowFeatureValues.length; ++win) {
            double startTime = MIDIMethods.getSecondsAtTick(startTicks[win], secondsPerTick);
            //check for non-negative
            startTime = (startTime > 0) ? startTime : 0;
            double endTime = MIDIMethods.getSecondsAtTick(endTicks[win], secondsPerTick);

            DataSet windowDataSet = new DataSet(null, null,
                    startTime, endTime,
                    windowFeatureValues[win], featureExtractorsNames, rootDataSet);
            rootDataSet.sub_sets[win] = windowDataSet;
        }
        if (overallFeatureValues != null) {
            rootDataSet.feature_values = overallFeatureValues;
            rootDataSet.feature_names = overallFeatureNames;
        }
        dataSets.add(rootDataSet);
    }
}