package twisk.vues;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import twisk.mondeIG.ActiviteIG;
import twisk.mondeIG.EtapeIG;
import twisk.mondeIG.SujetObserve;
import twisk.outils.TailleComposants;

public class VueActiviteIG extends VueEtapeIG implements Observateur {
    private HBox box;
    private Label temps;


    /**
     * Constructeur d'une vue d'une activité
     * @param sujet Sujet à observer
     * @param etape Activité associée à la vue
     */
    public VueActiviteIG(SujetObserve sujet, EtapeIG etape) {
        super(sujet, etape);
        setPrefHeight(TailleComposants.getInstance().getHauteurActivite());
        setPrefWidth(TailleComposants.getInstance().getLargeurActivite());
        titre.setTextFill(Color.valueOf("#0072F7"));

       if (!etape.isSelectionnee()){
            this.setStyle(getStyle()+"-fx-border-color: lightblue;");
       }

        temps = new Label(((ActiviteIG) etape).getDelai()+" tps+/-"+((ActiviteIG) etape).getEcart());
        temps.setStyle("-fx-font-weight: bold; -fx-font-family:Arial;-fx-font-size:11px;");
        box = new HBox();
        box.setStyle("-fx-border-color: Black; -fx-background-color: linear-gradient(to top left, #AEABAB, #DFDADA);");
        box.setAlignment(Pos.CENTER);
        box.setPrefSize(100,50);
        getChildren().addAll(temps,box);
     }

    @Override
    public void reagir() {

    }

    @Override
    public void etatDeLASimulation() {

    }
}
