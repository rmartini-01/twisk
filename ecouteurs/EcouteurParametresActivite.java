package twisk.ecouteurs;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
import javafx.util.Pair;
import twisk.exceptions.ExceptionParametreNonValide;
import twisk.mondeIG.ActiviteIG;
import twisk.mondeIG.MondeIG;
import twisk.mondeIG.SujetObserve;

import java.util.Optional;

public class EcouteurParametresActivite implements EventHandler<ActionEvent> {
    private SujetObserve sujet;
    /**
     * Constructeur d'un écouteur pour modifier les paramètres
     */
    public EcouteurParametresActivite(SujetObserve sujet) {
        this.sujet = sujet;
    }

    /**
     * Modifie l'affichage de la fenêtre
     * @param gridPane Le pane qui organise les champs
     */
    private void modifierLAffichage(GridPane gridPane){
        //On crée les champs pour rentrer les données
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 150, 10, 10));
    }

    /**
     * Créer l'affichage de la fenêtre pour rentrer les donnée
     * @param dialog Le dialogue de la fenêtre
     */
    private void creationAffichage(Dialog<Pair<String,String>> dialog){
        GridPane gridPane = new GridPane();
        modifierLAffichage(gridPane);
        MondeIG monde = (MondeIG)  sujet;
        ActiviteIG activite = (ActiviteIG) monde.getEtapeSelectionnee();
        TextField delaiStr = new TextField(Integer.toString(activite.getDelai()));
        TextField ecartStr = new TextField(Integer.toString(activite.getEcart()));
        gridPane.add(new Label("Délai:"), 0, 0);
        gridPane.add(delaiStr, 1, 0);
        gridPane.add(new Label("Ecart:"), 0, 1);
        gridPane.add(ecartStr, 1, 1);
        dialog.getDialogPane().setContent(gridPane);
        //Ajoute le bouton pour entrer les informations
        ButtonType enterButtonType = new ButtonType("OK",ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(enterButtonType);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == enterButtonType) {
                return new Pair<>(delaiStr.getText(), ecartStr.getText());
            }
            return null;
        });
    }
    /**
     * Crée une nouvelle fenêtre qui informe l'utilisateur de l'erreur
     */
    void afficherMessageDErreurs(){
        Alert alerte = new Alert(Alert.AlertType.ERROR);

        alerte.setTitle("Erreur de paramètres");
        alerte.setHeaderText(null);
        alerte.setContentText("Erreur : une des valeurs entrées n'est pas valide.\n(La fenêtre se fermera après 10 secondes)");
        alerte.show();
        //On ferme la fenêtre automatiquement après 10 secondes (si elle n'est pas déjà fermée)
        PauseTransition attente = new PauseTransition(Duration.seconds(10));
        attente.setOnFinished(event-> alerte.close());
        attente.play();
    }
    @Override
    public void handle(ActionEvent actionEvent) {

        Dialog<Pair<String,String>> dialog = new Dialog<>();
        dialog.setTitle("Modification des paramètres de l'activité");
        dialog.setHeaderText("Entrez les nouveaux paramètres de l'activité");

        creationAffichage(dialog);

        MondeIG monde = (MondeIG)  sujet;
        Optional<Pair<String, String>> result = dialog.showAndWait();

        result.ifPresent(parametres -> {
            try {
                monde.modifieLesParametres(Integer.parseInt(parametres.getKey()),Integer.parseInt(parametres.getValue()));
            } catch (ExceptionParametreNonValide exceptionParametreNonValide) {
                afficherMessageDErreurs();
            }
        });
    }
}
