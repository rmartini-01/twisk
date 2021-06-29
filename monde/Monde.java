package twisk.monde;

import twisk.outils.FabriqueNumero;

import java.util.Iterator;

public class Monde implements Iterable<Etape>{
    private SasSortie sortie;
    private SasEntree entree;
    private GestionnaireEtapes lesEtapes;


    /**
     * Constructeur de Monde
     */
    public Monde(){
        FabriqueNumero.getInstance().reset(); //On redémarrage le numéro d'étape à 0
        entree = new SasEntree();
        sortie = new SasSortie();
        lesEtapes = new GestionnaireEtapes();
        this.ajouter(entree, sortie); //sinon on aura 2 étapes de trop dans le monde
    }

    /**
     * définir la loi des clients à l'entrée
     * @param loi nouvelle loi
     */
    public void definirLoi(String loi){
        entree.setLoi(loi);
    }

    /**
     * Retourne le sas d'entrée du monde
     * @return le sas d'entrée
     */
    public SasSortie getSortie() {
        return sortie;
    }

    /**
     * Retourne le sas de sortie du monde
     * @return le sas de sortie
     */
    public SasEntree getEntree() {
        return entree;
    }

    /**
     * Ajoute les arguments comme successeurs du Sas d'entrée
     * @param etapes les successeurs du Sas d'entrée
     */
    public void aCommeEntree(Etape... etapes){
        entree.ajouterSuccesseur(etapes);
    }

    /**
     * Ajoute le Sas de sortie comme successeur des arguments
     * @param etapes les prédécesseur du Sas de sortie
     */
    public void aCommeSortie(Etape... etapes){
        for(Etape etape : etapes){
            if (etape.estUneActivite()) {//La sortie est obligatoirement une activité
                etape.ajouterSuccesseur(sortie);
            }
        }
    }

    /**
     * Ajoute les étapes etapes au monde
     * @param etapes les étapes à ajouter
     */
    public void ajouter(Etape... etapes){
        lesEtapes.ajouter(etapes);
    }

    /**
     * Retourne le nombre d'étapes dans le monde
     * @return Le nombre d'étapes dans le monde
     */
    public int nbEtapes(){
        return lesEtapes.nbEtapes();
    }

    /**
     * Retourne le nombre de successeurs de l'entrée
     * utile pour le test de aCommeEntree
     * @return nombre successeurs du sasEntree
     */
    public int nbSuccesseursEntree(){
        return entree.nbSuccesseurs();
    }

    /**
     * Retourne le nombre de prédécesseurs de la sortie
     * utile pour le test de aCommeEntree
     * @return nombre de prédécesseurs du sasSortie
     */
    public int nbSuccesseursSortie(){
        return sortie.nbSuccesseurs();
    }

    /**
     * Retourne le nombre de guichets
     * @return Le nombre de guichets
     */
    public int nbGuichets(){
        int nbGuichets = 0;
        for(Etape etape : this){
            if (etape.estUnGuichet()){
                nbGuichets++;
            }
        }
        return nbGuichets;
    }

    @Override
    public Iterator<Etape> iterator() {
        return lesEtapes.iterator();
    }

    @Override
    public String toString() {
        return "Monde{\nsortie=" + sortie.getNom() + ", entree=" + entree.getNom() +
                ",\nlesEtapes=\n" + lesEtapes +
                '}';
    }

    /**
     * retourne une chaine de caractères contenant le code C du parcours d'un monde par un client
     * en fonction des activités et des guichets qui s'y trouvent.
     * @return La chaîne de caractères représentant le code C
     */
    public String toC(){
        StringBuilder codeC = new StringBuilder(50);
        Etape suivant = this.lesEtapes.iterator().next();
        codeC.append("#include \"def.h\"\n");
        codeC.append("#include <time.h>\n");
        codeC.append("#include <sys/types.h>\n");
        codeC.append("#include \"lois.h\"\n");
        //Ajout, dans le code C, des "define"s des étapes
        for (Etape e : lesEtapes){
            codeC.append(e.toCConstante());
        }

        codeC.append("void simulation(int ids){\n");

        if (!estLineaire()){
            codeC.append("int nb;\n");
        }
        codeC.append(suivant.toC());
        codeC.append("\n}");
        return codeC.toString();
    }

    /**
     * Retourne si le monde est linéaire, c'est-à-dire si il n'y a pas de bifurcations
     * @return True si le monde n'a pas de bifurcations
     */
    public boolean estLineaire(){
        Iterator<Etape> iter = iterator();
        boolean aPlusieursSuccesseurs = false;
        //On regarde pour chaque étape si elle n'a pas plusieurs successeurs
        while(iter.hasNext() && !aPlusieursSuccesseurs){
            if (iter.next().nbSuccesseurs() >1 ){
                aPlusieursSuccesseurs = true;
            }
        }
        return !aPlusieursSuccesseurs;
    }

}
