package twisk.outils;

public class TailleComposants {
    private int largeurActivite;
    private int hauteurActivite;
    private int largeurGuichet;
    private int hauteurGuichet;
    private int rayonPointDeC;
    private int largeurFenetre;
    private int hauteurFenetre;
    private static TailleComposants instance = new TailleComposants();

    /**
     * Constructeur privé du singleton
     */
    private TailleComposants(){
        largeurActivite = 130;
        hauteurActivite = 100;
        largeurGuichet = 200;
        hauteurGuichet = 70;
        rayonPointDeC = 5;
        largeurFenetre = 900;
        hauteurFenetre = 700;
    }

    /**
     * Retourne l'instance de la classe
     * @return L'instance de la classe
     */
    public static TailleComposants getInstance(){
        return instance;
    }

    /**
     * Retourne la largeur d'un affichage d'une activité
     * @return La largeur
     */
    public int getLargeurActivite() {
        return largeurActivite;
    }

    /**
     * Retourne la hauteur d'un affichage d'une activité
     * @return La Hauteur
     */
    public int getHauteurActivite() {
        return hauteurActivite;
    }

    /**
     * Retourne le rayon d'une d'un point de contrôle
     * @return La valeur du rayon
     */
    public int getRayonPointDeC() {
        return rayonPointDeC;
    }

    /**
     * Retourne la largeur de la fenêtre
     * @return Largeur de la fenêtre
     */
    public int getLargeurFenetre() {
        return largeurFenetre;
    }
    /**
     * Retourne la hauteur de la fenêtre
     * @return Hauteur de la fenêtre
     */
    public int getHauteurFenetre() {
        return hauteurFenetre;
    }
    /**
     * Retourne la hauteur d'un affichage d'un guichet
     * @return La Hauteur
     */
    public int getHauteurGuichet() {
        return hauteurGuichet;
    }

    /**
     * Retourne la largeur d'un affichage d'un guichet
     * @return La largeur
     */
    public int getLargeurGuichet() {
        return largeurGuichet;
    }
}
