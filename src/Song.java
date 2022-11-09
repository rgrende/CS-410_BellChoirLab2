//imports
import java.io.File;
import java.util.List;
import java.util.LinkedList;
import java.util.Scanner;

//Class Description:

public class Song {
    //class variables
    private final String name;
    private final String path;
    private List<BellNote> notes;

    public Song(String name, String path) {
        this.name = name;
        this.path = path;
        this.notes = readSong(path);
    }

    public List<BellNote> getNotes() {
        //Returns the notes associated with of the song.
        return notes;
    }

    public String getName() {
        //Returns the name of the song.
        return name;
    }

    private static List<BellNote> readSong(String path) {
        //Here we are taking in a file, and converting it to a list of bell notes.
        List <BellNote> bns = new LinkedList<>();
        File file = new File(path);
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                BellNote temp = Tone.parseBellNote(scanner.nextLine());
                if (temp != null) {
                    //We don't like soup... or null notes.
                    bns.add(temp);
                }
            }
        } catch(java.io.IOException e) {
            //handling errors
        }
        return bns;
    }

    public static List<Song> uploadSongs() {
        //Directory of songs
        File songBook = new File("./songs");
        if (!songBook.exists()) {
            songBook.mkdirs();
            System.out.println("Directory didn't exist. We will create one.");
        }
        //Song within the list of files
        File[] songFiles = songBook.listFiles();
        List <Song> song = new LinkedList<>();
        if (songFiles.length == 0) {
            System.err.println("Song book is empty. Please add some songs.");
        } else {
            for (int i = 0; i < songFiles.length; i++) {
                String name = songFiles[i].getName();
                String path = songFiles[i].getPath();

                String songTitle;

                //remove extensions
                int ext = name.lastIndexOf(".");
                if (ext != -1) {
                    songTitle = name.substring(0, ext);
                } else {
                    songTitle = name;
                }
                song.add(new Song(songTitle, path));
            }
        }
        return song;
    }
}


//interface consists of the necessary methods
//list is interface, has to be defined with implementation