package twisk.vues;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import twisk.mondeIG.EtapeIG;
import twisk.mondeIG.GuichetIG;
import twisk.mondeIG.SujetObserve;
import twisk.outils.TailleComposants;

public class VueGuichetIG extends VueEtapeIG{
    private Label jetons;
    private HBox clients;

    /**
     * Constructeur d'une vue sur un guichet
     *
     * @param sujet Sujet à observer
     * @param etape Guichet associé à la vue
     */
    public VueGuichetIG(SujetObserve sujet, EtapeIG etape) {
        super(sujet, etape);
        setPrefHeight(TailleComposants.getInstance().getHauteurGuichet());
        setPrefWidth(TailleComposants.getInstance().getLargeurGuichet());
        titre.setTextFill(Color.valueOf("#2B7C2F"));
        if (!etape.isSelectionnee()){
            this.setStyle(getStyle()+"-fx-border-color: lightgreen;");
        }

        jetons = new Label(((GuichetIG) etape).getNbJetons()+" jetons");
        jetons.setStyle("-fx-font-weight: bold; -fx-font-family:Arial;-fx-font-size:11px;");
        clients = new HBox();
        clients.setAlignment(Pos.CENTER);

        //Espacement entre chaque carré
        clients.setSpacing(2);
        for (int i = 0; i<10;i++) {
            Rectangle carre = new Rectangle(15,15);
            carre.setStyle("-fx-fill : linear-gradient(to top left, #AEABAB, #DFDADA); -fx-stroke : black;");
            clients.getChildren().add(carre);
        }
        getChildren().addAll(jetons,clients);
    }

    @Override
    public void reagir() {

    }

    @Override
    public void etatDeLASimulation() {

    }
}
