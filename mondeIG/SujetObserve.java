package twisk.mondeIG;

import twisk.vues.Observateur;

import java.util.ArrayList;

public abstract class SujetObserve {
    protected ArrayList<Observateur> observateurs;

    /**
     * Constructeur
     */
    public SujetObserve() {
        observateurs = new ArrayList<>(4);
    }

    /**
     * Ajoute une vue à la liste des vues
     * @param obs Vue à ajouter
     */
    public void ajouterObservateur(Observateur obs){
        observateurs.add(obs);
    }

    /**
     * Met à jour tous les observateurs (vues)
     */
    public void notifierObservateurs(){
        for (Observateur obs : observateurs){
            obs.reagir(); //On informe toutes les vues d'une modification
        }
    }

    /**
     * Mise à jour l'état de la simulation quand elle est finie.
     */
    public void etatDeLaSimulation(){
        for(Observateur obs: observateurs){
            obs.etatDeLASimulation();
            //obs.reagir();
        }
    }
}
