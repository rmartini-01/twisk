package twisk.vues;

import javafx.scene.Cursor;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polyline;
import twisk.mondeIG.ArcIG;
import twisk.mondeIG.MondeIG;
import twisk.mondeIG.SujetObserve;

public class VueLigneDroiteIG extends VueArcIG{
    /**
     * Constructeur d'un arc ligne
     * @param sujet Les données du monde
     * @param arc L'arc de base
     */
    public VueLigneDroiteIG(SujetObserve sujet, ArcIG arc) {
        super(sujet, arc);
        monde = sujet;

        double startX = arc.getPoint1().getCentreX();
        double startY= arc.getPoint1().getCentreY();
        double endX = arc.getPoint2().getCentreX();
        double endY = arc.getPoint2().getCentreY();
        //On calcule la position des deux points à la base du triangle (le sommet/ étant le point de fin de la ligne)
        double angle = Math.atan2((endY - startY), (endX - startX)) - Math.PI / 2.0;
        double sin = Math.sin(angle);
        double cos = Math.cos(angle);
        double x1 = (- 1.0 / 2.0 * cos + Math.sqrt(3) / 2 * sin) * 20 + endX;
        double y1 = (- 1.0 / 2.0 * sin - Math.sqrt(3) / 2 * cos) * 20 + endY;
        double x2 = (1.0 / 2.0 * cos + Math.sqrt(3) / 2 * sin) * 20 + endX;
        double y2 = (1.0 / 2.0 * sin - Math.sqrt(3) / 2 * cos) * 20 + endY;

        Polyline triangle = new Polyline(endX,endY,x1,y1,x2,y2,endX,endY);

        triangle.setCursor(Cursor.HAND);
        //On crée la ligne entre les deux activités
        Line ligne = new Line(startX, startY, endX, endY);
        this.getChildren().addAll(ligne, triangle);

        //L'arc réagit quand on clique dessus (on le sélectionne)
        ligne.setCursor(Cursor.HAND);
        this.setOnMouseClicked(event-> ((MondeIG) sujet).selectionArc(arc));
        if (arc.isSelectionnee()){
            triangle.setStyle("-fx-stroke: red; -fx-fill : red; -fx-stroke-width : 3 ;");
            ligne.setStyle(" -fx-stroke-width : 3 ; -fx-stroke: red");
        } else {
            triangle.setStyle("-fx-fill : black; -fx-stroke-width : 3 ;");
            ligne.setStyle("-fx-stroke-width : 3 ;");
        }
    }
}
