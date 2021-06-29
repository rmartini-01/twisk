package twisk.monde;

public class SasEntree extends Activite{
    private String loi;

    /**
     * Constructeur
     */
    public SasEntree(){
        super("Entrée");
    }

    @Override
    public String toC(){
        StringBuilder codeC = new StringBuilder(30);
        codeC.append("entrer(").append(nomToConstante()).append(");\n");
        codeC.append(super.toC());
        return codeC.toString();
    }

    @Override
    public boolean estUnSasDEntree() {
        return true;
    }

    /**
     * Setter de la loi
     * @param loi nouvelle loi
     */
    public void setLoi(String loi) {
        this.loi = loi;
    }

    /**
     * Délai dans le sas entrée selon la loi choisie par l'utilisateur.
     * @return le délai à effectuer.( en C)
     */
    public  String delaiLoi(){
        StringBuilder codeC = new StringBuilder();
        //attente(delaiExponentiel(0.1));
        if(loi.equals("Uniforme")){
            codeC.append("attente(delaiUniforme(").append(this.temps).append(",").append(this.ecartTemps).append("));\n");
        }else if (loi.equals("Gaussienne")){
            codeC.append("attente(delaiGauss(").append(this.temps).append(",").append(this.ecartTemps).append("));\n");
        }else if(loi.equals("Exponentielle")){
            codeC.append("attente(delaiExponentiel(").append(1/temps).append("));\n");
        }
        return codeC.toString();
    }
}
