package twisk.outils;

public class FabriqueNumero {
    private int cptEtape;
    private int cptSemaphore;
    private int cptNumLib;
    private int cptSauvegardeMonde;
    private static FabriqueNumero instance = new FabriqueNumero();

    /**
     * Constructeur du singleton
     */
    private FabriqueNumero(){
        cptEtape = 0;
        cptSemaphore = 1;
        cptNumLib = 0;
        cptSauvegardeMonde = 1;
    }

    /**
     * Retourne l' instance de la fabrique
     * @return Instance de la fabrique
     */
    public static FabriqueNumero getInstance() {
        return instance;
    }

    /**
     * Retourne le numéro de l'étape et incrémente le numéro
     * @return Le numéro de l'étape
     */
    public int getNumeroEtape(){
        return cptEtape++;
    }

    /**
     * Retourne le numéro de la librairie et l'incrémente de un
     * @return le numéro de la librairie
     */
    public int getNumeroLibrairie(){
        return  cptNumLib++;
    }
    /**
     * Retourne le numéro du guichet et incrémente le numéro
     * @return Le numéro du guichet
     */
    public int getCptSemaphore() {
        return cptSemaphore++;
    }

    /**
     * Réinitialise le compteur d'étapes
     */
    public void reset(){
        cptEtape = 0;
        cptSemaphore = 1;
    }

    /**
     * Retourne le numéro du monde sauvegardé
     * @return Le numéro
     */
    public int getCptSauvegardeMonde() {
        return cptSauvegardeMonde++;
    }

    @Override
    public String toString() {
        return "FabriqueNumero{" +
                "cptEtape=" + cptEtape +
                ", cptSemaphore=" + cptSemaphore +
                '}';
    }
}
