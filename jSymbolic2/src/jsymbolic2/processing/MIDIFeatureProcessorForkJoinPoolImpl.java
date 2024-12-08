//package jsymbolic2.processing;
//
//import ace.datatypes.DataBoard;
//import ace.datatypes.FeatureDefinition;
//import jsymbolic2.featureutils.MIDIFeatureExtractor;
//import org.ddmal.jmei2midi.meielements.meispecific.MeiSpecificStorage;
//
//import javax.sound.midi.Sequence;
//
//public class MIDIFeatureProcessorForkJoinPoolImpl implements MIDIFeatureProcessorForkJoinPool {
//
//    public MIDIFeatureProcessorForkJoinPoolImpl(WindowInfo windowInfo,
//                                                MIDIFeatureExtractor[] allFeatureExtractors,
//                                                boolean[] featuresToSaveAmongAll,
//                                                boolean saveOverallRecordingFeatures) throws Exception {
//        checkWindowFlags(windowInfo.window_size(), windowInfo.window_overlap(), windowInfo.save_features_for_each_window(), saveOverallRecordingFeatures);
//        checkAtLeastOneFlagToSaveWasSelected(featuresToSaveAmongAll);
//        checkExtractors(allFeatureExtractors);
//
//        // Save parameters as fields
//        windowSize = windowInfo.window_size();
//        saveFeaturesForEachWindow = windowInfo.save_features_for_each_window();
//        this.saveOverallRecordingFeatures = saveOverallRecordingFeatures;
//
//        // Calculate the window offset
//        windowOverlapOffset = windowInfo.window_overlap() * windowSize;
//        if (windowOverlapOffset > windowSize)
//            throw new Exception("Window overlap offset is greater than window size, this is not possible.");
//        // Find which features need to be extracted and in what order. Also find
//        // the indices of dependencies and the maximum offsets for each feature.
//        findAndOrderFeaturesToExtract(allFeatureExtractors, featuresToSaveAmongAll);
//
//        featureExtractorsDefinitions = new FeatureDefinition[allFeatureExtractors.length];
//        featureExtractorsNames = new String[allFeatureExtractors.length];
//        for (int feat = 0; feat < featureExtractorsDefinitions.length; ++feat) {
//            featureExtractorsDefinitions[feat] = allFeatureExtractors[feat].getFeatureDefinition();
//            featureExtractorsNames[feat] = featureExtractorsDefinitions[feat].name;
//        }
//        overallFeatureDefinitions = generateOverallFeatureDefinitions();
//        overallFeatureNames = new String[overallFeatureDefinitions.length];
//        for (int feat = 0; feat < overallFeatureDefinitions.length; ++feat)
//            overallFeatureNames[feat] = overallFeatureDefinitions[feat].name;
//    }
//
//    @Override
//    public DataBoard generateDataBoard() throws Exception {
//        return null;
//    }
//
//    @Override
//    public double[][][] getFeatures(Sequence[] windows, MeiSpecificStorage meiSpecificStorage) throws Exception {
//        return new double[0][][];
//    }
//
//    @Override
//    public void extractFeaturesFromSequence(String name, Sequence sequence, MeiSpecificStorage meiSpecificStorage) throws Exception {
//
//    }
//
//    @Override
//    public boolean containsMeiFeatures() {
//        return false;
//    }
//
//    @Override
//    public MIDIFeatureExtractor[] getFinalFeaturesToBeExtracted() {
//        return new MIDIFeatureExtractor[0];
//    }
//}
