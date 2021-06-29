package twisk.outils;

import javafx.concurrent.Task;
import java.util.ArrayList;

public class GestionnaireThreads {
    private static GestionnaireThreads instance = new GestionnaireThreads();
    private ArrayList<Thread> lesThreads;

    private GestionnaireThreads(){
        lesThreads = new ArrayList<>(2);
    }

    /**
     * instance
     * @return retourne l'instance du gestionnaire
     */
    public static GestionnaireThreads getInstance(){
        return instance;
    }

    /**
     * lancer un thread
     * @param task tâche à exécuter
     */
    public void lancer(Task task){
        Thread thread = new Thread(task);
        lesThreads.add(thread);
        thread.start();
    }
    /**
     * détruire tous les threads
     */
    public void detruireTout(){
        for (Thread thread : lesThreads){
            thread.interrupt();
        }
    }
}
