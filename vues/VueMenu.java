package twisk.vues;

import javafx.application.Platform;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import twisk.ecouteurs.EcouteurNombreDeClients;
import twisk.ecouteurs.EcouteurParametresGuichet;
import twisk.mondeIG.MondeIG;
import twisk.mondeIG.SujetObserve;
import twisk.ecouteurs.EcouteurParametresActivite;
import twisk.ecouteurs.EcouteurRenommage;

import java.io.File;
import java.util.ArrayList;

public class VueMenu extends MenuBar implements Observateur{
    private SujetObserve sujet;
    private ArrayList<Menu> menus;
    /**
     * Constructeur de la vue d'un menu
     * @param sujet Sujet à observer
     */
    public VueMenu(SujetObserve sujet) {
        super();
        this.sujet = sujet;
        menus = new ArrayList<>(4);
        this.setStyle("-fx-background-color: #DFDADA; -fx-text-background-color: #DFDADA ; -fx-selection-bar:#0072F7");
        creerMenuFichier();
        creerMenuEdition();
        creerMenuMonde();
        creerMenuLois();
        creerMenuClients();
        creerMenuParametres();

        for (Menu m : menus){
            this.getMenus().add(m);
        }
        sujet.ajouterObservateur(this);
    }

    @Override
    public void reagir() {
        Runnable command = new Runnable() {
            @Override
            public void run() {
                if (((MondeIG) sujet).getNbEtapesDansLaSelection() == 1){
                    menus.get(5).setDisable(false); //Si il n'y a qu'une Etape sélectionnée, on peut modifier les paramètres
                    if (((MondeIG) sujet).getEtapeSelectionnee().estUneActivite()) {//Vérifier que l'étape est une activité
                        menus.get(1).getItems().get(1).setDisable(false); //Si il n'y a qu'une activité sélectionnée, on peut la renommer
                        menus.get(5).getItems().get(0).setDisable(false);  //S'il y a une activité sélectionnée on peut modifier ses paramètres

                    }else if(((MondeIG) sujet).getEtapeSelectionnee().estUnGuichet()){
                        menus.get(1).getItems().get(2).setDisable(false); //Si il n'y a qu'un guichet sélectionné, on peut le renommer
                        menus.get(5).getItems().get(1).setDisable(false); //S'il y a une activité sélectionnée on peut modifier ses paramètres
                    }

                } else {
                    //par défaut ils ne sont pas modifiable
                    menus.get(5).setDisable(true);
                    menus.get(1).getItems().get(1).setDisable(true);
                    menus.get(1).getItems().get(2).setDisable(true);
                    menus.get(5).getItems().get(0).setDisable(true);
                    menus.get(5).getItems().get(1).setDisable(true);

                }
                if (((MondeIG) sujet).isSimulationEnCours()){
                    //Les actions "charger" et "sauvegarder" sont inaccessibles pendant une simulation
                    menus.get(0).getItems().get(0).setDisable(true);
                    menus.get(0).getItems().get(1).setDisable(true);
                } else {
                    menus.get(0).getItems().get(0).setDisable(false);
                    menus.get(0).getItems().get(1).setDisable(false);
                }
            }
        };

        if(Platform.isFxApplicationThread()){
            command.run();
        } else {
            Platform.runLater(command);
        }
    }

    @Override
    public void etatDeLASimulation() {

    }

    /**
     * gérer le menu Fichier
     */
    private void creerMenuFichier(){
        Menu menuFichier = new Menu("Fichier");
        //Les items du menu Fichier
        MenuItem quitter = new MenuItem("Quitter");
        Menu charger = new Menu("Charger ");
        MenuItem sauvegarder = new MenuItem("Enregistrer");
        sauvegarder.setOnAction(event ->choixDuDocumentASauvegarder());

        MenuItem monde1 = new MenuItem("Monde 1");
        monde1.setOnAction(event -> ((MondeIG)sujet).chargerMonde(1));
        MenuItem monde2 = new MenuItem("Monde 2");
        monde2.setOnAction(event -> ((MondeIG)sujet).chargerMonde(2));
        MenuItem monde3 = new MenuItem("Monde 3");
        monde3.setOnAction(event -> ((MondeIG)sujet).chargerMonde(3));
        MenuItem monde4 = new MenuItem("Monde 4");
        monde4.setOnAction(e->((MondeIG)sujet).chargerMonde(4));

        MenuItem mondeSauvegarde = new MenuItem("Charger un monde perso");
        mondeSauvegarde.setOnAction(e->choixDuDocumentACharger());

        charger.getItems().addAll(monde1,monde2,monde3, monde4,mondeSauvegarde);
        //Appuyer sur quitter nous fait quitter twisk
        quitter.setOnAction(event-> {
            ((MondeIG)sujet).detruireThreads();
            Platform.exit();
        });

        menuFichier.getItems().addAll(charger, sauvegarder, quitter);
        menus.add(menuFichier);
    }
    /**
     * gérer le menu Edition
     * Supprimer la sélection
     * Renommer activité
     * Renommer guichet
     * Effacer la sélection
     */
    private void creerMenuEdition(){
        Menu menuEdition = new Menu("Edition");
        MondeIG monde = (MondeIG) sujet;
        //Les items du menu Edition
        MenuItem supprimer = new MenuItem("Supprimer la sélection");
        supprimer.setOnAction(event->monde.suppressionSelection());
        MenuItem renommerActivite = new MenuItem("Renommer l'activité");
        renommerActivite.setDisable(true);
        renommerActivite.setOnAction(new EcouteurRenommage( monde));
        MenuItem renommerGuichet = new MenuItem("Renommer le guichet");
        renommerGuichet.setDisable(true);
        renommerGuichet.setOnAction(new EcouteurRenommage( monde));
        MenuItem effacer = new MenuItem("Effacer la sélection");
        effacer.setOnAction(event->monde.effacerSelection());
        menuEdition.getItems().addAll(supprimer,renommerActivite, renommerGuichet, effacer);
        menus.add(menuEdition);
    }

    /**
     * gérer menu Monde
     * définir les entrées et les sorties
     */
    private void creerMenuMonde(){
        Menu menuMonde = new Menu("Monde");
        //Les items du menu Monde
        MondeIG monde = (MondeIG) sujet;
        MenuItem entree = new MenuItem("Entrée");
        entree.setOnAction(event->monde.selectionnerLesEntrees());
        MenuItem sortie = new MenuItem("Sortie");
        sortie.setOnAction(event->monde.selectionnerLesSorties());
        menuMonde.getItems().addAll(entree,sortie);
        menus.add(menuMonde);
    }

    /**
     * gérer Menu paramètres
     * Modifier les paramètres d'une activité
     * Modifier les paramètres d'un guichet
     */
    private void creerMenuParametres(){
        Menu menuParametres = new Menu("Paramètres");
        MenuItem modifierParametresActivite = new MenuItem("Modifier une activité");
        MenuItem modifierParametresGuichet = new MenuItem("Modifier un guichet");
        //Au départ on ne peut pas modifier les paramètres car aucune activité n'est sélectionnée
        menuParametres.setDisable(true);
        modifierParametresActivite.setDisable(true);
        modifierParametresGuichet.setDisable(true);

        modifierParametresActivite.setOnAction(new EcouteurParametresActivite(sujet));
        modifierParametresGuichet.setOnAction(new EcouteurParametresGuichet(sujet));
        menuParametres.getItems().addAll(modifierParametresActivite, modifierParametresGuichet);
        menus.add(menuParametres);
    }

    private void creerMenuClients() {
        Menu menuClients = new Menu("Clients");
        MenuItem modifierNbClients = new MenuItem("Nombre de clients");
        modifierNbClients.setOnAction(new EcouteurNombreDeClients(sujet));
        menuClients.getItems().add(modifierNbClients);
        menus.add(menuClients);
    }

    private void creerMenuLois(){
        Menu menuLois = new Menu("Lois");
        MenuItem loiUniforme = new MenuItem("Loi Uniforme");
        MenuItem loiGaussienne= new MenuItem("Loi de Gauss");
        MenuItem loiExpo = new MenuItem("Loi Exponentielle");

        loiUniforme.setOnAction(e-> ((MondeIG)sujet).setLoi("Uniforme"));

        loiGaussienne.setOnAction(e-> ((MondeIG)sujet).setLoi("Gaussienne"));

        loiExpo.setOnAction(e-> ((MondeIG)sujet).setLoi("Exponentielle"));
        menuLois.getItems().addAll(loiUniforme, loiGaussienne, loiExpo);
        menus.add(menuLois);
    }

    /**
     * Affiche une fenêtre pour choisir le monde à charger
     */
    public void choixDuDocumentACharger(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Serializable Files", "*.ser"));

        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null){
            ((MondeIG)sujet).charger(selectedFile.getAbsolutePath());
        }
    }
    /**
     * Affiche une fenêtre pour choisir le monde à sauvegarder
     */
    public void choixDuDocumentASauvegarder(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Serializable Files", "*.ser"));

        File selectedFile = fileChooser.showSaveDialog(null);

        if (selectedFile != null){
            ((MondeIG)sujet).sauvegarder(selectedFile.getAbsolutePath());
        }
    }
}
