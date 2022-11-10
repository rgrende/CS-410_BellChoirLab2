/**
 * Class Description: This is the BellNote class that looks at the parameters of BellNote.
 * Those parameters are Note and NoteLength both of which make up a BellNote.
 */
class BellNote {
    //These are the BellNote class variables.
    final Note note;
    final NoteLength length;

    BellNote(Note note, NoteLength length) {
        this.note = note;
        this.length = length;
    }
}

/**
 * This is an enumeration of NoteLength. There are 10 different note lengths that my program recognizes.
 * If a song is uploaded that has a note with a note length that is not within the enumeration of note,
 * then the program will throw an error.
 */
enum NoteLength {
    WHOLE(1.0f),
    HALF(0.5f),
    DOTHALF(0.75f),
    QUARTER(0.25f),
    DOTQUARTER(0.375f),
    TRIPLET(0.22f),
    EIGHTH(0.125f),
    DOTEIGHTH(0.1875f),
    SIXTEENTH(0.0625f),
    DOTSIXTEENTH(0.09375f);


    private final int timeMs;

    private NoteLength(float length) {
        timeMs = (int) (length * Note.MEASURE_LENGTH_SEC * 1000);
    }

    public int timeMs() {
        //returns time in milliseconds
        return timeMs;
    }
}

/**
 * This is the enumeration for Note. I have included all eight octaves in my program. The sharps are
 * identified with an "S" in the note while rests is represented by "REST".
 */
enum Note {
    // REST Must be the first 'Note'
    //I have added all eight octaves to allow more songs to be played appropriately.
    REST, A0, A0S, B0, C0, C0S, D0, D0S, E0, F0, F0S, G0, G0S, A1, A1S, B1, C1, C1S, D1, D1S, E1, F1, F1S, G1,
    G1S, A2, A2S, B2, C2, C2S, D2, D2S, E2, F2, F2S, G2, G2S, A3, A3S, B3, C3, C3S, D3, D3S, E3, F3, F3S, G3,
    G3S, A4, A4S, B4, C4, C4S, D4, D4S, E4, F4, F4S, G4, G4S, A5, A5S, B5, C5, C5S, D5, D5S, E5, F5, F5S, G5,
    G5S, A6, A6S, B6, C6, C6S, D6, D6S, E6, F6, F6S, G6, G6S, A7, A7S, B7, C7, C7S, D7, D7S, E7, F7, F7S, G7,
    G7S, A8, A8S, B8, C8;


    public static final int SAMPLE_RATE = 256 * 1024; // ~256KHz which makes for better sound quality.
    //Jake told me this is better quality at least...
    public static final int MEASURE_LENGTH_SEC = 2;

    // Circumference of a circle divided by # of samples
    private static final double step_alpha = (2.0d * Math.PI) / SAMPLE_RATE;

    private final double FREQUENCY_A_HZ = 27.5d;
    private final double MAX_VOLUME = 50.0d;

    private final byte[] sinSample = new byte[MEASURE_LENGTH_SEC * SAMPLE_RATE];

    /**
     * The note class calculates the frequency of each note. It is important to note that octaves
     * change at every "A" note as opposed to every "C".
     */
    private Note() {
        int n = this.ordinal();
        if (n > 0) {
            // Calculate the frequency!
            final double halfStepUpFromA = n - 1;
            final double exp = halfStepUpFromA / 12.0d;
            final double freq = FREQUENCY_A_HZ * Math.pow(2.0d, exp);

            // Create sinusoidal data sample for the desired frequency
            final double sinStep = freq * step_alpha;
            for (int i = 0; i < sinSample.length; i++) {
                sinSample[i] = (byte) (Math.sin(i * sinStep) * MAX_VOLUME);
            }
        }
    }


    public byte[] sample() {
        return sinSample;
    }
}

