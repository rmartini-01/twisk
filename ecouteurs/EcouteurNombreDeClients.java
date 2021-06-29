package twisk.ecouteurs;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.util.Duration;
import twisk.exceptions.ExceptionParametreNonValide;
import twisk.mondeIG.MondeIG;
import twisk.mondeIG.SujetObserve;

import static java.lang.Integer.parseInt;

public class EcouteurNombreDeClients implements EventHandler<ActionEvent> {
    private SujetObserve sujet;
    /**
     * Constructeur d'un écouteur pour modifier le nombre de clients
     */
    public EcouteurNombreDeClients(SujetObserve sujet) {
        this.sujet = sujet;
    }

    /**
     * Affiche un message d'alerte lorqu'un paramètres n'est pas valide
     */
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
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Choix du nombre de clients");
        dialog.setHeaderText( "Choisir le nombre de clients");
        dialog.setContentText("Nombre de clients: ");
        //On place le nombre de clients actuel dans le champ
        dialog.getEditor().setText(String.valueOf(((MondeIG) sujet).getNbDeClients()));
        Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);

        okButton.setOnAction(parametres -> {
            try {
                ((MondeIG) sujet).modifieNbClients(parseInt(dialog.getEditor().getText()));
            } catch (ExceptionParametreNonValide exceptionParametreNonValide) {
                afficherAlerte();
            }
        });
        dialog.showAndWait();
    }
}
