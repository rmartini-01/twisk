package twisk.mondeIG;

import twisk.outils.FabriqueNumero;
import twisk.outils.TailleComposants;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

public abstract class EtapeIG implements Iterable<PointDeControleIG>,Serializable {

    private String nom;
    private String identifiant;

    private int posX;
    private int posY;
    private int largeur;
    private int hauteur;
    private ArrayList<PointDeControleIG> pointsDeControle;
    private boolean selectionnee;
    private boolean estUneEntree;
    private boolean estUneSortie;
    private LinkedList<EtapeIG> successeurs;


    /**
     * Constructeur d'un affichage d'une étape
     * @param nom Nom de l'étape
     * @param idf Identifiant de l'étape
     * @param larg Largeur de l'étape à l'écran
     * @param haut Hauteur de l'affichage
     */
    protected EtapeIG(String nom,String idf,int larg, int haut) {
        this.nom = nom;
        identifiant = idf;
        largeur = larg;
        hauteur = haut;
        Random rand = new Random();
        posX = rand.nextInt(TailleComposants.getInstance().getLargeurFenetre()-larg);
        posY = rand.nextInt(TailleComposants.getInstance().getHauteurFenetre()-2*haut);
        selectionnee = false;
        pointsDeControle = new ArrayList<>(4);
        //Le point du haut de l'étape
        pointsDeControle.add(new PointDeControleIG(posX+(largeur/2),posY,identifiant+"H",this));
        //Le point à gauche de l'étape
        pointsDeControle.add(new PointDeControleIG(posX,posY+(hauteur/2),identifiant+"G",this));
        //Le point du bas de l'étape
        pointsDeControle.add(new PointDeControleIG(posX+(largeur/2),posY+hauteur,identifiant+"B",this));
        //Le point à droite de l'étape
        pointsDeControle.add(new PointDeControleIG(posX+largeur,posY+(hauteur/2),identifiant+"D",this));
        estUneEntree = false;
        estUneSortie = false;
        successeurs = new LinkedList<EtapeIG>();
    }

    /**
     * Retourne l'identifiant de l'étape
     * @return L'identifiant
     */
    public String getIdentifiant() {
        return identifiant;
    }

    /**
     * Retourne le nom de l'étape
     * @return Le nom
     */
    public String getNom() {
        return nom;
    }

    /**
     * Renomme l'étape avec le nom donné en paramètre
     * @param nom Nouveau nom de l'étape
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Retourne la position en abscisse du point en haut à gauche de l'étape
     * @return Valeur de l'abscisse du point haut gauche
     */
    public int getPosX() {
        return posX;
    }
    /**
     * Retourne la position en ordonnée du point en haut à gauche de l'étape
     * @return Valeur de l'ordonnée du point haut gauche
     */
    public int getPosY() {
        return posY;
    }

    /**
     * Remplace la valeur en abscisse du point en haut à gauche de l'étape par celle donnée en paramètre
     * @param posX Nouvelle postion en abscisse
     */
    public void setX(int posX) {
        this.posX = posX;
    }
    /**
     * Remplace la valeur en ordonnée du point en haut à gauche de l'étape par celle donnée en paramètre
     * @param posY Nouvelle postion en ordonnée
     */
    public void setY(int posY) {
        this.posY = posY;
    }

    /**
     *  Retourne les successeurs de l'étape
     * @return les successeurs de l'étape
     */
    public LinkedList<EtapeIG> getSuccesseurs() {
        return successeurs;
    }

    /**
     * Retourne le nombre de successeurs
     * @return Nombre de successeurs
     */
    public int getNbSuccesseurs(){
        return successeurs.size();
    }

    /**
     * modifie les successeurs de l'étape
     * @param successeurs nouveaux successeurs
     */
    public void setSuccesseurs(LinkedList<EtapeIG> successeurs) {
        this.successeurs = successeurs;
    }

    /**
     * Ajoute l'étape passée en paramètre aux successeurs de l'étape
     * @param e L'étape à ajouter aux successeurs
     */
    public void ajouterSuccesseur(EtapeIG e){
        this.successeurs.add(e);
    }

    /**
     * Supprime l'étape passée en paramètre des successeurs de l'étape
     * @param e L'étape à supprimer des successeurs
     */
    public void supprimerSuccesseur(EtapeIG e){
        this.successeurs.remove(e);
    }

    /**
     * Retourne un itérateur sur les points de contrôle
     * @return Un itérateur sur les points de contrôle de l'étape
     */
    public Iterator<PointDeControleIG> iterator(){
        return pointsDeControle.iterator();
    }

    /**
     * Retourne si l'étape est sélectionnée
     * @return True si l'étape est sélectionnée, false sinon
     */
    public boolean isSelectionnee() {
        return selectionnee;
    }

    /**
     * Modifie si l'étape est sélectionnée en fonction de l'argument
     * @param selectionnee Nouvel état de sélection de l'étape
     */
    public void setSelectionnee(boolean selectionnee) {
        this.selectionnee = selectionnee;
    }

    /**
     * Retourne si un arc, en paramètre, relie l'étape (le récepteur) et une autre étape
     * @param arc Un arc
     * @return true si l'arc est lié à l'étape, false sinon
     */
    public boolean estSurLArc(ArcIG arc){
        Iterator<PointDeControleIG> iter = iterator();
        PointDeControleIG pdc;
        boolean estLie = false;
        //On vérifie pour chaque point de contrôle si l'arc ne possède pas ce point
        while (iter.hasNext() && !estLie){
            pdc = iter.next();
            if (pdc.equals(arc.getPoint1()) || pdc.equals(arc.getPoint2())){
                estLie = true;
            }
        }
        return estLie;
    }

    /**
     * Déplace une étape et ses points de contrôle aux coordonnées passées en paramètre
     * @param posX Nouvelle position de l'abscisse du point haut gauche de l'étape
     * @param posY Nouvelle postion de l'ordonnée du point haut gauche de l'étape
     */
    public void deplacerEtape(int posX, int posY){
        setX(posX);
        setY(posY);
        for(PointDeControleIG pdc : this) {
            if (pdc.getIdentifiant().equals(identifiant+"H")){
                pdc.deplacerPointDeControle(posX+(largeur/2),posY);
            }
            if (pdc.getIdentifiant().equals(identifiant+"G")){
                pdc.deplacerPointDeControle(posX,posY+(hauteur/2));
            }
            if (pdc.getIdentifiant().equals(identifiant+"D")){
                pdc.deplacerPointDeControle(posX+largeur,posY+(hauteur/2));
            }
            if (pdc.getIdentifiant().equals(identifiant+"B")){
                pdc.deplacerPointDeControle(posX+(largeur/2),posY+hauteur);
            }
        }
    }

    /**
     * Modifie si l'étape est une entrée
     */
    public void changeEntree(){
        estUneEntree = !estUneEntree;//Si on sélectionne une entrée, elle n'est plus une entrée
    }

    /**
     * Modifie si l'étape est une sortie
     */
    public void changeSortie(){
        estUneSortie = !estUneSortie;
    }

    /**
     * Indique si l'étape est une activité
     * @return True si l'étape est une activité, false sinon
     */
    public boolean estUneActivite(){
        return false;
    }


    /**
     * getter
     * @return vrai si l'activité est précédée par un guichet, faux sinon
     */
    public boolean estUneActiviteRestreinte() {
        return false;
    }

    /**
     * Retourne si l'étape est une entrée
     * @return true si l'étape est une entrée, sinon false
     */
    public boolean estUneEntree() {
        return estUneEntree;
    }

    /**
     * Retourne si l'étape est une sortie
     * @return true si l'étape est une sortie, sinon false
     */
    public boolean estUneSortie() {
        return estUneSortie;
    }

    /**
     * Retourne le point de contrôle dont le rang est numPoint de l'étape (Fonction utilisée seulement dans les tests)
     * @param numPoint Rang du point de contrôle
     * @return Le point de contrôle
     */
    public PointDeControleIG getPointDeControle(int numPoint){
        PointDeControleIG point = null;
        if (numPoint>=0 && numPoint<4) {
            point = pointsDeControle.get(numPoint);
        }
        return point;
    }

    /**
     * Retourne si l'étape est un guichet
     * @return True si l'étape est un guichet, false sinon
     */
    public boolean estUnGuichet() {
        return false;
    }

    /**
     * Retourne la largeur de l'étape
     * @return Largeur de l'étape
     */
    public int getLargeur() {
        return largeur;
    }
    /**
     * Retourne la hauteur de l'étape
     * @return Hauteur de l'étape
     */
    public int getHauteur() {
        return hauteur;
    }

    /**
     * Retourne un itérateur sur les successeurs de l'étape
     * @return L'itérateur
     */
    public Iterator<EtapeIG> iteratorSuccesseurs(){
        return successeurs.iterator();
    }

    /**
     * Retourne si le receveur est accessible à partir de l'étape en paramètre
     * @param etape Etape
     * @return true si on peut atteindre le receveur à partir de l'étape etape, false sinon
     */
    public boolean estAccessibleDepuis(EtapeIG etape){
        boolean accessible = false;
        Iterator<EtapeIG> iter = etape.iteratorSuccesseurs();
        EtapeIG successeur;
        while (iter.hasNext() && !accessible){
            successeur = iter.next();
            if (successeur.equals(this)){
                accessible = true;
            } else{
                accessible = this.estAccessibleDepuis(successeur);
            }

        }
        return accessible;
    }

    /**
     * Retourne si l'étape a comme successeur l'étape IG en paramètre
     * @param etape Le successeur
     * @return true si etape est un successeur de l'étape
     */
    public boolean aCommeSuccesseur (EtapeIG etape){
        return successeurs.contains(etape);
    }

}
