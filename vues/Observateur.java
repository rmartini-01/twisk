package twisk.vues;

public interface Observateur {
    /**
     * Met à jour les vues en fonction des données du monde
     */
    void reagir();
    /**
     * Met à jour l'état de la simulation dans le monde graphique.
     */
    void etatDeLASimulation();
}
