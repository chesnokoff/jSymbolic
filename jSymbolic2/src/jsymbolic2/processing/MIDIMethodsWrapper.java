package jsymbolic2.processing;

import mckay.utilities.sound.midi.MIDIMethods;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Track;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class MIDIMethodsWrapper {
    public static Sequencer playMIDISequence(Sequence midi_sequence) throws Exception {
        return MIDIMethods.playMIDISequence(midi_sequence);
    }

    public static String getMIDIFileFormatData(File file) throws Exception {
        return MIDIMethods.getMIDIFileFormatData(file);
    }

    public static double[] getSecondsPerTick(Sequence sequence) {
        return MIDIMethods.getSecondsPerTick(sequence);
    }

    public static double getSecondsAtTick(int at_tick, double[] seconds_per_tick) {
        return MIDIMethods.getSecondsAtTick(at_tick, seconds_per_tick);
    }

    public static Sequence[] breakSequenceIntoWindows(Sequence original_sequence, double window_duration, double window_overlap_offset, int[] window_start_ticks, int[] window_end_ticks) throws Exception {
        return MIDIMethods.breakSequenceIntoWindows(original_sequence, window_duration, window_overlap_offset, window_start_ticks, window_end_ticks);
    }

    public static void checkMetaMessages(MidiEvent thisEvent, int track_index) {
        MIDIMethods.checkMetaMessages(thisEvent, track_index);
    }

    public static List<int[]> getStartEndTickArrays(Sequence original_sequence, double window_duration, double window_overlap_offset, double[] seconds_per_tick) throws Exception {
        if (original_sequence.getDivisionType() != 0.0F) {
            throw new Exception("The specified MIDI sequence uses SMPTE time encoding.\nOnly PPQ time encoding is accepted here.");
        } else if ((double) original_sequence.getTickLength() > 2.147483646E9) {
            throw new Exception("The MIDI sequence could not be processed because it is too long.");
        } else {
            List<Integer> window_start_ticks_list = new LinkedList<>();
            List<Integer> window_end_ticks_list = new LinkedList<>();
            double total_duration = (double) original_sequence.getMicrosecondLength() / 1000000.0;
            double time_interval_to_next_tick = window_duration - window_overlap_offset;
            boolean found_next_tick = false;
            int tick_of_next_beginning = 0;
            int this_tick = 0;

            double seconds_accumulated_so_far;
            for (double total_seconds_accumulated_so_far = 0.0; total_seconds_accumulated_so_far <= total_duration && this_tick < seconds_per_tick.length; total_seconds_accumulated_so_far += seconds_accumulated_so_far - window_overlap_offset) {
                window_start_ticks_list.add(this_tick);
                if (window_start_ticks_list.size() > 2 && Objects.equals(window_start_ticks_list.get(0), window_start_ticks_list.get(1))) {
                    throw new RuntimeException("Could not find start and end ticks");
                }
                seconds_accumulated_so_far = 0.0;

                while (seconds_accumulated_so_far < window_duration && this_tick < seconds_per_tick.length) {
                    seconds_accumulated_so_far += seconds_per_tick[this_tick];
                    ++this_tick;
                    if (!found_next_tick && seconds_accumulated_so_far >= time_interval_to_next_tick) {
                        tick_of_next_beginning = this_tick;
                        found_next_tick = true;
                    }
                }

                window_end_ticks_list.add(this_tick - 1);
                if (window_end_ticks_list.size() > 2 && Objects.equals(window_end_ticks_list.get(0), window_end_ticks_list.get(1))) {
                    throw new RuntimeException("Could not find start and end ticks");
                }

                if (found_next_tick) {
                    this_tick = tick_of_next_beginning;
                }

                found_next_tick = false;
            }

            List<int[]> startEndTickList = new ArrayList<>();
            int[] start_tick_array = Arrays.stream(window_start_ticks_list.toArray(new Integer[0])).mapToInt((i) -> i).toArray();
            int[] end_tick_array = Arrays.stream(window_end_ticks_list.toArray(new Integer[0])).mapToInt((i) -> i).toArray();
            startEndTickList.add(start_tick_array);
            startEndTickList.add(end_tick_array);
            return startEndTickList;
        }
    }

    public static void passEventToAllAppropriateWindows(MidiEvent thisEvent, double window_overlap_offset, int sequence_index, int startTick, int track_index, int[] window_start_ticks, int[] window_end_ticks, Track[][] windowed_tracks) {
        MIDIMethods.passEventToAllAppropriateWindows(thisEvent, window_overlap_offset, sequence_index, startTick, track_index, window_start_ticks, window_end_ticks, windowed_tracks);
    }
}
