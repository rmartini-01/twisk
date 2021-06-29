package twisk.monde;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

public class GestionnaireEtapes implements Iterable<Etape>{
    private LinkedList<Etape> etapes;

    /**
     * Constructeur
     */
    public GestionnaireEtapes(){
        etapes = new LinkedList<>();
    }

    /**
     * Ajoute les étapes en arguments à l'ensemble des étapes du gestionnaire
     * @param etapes les étapes à ajouter
     */
    public void ajouter(Etape... etapes){
        Collections.addAll(this.etapes, etapes);
    }

    /**
     * Retourne le nombre d'étapes dans le gestionnaire
     * @return Le nombre d'étapes dans le gestionnaire
     */
    public int nbEtapes() {
        return etapes.size();
    }

    @Override
    public Iterator<Etape> iterator() {
        return etapes.iterator();
    }

    @Override
    public String toString() {
        int length = 0;
        //On détermine la taille du StringBuilder
        for (Etape e : this){
            length += e.toString().length()+1;
        }

        StringBuilder affichageDesEtapes = new StringBuilder(length);
        for (Etape e : etapes){
            affichageDesEtapes.append(e.toString());
            affichageDesEtapes.append("\n");
        }
        return affichageDesEtapes.toString();
    }
}
