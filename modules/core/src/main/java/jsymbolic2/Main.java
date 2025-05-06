package jsymbolic2;

import jsymbolic2.processing.cli.CLI;

/**
 * The jSymbolic runnable class. See the README or manual for more details on jSymbolic.
 *
 * @author Cory McKay and Tristano Tenaglia
 */
public class Main {
    /**
     * Runs the jSymbolic Feature Extractor. Operation will take place either in GUI or entirely via command
     * line processing, depending on the provided command line arguments.
     *
     * @param args Command line input parameter arguments.
     */
    public static void main(String[] args) {
        new CLI().run(args);
    }
}