//imports
import java.io.File;
import java.util.List;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * Class Description: This is the Song class that identifies a song based on the properties of
 * BellNotes. It returns a song name, path, and list of bell notes.
 */

public class Song {
    //Class variables
    private final String name;
    private final String path;
    private List<BellNote> notes;

    //Defining Song
    public Song(String name, String path) {
        this.name = name;
        this.path = path;
        this.notes = readSong(path);
    }

    /**
     * Returns the notes associated with of the song.
     */
    public List<BellNote> getNotes() {
        return notes;
    }

    /**
     * Returns the name of the song.
     */
    public String getName() {
        return name;
    }

    /**
     * This method reads the list of BellNotes from a file and converts them a song.
     * When a file is passed that includes a line that does not properly define a BellNote,
     * such as with a note and a note length, this method will skip that line of null notes
     * and continue playing.
     */
    private static List<BellNote> readSong(String path) {
        //Here we are taking in a file, and converting it to a list of bell notes.
        List <BellNote> bns = new LinkedList<>();
        File file = new File(path);
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                BellNote temp = parseBellNote(scanner.nextLine());
                if (temp != null) {
                    //We don't like soup... or null notes.
                    bns.add(temp);
                }
            }
        } catch(java.io.IOException e) {
            //handle errors
        }
        return bns;
    }

    /**
     * This method uploads songs.
     */
    public static List<Song> uploadSongs() {
        //Directory of songs
        //race conditions
        //validation for no songs
        //validation for no directory
        //validation for empty directory
        File songBook = new File("songs");
        if (!songBook.exists()) {
            songBook.mkdirs();
            System.out.println("Directory didn't exist. We will create one.");
        }
        //Song is a list of its own songs within the list of files.
        File[] songFiles = songBook.listFiles();
        List <Song> song = new LinkedList<>();
        if (songFiles.length == 0) {
            System.err.println("Song book is empty. Please add some songs.");
        } else {
            for (int i = 0; i < songFiles.length; i++) {
                String name = songFiles[i].getName();
                String path = songFiles[i].getPath();

                String songTitle;

                //remove extensions of file types so that all songs are eligible to play.
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

    /**
     * This method came from the TicTacToe program but was modified to fit the BellChoir program.
     * When a file is passed, this method will split the lines into two fields, one for notes and one for length.
     * It then returns a new BellNote with a note and note length.
     */
    private static BellNote parseBellNote(String line) {
        final String[] fields = line.split("\\s+");
        if (fields.length == 2) {
            Note note = parseNote(fields[0]);
            if (note == null) {
                //if note is null, return null
                return null;
            }
            NoteLength length = parseNoteLength(fields[1]);
            if (length == null) {
                //if length is null, return null
                return null;
            }
            return new BellNote(note, length);
        }
        return null;
    }

    /**
     * This method looks at the first field of the file passed, which is note.
     * If the note is within the Note enumeration and is valid, the method will return a valid note.
     */
    private static Note parseNote(String note) {
        // Null note = null note
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

    /**
     * This method contains a switch statement that iterates through all the note lengths given by the program.
     * If a file is passed with a note length that doesn't exist here, it will return null.
     */
    private static NoteLength parseNoteLength(String length) {
        switch (length) {
            case "1":
                return NoteLength.WHOLE;
            case "2D":
                return NoteLength.DOTHALF;
            case "2":
                return NoteLength.HALF;
            case "4":
                return NoteLength.QUARTER;
            case "4D":
                return NoteLength.DOTQUARTER;
            case "T":
                return NoteLength.TRIPLET;
            case "8":
                return NoteLength.EIGHTH;
            case"8D":
                return NoteLength.DOTEIGHTH;
            case "16":
                return NoteLength.SIXTEENTH;
            case"16D":
                return NoteLength.DOTSIXTEENTH;

            default:
                return null;
        }
    }
}


//interface consists of the necessary methods
//list is interface, has to be defined with implementation