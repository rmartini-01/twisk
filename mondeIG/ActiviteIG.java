package twisk.mondeIG;

public class ActiviteIG extends EtapeIG{
    private int delai;
    private int ecart;
    private boolean estActiviteRestreinte;
    /**
     * Constructeur d'un affichage d'une activité
     * @param nom Nom de l'activité
     * @param idf Identifiant de l'activité
     * @param larg Largeur de l'activité à l'écran
     * @param haut Hauteur de l'activité à l'écran
     */
    public ActiviteIG(String nom, String idf, int larg, int haut) {
        super(nom, idf, larg, haut);
        delai = 5;
        ecart = 2;
        estActiviteRestreinte = false;
    }

    @Override
    public boolean estUneActivite() {
        return true;
    }

    /**
     * Modifie le délai de l'activité par le nombre en paramètre
     * @param delai nouvelle durée de l'activité
     */
    public void setDelai(int delai) {
        this.delai = delai;
    }

    /**
     * Modifie l'écart de temps de l'activité par l'entier donné en paramètre
     * @param ecart Nouvel écart de temps de l'activité
     */
    public void setEcart(int ecart) {
        this.ecart = ecart;
    }

    /**
     * Retourne l'écart de temps de l'activité
     * @return L'écart de temps
     */
    public int getEcart() {
        return ecart;
    }

    /**
     * Retourne le délai de l'activité
     * @return Le délai
     */
    public int getDelai() {
        return delai;
    }

    @Override
    public boolean estUneActiviteRestreinte() {
        return estActiviteRestreinte;
    }

    /**
     * Setter
     * @param b boolean pour dire si l'activité est restreinte ou non.
     */
    public void setEstUneActiviteRestreinte(boolean b){
        this.estActiviteRestreinte = b;
    }



}
