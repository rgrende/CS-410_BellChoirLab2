//imports
import javax.sound.sampled.SourceDataLine;

/**
 * Class Description: This is the {@code Choir} class that contains my run method. This is also the class that
 * is in charge of all the threads. The members of the choir have a one note to play for a specific amount of time.
 */

public class Choir implements Runnable {
    //Class variables
    private final Note note;
    private SourceDataLine line;
    private final Thread thread;
    private boolean turn;
    private NoteLength length;
    private volatile boolean timeToPlay;

    /**
     * Here we define {@code Choir} as a note, line, and a thread.
     * Line and note are objects.
     */
    Choir(Note note) {
        this.note = note;
        line = null;
        thread = new Thread(this, note.toString());
        thread.start();
        turn = false;
    }

    /**
     * This method stops the choir threads.
     */
    public void choirStop() {
        timeToPlay = false;
    }

    /**
     * Waiting to stop the threads.
     */
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


    /**
     * This is where the {@code Conductor} tells the {@code Choir} threads to take their turn. If it is the members turn,
     * the turn is set to true and synchronized. An exception is thrown if the conductor tries to give the same
     * choir member a turn when and if they haven't completed their turn.
     */
    public void takeTurn(NoteLength length, SourceDataLine line) {
        synchronized (this) {
            if (turn) {
                System.out.println("Attempted to give another turn to this choir member who hasn't finished their turn.");
            }
            turn = true;
            this.length = length;
            this.line = line;
            notifyAll();
            //notifying the conductor and other members
            while (turn) {
                try {
                    wait();
                } catch (InterruptedException ignored) {

                }
            }
        }
    }

    private void playNote() {
        //check to see if line is !null
        final int ms = Math.min(length.timeMs(), Note.MEASURE_LENGTH_SEC * 1000);
        final int length = Note.SAMPLE_RATE * ms / 1000;
        line.write(note.sample(), 0, length);
        line.write(Note.REST.sample(), 0, 50);
    }

    /**
     * The run method tells the {@code Choir} to wait until the {@code Conductor} says it's their
     * turn and notifies them once it is their turn to play. Once the turn taken the turn is set to false.
     * The choir member then notifies the threads that they are done playing.
     */
    public void run() {
        timeToPlay = true;
        //Preventing all the notes from playing at once.
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


