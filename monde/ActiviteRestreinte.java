package twisk.monde;

import java.util.Iterator;

public class ActiviteRestreinte extends Activite {
    /**
     * constructeur avec un nom
     * @param nom nom de l'activité
     */
    public ActiviteRestreinte(String nom) {
        super(nom);
    }

    /**
     * Constructueur d'une activité restreinte avec un nom, une durée et un écart de temps
     * @param nom nom de l'activité
     * @param t durée de l'activité
     * @param e écart de temps
     */
    public ActiviteRestreinte(String nom, int t, int e) {
        super(nom, t, e);
    }

    @Override
    public String toC() {
        assert(successeurs.iterator().hasNext()):"L'étape n'a aucun successeur";
        StringBuilder str = new StringBuilder(25*nbSuccesseurs()); //Donné à titre indicatif
        Iterator<Etape> iter = successeurs.iterator();
        Etape successeur;

        if (nbSuccesseurs()>1) {
            str.append("nb = (int) ( (rand() / (float) RAND_MAX ) * ").append(nbSuccesseurs()).append(");\n");
            str.append("switch(nb){\n");
            for (int i = 0; i<nbSuccesseurs();i++){
                str.append("case ").append(i).append(" :\n");
                successeur = iter.next();

                str.append("transfert(").append(nomToConstante()).append(",").append(successeur.nomToConstante()).append(");\n");
                str.append(successeur.toC());
                str.append("break;\n");
            }
            str.append("}\n");
        } else {//Si il n'y a qu'un seul successeur, on ne fait pas de switch
            successeur = iter.next();
            //transfert des clients de l'étape actuelle à l'étape suivante
            str.append("transfert(").append(nomToConstante()).append(",").append(successeur.nomToConstante()).append(");\n");
            str.append(successeur.toC());
        }
        return str.toString();
    }

    @Override
    public String toCDelai() {
        StringBuilder str = new StringBuilder(16); //Donné à titre indicatif
        str.append("delai(").append(getTemps()).append(",").append(getEcartTemps()).append(");\n");
        return str.toString();
    }
}
