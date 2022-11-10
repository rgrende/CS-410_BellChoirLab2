//imports

import java.util.*;

/**
 * Class Description: This is the JukeBox class that contains main method.
 */

public class JukeBox {

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
        //Global song list
        final List<Song> songs = Song.uploadSongs();
        Conductor conductor;
        final Map<Note, Choir> choirMembers = getMembers(songs);
        boolean playing = true;
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
            while (!scan.hasNext("[1-" + songs.size() + "]|(-1)")) {
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
            //need something here...
            if (song != null) {
                // this needs work
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

//invalid song length
//invalid file
//invalid note length
//invalid song length with three inputs, or one input
//give good feedback

