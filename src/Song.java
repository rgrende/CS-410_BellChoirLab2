//imports
import java.io.File;
import java.util.List;
import java.util.LinkedList;

//Class Description:

public class Song {
    private final String name;
    private final String path;
    private List<BellNote> notes;

    public Song(String name, String path) {
        this.name = name;
        this.path = path;
        this.notes = readSong(path);
    }

    public List<BellNote> getNotes() {
        return notes;
    }

    public String getName() {
        return name;
    }

    public static boolean validateSong() {
        boolean success = true;
        // Validate the first line of notes
        // Validate the song itself
        if (notes.isEmpty()) {
            System.err.println("Error: No song loaded.");
            return false;
        }
        return success;
    }

    private static LinkedList<BellNote> readSong(String path) {

    }

    public static List<Song> uploadSongs() {
        File songBook = new File("songs");

        if (!songBook.exists()) {
            songBook.mkdirs();
            System.out.println("Directory didn't exist, created one.");
        }
        File[] Songs = songBook.listFiles();
        songs = new Song[Songs.length];
        if (songs.length == 0) {
            System.err.println("Song book is empty. Please add some songs.")
        } else {
            for (int i = 0; i < songBook.length(); i++) {
                String name = Songs[i].getName();
                String path = Songs[i].getPath();

                String songTitle;

                //remove extensions
                int ext = name.lastIndexOf(".");
                if (ext != -1) {
                    songTitle = name.substring(0, ext);
                } else {
                    songTitle = name;
                }
                songs[i] = new Song(songTitle, path);
            }
        }
        return songs;
    }
}


