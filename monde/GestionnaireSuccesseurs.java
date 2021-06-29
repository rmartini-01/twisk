package twisk.monde;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;

public class GestionnaireSuccesseurs implements Iterable<Etape>{
    private LinkedList<Etape> successeurs;

    /**
     * Constructeur
     */
    public GestionnaireSuccesseurs(){
        successeurs = new LinkedList<Etape>();
    }

    /**
     * ajouter des étapes à la suite
     * @param etape un certain nombre d'étapes à ajouter à la suite.
     */
    public void ajouter(Etape... etape){
        successeurs.addAll(Arrays.asList(etape));
    }

    /**
     * @return nombre d'étapes suivantes
     */
    public int nbEtapes(){
        return successeurs.size();
    }

    @Override
    public Iterator<Etape> iterator() {
        return successeurs.iterator();
    }

    @Override
    public String toString() {
        int length = 0;
        for (Etape e : this){
            length += e.nom.length()+1;
        }
        StringBuilder nom = new StringBuilder(length);
        for (Etape e : successeurs){
            nom.append(" - ");
            nom.append(e.nom);
        }
        return nom.toString();
    }
}
