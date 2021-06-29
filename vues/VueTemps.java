package twisk.vues;

import javafx.application.Platform;

import javafx.scene.control.Label;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import twisk.mondeIG.MondeIG;
import twisk.mondeIG.SujetObserve;


public class VueTemps extends VBox implements Observateur{

    private SujetObserve monde;
    private Label temps;
    private Label modeSimulation;


    /**
     * Constructeur
     * @param m sujet observé monde
     */
    public VueTemps(SujetObserve m) {
        this.monde =  m;
        monde.ajouterObservateur(this);
        temps = new Label("Temps de la simulation : "+ ((MondeIG)monde).getTimer() + " secondes");
        temps.setStyle("-fx-font-weight: bold; -fx-font-family:Arial;-fx-font-size:13px; -fx-padding: 0 4 0 0 ; ");

        modeSimulation = new Label("Loi de la simulation: " + ((MondeIG)monde).getLoi());
        modeSimulation.setStyle("-fx-font-weight: bold; -fx-font-family:Arial;-fx-font-size:13px; -fx-padding: 0 4 0 0 ;");
        getChildren().addAll(temps, modeSimulation);
        definirLaVueCommeDestination();
        activeLeDepotDUneActivite();
    }

    @Override
    public void reagir() {
        Runnable command = new Runnable() {
            @Override
            public void run() {
                temps.setText("Temps de la simulation : " + ((MondeIG)monde).getTimer() + " secondes");
                modeSimulation.setText("Loi de la simulation: " + ((MondeIG)monde).getLoi());
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
     * Définit la vue comme une destination du drag n drop
     */
    private void definirLaVueCommeDestination(){
        //On définit la vue monde comme une destination (où on peut déplacer les activités)
        this.setOnDragOver(dragEvent->{
            if( dragEvent.getGestureSource() != this && dragEvent.getDragboard().hasString()){
                dragEvent.acceptTransferModes(TransferMode.MOVE);
            }
            dragEvent.consume();
        });
    }

    /**
     * Modifie les coordonnées de l'étape déposée au niveau de la vue
     */
    private void activeLeDepotDUneActivite(){
        //On fait réagir la vue lorsqu'une activité est déposée dans la vue
        this.setOnDragDropped(dragEvent->{
            Dragboard dragBoard = dragEvent.getDragboard();
            boolean success = false;
            if (dragBoard.hasString()){
                String id = dragBoard.getString();
                ((MondeIG) monde).deplacerEtape(id,(int) Math.round(dragEvent.getSceneX()),(int) Math.round(dragEvent.getSceneY())-29);
                success = true;
            }
            dragEvent.setDropCompleted(success);
            dragEvent.consume();
        });
    }

}
