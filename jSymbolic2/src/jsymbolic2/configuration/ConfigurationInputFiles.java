package jsymbolic2.configuration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A List for both the valid and invalid input files in the configuration file.
 *
 * @author Tristano Tenaglia
 */
public class ConfigurationInputFiles {
    private List<File> validFiles;
    private List<File> invalidFiles;

    public ConfigurationInputFiles() {
        validFiles = new ArrayList<>();
        invalidFiles = new ArrayList<>();
    }

    public List<File> getInvalidFiles() {
        return invalidFiles;
    }

    public List<File> getValidFiles() {
        return validFiles;
    }

    public void addInvalidFile(File file) {
        invalidFiles.add(file);
    }

    public void addValidFile(File file) {
        validFiles.add(file);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (null == o || getClass() != o.getClass()) return false;

        ConfigurationInputFiles that = (ConfigurationInputFiles) o;

        if (!Objects.equals(validFiles, that.validFiles)) return false;
        return Objects.equals(invalidFiles, that.invalidFiles);

    }

    @Override
    public int hashCode() {
        int result = null != validFiles ? validFiles.hashCode() : 0;
        result = 31 * result + (null != invalidFiles ? invalidFiles.hashCode() : 0);
        return result;
    }
}
