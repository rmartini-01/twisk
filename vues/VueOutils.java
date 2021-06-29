package twisk.vues;

import javafx.application.Platform;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import twisk.ecouteurs.EcouteurFenetreDeStats;
import twisk.exceptions.MondeException;
import twisk.mondeIG.MondeIG;
import twisk.mondeIG.SujetObserve;

import java.util.Objects;

public class VueOutils extends TilePane implements Observateur {
    private Button ajouterActivite;
    private Button ajouterGuichet;
    private Button simuler;
    private Button stats;
    private SujetObserve monde;

    /**
     * Constructeur de la vue des outils (ajout d'une activité)
     * @param monde Les informations sur l'affichage du monde
     */
    public VueOutils(SujetObserve monde) {
        super();
        this.monde = monde;
        ajouterActivite = new Button();
        ajouterGuichet = new Button();
        simuler = new Button();
        //Bouton pour l'ajout d'une activité
        Tooltip tooltip = new Tooltip("Ajouter une activité");
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/twisk/ressources/images/ajouterActivite.png")),35,35,true,true);
        ImageView imageV = new ImageView(image);
        ajouterActivite.setGraphic(imageV);
        ajouterActivite.setTooltip(tooltip);
        ajouterActivite.setOnAction(event->((MondeIG) monde).ajouter("Activite"));
        //Bouton pour l'ajout d'un guichet
        tooltip = new Tooltip("Ajouter un guichet");
        image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/twisk/ressources/images/ajouterGuichet.png")),35,35,true,true);
        imageV = new ImageView(image);
        ajouterGuichet.setGraphic(imageV);
        ajouterGuichet.setTooltip(tooltip);
        ajouterGuichet.setOnAction(event->((MondeIG) monde).ajouter("Guichet"));
        this.getChildren().addAll(ajouterActivite,ajouterGuichet);
        //Bouton pour lancer la simulation
        tooltip = new Tooltip("Lancer la simulation");
        image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/twisk/ressources/images/simuler.png")),35,35,true,true);
        imageV = new ImageView(image);
        simuler.setGraphic(imageV);

        simuler.setTooltip(tooltip);
        simuler.setOnAction(event-> {
            try {
                //à chaque clique du bouton on met à jour la valeur de simulationEnCours
                ((MondeIG)monde).setSimulationEnCours(!((MondeIG) monde).isSimulationEnCours());

                if(!((MondeIG)monde).isSimulationEnCours()){
                    //effacer les pid des clients et nettoyer les structures de données
                    ((MondeIG) monde).detruireThreads();
                }else{
                    //pour être sûr de ne lancer qu'une simulation à la fois
                    ((MondeIG) monde).simuler();
                }
            } catch (MondeException e) {
                //affichage d'une fenêtre modale
                afficherMondeException(e.getMessage());
            }
        });
        this.getChildren().add(simuler);

        stats = new Button();
        stats.setOnAction(new EcouteurFenetreDeStats(monde));
        stats.setTooltip(new Tooltip("Parcours des clients"));
        stats.setCursor(Cursor.HAND);
        Image iconParcours = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/twisk/ressources/images/parcours.png")), 35, 35, true, true);
        stats.setGraphic(new ImageView(iconParcours));
        getChildren().add(stats);
        monde.ajouterObservateur(this);//La vue s'inscrit comme vue auprès du monde
    }

    /**
     * Fenêtre modale à afficher dans le cas où le monde est incorrect.
     */
    public void afficherMondeException(String e){
        Alert alerte = new Alert(Alert.AlertType.ERROR);
        alerte.setTitle("Monde incorrect.");
        alerte.setHeaderText("Le monde est incorrect.");
        alerte.setContentText(e);
        alerte.showAndWait();
    }
    @Override
    public void reagir() {
        Runnable command = new Runnable() {
            @Override
            public void run() {
                if (!((MondeIG) monde).isSimulationEnCours()) {
                    Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/twisk/ressources/images/simuler.png")), 35, 35, true, true);
                    ImageView imageV = new ImageView(image);
                    simuler.setGraphic(imageV);
                } else {
                    Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/twisk/ressources/images/pause.png")), 35, 35, true, true);
                    simuler.setGraphic(new ImageView(image));
                    simuler.setTooltip(new Tooltip("Arrêter la simulation."));
                }
            }
        };
        if (Platform.isFxApplicationThread()) {
            command.run();
        } else {
            Platform.runLater(command);
        }

    }

    @Override
    public void etatDeLASimulation() {

    }

}
