package twisk.monde;

public class SasSortie extends Activite{
    /**
     * Constructeur
     */
    public SasSortie(){
        super("Sortie");
    }

    @Override
    public String toC (){
        return "";
    }

    @Override
    public boolean estUnSasDeSortie() {
        return true;
    }
}
