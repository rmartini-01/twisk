package twisk.simulation;
import javafx.concurrent.Task;
import twisk.monde.*;
import twisk.mondeIG.SujetObserve;
import twisk.outils.GestionnaireThreads;
import twisk.outils.KitC;
import twisk.outils.Temps;

public class Simulation extends SujetObserve {
    private static final String OS  = System.getProperty("os.name").toLowerCase();
    private final KitC kit;
    private final GestionnaireClients gestionnaireClients;
    private int nbClients;
    /**
     * Constructeur
     */
    public Simulation(){
        kit = new KitC();
        kit.creerEnvironnement();
        this.nbClients = 6;
        gestionnaireClients = new GestionnaireClients(nbClients);
    }

    /**
     * Modifie le nombre de clients du monde
     * @param nbClients Nouveau nombre de clients
     */
    public void setNbClients(int nbClients) {
        this.nbClients = nbClients;
    }

    /**
     * Getter
     * @return le gestionnaire des clients
     */
    public GestionnaireClients getGestionnaireClients() {
        return gestionnaireClients;
    }

    /**
     * Retourne la chaîne de caractères affichant les pid d'une étape
     * @param placementClients Le tableau des pid
     * @param numEtape Le numéro de l'étape
     * @param etape l'étape actuelle
     * @return La chaîne de caractères contenant les pid
     */
    private String affichePidClientsDUneEtape(int[] placementClients, int numEtape, Etape etape){
        int nbClientsDansActivite = placementClients[numEtape*(nbClients+1)];
        StringBuilder affichage = new StringBuilder(nbClientsDansActivite*7);//Donné à titre indicatif
        for(int j=1; j<=nbClientsDansActivite;j++ ){
            //Affiche le pid
            affichage.append(placementClients[numEtape*(nbClients+1)+j]);
            if(numEtape*(nbClients+1)+j != numEtape* (nbClients+1)+nbClientsDansActivite){
                affichage.append(", ");
            }
            //on met à jour le placement des clients dans le gestionnaire des clients
            gestionnaireClients.allerA(placementClients[numEtape*(nbClients+1)+j], etape, j);

        }
        return affichage.toString();
    }

    /**
     * Affiche les numéros de pid de chaque client
     * @param clients Les numéros de pid de chaque clients
     */
    private void afficheLesPid(int[] clients){
        System.out.print("\nLes clients :   ");
        for(int j = 0; j<nbClients;j++){
            System.out.print(clients[j]);
            if (j != nbClients-1){
                System.out.print(", ");
            }
        }
        System.out.print("\n");
    }

    /**
     * Affiche la ligne d'une étapes en iniquand les clients s'y trouvant
     * @param placementClients Tableau contenant les pids des clients et le nombre de clients pas étape
     * @param etape L'étape dont on veut les informations
     */
    private void afficheLEtape(int[] placementClients, Etape etape){
        int numEtape = etape.getNumEtape(); //Le numéro de l'activité
        int nbClientsDansActivite = placementClients[numEtape*(nbClients+1)];
        StringBuilder affichage = new StringBuilder(nbClientsDansActivite*7+20);//Donné à titre indicatif
        affichage.append("\nétape ").append(numEtape).append(" (").append(etape.getNom()).append(") ").append(placementClients[numEtape * (nbClients + 1)]);

        if(placementClients[numEtape*(nbClients+1)]>1){
            affichage.append(" clients : ");
        }else{
            affichage.append(" client ");
            if (placementClients[numEtape*(nbClients+1)]==1){
                affichage.append(" : ");
            }
        }
        //Affichage des pid des clients dans l'activité i
        affichage.append(affichePidClientsDUneEtape(placementClients,numEtape , etape));
        System.out.print(affichage);
    }
    /**
     * Simule un monde crée par un fichier C et construit la librairie qui permettra le mouvement des clients dans le monde.
     * @param monde monde actuel
     */
    public void simuler(Monde monde) {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    //System.out.println("Le monde contient : "+ monde.nbEtapes() + " étapes.");
                    //On affiche toutes les étapes du monde et leur(s) successeur(s)
                    /*for (Etape etape : monde) {
                        System.out.println(etape);
                    }*/
                    //on affiche le code C
                    //System.out.println(monde.toC());
                    //on écrit le code C dans client.c à partir du monde créé
                    kit.creerFichier(monde.toC());
                    //Compile le fichier client.c
                    kit.compiler();
                    //Construction de la librairie libTwisk(.so ou .dylib)
                    kit.construireLaLibrairie();
                    //Charge la librairie
                    if(OS.contains("mac os")){
                        System.load("/tmp/twisk/libTwisk"+kit.getNumlib()+".dylib");
                    } else {
                        System.load("/tmp/twisk/libTwisk"+kit.getNumlib()+".so");
                    }
                    //Permet d'actualiser l'image sur le bouton
                    notifierObservateurs();
                    int[] jetons = new int[monde.nbGuichets()];
                    //On enregistre tous les nombres de jetons dans le tableau "jetons"
                    for(Etape g : monde){
                        if(g.estUnGuichet()){
                            jetons[g.getNbSemaphore()-1] = g.getNbjetons();
                        }
                    }

                    int[] clients = start_simulation(monde.nbEtapes(), monde.nbGuichets(), nbClients,jetons );
                    //On affiche les pid des clients
                    //afficheLesPid(clients);
                    //on instancie les clients dans le gestionnaire
                    gestionnaireClients.setClients(clients);
                    int[] placementClients;

                    do {
                        //affichage = new StringBuilder(40*monde.nbEtapes());
                        placementClients = ou_sont_les_clients(monde.nbEtapes(), nbClients);
                        //on affiche les modifications sur la SS (partie à supprimer une fois que l'affichage interface fonctionne)
                        for (Etape etape : monde) {
                            //afficheLEtape(placementClients, etape);
                            int nbClientsDansActivite = placementClients[etape.getNumEtape()*(nbClients+1)];
                             for(int j=1; j<=nbClientsDansActivite;j++ ){
                                //on met à jour le placement des clients dans le gestionnaire des clients
                                gestionnaireClients.allerA(placementClients[etape.getNumEtape()*(nbClients+1)+j], etape, j);
                            }
                            notifierObservateurs();
                        }
                        //System.out.print("\n"); //Passage à la ligne pour un plus bel affichage entre chaque fois qu'on regarde les clients

                        Thread.sleep(1000);
                        } while (placementClients[(nbClients + 1)] != nbClients) ;
                }catch (InterruptedException e) {
                    //tuer les processus C
                    for(Client c : gestionnaireClients){
                        kit.detruireClients(c.getNumeroClient());
                    }
                    //nettoyer les structures de données
                    nettoyage();
                    //mise à jour de l'interface
                    notifierObservateurs();
                }
                //on prévient le monde que la simulation est finie
                etatDeLaSimulation();
                Temps.getInstance().detruireTimer();
                //nettoyer les structures de données
                nettoyage();
                return null;
            }
        };
        GestionnaireThreads.getInstance().lancer(task);
    }

    /**
     * Getter du kitC
     * @return kitC de l'application
     */
    public KitC getKit() {
        return kit;
    }

    //Déclaration des fonctions natives
    /**
     * Retourne le tableau des numéros des processus créés (pid)
     * @param nbEtapes Nombre d'étapes dans le monde
     * @param nbGuichets Nombre de guichets dans le monde
     * @param nbClients Nombre de clients dans le monde
     * @param tabJetonsGuichet Tableau rempli du nombre de jetons pour chaque guichet
     * @return Tableau des numéros des processus
     */
    public native int[] start_simulation(int nbEtapes,int nbGuichets,int nbClients,int[] tabJetonsGuichet);

    /**
     * Retourne l'adresse du tableau où sont stockées les informations des clients
     * @param nbEtapes Nombre d'étapes dans le monde
     * @param nbClients Nombre de clients dans le monde
     * @return le tableau contenant les informations des clients
     */
    public native int[] ou_sont_les_clients(int nbEtapes, int nbClients);

    /**
     * Nettoie les structures de données créées
     */
    public native void nettoyage();
}
