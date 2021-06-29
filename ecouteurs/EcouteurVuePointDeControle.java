package twisk.ecouteurs;


import javafx.animation.PauseTransition;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import twisk.exceptions.ExceptionAjoutDArcImpossible;
import twisk.mondeIG.MondeIG;
import twisk.mondeIG.PointDeControleIG;
import twisk.mondeIG.SujetObserve;

public class EcouteurVuePointDeControle implements EventHandler<MouseEvent> {

    private PointDeControleIG pointDeControle;
    private SujetObserve monde;

    /**
     *  Constructeur d'un écouteur lorsque l'on clique sur un point de contrôle
     * @param sujet Sujet à observer
     * @param pdc La point de contrôle sur lequel on clique
     */
    public EcouteurVuePointDeControle(SujetObserve sujet, PointDeControleIG pdc) {
        this.monde = sujet;
        pointDeControle = pdc;
    }

    /**
     * Crée une nouvelle fenêtre qui informe l'utilisateur de l'erreur
     */
    private void afficherMessageDErreurs(int code){
        //On crée une fenêtre d'erreur
        Alert alerte = new Alert(Alert.AlertType.ERROR);
        alerte.setTitle("Erreur de création d'un arc");
        alerte.setHeaderText(null);
        if (code == 2){
            alerte.setContentText("Erreur : arc impossible à ajouter.\nDeux guichets ne peuvent pas être reliés");
        } else if (code == 1) {
            alerte.setContentText("Erreur : arc impossible à ajouter.\nDeux points sur la même étape ne peuvent pas \nêtre reliés");
        } else if (code == 3) {
            alerte.setContentText("Erreur : arc impossible à ajouter.\nCréation de boucle interdite.");
        }

        alerte.show();
        //On ferme la fenêtre automatiquement après 10 secondes (si elle n'est pas déjà fermée)
        PauseTransition attente = new PauseTransition(Duration.seconds(10));
        attente.setOnFinished(event-> alerte.close());
        attente.play();
    }
    @Override
    public void handle(MouseEvent mouseEvent) {
        try {
            ((MondeIG) monde).selectionPoint(pointDeControle);
        } catch (ExceptionAjoutDArcImpossible e) {

            afficherMessageDErreurs(e.getCode());
        }

    }
}
