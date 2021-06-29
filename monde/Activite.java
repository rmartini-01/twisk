package twisk.monde;

import java.util.Iterator;

public class Activite extends Etape{
    protected int temps;
    protected int ecartTemps;

    /**
     * Conctructeur d'une activité
     * @param nom de l'activité
     */
    public Activite(String nom){
        super(nom);
        temps = 5; //On fixe le temps à 5s
        ecartTemps = 1;//On fixe l'écart de temps à 1s
    }

    /**
     * Constructeur d'une activité ayant un temps limité et un temps d'écart.
     * @param nom de l'activité
     * @param t durée de l'activité
     * @param e écart temps de l'activité
     */
    public Activite(String nom, int t, int e){
        super(nom);
        temps = t;
        ecartTemps = e;
    }

    /**
     * Retourne la durée d'une étape
     * @return La durée
     */
    public int getTemps() {
        return temps;
    }

    /**
     * Retourne l'écart de temps d'une étape
     * @return L'écart de temps
     */
    public int getEcartTemps() {
        return ecartTemps;
    }

    @Override
    public boolean estUneActivite(){
        return true;
    }

    @Override
    public String toC() {
        assert(this.successeurs.iterator().hasNext() ): "L'étape n'a pas de successeurs.";
        StringBuilder codeC = new StringBuilder(70*nbSuccesseurs());
        Iterator<Etape> iter = successeurs.iterator();
        Etape successeur;

        if(this.estUnSasDEntree()){
            // faire fonction dans sasEntrée
            codeC.append(((SasEntree)this).delaiLoi());
        }else{
            codeC.append("delai(").append(this.temps).append(",").append(this.ecartTemps).append(");\n");
        }
        if (nbSuccesseurs()>1) {//On met un switch si l'activité a plusieurs successeurs
            codeC.append("nb = (int) ( (rand() / (float) RAND_MAX ) * ").append(nbSuccesseurs()).append(");\n");
            codeC.append("switch(nb){\n");
            for (int i = 0; i<nbSuccesseurs();i++){
                codeC.append("case ").append(i).append(" :\n");
                successeur = iter.next();

                codeC.append("transfert(").append(nomToConstante()).append(",").append(successeur.nomToConstante()).append(");\n");
                codeC.append(successeur.toC());
                codeC.append("break;\n");
            }
            codeC.append("}\n");
        } else {//Si il n'y a qu'un seul successeur, on ne fait pas de switch
            successeur = iter.next();
            //transfert des clients de l'étape actuelle à l'étape suivante
            codeC.append("transfert(").append(nomToConstante()).append(",").append(successeur.nomToConstante()).append(");\n");
            codeC.append(successeur.toC());
        }

        return codeC.toString();
    }

}
