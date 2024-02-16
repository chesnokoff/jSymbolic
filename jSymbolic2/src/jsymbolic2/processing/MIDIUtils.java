package jsymbolic2.processing;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.Sequence;
import javax.sound.midi.Track;

public class MIDIUtils {
    public static final int DESIRED_MAX_PPQN = 1920;
    public static final int DESIRED_MAX_TICK_LENGTH = 5_000_000;
    // maximum 1h length
    public static final long DESIRED_MAX_MICROSECONDS_LENGTH = 60 * 60 * 1_000_000L;


    public static Sequence checkAndLowerResolution(Sequence midiSequence) throws Exception {
        if (midiSequence.getTickLength() > DESIRED_MAX_TICK_LENGTH) {
            if (midiSequence.getResolution() > DESIRED_MAX_PPQN) {
                return changeResolution(midiSequence, DESIRED_MAX_PPQN);
            }
            if (midiSequence.getMicrosecondLength() > DESIRED_MAX_MICROSECONDS_LENGTH) {
                throw new Exception(String.format("Midi is too long! Ticks: %d, Seconds: %f",
                        midiSequence.getTickLength(),
                        midiSequence.getMicrosecondLength() / 1_000_000.0));
            }
        }
        return midiSequence;
    }

    /**
     * Change resolution (TPQN) and retiming events.
     *
     * @param sourceSeq  Sequence to be processed.
     * @param resolution Ticks per quarter note of new sequence.
     * @return New sequence with new resolution.
     * @throws InvalidMidiDataException throw if MIDI data is invalid.
     */
    public static Sequence changeResolution(Sequence sourceSeq, int resolution) throws InvalidMidiDataException {
        // sequence must be tick-based
        if (sourceSeq.getDivisionType() != Sequence.PPQ) {
            //throw new UnsupportedOperationException("SMPTE is not supported.");
            return sourceSeq;
        }

        Sequence seq = new Sequence(sourceSeq.getDivisionType(), resolution);
        double timingRate = (double) resolution / sourceSeq.getResolution();

        // process all input tracks
        for (int trackIndex = 0; trackIndex < sourceSeq.getTracks().length; trackIndex++) {
            Track sourceTrack = sourceSeq.getTracks()[trackIndex];
            Track track = seq.createTrack();

            // process all events
            for (int eventIndex = 0; eventIndex < sourceTrack.size(); eventIndex++) {
                MidiEvent sourceEvent = sourceTrack.get(eventIndex);
                MidiEvent event = new MidiEvent(sourceEvent.getMessage(), Math.round(sourceEvent.getTick() * timingRate));
                track.add(event);
            }
        }

        // if the target resolution is shorter than source resolution,
        // events at different timing might be located at the same timing.
        // As a result, there might be zero-length note and/or
        // same control changes at the same timing.
        //
        // Probably, they should be removed for better conversion.
        // I do not remove them anyway at the moment,
        // because it does not cause any major problems.

        return seq;
    }

}
