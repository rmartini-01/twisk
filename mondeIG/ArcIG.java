package twisk.mondeIG;

import twisk.outils.FabriqueNumero;

import java.awt.*;
import java.io.*;

public abstract class ArcIG implements Serializable {
    private PointDeControleIG point1;
    private PointDeControleIG point2;
    private Point pointIntermediaire1;
    private Point pointIntermediaire2;
    private boolean selectionnee;

    /**
     * Constructeur
     * @param point1 Point de départ de l'arc
     * @param point2 Point d'arrivée de l'arc
     */
    public ArcIG(PointDeControleIG point1, PointDeControleIG point2) {
        this.point1 = point1;
        this.point2 = point2;
        selectionnee = false;
    }

    /**
     *
     *
     * @param p1 premier point
     * @param p2 deuxième point
     * @param p3 troisième point
     * @param p4 quatrièeme point
     */
    public ArcIG(PointDeControleIG p1, Point p2, Point p3, PointDeControleIG p4){
        this.point1 = p1;
        this.pointIntermediaire1 = p2;
        this.pointIntermediaire2 = p3;
        this.point2 = p4;
        selectionnee = false;
    }
    /**
     * Retourne le point de départ de l'arc
     * @return Le point de départ
     */
    public PointDeControleIG getPoint1() {
        return point1;
    }
    /**
     * Retourne le point de départ d'arrivée
     * @return Le point d'arrivée
     */
    public PointDeControleIG getPoint2() {
        return point2;
    }

    /**
     * Retourne le premier point intermédiaire de la courbe
     * @return deuxième point de la courbe
     */
    public Point getPointIntermediaire1() {
        return pointIntermediaire1;
    }
    /**
     * Retourne le deuxième point intermédiaire de la courbe
     * @return troisième point de la courbe
     */
    public Point getPointIntermediaire2() {
        return pointIntermediaire2;
    }
    /**
     * Retourne vrai si l'arc est une ligne.
     * @return par défaut, l'arc est une ligne.
     */
    public  boolean estUneLigne(){
        return true;
    }

    /**
     * Definit si l'arc est selectionné ou non
     * @param selectionnee Variable indiquant si l'arc est selectionné
     */
    public void setSelectionnee(boolean selectionnee) {
        this.selectionnee = selectionnee;
    }

    /**
     * Indique si l'arc est selectionné ou non
     * @return True si l'arc est sélectionné, false sinon
     */
    public boolean isSelectionnee() {
        return selectionnee;
    }
}
