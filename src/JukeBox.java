//imports
import javax.management.NotificationEmitter;
import javax.sound.sampled.AudioFormat;
import java.io.File;
import java.util.*;
//sout
//psvm

//Class Description:

public class JukeBox {
    public boolean playing = true;

    private static Map<Note, Choir> getMembers(List<Song> songs) {
        LinkedList<Note> usedNotes = new LinkedList<Note>();
        HashMap<Note, Choir> choirMembers = new HashMap<Note, Choir>();
        for (Song song : songs) {
            for (BellNote bn : song.getNotes()) {
                Note note = bn.note;
                if (!usedNotes.contains(note)) {
                    usedNotes.add(note);
                    choirMembers.put(note, new Choir(note));
                }
            }
        }
        return choirMembers;
    }

    public static void main(String[] args) throws Exception {
        final List<Song> songs = Song.uploadSongs();
        final Conductor conductor = new Conductor(getMembers(songs));
        while (playing) {
            System.out.println("Song List:");
            //is this right?
            for (int i = 0; i < songs.size(); i++) {
                Song song = songs.get(i);
                System.out.println(" " + (i + 1) + ") " + song.getName());
            }
            System.out.println("Enter a number from the list to play a song.")
            System.out.println("If you wish to exit enter -1.");
            //checking for validation!!
            //why system.in?
            Scanner scan = new Scanner(System.in);
            while (!scan.hasNext("[1-" + songs.size() + "]|(0")) {
                System.out.println("Not a valid song number. Please try again.");
                scan.next();
            }

            int songNum = scan.nextInt() - 1;
            if (songNum + 1 == -1) {
                System.out.println("Shutting Down the  Jukebox...");
                scan.close();
                System.exit(-1);
            }

            Song song = songs.get(songNum);
            //need something here...
            if (song != null) {
                // this needs work
                conductor = new Conductor();
                System.out.println(song.getName() + "is playing.");
                conductor.playTheSong(song);
            } else {
                System.out.println("Error with playing the selected track of " + song.getName() + ".");
                System.out.println("Please select a different song.");
            }
        }
    }
}

//invalid song length
//invalid file
//invalid note length
//invalid song length with three inputs, or one input
//give good feedback

