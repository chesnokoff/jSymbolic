package jsymbolic2.processing;

public record SaveInfo(String feature_values_save_path, String feature_definitions_save_path,
                       boolean save_overall_recording_features, boolean save_arff_file, boolean save_csv_file) {
}