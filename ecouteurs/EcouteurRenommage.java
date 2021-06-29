package twisk.ecouteurs;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import twisk.mondeIG.MondeIG;
import twisk.mondeIG.SujetObserve;

import java.util.Optional;

public class EcouteurRenommage implements EventHandler<ActionEvent> {

    private SujetObserve sujet;

    /**
     * Constructeur d'un écouteur servant à renommer une activité
     * @param sujet Sujet à observer
     */
    public EcouteurRenommage(SujetObserve sujet) {
        this.sujet = sujet;
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        MondeIG monde = (MondeIG)  sujet;
        //On crée la nouvelle fenêtre pour entrer le nouveau nom
        TextInputDialog fenetreRenommage = new TextInputDialog();
        //On modifie les composants de la fenêtre
        fenetreRenommage.setTitle("Changement du nom de l'étape");
        fenetreRenommage.setHeaderText("Entrez le nouveau nom de l'étape");
        fenetreRenommage.setContentText("Nouveau nom : ");
        //On affiche la fenêtre
        Optional<String> nouveauNom = fenetreRenommage.showAndWait();

        //On renomme l'étape et on actualisse l'affichage
        nouveauNom.ifPresent(name -> monde.modifieLeNom(name));
    }
}
