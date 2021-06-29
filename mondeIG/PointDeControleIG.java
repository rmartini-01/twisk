package twisk.mondeIG;

import java.io.Serializable;

public class PointDeControleIG implements Serializable {
    private int centreX;
    private int centreY;
    private String identifiant;
    private EtapeIG etape;
    private boolean selectionne;

    /**
     * Constructeur d'un point de contrôle
     * @param centreX Abscisse du centre du point
     * @param centreY Ordonnée du centre du point
     * @param identifiant Identifiant du point de contrôle
     * @param etape Etape sur lequel est le point de contrôle
     */
    public PointDeControleIG(int centreX, int centreY, String identifiant, EtapeIG etape) {
        this.centreX = centreX;
        this.centreY = centreY;
        this.identifiant = identifiant;
        this.etape = etape;
        selectionne = false;
    }
    /**
     * Retourne la position du centre en abscisse à partir du coin haut gauche
     * @return Position du centre en abscisse
     */
    public int getCentreX() {
        return centreX;
    }

    /**
     * Retourne la position du centre en ordonnée à partir du coin haut gauche
     * @return Position du centre en ordonnée
     */
    public int getCentreY() {
        return centreY;
    }

    /**
     * Retourne l'identifiant du point de contrôle
     * @return L'identifiant
     */
    public String getIdentifiant() {
        return identifiant;
    }

    /**
     * Retourne l'étape sur lequel se trouve le point de contrôle
     * @return L'étape associée au point de contrôle
     */
    public EtapeIG getEtape() {
        return etape;
    }

    /**
     * Retourne si les deux points de contrôle (le récepteur et l'argument) se trouve sur la même étape
     * @param pointDeC Le second point de contrôle
     * @return True si les deux points sont sur la même étape
     */
    public boolean estSurLaMemeEtapeQue (PointDeControleIG pointDeC){
        return etape.equals(pointDeC.etape);
    }

    /**
     * Déplace un point de contrôle aux coordonnées données en paramètre
     * @param centreX Nouvelle coordonnée du centre en abscisse
     * @param centreY Nouvelle coordonnée du centre en ordonnée
     */
    public void deplacerPointDeControle(int centreX, int centreY){
        this.centreX = centreX;
        this.centreY = centreY;
    }

    /**
     * Indique si le point de contrôle est sélectionné
     * @return True si le point de contrôle est sélectionné, false sinon
     */
    public boolean estSelectionne() {
        return selectionne;
    }

    /**
     * Modifie la sélection du point de contrôle
     */
    public void changeLaSelection(){
        selectionne = !selectionne;
    }
}
