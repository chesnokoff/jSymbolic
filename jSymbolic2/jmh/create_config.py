import argparse
import os


class Command:
    OUTPUT_DIR = "."

    CONFIGS_DIR = "."

    WINDOW_SIZE = 0.0

    OVERLAP = 0.0

    ARFF = False

    CSV = False

    CSV = False

    SAVE_WINDOW = False

    SAVE_OVERALL = False

    INPUT_FILES = []

    def __init__(self, args):
        self.OUTPUT_DIR = os.path.relpath(os.path.normpath(args.output))
        self.CONFIGS_DIR = os.path.relpath(os.path.normpath(args.path))
        self.WINDOW_SIZE = args.window
        self.OVERLAP = args.overlap
        self.ARFF = args.arff
        self.CSV = args.csv
        self.SAVE_WINDOW = args.csv
        self.SAVE_OVERALL = args.csv
        self.INPUT_FILES = args.csv


HISTOGRAM_FEATURES = ["Basic Pitch Histogram",
                      "Pitch Class Histogram",
                      "Folded Fifths Pitch Class Histogram",
                      "Melodic Interval Histogram",
                      "Vertical Interval Histogram",
                      "Wrapped Vertical Interval Histogram",
                      "Chord Type Histogram",
                      "Rhythmic Value Histogram",
                      "Rhythmic Value Median Run Lengths Histogram",
                      "Rhythmic Value Variability in Run Lengths Histogram",
                      "Beat Histogram Tempo Standardized",
                      "Beat Histogram"]

FEATURES = ["Number of Pitches",
            "Number of Pitch Classes",
            "Number of Common Pitches",
            "Number of Common Pitch Classes",
            "Range",
            "Importance of Bass Register",
            "Importance of Middle Register",
            "Importance of High Register",
            "Dominant Spread",
            "Strong Tonal Centres",
            "Mean Pitch",
            "Mean Pitch Class",
            "Most Common Pitch",
            "Most Common Pitch Class",
            "Prevalence of Most Common Pitch",
            "Prevalence of Most Common Pitch Class",
            "Relative Prevalence of Top Pitches",
            "Relative Prevalence of Top Pitch Classes",
            "Interval Between Most Prevalent Pitches",
            "Interval Between Most Prevalent Pitch Classes",
            "Pitch Variability",
            "Pitch Class Variability",
            "Pitch Class Variability After Folding",
            "Pitch Skewness",
            "Pitch Class Skewness",
            "Pitch Class Skewness After Folding",
            "Pitch Kurtosis",
            "Pitch Class Kurtosis",
            "Pitch Class Kurtosis After Folding",
            "Major or Minor",
            "First Pitch",
            "First Pitch Class",
            "Last Pitch",
            "Last Pitch Class",
            "Glissando Prevalence",
            "Average Range of Glissandos",
            "Vibrato Prevalence",
            "Microtone Prevalence",
            "Most Common Melodic Interval",
            "Mean Melodic Interval",
            "Number of Common Melodic Intervals",
            "Distance Between Most Prevalent Melodic Intervals",
            "Prevalence of Most Common Melodic Interval",
            "Relative Prevalence of Most Common Melodic Intervals",
            "Amount of Arpeggiation",
            "Repeated Notes",
            "Chromatic Motion",
            "Stepwise Motion",
            "Melodic Thirds",
            "Melodic Perfect Fourths",
            "Melodic Tritones",
            "Melodic Perfect Fifths",
            "Melodic Sixths",
            "Melodic Sevenths",
            "Melodic Octaves",
            "Melodic Large Intervals",
            "Minor Major Melodic Third Ratio",
            "Melodic Embellishments",
            "Direction of Melodic Motion",
            "Average Length of Melodic Arcs",
            "Average Interval Spanned by Melodic Arcs",
            "Melodic Pitch Variety",
            "Average Number of Simultaneous Pitch Classes",
            "Variability of Number of Simultaneous Pitch Classes",
            "Average Number of Simultaneous Pitches",
            "Variability of Number of Simultaneous Pitches",
            "Most Common Vertical Interval",
            "Second Most Common Vertical Interval",
            "Distance Between Two Most Common Vertical Intervals",
            "Prevalence of Most Common Vertical Interval",
            "Prevalence of Second Most Common Vertical Interval",
            "Prevalence Ratio of Two Most Common Vertical Intervals",
            "Vertical Unisons",
            "Vertical Minor Seconds",
            "Vertical Thirds",
            "Vertical Tritones",
            "Vertical Perfect Fourths",
            "Vertical Perfect Fifths",
            "Vertical Sixths",
            "Vertical Sevenths",
            "Vertical Octaves",
            "Perfect Vertical Intervals",
            "Vertical Dissonance Ratio",
            "Vertical Minor Third Prevalence",
            "Vertical Major Third Prevalence",
            "Chord Duration",
            "Partial Chords",
            "Standard Triads",
            "Diminished and Augmented Triads",
            "Dominant Seventh Chords",
            "Seventh Chords",
            "Non-Standard Chords",
            "Complex Chords",
            "Minor Major Triad Ratio",
            "Initial Time Signature",
            "Simple Initial Meter",
            "Compound Initial Meter",
            "Complex Initial Meter",
            "Duple Initial Meter",
            "Triple Initial Meter",
            "Quadruple Initial Meter",
            "Metrical Diversity",
            "Total Number of Notes",
            "Note Density per Quarter Note",
            "Note Density per Quarter Note per Voice",
            "Note Density per Quarter Note Variability",
            "Range of Rhythmic Values",
            "Number of Different Rhythmic Values Present",
            "Number of Common Rhythmic Values Present",
            "Prevalence of Very Short Rhythmic Values",
            "Prevalence of Short Rhythmic Values",
            "Prevalence of Medium Rhythmic Values",
            "Prevalence of Long Rhythmic Values",
            "Prevalence of Very Long Rhythmic Values",
            "Prevalence of Dotted Notes",
            "Shortest Rhythmic Value",
            "Longest Rhythmic Value",
            "Mean Rhythmic Value",
            "Most Common Rhythmic Value",
            "Prevalence of Most Common Rhythmic Value",
            "Relative Prevalence of Most Common Rhythmic Values",
            "Difference Between Most Common Rhythmic Values",
            "Rhythmic Value Variability",
            "Rhythmic Value Skewness",
            "Rhythmic Value Kurtosis",
            "Mean Rhythmic Value Run Length",
            "Median Rhythmic Value Run Length",
            "Variability in Rhythmic Value Run Lengths",
            "Mean Rhythmic Value Offset",
            "Median Rhythmic Value Offset",
            "Variability of Rhythmic Value Offsets",
            "Complete Rests Fraction",
            "Partial Rests Fraction",
            "Average Rest Fraction Across Voices",
            "Longest Complete Rest",
            "Longest Partial Rest",
            "Mean Complete Rest Duration",
            "Mean Partial Rest Duration",
            "Median Complete Rest Duration",
            "Median Partial Rest Duration",
            "Variability of Complete Rest Durations",
            "Variability of Partial Rest Durations",
            "Variability Across Voices of Combined Rests",
            "Number of Strong Rhythmic Pulses - Tempo Standardized",
            "Number of Moderate Rhythmic Pulses - Tempo Standardized",
            "Number of Relatively Strong Rhythmic Pulses - Tempo Standardized",
            "Strongest Rhythmic Pulse - Tempo Standardized",
            "Second Strongest Rhythmic Pulse - Tempo Standardized",
            "Harmonicity of Two Strongest Rhythmic Pulses - Tempo Standardized",
            "Strength of Strongest Rhythmic Pulse - Tempo Standardized",
            "Strength of Second Strongest Rhythmic Pulse - Tempo Standardized",
            "Strength Ratio of Two Strongest Rhythmic Pulses - Tempo Standardized",
            "Combined Strength of Two Strongest Rhythmic Pulses - Tempo Standardized",
            "Rhythmic Variability - Tempo Standardized",
            "Rhythmic Looseness - Tempo Standardized",
            "Polyrhythms - Tempo Standardized",
            "Initial Tempo",
            "Mean Tempo",
            "Tempo Variability",
            "Duration in Seconds",
            "Note Density",
            "Note Density Variability",
            "Average Time Between Attacks",
            "Average Time Between Attacks for Each Voice",
            "Variability of Time Between Attacks",
            "Average Variability of Time Between Attacks for Each Voice",
            "Minimum Note Duration",
            "Maximum Note Duration",
            "Average Note Duration",
            "Variability of Note Durations",
            "Amount of Staccato",
            "Number of Strong Rhythmic Pulses",
            "Number of Moderate Rhythmic Pulses",
            "Number of Relatively Strong Rhythmic Pulses",
            "Strongest Rhythmic Pulse",
            "Second Strongest Rhythmic Pulse",
            "Harmonicity of Two Strongest Rhythmic Pulses",
            "Strength of Strongest Rhythmic Pulse",
            "Strength of Second Strongest Rhythmic Pulse",
            "Strength Ratio of Two Strongest Rhythmic Pulses",
            "Combined Strength of Two Strongest Rhythmic Pulses",
            "Rhythmic Variability",
            "Rhythmic Looseness",
            "Polyrhythms",
            "Pitched Instruments Present",
            "Unpitched Instruments Present",
            "Note Prevalence of Pitched Instruments",
            "Note Prevalence of Unpitched Instruments",
            "Time Prevalence of Pitched Instruments",
            "Variability of Note Prevalence of Pitched Instruments",
            "Variability of Note Prevalence of Unpitched Instruments",
            "Number of Pitched Instruments",
            "Number of Unpitched Instruments",
            "Unpitched Percussion Instrument Prevalence",
            "String Keyboard Prevalence",
            "Acoustic Guitar Prevalence",
            "Electric Guitar Prevalence",
            "Violin Prevalence",
            "Saxophone Prevalence",
            "Brass Prevalence",
            "Woodwinds Prevalence",
            "Orchestral Strings Prevalence",
            "String Ensemble Prevalence",
            "Electric Instrument Prevalence",
            "Maximum Number of Independent Voices",
            "Average Number of Independent Voices",
            "Variability of Number of Independent Voices",
            "Voice Equality - Number of Notes",
            "Voice Equality - Note Duration",
            "Voice Equality - Dynamics",
            "Voice Equality - Melodic Leaps",
            "Voice Equality - Range",
            "Importance of Loudest Voice",
            "Relative Range of Loudest Voice",
            "Relative Range Isolation of Loudest Voice",
            "Relative Range of Highest Line",
            "Relative Note Density of Highest Line",
            "Relative Note Durations of Lowest Line",
            "Relative Size of Melodic Intervals in Lowest Line",
            "Voice Overlap",
            "Voice Separation",
            "Variability of Voice Separation",
            "Parallel Motion",
            "Similar Motion",
            "Contrary Motion",
            "Oblique Motion",
            "Parallel Fifths",
            "Parallel Octaves",
            "Dynamic Range",
            "Variation of Dynamics",
            "Variation of Dynamics In Each Voice",
            "Average Note to Note Change in Dynamics"]


def tree(directory):
    files = []
    for dirpath, _, filenames in os.walk(directory):
        for f in filenames:
            files.append(os.path.relpath(os.path.join(dirpath, f)))
    return files


def generate_part_of_config(command):
    options = f"""<jSymbolic_options>
window_size={command.WINDOW_SIZE}
window_overlap={command.OVERLAP}
save_features_for_each_window={str(command.SAVE_WINDOW).lower()}
save_overall_recording_features={str(command.SAVE_OVERALL).lower()}
convert_to_arff={str(command.ARFF).lower()}
convert_to_csv={str(command.CSV).lower()}"""
    input_files = "<input_files>\n"
    for path in args.input:
        input_files += '\n'.join(tree(path))
    return '\n'.join([options, input_files])


def get_args():
    parser = argparse.ArgumentParser(
        prog='Configfiles creator',
        description='Creates new configfiles based on arguments',
        epilog='Text at the bottom of help')
    parser.add_argument("-w", "--window", type=float, default=0.0, help="Size of window")
    parser.add_argument("--overlap", type=float, default=0.0, help="Overlap of window")
    parser.add_argument("--arff", type=bool, default=False, help="Boolean save to arff")
    parser.add_argument("--csv", type=bool, default=False, help="Boolean to csv")
    parser.add_argument("--save-window", type=bool, default=False,
                        help="Save features for each window")
    parser.add_argument("--save-overall", type=bool, default=True, help="Save overall features")
    parser.add_argument("-o", "--output", type=str, default='.',
                        help="Directory where to save results of feature extraction")
    parser.add_argument("-i", "--input", action="append", required=True,
                        help="Input files to process")
    parser.add_argument("-p", "--path", type=str, default='.',
                        help="Directory where to save config files")
    return parser.parse_args()


def generate_config_with_all_features():
    base_name = "all-features"
    output = f"""<output_files>
feature_values_save_path={os.path.normpath(args.output) + "/" + base_name + "-values.xml"}
feature_definitions_save_path={os.path.normpath(args.output) + "/" + base_name + "-definitions.xml"}"""
    features = "<features_to_extract>" + '\n' + '\n'.join(FEATURES) + '\n' + '\n'.join(
        HISTOGRAM_FEATURES)
    with open(os.path.normpath(args.path) + '/' + base_name + "-config.txt", 'w+') as file:
        file.write(config + '\n' + output + '\n' + features)


def generate_config_no_histograms():
    base_name = "all-features-no-hist"
    output = f"""<output_files>
feature_values_save_path={os.path.normpath(args.output) + "/" + base_name + "-values.xml"}
feature_definitions_save_path={os.path.normpath(args.output) + "/" + base_name + "-definitions.xml"}"""
    features = "<features_to_extract>" + '\n' + '\n'.join(FEATURES)
    with open(os.path.normpath(args.path) + '/' + base_name + "-config.txt", 'w+') as file:
        file.write(config + '\n' + output + '\n' + features)


def generate_config_for_feature(feature_name):
    base_name = feature_name.replace(" ", "-")
    output = f"""<output_files>
feature_values_save_path={os.path.normpath(args.output) + "/" + base_name + "-values.xml"}
feature_definitions_save_path={os.path.normpath(args.output) + "/" + base_name + "-definitions.xml"}"""
    features = "<features_to_extract>" + '\n' + feature_name
    with open(os.path.normpath(args.path) + '/' + base_name + "-config.txt", 'w+') as file:
        file.write(config + '\n' + output + '\n' + features)


if __name__ == "__main__":
    args = get_args()
    command = Command(args)
    config = generate_part_of_config(command)
    generate_config_with_all_features()
    generate_config_no_histograms()
    for feature in FEATURES:
        generate_config_for_feature(feature)
