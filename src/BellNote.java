//imports

//Class Description:


class BellNote {
    //the note length and the note itself
    final Note note;
    final NoteLength length;

    BellNote(Note note, NoteLength length) {
        this.note = note;
        this.length = length;
    }
}

//if needed, add dotted notes
enum NoteLength {
    WHOLE(1.0f),
    HALF(0.5f),
    QUARTER(0.25f),
    EIGTH(0.125f),
    SIXTEENTH(0.0625f);


    private final int timeMs;

    private NoteLength(float length) {
        timeMs = (int) (length * Note.MEASURE_LENGTH_SEC * 1000);
    }

    public int timeMs() {
        return timeMs;
    }
}

enum Note {
    // REST Must be the first 'Note'
    REST, A0, A0S, B0, C0, C0S, D0, D0S, E0, F0, F0S, G0, G0S, A1, A1S, B1, C1, C1S, D1, D1S, E1, E1S, F1, F1S, G1,
    G1S, A2, A2S, B2, C2, C2S, D2, D2S, E2, F2, F2S, G2, G2S, A3, A3S, B3, C3, C3S, D3, D3S, E3, E3S, F3, F3S, G3,
    G3S, A4, A4S, B4, C4, C4S, D4, D4S, E4, F4, F4S, G4, G4S, A5, A5S, B5, C5, C5S, D5, D5S, E5, E5S, F5, F5S, G5,
    G5S, A6, A6S, B6, C6, C6S, D6, D6S, E6, F6, F6S, G6, G6S, A7, A7S, B7, C7, C7S, D7, D7S, E7, E7S, F7, F7S, G7,
    G7S, A8, A8S, B8, C8;


    public static final int SAMPLE_RATE = 48 * 1024; // ~48KHz
    public static final int MEASURE_LENGTH_SEC = 1;

    // Circumference of a circle divided by # of samples
    private static final double step_alpha = (2.0d * Math.PI) / SAMPLE_RATE;

    private final double FREQUENCY_A_HZ = 440.0d;
    private final double MAX_VOLUME = 127.0d;

    private final byte[] sinSample = new byte[MEASURE_LENGTH_SEC * SAMPLE_RATE];

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
//    public static Note getNote(String str) {
//        swtich (str) {
//            case "B0b":
//                return Note.valueOf(A0S);
//            break;
//            case "C0b":
//                return Note.valueOf(C0b);
//            break;
//            default:
//                try {
//
//                }
//        }
//
//    }

    public byte[] sample() {
        return sinSample;
    }
}

