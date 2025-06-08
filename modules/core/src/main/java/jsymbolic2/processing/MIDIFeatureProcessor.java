package jsymbolic2.processing;

import ace.datatypes.DataBoard;
import ace.datatypes.DataSet;
import ace.datatypes.FeatureDefinition;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinTask;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import mckay.utilities.sound.midi.MIDIMethods;
import org.apache.commons.lang3.ArrayUtils;
import org.ddmal.jmei2midi.meielements.meispecific.MeiSpecificStorage;

import javax.sound.midi.Sequence;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountedCompleter;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.IntStream;


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
    /** */
    private static final Map<String, MIDIFeatureExtractor> name2extractor = new ConcurrentHashMap<>();

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
     * Array of FeatureDefinitions of extractors.
     */
    private final FeatureDefinition[] featureExtractorsDefinitions;

    /**
     * Array of overall FeatureDefinitions' names.
     */
    private final List<String> overallFeatureNames;

    /**
     * Array of extractors' FeatureDefinitions names.
     */
    private final String[] featureExtractorsNames;

    private final List<String> featureExtractorsNamesToSave;

    /**
     * Validates and stores the configuration to use for extracting features
     * from MIDI recordings.
     * <p>
     * meaning no overlap.
     *
     * @param allFeatureExtractors         All features that can be
     *                                     extracted.
     * @param featuresToSaveAmongAll       Which features are to be
     *                                     saved. Entries correspond to
     *                                     the allFeatureExtractors
     *                                     parameter.
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
    public MIDIFeatureProcessor(WindowInfo windowInfo,
        MIDIFeatureExtractor[] allFeatureExtractors,
        boolean[] featuresToSaveAmongAll,
        boolean saveOverallRecordingFeatures) throws Exception {

        checkWindowFlags(windowInfo.window_size(), windowInfo.window_overlap(), windowInfo.save_features_for_each_window(), saveOverallRecordingFeatures);
        checkAtLeastOneFlagToSaveWasSelected(featuresToSaveAmongAll);
        checkExtractors(allFeatureExtractors);

        // Save parameters as fields
        windowSize = windowInfo.window_size();
        saveFeaturesForEachWindow = windowInfo.save_features_for_each_window();
        this.saveOverallRecordingFeatures = saveOverallRecordingFeatures;

        for (MIDIFeatureExtractor allFeatureExtractor : allFeatureExtractors) {
            name2extractor.put(allFeatureExtractor.getFeatureDefinition().name, allFeatureExtractor);
        }
        // Calculate the window offset
        windowOverlapOffset = windowInfo.window_overlap() * windowSize;
        if (windowOverlapOffset > windowSize)
            throw new Exception("Window overlap offset is greater than window size, this is not possible.");

        featureExtractorsNamesToSave = IntStream.range(0, allFeatureExtractors.length)
            .filter(i -> featuresToSaveAmongAll[i])
            .mapToObj(i -> allFeatureExtractors[i].getName())
            .toList();


        featureExtractorsDefinitions = new FeatureDefinition[allFeatureExtractors.length];
        featureExtractorsNames = new String[allFeatureExtractors.length];
        for (int feat = 0; feat < featureExtractorsDefinitions.length; ++feat) {
            featureExtractorsDefinitions[feat] = allFeatureExtractors[feat].getFeatureDefinition();
            featureExtractorsNames[feat] = featureExtractorsDefinitions[feat].name;
        }

        List<FeatureDefinition> overallFeatureDefinitions = generateOverallFeatureDefinitions();
        overallFeatureNames = overallFeatureDefinitions.stream().map(definition -> definition.name).toList();
    }

    private void checkExtractors(MIDIFeatureExtractor[] allFeatureExtractors) throws Exception {
        // Verify that feature names referred to by all dependencies actually exist.
        for (MIDIFeatureExtractor featureExtractor : allFeatureExtractors) {
            String[] thisFeatureDependencies = featureExtractor.getDepenedencies();
            if (null == thisFeatureDependencies) {
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
                        featureExtractor.getName() +
                        " feature needs the " + dependency +
                        " feature in order to be calculated, yet no feature with the latter name could be found.");
                }
            }
        }
    }

    private void checkAtLeastOneFlagToSaveWasSelected(boolean[] featuresToSaveAmongAll) throws Exception {
        for (boolean featureToSaveFlag : featuresToSaveAmongAll) {
            if (featureToSaveFlag) {
                return;
            }
        }

        throw new Exception("No features have been set to be saved.");
    }

    private void checkWindowFlags(double windowSize, double windowOverlap, boolean saveFeaturesForEachWindow, boolean saveOverallRecordingFeatures) throws Exception {
        // Throw an exception if the control parameters are invalid
        if (!saveFeaturesForEachWindow && !saveOverallRecordingFeatures)
            throw new Exception("""
                    You must save at least one of the windows-based
                    features and the overall file-based features if
                    windows are to be used.""");
        if (0.0 > windowOverlap || 1.0 <= windowOverlap)
            throw new Exception("""
                    Window overlap fraction is %s.
                    This value must be 0.0 or above and less than 1.0.""".formatted(windowOverlap));
        if (0.0 > windowSize)
            throw new Exception("""
                    Window size is %s.
                    This value must be at or above 0.0 seconds.""".formatted(windowSize));
    }

    /**
     * @return True if it has features for MEI files.
     */
    public boolean containsMeiFeatures() {
        return false;
    }

    /**
     * Extract the features from the provided MeiSequence.
     *
     * @param name               How to name sequence in DataBoard.
     * @param sequence
     * @param meiSpecificStorage
     * @throws Exception When an unforeseen runtime exception occurs.
     */
    public void extractFeaturesFromSequence(String name, Sequence sequence, MeiSpecificStorage meiSpecificStorage)
            throws Exception {
        // Extract the data from the file and check for exceptions
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
        Map<String, double[][]> windowFeatureValues = getFeatures(windows, meiSpecificStorage);
        // Find the feature averages and standard deviations if appropriate
        Map<String, double[]> overallFeatureValues = null;
        if (saveOverallRecordingFeatures) {
            overallFeatureValues = generateOverallRecordingFeatures(windowFeatureValues);
        }
        addDataSet(windowFeatureValues, name, overallFeatureValues, startTicks, endTicks, secondsPerTick);
    }

    private static class Worker extends CountedCompleter<Void> {
        private final List<Sequence> sequences;
        private final List<MIDIIntermediateRepresentations> representations;
        private final ConcurrentMap<String, double[][]> map;
        private final MIDIFeatureExtractor featureExtractor;

        private Worker(CountedCompleter<?> completer,
            List<Sequence> sequences,
            List<MIDIIntermediateRepresentations> representations,
            ConcurrentMap<String, double[][]> map,
            MIDIFeatureExtractor featureExtractor) {
            super(completer);
            this.sequences = sequences;
            this.representations = representations;
            this.map = map;
            this.featureExtractor = featureExtractor;
        }

        @Override
        public void compute() {
            List<Worker> workers = new ArrayList<>();
            for (String dependency : ArrayUtils.nullToEmpty(featureExtractor.getDepenedencies())) {
                workers.add(new Worker(this,
                    sequences,
                    representations,
                    map,
                    name2extractor.get(dependency)));
            }
            addToPendingCount(workers.size());
            workers.forEach(ForkJoinTask::fork);
            tryComplete();
        }

        @Override
        public void onCompletion(CountedCompleter<?> caller) {
            double[][] results = new double[sequences.size()][];
            for (int i = 0; i < representations.size(); i++) {
                double[][][] dependencies = new double[sequences.size()][ArrayUtils.nullToEmpty(featureExtractor.getDepenedencies()).length][];
                for (int j = 0; j < ArrayUtils.nullToEmpty(featureExtractor.getDepenedencies()).length; j++) {
                    String dependency = featureExtractor.getDepenedencies()[j];
                    dependencies[i][j] = map.get(dependency)[i];
                }
                try {
                    results[i] = featureExtractor.extractFeature(sequences.get(i),
                        representations.get(i),
                        dependencies[i]);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            map.putIfAbsent(featureExtractor.getName(), results);
        }
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
    public Map<String, double[][]> getFeatures(Sequence[] windows, MeiSpecificStorage meiSpecificStorage) throws Exception {
        // Extract features from each window one by one and add save the results.
        // The last window is zero-padded at the end if it falls off the edge of the
        // provided samples.
        List<MIDIIntermediateRepresentations> representations = new ArrayList<>(windows.length);
        for (int win = 0; win < windows.length; win++) {
            // Extract information from sequence that is needed to extract features
            MIDIIntermediateRepresentations intermediate = new MIDIIntermediateRepresentations(windows[win]);
            representations.add(intermediate);
        }

        ForkJoinPool pool = ForkJoinPool.commonPool();

        ConcurrentHashMap<String, double[][]> resultsMap = new ConcurrentHashMap<>();

        List<ForkJoinTask<Void>> forkJoinTasks = featureExtractorsNamesToSave.stream()
            .map(featureName -> new Worker(null, Arrays.asList(windows), representations, resultsMap, name2extractor.get(featureName)))
            .map(pool::submit)
            .toList();

        forkJoinTasks.forEach(voidForkJoinTask -> {
            try {
                voidForkJoinTask.get();
            }
            catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        });

        return resultsMap;
    }

    /**
     * Generates DataBoard based in list of DataSets that were constructed during features extractions
     *
     * @return DataBoard of processed recordings
     * @throws Exception Throws an exception if it cannot create DataBoard object.
     */
    public DataBoard generateDataBoard() throws Exception {
        DataBoard dataBoard = new DataBoard(null, null, dataSets.toArray(new DataSet[0]), null);
        if (saveOverallRecordingFeatures) {
            dataBoard.feature_definitions = IntStream.range(0, featureExtractorsDefinitions.length)
                    .mapToObj(i -> featureExtractorsDefinitions[i])
                    .toArray(FeatureDefinition[]::new);
        } else {
            dataBoard.feature_definitions = featureExtractorsNamesToSave.stream()
                .map(featureName -> name2extractor.get(featureName).getFeatureDefinition())
                .toArray(FeatureDefinition[]::new);
        }
        return dataBoard;
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
    private Map<String, double[]> generateOverallRecordingFeatures(Map<String, double[][]> windowFeatureValues) {
        Map<String, double[]> featureOverallValues = new HashMap<>(featureExtractorsNamesToSave.size() * 2);
        if (!saveFeaturesForEachWindow) {
            return featureOverallValues;
        }

        for (int feat = 0; feat < overallFeatureNames.size(); feat += 2) {
            String originalName = featureExtractorsNamesToSave.get(feat / 2);
            double[][] data = windowFeatureValues.get(originalName);

            int nonNullCnt = 0;

            double[] averages = new double[data[0].length];
            for (int i = 0; i < data.length; i++) {
                if (data[i] == null) {
                    continue;
                }
                nonNullCnt++;
                for (int j = 0; j < averages.length; j++) {
                    averages[j] += data[i][j];
                }
            }
            for (int i = 0; i < averages.length; i++) {
                averages[i] /= nonNullCnt;
            }

            double[] stdvs = Arrays.copyOf(averages, averages.length);
            for (int i = 0; i < data.length; i++) {
                for (int j = 0; j < averages.length; j++) {
                    stdvs[j] += Math.sqrt(data[i][j] - averages[j]);
                }
            }
            for (int i = 0; i < stdvs.length; i++) {
                stdvs[i] = Math.sqrt(stdvs[i] / (nonNullCnt - 1));
            }

            featureOverallValues.put(overallFeatureNames.get(feat), averages);
            featureOverallValues.put(overallFeatureNames.get(feat + 1), stdvs);
        }

        return featureOverallValues;
    }

    /**
     * Generates FeatureDefinitions for overall features
     *
     * @return array of overall FeatureDefinitions
     */
    private List<FeatureDefinition> generateOverallFeatureDefinitions() {
        List<FeatureDefinition> definitions = new ArrayList<>(featureExtractorsNamesToSave.size() * 2);
        for (String name: featureExtractorsNamesToSave) {
            // Make the definitions
            FeatureDefinition thisDef = name2extractor.get(name).getFeatureDefinition();
            FeatureDefinition averageDefinition = new FeatureDefinition(thisDef.name + " Overall Average", thisDef.description + "\nThis is the overall average over all windows.", thisDef.is_sequential, thisDef.dimensions);
            FeatureDefinition stdvDefinition = new FeatureDefinition(thisDef.name + " Overall Standard Deviation", thisDef.description + "\nThis is the overall standard deviation over all windows.", thisDef.is_sequential, thisDef.dimensions);
            // Store the results
            definitions.add(averageDefinition);
            definitions.add(stdvDefinition);
        }
        // Finalize the values
        return definitions;
    }

    /**
     * Generates DataSet for current recording and adds it in list of DataSets
     *
     * @param windowFeatureValues  The extracted window feature values
     *                             for this recording. The first
     *                             indice identifies the window, the
     *                             second identifies the feature and
     *                             the third identifies the feature
     *                             value. The third dimension will
     *                             be null if the given feature could
     *                             not be extracted for the given
     *                             window.
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
     * @param startTicks           The start ticks that correspond to each MIDI window.
     * @param endTicks             The end ticks that correspond to each MIDI window.
     * @param secondsPerTick       The number of seconds in a MIDI tick given by the sequence.
     */
    private void addDataSet(Map<String, double[][]> windowFeatureValues, String identifier,
                            Map<String, double[]> overallFeatureValues,
                            int[] startTicks,
                            int[] endTicks,
                            double[] secondsPerTick) {
        DataSet rootDataSet = new DataSet(identifier, null, Double.NaN, Double.NaN,
                null, null, null);

        if (!saveFeaturesForEachWindow) {

            rootDataSet.feature_values = new double[windowFeatureValues.size()][];
            rootDataSet.feature_names = new String[windowFeatureValues.size()];

            int cnt = 0;

            for (Map.Entry<String, double[][]> entry : windowFeatureValues.entrySet()) {
                rootDataSet.feature_values[cnt] = entry.getValue()[0];
                rootDataSet.feature_names[cnt] = entry.getKey();
                cnt++;
            }

            dataSets.add(rootDataSet);

            return;
        }

        rootDataSet.sub_sets = new DataSet[startTicks.length];

        for (int win = 0; win < startTicks.length; ++win) {
            double startTime = MIDIMethods.getSecondsAtTick(startTicks[win], secondsPerTick);
            //check for non-negative
            startTime = (startTime > 0) ? startTime : 0;
            double endTime = MIDIMethods.getSecondsAtTick(endTicks[win], secondsPerTick);

            DataSet windowDataSet = new DataSet(null, null, startTime, endTime, null, null, rootDataSet);

            windowDataSet.feature_values = new double[windowFeatureValues.size()][];
            windowDataSet.feature_names = new String[windowFeatureValues.size()];

            int cnt = 0;
            for (Map.Entry<String, double[][]> entry : windowFeatureValues.entrySet()) {
                windowDataSet.feature_values[cnt] = entry.getValue()[win];
                windowDataSet.feature_names[cnt] = entry.getKey();
                cnt++;
            }

            rootDataSet.sub_sets[win] = windowDataSet;
        }

        if (overallFeatureValues != null) {
            rootDataSet.feature_values = new double[windowFeatureValues.size()][];
            rootDataSet.feature_names = new String[windowFeatureValues.size()];

            int cnt = 0;
            for (Map.Entry<String, double[]> entry : overallFeatureValues.entrySet()) {
                rootDataSet.feature_values[cnt] = entry.getValue();
                rootDataSet.feature_names[cnt] = entry.getKey();
                cnt++;
            }
        }

        dataSets.add(rootDataSet);
    }
}