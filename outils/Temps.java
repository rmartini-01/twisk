package twisk.outils;


import java.util.Timer;
import java.util.TimerTask;

public class Temps {
    private int secondsPassed;
    protected Timer timer;
    protected TimerTask task;
    private static Temps instance = new Temps();

    /**
     * Constructeur
     */
    private Temps(){
        secondsPassed = 0;
        timer = new Timer();
    }

    public static Temps getInstance(){
        return instance;
    }

    /**
     * Démarrer le timer
     */
    public void startTimer(){
        secondsPassed = 0;
        detruireTimer();
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                secondsPassed++;
            }
        };
        timer.scheduleAtFixedRate(task, 1000, 1000);
    }

    /**
     * stopper le timer
     */
    public void detruireTimer(){
        timer.cancel();
        timer.purge();
    }

    /**
     * getter
     * @return le temps actuel
     */
    public int getSecondsPassed() {
        return secondsPassed;
    }

}
