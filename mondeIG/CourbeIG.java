package twisk.mondeIG;

import java.awt.*;

public class CourbeIG extends ArcIG {
    /**
     * Instantiates a new Courbe ig.
     *
     * @param p1 the p 1
     * @param p2 the p 2
     * @param p3 the p 3
     * @param p4 the p 4
     */
    public CourbeIG(PointDeControleIG p1, Point p2, Point p3, PointDeControleIG p4) {
        super(p1, p2, p3, p4);

    }
    @Override
    public boolean estUneLigne(){
        return false;
    }
}
