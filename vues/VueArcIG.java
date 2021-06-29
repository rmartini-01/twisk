package twisk.vues;

import javafx.scene.Cursor;
import javafx.scene.layout.Pane;

import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polyline;
import twisk.mondeIG.ArcIG;
import twisk.mondeIG.MondeIG;
import twisk.mondeIG.SujetObserve;

public abstract class VueArcIG extends Pane {
    private ArcIG arc;
    protected SujetObserve monde;

    /**
     * Constructeur d'une vue sur un arc
     * @param arc L'arc avec lequel la vue est associée
     * @param sujet Sujet à observer
     */
    public VueArcIG(SujetObserve sujet, ArcIG arc){
        super();
        this.arc = arc;
        this.monde = sujet;
        this.setOnMouseClicked(event-> ((MondeIG) sujet).selectionArc(arc));

        this.setPickOnBounds(false);
    }

}
