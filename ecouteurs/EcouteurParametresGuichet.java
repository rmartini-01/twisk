package twisk.ecouteurs;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.util.Duration;
import twisk.exceptions.ExceptionParametreNonValide;
import twisk.mondeIG.MondeIG;
import twisk.mondeIG.SujetObserve;


import static java.lang.Integer.parseInt;

public class EcouteurParametresGuichet implements EventHandler<ActionEvent> {
    private final SujetObserve sujet;
    /**
     * Constructeur d'un écouteur pour modifier les paramètres
     */
    public EcouteurParametresGuichet(SujetObserve sujet) {
        this.sujet = sujet;
    }
    private void afficherAlerte(){
        Alert alerte = new Alert(Alert.AlertType.ERROR);
        alerte.setTitle("Paramètre invalide");
        alerte.setHeaderText(null);
        alerte.setContentText("Veuillez choisir un nombre supérieur à 0.\n(La fenêtre se fermera après 10 secondes)");
        alerte.show();
        //On ferme la fenêtre automatiquement après 10 secondes (si elle n'est pas déjà fermée)
        PauseTransition attente = new PauseTransition(Duration.seconds(10));
        attente.setOnFinished(event-> alerte.close());
        attente.play();
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        final TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Paramètres du nombre de jetons dans le guichet.");
        dialog.setHeaderText( "Choisir le nombre de jetons.");
        dialog.setContentText("Nombre de jetons: ");
        Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);

        okButton.setOnAction(parametres -> {
            try {
                ((MondeIG) sujet).modifieNbJetons(parseInt(dialog.getEditor().getText()));
            } catch (ExceptionParametreNonValide exceptionParametreNonValide) {
                afficherAlerte();
            }
        });
        dialog.showAndWait();

    }
}
