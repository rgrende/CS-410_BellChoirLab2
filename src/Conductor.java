//imports

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.AudioFormat;
import java.util.Map;

/**
 * Class Description: This is the Conductor class. The conductor is in charge of checking
 * and telling the members of the choir when to play and what note to play. I decided to use a Map
 * as my main data structure when keeping track of what note is playing and what member is assigned
 * to play that note.
 */

public class Conductor {
    //class variables
    private final Map<Note, Choir> choirMembers;
    public final AudioFormat af;

    /**
     * In the constructor, I have the map that requires both a note, and a choir member.
     * There was thought that if the audio format method was given another channel, harmony could be achieved.
     */
    Conductor(Map<Note, Choir> choirMembers) {
        this.choirMembers = choirMembers;
        //go back and give it another channel!!
        af = new AudioFormat(Note.SAMPLE_RATE, 8, 1, true, false);
    }

    /**
     * playTheSong method opens the lines of each file and reads the note and note length.
     * As each line is being read, a member of the choir is assigned a single note to play.
     * Once the line has been read, another choir member gets to take turn to play the next note.
     */
    public void playTheSong(Song song) throws LineUnavailableException {
        if (song.valid()) {
            try (final SourceDataLine line = AudioSystem.getSourceDataLine(af)) {
                //opens the line to read
                line.open();
                line.start();
                for (BellNote bn : song.getNotes()) {
                    Note note = bn.note;
                    NoteLength length = bn.length;
                    choirMembers.get(note).takeTurn(length, line);
                }
                //drains the line after each read
                line.drain();
            } catch (LineUnavailableException e) {
                System.out.println(song.getName() + " is not working...");
                System.exit(-1);
                //exit the code if problem occurs here
            }
        } else {
            System.out.println("The selected song is not valid. Please try another song.");
        }
    }
}