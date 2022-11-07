//imports
import javax.sound.sampled.SourceDataLine;


//Class Description:

public class Choir implements Runnable {
    //class variables for the Choir class
    private final Note note;
    private SourceDataLine line;
    private final Thread thread;
    private boolean turn;
    private NoteLength length;
    private volatile boolean timeToPlay;

    Choir(Note note) {
        this.note = note;
        line = null;
        thread = new Thread(this, note.toString());
        thread.start();
        turn = false;
    }

    public void choirStop() {
        timeToPlay = false;
    }

    public void waitToStop() {
        try {
            thread.join();
        } catch (InterruptedException e) {
            System.err.println(thread.getName() + " stop choir malfunction.");
        }
    }

    public void setLine(SourceDataLine line) {
        this.line = line;
    }

    public void takeTurn(NoteLength length) {
        synchronized (this) {
            if (turn) {
                System.out.println("Attempted to give another turn to this choir member who hasn't finished their turn.");
            }
            turn = true;
            this.length = length;
            notifyAll();
        }
    }

    private void playNote() {
        //check to see if line is !null
        final int ms = Math.min(length.timeMs(), Note.MEASURE_LENGTH_SEC * 1000);
        final int length = Note.SAMPLE_RATE * ms / 1000;
        line.write(note.sample(), 0, length);
        line.write(Note.REST.sample(), 0, 50);
    }

    public void run() {
        timeToPlay = true;
        synchronized (this) {
            while (timeToPlay) {
                while (!turn) {
                    try {
                        wait();
                    } catch (InterruptedException ignored) {
                    }
                }
                playNote();
                turn = false;
                length = null;
                notify();
            }
        }
    }
}


