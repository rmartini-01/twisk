package twisk.monde;

import twisk.outils.FabriqueNumero;


public class Guichet extends Etape {
    private int nbjetons;
    private int nbSemaphore;

    /**
     * Constructeur d'un guichet
     * @param nom le nom du guichet
     */
    public Guichet(String nom){
        super(nom);
        nbjetons = 5;
        nbSemaphore = FabriqueNumero.getInstance().getCptSemaphore();
    }

    /**
     * Constructeur d'un guichet avec un nom et un nombre de jetons
     * @param nom nom du guichet
     * @param nb nombre des jetons
     */
    public Guichet(String nom, int nb){
        super(nom);
        this.nbjetons = nb;
        nbSemaphore = FabriqueNumero.getInstance().getCptSemaphore();
    }

    @Override
    public boolean estUnGuichet() {
        return true;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public String toC() {
        assert(successeurs.iterator().hasNext()):"L'étape n'a aucun successeur";
        //On ajoute les lignes pour un guichet au code C
        StringBuilder str = new StringBuilder(50);  //Donné à titre indicatif
        Etape successeur = successeurs.iterator().next();

        str.append("P(ids,").append("SEM_").append(nomToConstante());
        str.append(");\ntransfert(").append(nomToConstante()).append(",").append(successeur.nomToConstante()).append(");\n");
        str.append(successeur.toCDelai());
        str.append("V(ids,").append("SEM_").append(nomToConstante()).append(");\n");
        //On appelle toC du successeur pour avoir les lignes suivantes du code C
        // (celles générées par le successeur)
        str.append(successeur.toC());
        return str.toString();
    }

    @Override
    public int getNbSemaphore() {
        return nbSemaphore;
    }

    @Override
    public int getNbjetons() {
        return nbjetons;
    }


    @Override
    public String toCConstante() {
        //On récupère la ligne de C s'occupant de la constante de l'étape
        String constanteEtape = super.toCConstante();
        StringBuilder codeC = new StringBuilder(constanteEtape.length()+17+nom.length());
        codeC.append(constanteEtape);
        //On ajoute la ligne de la constante du guichet
        codeC.append("#define SEM_").append(nomToConstante()).append(" ").append(nbSemaphore).append("\n");
        return codeC.toString();
    }
}
