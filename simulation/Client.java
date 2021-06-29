package twisk.simulation;

import twisk.monde.Etape;
import twisk.outils.Temps;

import java.util.ArrayList;

public class Client {
    private final int numeroClient;
    private int rang;
    private Etape etape;
    private final double[] couleur;
    private ArrayList<String> historiqueEtapes;
    private int tempsJusquAArrivee;

    /**
     * Constructeur d'un client
     * @param numero numéro du processus du client
     */
    public Client(int numero){
        numeroClient = numero; //numéro du client
        couleur = new double[3]; //tableau à 3 cases pour les couleurs rgb.
        //pour gérer les couleurs des cercles clients
        couleur[0] = ((Math.random() * (1)));
        couleur[1] = (Math.random() * (1));
        couleur[2] =  (Math.random() * (1));
        //par défaut les clients n'ont pas de rang ni d'étape
        historiqueEtapes = new ArrayList<>();
        tempsJusquAArrivee = -1;
    }

    /**
     * Getter des couleurs de la vue d'un client
     * @param rgb rouge = 0, vert = 1, bleu = 2
     * @return la valeur de la couleur
     */
    public double getCouleurs(int rgb){
        return couleur[rgb];
    }

    /**
     * met à jour les attributs etape et rang d'un client
     * @param etape l'étape du client
     * @param rang le rang du client
     */
    public void allerA(Etape etape, int rang){
        this.etape = etape;
        this.rang = rang;
        if(!historiqueEtapes.contains(etape.getNom())){ // pour ne pas rajouter la même étape deux fois
            historiqueEtapes.add(etape.getNom());
        }
        if(etape.estUnSasDeSortie() && tempsJusquAArrivee==-1){
            tempsJusquAArrivee= Temps.getInstance().getSecondsPassed();
        }
    }

    /**
     * Retourne le rang d'un client
     * @return Le rang d'un client
     */
    public int getRang(){
        return this.rang;
    }

    /**
     * Retourne le numéro du client
     * @return Numéro du client
     */
    public int getNumeroClient() {
        return numeroClient;
    }

    /**
     * Retourne le nom de l'étape sur laquelle est le client
     * @return Le nom de l'étape
     */
    public String getNomDeLEtape(){
        return etape.getNom();
    }

    /**
     * Getter de l'étape actuelle du client
     * @return L'étape du client
     */
    public Etape getEtape() {
        return etape;
    }

    /**
     * Retourne quand le client est arrivée à la sortie
     * @return le temps qu'a mis le client pour arriver à la sortie
     */

    public int getTempsJusquAArrivee() {
        return tempsJusquAArrivee;
    }


    /**
     * Retourne le nom des étapes dans lesquels est passé le client
     * @return Les étapes
     */
    public String getHistoriqueClients(){
        StringBuilder str = new StringBuilder(historiqueEtapes.size() * 21);
        for (String etape : historiqueEtapes){
            str.append(etape);
            str.append(" ");
        }
        return str.toString();
    }
}
