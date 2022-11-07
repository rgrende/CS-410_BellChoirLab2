import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

//Class Description: This is the Tone class that loads notes into the program.
//This

public class Tone {

    // Mary had a little lamb
    private static List<BellNote> loadNotes(String filename) {
        final List<BellNote> song = new ArrayList<>();
        final File file = new File(filename);
        if (file.exists()) {
            try (FileReader fileReader = new FileReader(file);
                 BufferedReader br = new BufferedReader(fileReader)) {
                String line;
                while ((line = br.readLine()) != null) {

                    final BellNote bn = parseBellNote(line);
                    if (bn != null) {
                        song.add(bn);
                    } else {
                        System.err.println("Error: Invalid note '" + line + "'");
                    }
                }
            } catch (IOException ignored) {
            }
        } else {
            System.err.println("File '" + filename + "' not found");
        }
        return song;
    }

    private static BellNote parseBellNote(String line) {
        final String[] fields = line.split("\\s+");
        if (fields.length == 2) {
            Note note = parseNote(fields[0]);
            if (note == null) {
                return null;
            }
            NoteLength length = parseNoteLength(fields[1]);
            if (length == null) {
                return null;
            }
            return new BellNote(note, length);
        }
        return null;
    }

    private static Note parseNote(String note) {
        // If you give me garbage, I'll give it back
        if (note == null) {
            return null;
        }
        for (Note validNote : Note.values()) {
            if (validNote.name().equals(note.toUpperCase().trim())) {
                return validNote;
            }
        }
        return null;
    }

    private static NoteLength parseNoteLength(String length) {
        switch (length) {
            case "1":
                //return Integer.parseNoteLength(length);
                return NoteLength.WHOLE;

            case "2":
                return NoteLength.HALF;

            case "4":
                return NoteLength.QUARTER;

            case "8":
                return NoteLength.EIGTH;

            case "16":
                return NoteLength.SIXTEENTH;

            default:
                return null;
        }
    }

    private final AudioFormat af;

    Tone(AudioFormat af) {
        this.af = af;
    }

    void playSong(List<BellNote> song) throws LineUnavailableException {
        System.out.println("Play song. Song length =" + song.size());
        try (final SourceDataLine line = AudioSystem.getSourceDataLine(af)) {
            line.open();
            line.start();

            for (BellNote bn : song) {
                playNote(line, bn);
            }
            line.drain();
        }
    }

    private void playNote(SourceDataLine line, BellNote bn) {
        final int ms = Math.min(bn.length.timeMs(), Note.MEASURE_LENGTH_SEC * 1000);
        final int length = Note.SAMPLE_RATE * ms / 1000;
        line.write(bn.note.sample(), 0, length);
        line.write(Note.REST.sample(), 0, 50);
    }
}
