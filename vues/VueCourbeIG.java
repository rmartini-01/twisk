package twisk.vues;

import javafx.scene.Cursor;
import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.Polyline;
import twisk.mondeIG.ArcIG;
import twisk.mondeIG.MondeIG;
import twisk.mondeIG.SujetObserve;

public class VueCourbeIG extends VueArcIG{

    /**
     * Constructeur d'un arc courbe
     * @param sujet Les données du monde
     * @param arc L'arc de base
     */
    public VueCourbeIG(SujetObserve sujet, ArcIG arc) {
        super(sujet, arc);

        double x1 = arc.getPoint1().getCentreX();
        double y1 = arc.getPoint1().getCentreY();

        double x2 = arc.getPointIntermediaire1().getX();
        double y2 = arc.getPointIntermediaire1().getY();

        double x3 = arc.getPointIntermediaire2().getX();
        double y3 = arc.getPointIntermediaire2().getY();

        double x4 = arc.getPoint2().getCentreX();
        double y4 =arc.getPoint2().getCentreY();
        CubicCurve curve = new CubicCurve(x1, y1, x2, y2, x3, y3, x4, y4);
        curve.setStroke(Color.BLACK);
        curve.setFill(Color.TRANSPARENT);

        //On calcule la position des deux points à la base du triangle (le sommet/ étant le point de fin de la ligne)
        double angle = Math.atan2((y4 - y3), (x4 - x3)) - Math.PI / 2.0;
        double sin = Math.sin(angle);
        double cos = Math.cos(angle);
        double a = (- 1.0 / 2.0 * cos + Math.sqrt(3) / 2 * sin) * 20 + x4;
        double b = (- 1.0 / 2.0 * sin - Math.sqrt(3) / 2 * cos) * 20 + y4;
        double c = (1.0 / 2.0 * cos + Math.sqrt(3) / 2 * sin) * 20 + x4;
        double d = (1.0 / 2.0 * sin - Math.sqrt(3) / 2 * cos) * 20 + y4;

        Polyline triangle = new Polyline(x4, y4, a, b, c, d, x4, y4);

        //L'arc réagit quand on clique dessus (on le sélectionne)
        this.setOnMouseClicked(event-> ((MondeIG) sujet).selectionArc(arc));
        curve.setCursor(Cursor.HAND);
        triangle.setCursor(Cursor.HAND);
        if (arc.isSelectionnee()){
            triangle.setStyle("-fx-stroke: red; -fx-fill : red; -fx-stroke-width : 3 ;");
            curve.setStyle(" -fx-stroke-width : 3 ; -fx-stroke: red");
        } else {
            triangle.setStyle("-fx-fill : black; -fx-stroke-width : 3 ;");
            curve.setStyle("-fx-stroke-width : 3 ;");
        }
        this.getChildren().addAll(curve, triangle);
    }
}
