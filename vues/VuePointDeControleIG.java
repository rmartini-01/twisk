package twisk.vues;

import javafx.scene.Cursor;
import javafx.scene.shape.Circle;
import twisk.mondeIG.PointDeControleIG;
import twisk.mondeIG.SujetObserve;
import twisk.ecouteurs.EcouteurVuePointDeControle;
import twisk.outils.TailleComposants;

public class VuePointDeControleIG extends Circle {
    private PointDeControleIG pointDeControle;
    private SujetObserve sujet;

    /**
     * Constructeur d'une vue sur un point de contrôle à partir des coordonnées de son centre
     * @param pointDeControle Point de contrôle associé à la vue
     * @param sujet Sujet à observer
     */
    public VuePointDeControleIG(PointDeControleIG pointDeControle, SujetObserve sujet) {
        super(pointDeControle.getCentreX(), pointDeControle.getCentreY(), TailleComposants.getInstance().getRayonPointDeC());
        this.pointDeControle = pointDeControle;
        this.sujet = sujet;
        this.setCursor(Cursor.HAND);
        this.setOnMouseClicked(new EcouteurVuePointDeControle(sujet,pointDeControle));
        if (pointDeControle.estSelectionne()){
            this.setStyle("-fx-fill : red;");
        } else {
            this.setStyle("-fx-fill : black;");
        }
    }
}
