package twisk.mondeIG;

public class GuichetIG extends EtapeIG{
    private int nbJetons;
    private boolean sensDeCirculation;

    /**
     * Constructeur d'un affichage d'une activité
     *
     * @param nom  Nom de l'activité
     * @param idf  Identifiant de l'activité
     * @param larg Largeur de l'activité à l'écran
     * @param haut Hauteur de l'activité à l'écran
     */
    public GuichetIG(String nom, String idf, int larg, int haut) {
        super(nom, idf, larg, haut);
        nbJetons = 5;
        sensDeCirculation = true;
    }

    /**
     * getter du sens de circulation
     * @return vrai si les clients avancent de la gauche vers la droite, faux sinon
     */
    public boolean isSensDeCirculation() {
        return sensDeCirculation;
    }

    /**
     * savoir dans quel sens le guichet est fait
     * @param source étape de laquelle l'arc part
     * @param destination étape vers laquelle l'arc arrive
     */
    public void quelSensDeCirculation(EtapeIG source, EtapeIG destination){
        if(source.estUnGuichet()){
            ((GuichetIG) source).setSensDeCirculation(source.getPosX() <= destination.getPosX());
        }
    }

    /**
     * mise à jour du sens de circulation
     * @param sensDeCirculation le nouveau sens de circulation
     */
    public void setSensDeCirculation(boolean sensDeCirculation) {
        this.sensDeCirculation = sensDeCirculation;
    }

    /**
     * Retourne le nombre de jetons du guichet
     * @return Nombre de jetons du guichet
     */
    public int getNbJetons() {
        return nbJetons;
    }

    /**
     * Modifie le nombre de jetons du guichet
     * @param nbJetons nouveau nombre de jetons
     */
    public void setNbJetons(int nbJetons) {
        this.nbJetons = nbJetons;
    }

    @Override
    public boolean estUnGuichet() {
        return true;
    }
}
