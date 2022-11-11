//imports
import java.util.*;

/**
 * Class Description: This is the JukeBox class that contains main method. I used a map as
 * my data structure in this class in order to help me keep track of the choir members
 * the songs that they are able to play. The members of the choir are given a list of songs
 * that contain bell notes. If a note comes up that the members don't know how to play, that note
 * is added to a list called usedNotes.
 */

public class JukeBox {

    private static Map<Note, Choir> getMembers(List<Song> songs) {
        LinkedList<Note> usedNotes = new LinkedList<Note>();
        //hash map to keep track of the choir members
        HashMap<Note, Choir> choirMembers = new HashMap<Note, Choir>();
        for (Song song : songs) {
            for (BellNote bn : song.getNotes()) {
                Note note = bn.note;
                if (!usedNotes.contains(note)) {
                    //adding new notes to the used notes list
                    usedNotes.add(note);
                    choirMembers.put(note, new Choir(note));
                }
            }
        }
        return choirMembers;
    }

    public static void main(String[] args) throws Exception {
        //Global song list
        final List<Song> songs = Song.uploadSongs();
        Conductor conductor;
        final Map<Note, Choir> choirMembers = getMembers(songs);
        boolean playing = true;
        System.out.println("        Welcome to the BellChoir      ");
        System.out.println("   _       _        _               \n" +
                "  (_)     | |      | |              \n" +
                "   _ _   _| | _____| |__   _____  __\n" +
                "  | | | | | |/ / _ \\ '_ \\ / _ \\ \\/ /\n" +
                "  | | |_| |   <  __/ |_) | (_) >  < \n" +
                "  | |\\__,_|_|\\_\\___|_.__/ \\___/_/\\_\\\n" +
                " _/ |                               \n" +
                "|__/                               "
                );

        while (playing) {
            System.out.println("Song List:");
            for (int i = 0; i < songs.size(); i++) {
                Song song = songs.get(i);
                System.out.println(" " + (i + 1) + ") " + song.getName());
            }
            System.out.println("Enter a number from the list to play a song.");
            System.out.println("If you wish to exit enter -1.");
            //Checking for validation!!
            //Why system.in? Because we are taking the input from the keyboard since it pulls from the console.
            Scanner scan = new Scanner(System.in);
            //Jaden helped me understand regex and how it operates.
            while (!scan.hasNext("[0-" + songs.size() + "]|(-1)")) {
                if (!scan.hasNextInt()) {
                    System.out.println("Please add a song to the 'songs' directory.");
                }
                System.out.println("Not a valid song number. Please try again.");
                scan.next();
            }

            int songNum = scan.nextInt() - 1;
            if (songNum + 1 == -1) {
                System.out.println("Shutting Down the Jukebox...");
                scan.close();
                System.exit(-1);
            }

            Song song = songs.get(songNum);

            if (song != null) {
                conductor = new Conductor(choirMembers);
                System.out.println(song.getName() + " is playing.");
                conductor.playTheSong(song);
            } else {
                System.out.println("Error with playing the selected track of " + song.getName() + ".");
                System.out.println("Please select a different song.");
            }
        }
    }
}


