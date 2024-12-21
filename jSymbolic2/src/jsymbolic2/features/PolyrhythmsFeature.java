package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the number of beat histogram peaks with magnitudes at least 30% as high as
 * the magnitude of the highest peak, and whose bin labels are not integer multiples or factors (using only
 * multipliers of 1, 2, 3, 4, 6 and 8, and with an accepted error of +/- 3 bins) of the bin label of the peak
 * with the highest magnitude. This number is then divided by the total number of bins with frequencies over
 * 30% of the highest magnitude.
 *
 * @author Cory McKay
 */
public class PolyrhythmsFeature implements Feature {

    @Override()
    public String getName() {
        return "Polyrhythms";
    }

    @Override()
    public String getCode() {
        return "RT-29";
    }

    @Override()
    public String getDescription() {
        return "Number of beat histogram peaks with magnitudes at least 30% as high as the magnitude of the highest peak, and whose bin labels are not integer multiples or factors (using only multipliers of 1, 2, 3, 4, 6 and 8, and with an accepted error of +/- 3 bins) of the bin label of the peak with the highest magnitude. This number is then divided by the total number of bins with frequencies over 30% of the highest magnitude.";
    }

    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null) {
            // Find the number of sufficiently large peaks
            int count = 0;
            for (int bin = 0; bin < sequence_info.beat_histogram_thresholded_table.length; bin++) if (sequence_info.beat_histogram_thresholded_table[bin][2] > 0.001)
                count++;
            // Store the peak bins
            int[] peak_bins = new int[count];
            int so_far = 0;
            for (int bin = 0; bin < sequence_info.beat_histogram_thresholded_table.length; bin++) {
                if (sequence_info.beat_histogram_thresholded_table[bin][2] > 0.001) {
                    peak_bins[so_far] = bin;
                    so_far++;
                }
            }
            // Find the highest peak
            int highest_index = 0;
            double max_so_far = 0.0;
            for (int bin = 0; bin < peak_bins.length; bin++) {
                if (sequence_info.beat_histogram_thresholded_table[peak_bins[bin]][2] > max_so_far) {
                    max_so_far = sequence_info.beat_histogram_thresholded_table[peak_bins[bin]][2];
                    highest_index = peak_bins[bin];
                }
            }
            // Find the number of peak bins which are multiples or factors of the highest bin
            int hits = 0;
            for (int i = 0; i < peak_bins.length; i++) {
                int left_limit = peak_bins[i] - 3;
                if (left_limit < 0)
                    left_limit = 0;
                int right_limit = peak_bins[i] + 4;
                if (right_limit > sequence_info.beat_histogram_thresholded_table.length)
                    right_limit = sequence_info.beat_histogram_thresholded_table.length;
                int[] multipliers = { 1, 2, 3, 4, 6, 8 };
                for (int j = left_limit; j < right_limit; j++) {
                    if (mckay.utilities.staticlibraries.MathAndStatsMethods.isFactorOrMultiple(j, highest_index, multipliers)) {
                        hits++;
                        // exit loop
                        left_limit = right_limit + 1;
                    }
                }
            }
            // Calculate the feature value
            if (peak_bins.length == 0)
                value = 0.0;
            else
                value = (double) hits / (double) peak_bins.length;
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
