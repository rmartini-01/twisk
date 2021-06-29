package twisk.monde;
import twisk.outils.FabriqueNumero;

import java.io.Serializable;
import java.text.Normalizer;
import java.util.Iterator;

public abstract class Etape implements Iterable<Etape> {
    protected String nom;
    protected GestionnaireSuccesseurs successeurs;
    protected int numEtape;


    /**
     * Constructeur d'une étape
     * @param nom nom de l'étape
     */
    protected Etape(String nom){
        this.nom = nom;
        this.successeurs = new GestionnaireSuccesseurs();
        numEtape = FabriqueNumero.getInstance().getNumeroEtape(); //On donne un numéro à l'étape à l'aide de la fabrique

    }

    /**
     * Retourne le numéro de l'étape
     * @return Numéro de l'étape
     */
    public int getNumEtape() {
        return numEtape;
    }

    /**
     * Fonction pour ajouter un successeur à une étape.
     * @param e un tableau d'étapes
     */
    public void ajouterSuccesseur(Etape...e){

        successeurs.ajouter(e);
    }

    /**
     * Renvoie si une étape est une activité
     * @return false si l'étape n'est pas une activité
     */
    public boolean estUneActivite(){
        return false;
    }

    /**
     * Renvoie si une étape est un guichet
     * @return false si l'étape n'est pas un guichet
     */
    public boolean estUnGuichet(){
        return false;
    }

    /**
     * retourne le nombre de successeurs de l'étape
     * @return nombre d'étape du GestionnaireSuccesseur
     */
    public int nbSuccesseurs(){
        return this.successeurs.nbEtapes();
    }

    @Override
    public Iterator<Etape> iterator(){
        return successeurs.iterator();
    }

    /**
     * Retourne le nom de l'étape
     * @return Le nom de l'étape
     */
    public String getNom() {
        return nom;
    }

    @Override
    public String toString() {
        return this.getNom() + " : " + this.nbSuccesseurs() + " successeur(s)" + successeurs.toString();
    }

    /**
     * Retourne le code C produit pour une étape
     * @return une chaine de caractères contenant le code C du parcours d'ue étape par un client en fonction de ses successeurs
     */
    public abstract String toC();

    /**
     * Retourne le code C du délai d'une activité restreinte
     * @return chaine de caractère du delai(temps, ecartTemps)
     */
    public String toCDelai(){
        return "";
    }

    /**
     * Retourne le nombre de jetons du guichet. Sur autre chose qu'un guichet, retourne -1
     * @return Le nombre de jetons
     */
    public int getNbjetons(){
        return -1;
    }

    /**
     *Retourne le numéro deSémaphore de l'étape
     */
    public int getNbSemaphore(){
        return 0;
    }
    /**
     * Retourne la ligne en C définissant une constante
     * @return La ligne en C
     */
    public String toCConstante(){
        StringBuilder codeC = new StringBuilder(12+nom.length());
        codeC.append("#define ").append(nomToConstante()).append(" ").append(getNumEtape()).append("\n");
        return codeC.toString();
    }

    /**
     * Retourne un nom de constante d'étape conforme(sans accents et sans espaces)
     * @return Le nom de la constante
     */
    public String nomToConstante(){
        //On met toutes les lettres en majuscule et des tirets à la place des espaces et des apostrophes
        String nomConstante = getNom().toUpperCase().replaceAll("[ '/]","_");
        //On enlève les accents
        nomConstante = Normalizer.normalize(nomConstante, Normalizer.Form.NFD);
        nomConstante = nomConstante.replaceAll("[^\\p{ASCII}]", "");
        return nomConstante;
    }

    /**
     * Retourne si une étape est un sas d'entrée
     * @return true si c'est un sas d'entrée, false sinon
     */
    public boolean estUnSasDEntree(){
        return false;
    }

    /**
     * Retourne si une étape est un sas de Sortie
     * @return true si c'est un sas de sortie, false sinon
     */
    public boolean estUnSasDeSortie(){
        return false;
    }
}
