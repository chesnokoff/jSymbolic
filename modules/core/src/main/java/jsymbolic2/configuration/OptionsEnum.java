package jsymbolic2.configuration;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * All the headers (i.e. sections) required by the jSymbolic configuration file.
 *
 * @author Tristano Tenaglia
 */
public enum OptionsEnum {
    window_size,
    window_overlap,
    save_features_for_each_window,
    save_overall_recording_features,
    convert_to_arff,
    convert_to_csv;

    /**
     * Set where all enum elements are stored for easy lookup.
     */
    private static final Set<String> layerChildHashNames = new HashSet<>();

    static {
        for (OptionsEnum value : EnumSet.allOf(OptionsEnum.class)) {
            OptionsEnum.layerChildHashNames.add(value.name());
        }
    }

    /**
     * Check to see if this enum is associated to the appropriate value type.
     *
     * @param name Name of mei element to be checked
     * @return true if the given name is part of this enum
     */
    public static boolean contains(String name) {
        return OptionsEnum.layerChildHashNames.contains(name);
    }

    /**
     * @param values Compare all the enum names to the names given in the values.
     * @return True if all the names are in fact in the input values list, otherwise false.
     */
    public static boolean allOptionsExist(List<String> values) {
        for (String s : OptionsEnum.layerChildHashNames) {
            if (!values.contains(s)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check to ensure that the value type  matches the string representation of the value passed in
     * and the corresponding enum type specified in the switch statement.
     *
     * @param value The value that needs to be compared to.
     * @return True if the type of the passed in value matches the corresponding enum type,
     * otherwise if its not one of the enum types, then false.
     */
    public boolean checkValue(String value) {
        return switch (this) {
            case window_size ->
                //Greater than 0.1 as per the GUI checks
                    value.matches("\\d*\\.?\\d*") && (0.0 <= Double.parseDouble(value));
            case window_overlap ->
                //Between 0 and 1 (not inclusive) as per the GUI checks
                    value.matches("0?.\\d*") && (0 <= Double.parseDouble(value)) && (1.0 > Double.parseDouble(value));
            case save_features_for_each_window -> OptionsEnum.isBoolean(value);
            case save_overall_recording_features -> OptionsEnum.isBoolean(value);
            case convert_to_arff -> OptionsEnum.isBoolean(value);
            case convert_to_csv -> OptionsEnum.isBoolean(value);
            default -> false;
        };
    }

    /**
     * @param value The value string that needs to be checked.
     * @return True if the value passed in is either the string "true" or "false", otherwise false.
     */
    private static boolean isBoolean(String value) {
        return "true".equals(value) || "false".equals(value);
    }
}
