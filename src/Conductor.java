//imports
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.AudioFormat;
import java.util.Map;

//Class Description:

public class Conductor {
    private final Map<Note, Choir> choirMembers; //this
    public final AudioFormat af;


    Conductor(Map<Note, Choir> choirMembers) {
        this.choirMembers = choirMembers;
        //go back and give it another channel!!
        af = new AudioFormat(Note.SAMPLE_RATE, 8, 1, true, false);
    }

    public void playTheSong(Song song) throws LineUnavailableException {
        try (final SourceDataLine line = AudioSystem.getSourceDataLine(af)) {
            for (BellNote bn : song.getNotes()) {
                Note note = bn.note;
                NoteLength length = bn.length;
                choirMembers.get(note).takeTurn(length);
            }
            //set line to null
        } catch (LineUnavailableException e) {
            System.out.println(song.getName() + " not working...");
        }
    }
}

//add in playSong a for loop for each of the members and sets the line. So it knows what line they play
//set to null after it gets done playing
//after try