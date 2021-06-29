package twisk.simulation;

import twisk.monde.Etape;

import java.util.HashMap;
import java.util.Iterator;

public class GestionnaireClients implements Iterable<Client> {
    private HashMap<Integer, Client> clients;
    private int nbClients;

    /**
     * Constructeur d'un gestionnaire de clients
     */
    public GestionnaireClients(){
        this.nbClients = 0;
        clients = new HashMap<>();
    }
    /**
     * Constructeur d'un gestionnaire de clients
     * @param nbClients nombre de clients dans le monde
     */
    public GestionnaireClients(int nbClients){
        this.nbClients = nbClients;
        clients = new HashMap<>(nbClients);
    }

    /**
     * setter pour le nombre des clients
     * @param nbClients nombre de clients dans le monde
     */
    public void setNbClients(int nbClients){
        this.nbClients = nbClients;
    }
    /**
     * fonction pour instancier les clients
     * @param tabClients tableau contenant les numéros des processus des clients
     */
    public void setClients(int...tabClients){
        reset();
        for(int i : tabClients){
            clients.put(i , new Client(i));
        }
    }

    /**
     * fonction pour mettre à jour l'étape et le rang d'un client
     * @param numeroClient numéro du processus d'un client
     * @param etape étape du client
     * @param rang rang du client
     */
    public void allerA(int numeroClient, Etape etape, int rang){
        Client c = clients.get(numeroClient);
        c.allerA(etape, rang);
    }

    /**
     * fonction pour supprimer les client pour faire une nouvelle simulation
     */
    public void reset(){
        clients.clear();
    }

    /**
     * iterator sur les clients
     * @return iterateur des clients du monde
     */
    public Iterator<Client> iterator(){
        return clients.values().iterator();
    }

    /**
     * getter du nombre de clients
     * @return nombre de clients dans le monde
     */
    public int getNbClients(){
        return clients.size();
    }

    /**
     * Retourne le client dont le numéro est passé en paramètre
     * @param numeroClient Le numéro du clietn qu'on veut récupérer
     * @return Le client
     */
    public Client getClient(int numeroClient){
        return clients.get(numeroClient);
    }
}
