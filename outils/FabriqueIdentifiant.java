package twisk.outils;

public class FabriqueIdentifiant {
    private int noEtape = 0;
    private static FabriqueIdentifiant instance = new FabriqueIdentifiant();

    /**
     * Retourne l'instance de la classe
     * @return L'instance de la classe
     */
    public static FabriqueIdentifiant getInstance(){
        return instance;
    }

    /**
     * Retourne l'identifiant d'une étape
     * @return L'identifiant fabriqué
     */
    public String getIdentifiantEtape(){
        return Integer.toString(noEtape++);//On convertit le numéro d'étape en chaîne de caractères
    }

    public void reset(){
        noEtape = 0;
    }
}
