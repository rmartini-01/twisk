package twisk.mondeIG;

import twisk.exceptions.ExceptionAjoutDArcImpossible;
import twisk.exceptions.ExceptionParametreNonValide;
import twisk.exceptions.MondeException;
import twisk.monde.*;
import twisk.outils.*;
import twisk.simulation.Client;
import twisk.simulation.GestionnaireClients;
import twisk.vues.Observateur;
import java.awt.*;
import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class MondeIG extends SujetObserve implements Observateur , Iterable<EtapeIG>,Serializable {

    private HashMap<String,EtapeIG> hm;
    private LinkedList<ArcIG> arcs;
    private transient PointDeControleIG premierPointSelectionne;
    private transient ArrayList<EtapeIG> selectionEtapes;
    private transient ArrayList<ArcIG> selectionArcs;
    private transient ArrayList<Point> pointsSelectionnes;
    private transient CorrespondanceEtapes correspondance;
    private transient GestionnaireClients gestionnaireClients;
    private transient GestionnaireThreads gestionnaireThreads = GestionnaireThreads.getInstance();
    private transient boolean simulationEnCours;
    private transient int nbDeClients;
    private transient GestionnaireExemples  exemples;
    private transient String loi;
    private transient Temps temps;

    /**
     * Constructeur d'un affichage d'un monde
     */
    public MondeIG(){
        super();
        hm = new HashMap<>();// Pour l'instant on ne donne pas de capacité initiale (elle est à 16)
        arcs = new LinkedList<>();
        premierPointSelectionne = null;
        selectionEtapes = new ArrayList<>();
        selectionArcs = new ArrayList<>(4);
        pointsSelectionnes = new ArrayList<>(); //destinée à contenir les points appuyé avec la souris
        gestionnaireClients = null;
        simulationEnCours = false;
        temps = Temps.getInstance();
        //Le monde contient une activité
        String cle = FabriqueIdentifiant.getInstance().getIdentifiantEtape();
        ActiviteIG activite = new ActiviteIG("Activite " + cle, cle,
                TailleComposants.getInstance().getLargeurActivite(), TailleComposants.getInstance().getHauteurActivite());
        hm.put(cle, activite);
        nbDeClients = 6;
        exemples = new GestionnaireExemples(this);
        loi = "Gaussienne";
    }

    /**
     * Ajoute une étape au monde affiché
     * @param type Type de l'étape à ajouter
     */
    public void ajouter(String type){
        if (type.equals("Activite")) {
            String cle = FabriqueIdentifiant.getInstance().getIdentifiantEtape();

            ActiviteIG activite = new ActiviteIG("Activité " + cle, cle,
                    TailleComposants.getInstance().getLargeurActivite(), TailleComposants.getInstance().getHauteurActivite());
            ajouterUneEtape(activite);
        }
        if (type.equals("Guichet")){
            String cle = FabriqueIdentifiant.getInstance().getIdentifiantEtape();
            GuichetIG guichet = new GuichetIG("Guichet "+cle,cle,TailleComposants.getInstance().getLargeurGuichet(),
                    TailleComposants.getInstance().getHauteurGuichet());
            ajouterUneEtape(guichet);
        }
        notifierObservateurs();
    }

    /**
     * Ajoute une étape au monde
     * @param etape L'étape à ajouter
     */
    public void ajouterUneEtape(EtapeIG etape){
        hm.put(etape.getIdentifiant(),etape);
    }

    /**
     * Retourne le nombre d'étapes dans le monde
     * @return Le nombre d'étapes
     */
    public int getNbEtapesIG(){
        return hm.size();
    }

    @Override
    public Iterator<EtapeIG> iterator() {
        return hm.values().iterator();
    }

    /**
     * Ajoute un nouvel arc reliant 2 points du monde
     * @param pt1 Premier point de l'arc
     * @param pt2 Second point de l'arc
     */
    public void ajouter(PointDeControleIG pt1, PointDeControleIG pt2) throws ExceptionAjoutDArcImpossible{
        //On vérifie que cela ne crée pas de boucle
        if (pt1.getEtape().estAccessibleDepuis(pt2.getEtape())){
            premierPointSelectionne = null;
            notifierObservateurs();//Pour actualiser les points si une exception est lancée
            throw new ExceptionAjoutDArcImpossible("Création d'une boucle",3);
        }
        if (pt1.estSurLaMemeEtapeQue(pt2)) {
            premierPointSelectionne = null;
            notifierObservateurs();//Pour actualiser les points si une exception est lancée
            //Les deux points ne peuvent pas être sur la même étape
            throw new ExceptionAjoutDArcImpossible("Arc sur la même étape",1);

        } else { // Les deux points ne sont pas sur la même étape
            if (pt1.getEtape().estUnGuichet() && pt2.getEtape().estUnGuichet()){
                premierPointSelectionne = null;
                notifierObservateurs();//Pour actualiser les points si une exception est lancée
                throw new ExceptionAjoutDArcImpossible("Arc entre deux guichets",2);
            } else { //Ajout d'un arc courbe
                if (this.pointsSelectionnes.size()==3) {
                    this.arcs.add(new CourbeIG(pt1, pointsSelectionnes.get(1), pointsSelectionnes.get(2), pt2));
                } else {
                    this.arcs.add(new LigneDroiteIG(pt1, pt2));
                }
                pt1.getEtape().ajouterSuccesseur(pt2.getEtape());
            }

        }
        pointsSelectionnes.clear();
    }

    /**
     * ajoute un point appuyé à l'écran à la collection des points
     * @param x abscisse du point
     * @param y ordonnée du point
     */
    public void ajouterPoint(double x, double y){
        Point p = new Point((int)x,(int) y);
        this.pointsSelectionnes.add(p);
    }

    /**
     * Retourne l'itérateur sur les arcs du monde
     * @return l'itérateur
     */
    public Iterator<ArcIG> iteratorArcs(){
        return arcs.iterator();
    }

    /**
     * Selectionne le point passé en paramètre
     * @param pdc Le point de contrôle sélectionné
     * @throws ExceptionAjoutDArcImpossible L'exception lorsque deux points sur la même étape sont sélectionnés
     */
    public void selectionPoint(PointDeControleIG pdc) throws ExceptionAjoutDArcImpossible {
        pdc.changeLaSelection();
        if (premierPointEstSelectionne()){
            pdc.changeLaSelection();
            premierPointSelectionne.changeLaSelection();
            ajouter(premierPointSelectionne,pdc);
            premierPointSelectionne = null;
        } else {
            premierPointSelectionne = pdc;
        }
        notifierObservateurs();
    }

    /**
     * Retourne si un premier point est sélectionné
     * @return True si un point est sélectionné, false sinon
     */
    public boolean premierPointEstSelectionne(){
        return premierPointSelectionne != null;
    }

    /**
     * Ajoute l'étape en paramètre à la sélection
     * @param etape L'étape qui est sélectionnée
     */
    public void selectionEtape(EtapeIG etape){
        int index = selectionEtapes.indexOf(etape);
        if (index==-1){ //Si l'étape ne se trouve pas dans la liste on la met
            selectionEtapes.add(etape);
            etape.setSelectionnee(true);
        } else { //L'étape est déjà dans la liste, on l'enlève de la liste (on la déselectionne)
            selectionEtapes.remove(index);
            etape.setSelectionnee(false);
        }
        notifierObservateurs();
    }

    /**
     * Supprime tous les éléments sélectionnés
     */
    public void suppressionSelection(){
        String idfEtape;
        //On supprime les étapes sélectionnées et leurs arcs
        for (EtapeIG e : selectionEtapes){
            idfEtape = e.getIdentifiant();
            //On supprime les arcs
            supprimeArcsSurEtape(e);
            //On supprime les étapes
            hm.remove(idfEtape);
        }
        ArcIG arc;
        //On supprime les arcs sélectionnés
        Iterator<ArcIG> iter = iteratorArcs();
        while (iter.hasNext()) {
            arc = iter.next();
            if (arc.isSelectionnee()) {
                EtapeIG eSource = arc.getPoint1().getEtape();
                EtapeIG eSuccesseur = arc.getPoint2().getEtape();
                //Si on supprime un arc entre un guichet et une activité, l'activité n'est plus restreinte
                if (eSource.estUnGuichet() && eSuccesseur.estUneActiviteRestreinte()){
                    ((ActiviteIG)eSuccesseur).setEstUneActiviteRestreinte(false);
                }
                eSource.supprimerSuccesseur(eSuccesseur); //on supprime le successeur

                iter.remove();
            }
        }
        //On désélectionne toutes les étapes (on vide la sélection)
        effacerSelection();
    }

    /**
     * Supprime tous les arcs sur l'étape donné en paramètre
     * @param etape L'étape
     */
    public void supprimeArcsSurEtape (EtapeIG etape){
        //On utilise pas un foreach car on fait une suppression
        ArcIG arc;
        Iterator<ArcIG> iter = iteratorArcs();
        while (iter.hasNext()) {
            arc = iter.next();
            if (etape.estSurLArc(arc)) { //On supprime l'arc s'il se trouve sur l'étape qui sera supprimée
                //On enlève l'étape du bout de la flèche des successeurs de l'étape du début de la flèche
                EtapeIG etapeDeDepart = arc.getPoint1().getEtape();
                EtapeIG etapeDArrivee = arc.getPoint2().getEtape();
                if (etapeDeDepart.estUnGuichet() && etapeDArrivee.estUneActiviteRestreinte()){
                    ((ActiviteIG)etapeDArrivee).setEstUneActiviteRestreinte(false);
                }
                etapeDeDepart.supprimerSuccesseur(etapeDArrivee); //on supprime le successeur

                iter.remove();
            }
        }
        //Dans le cas où l'arc se trouve aussi dans la liste des arcs sélectionnés, il est supprimé dans suppression sélection
    }

    /**
     * Retourne le nombre d'étapes sélectionnées
     * @return Le nombre d'étapes sélectionnées
     */
    public int getNbEtapesDansLaSelection(){
        return selectionEtapes.size();
    }
    /**
     * Retourne le nombre d'arcs sélectionnés
     * @return Le nombre d'arcs sélectionnés
     */
    public int getNbArcsDansLaSelection(){
        return selectionArcs.size();
    }

    /**
     * Retourne le nombre d'arcs
     * @return Le nombre d'arcs
     */
    public int getNbArcs(){
        return arcs.size();
    }

    /**
     * Retourne l'étape sélectionnée pour être renommée
     * @return L'étape que l'on veut renommer
     */
    public EtapeIG getEtapeSelectionnee(){
        //On ne renomme que lorsqu'il n'y a qu'une seule étape, alors elle se trouve à la position 0
        return selectionEtapes.get(0);
    }

    /**
     * Déplace l'étape dont l'identifiant est id aux coordonnées passées en paramètre du centre de l'étape
     * @param id Identifiant de l'étape que l'on veut déplacer
     * @param posX Nouvelle position en abscisse du centre de l'étape
     * @param posY Nouvelle position en abscisse du centre de l'étape
     */
    public void deplacerEtape(String id, int posX, int posY){
        Iterator<EtapeIG> etapes = iterator();
        EtapeIG etape;
        boolean trouve = false;
        while (etapes.hasNext() && !trouve){
            etape = etapes.next();
            if (etape.getIdentifiant().equals(id)){
                etape.deplacerEtape(posX-(TailleComposants.getInstance().getLargeurActivite()/2),
                        posY-(TailleComposants.getInstance().getHauteurActivite()/2));
                trouve = true;
            }
        }

        notifierObservateurs();
    }

    /**
     * Ajoute l'arc en paramètre à la sélection
     * @param arc Arc à sélectionner
     */
    public void selectionArc(ArcIG arc) {
        int index = selectionArcs.indexOf(arc);
        if (index==-1){
            selectionArcs.add(arc);
            arc.setSelectionnee(true);
        } else {
            selectionArcs.remove(index);
            arc.setSelectionnee(false);
        }
        notifierObservateurs();
    }

    /**
     * Vide la sélection
     */
    public void effacerSelection(){
        for (EtapeIG etape : selectionEtapes){
            etape.setSelectionnee(false);
        }
        for (ArcIG arc : selectionArcs){
            arc.setSelectionnee(false);
        }
        selectionEtapes.clear();
        selectionArcs.clear();
        notifierObservateurs();
    }
    /**
     * Inscrit les étapes de la sélection comme des entrées
     */
    public void selectionnerLesEntrees(){
        for (EtapeIG etape : selectionEtapes){
            etape.changeEntree();
        }
        effacerSelection(); //Après avoir fait l'opération, on vide la sélection
    }

    /**
     * Inscrit les étapes de la sélection comme des sorties
     */
    public void selectionnerLesSorties(){
        for (EtapeIG etape : selectionEtapes){
            etape.changeSortie();
        }
        effacerSelection(); //Après avoir fait l'opération, on vide la sélection
    }

    /**
     * Modifie le délai de l'activité sélectionnée
     * @param delai Nouvelle valeur du délai
     * @throws ExceptionParametreNonValide Exception envoyée si la valeur du délai n'est pas valide
     */
    public void modifieLeDelai(int delai) throws ExceptionParametreNonValide {
        EtapeIG etape = getEtapeSelectionnee();
        if (delai <= 0) {
            throw new ExceptionParametreNonValide("Délai invalide");
        } else {
            ((ActiviteIG) etape).setDelai(delai);
        }
    }

    /**
     * Modifie l'écart de l'activité sélectionnée
     * @param ecart Nouvelle valeur de l'écart
     * @throws ExceptionParametreNonValide Exception envoyée si la valeur de l'écart n'est pas valide
     */
    public void modifieLEcart(int ecart) throws ExceptionParametreNonValide {
        EtapeIG etape = getEtapeSelectionnee();
        if (ecart <= 0 || ecart > ((ActiviteIG) etape).getDelai()) {
            throw new ExceptionParametreNonValide("Délai invalide");
        } else {
            ((ActiviteIG) etape).setEcart(ecart);
        }
    }

    /**
     * Modifie les paramètres (délai et écart)
     * @param delai Nouvelle valeur du délai
     * @param ecart Nouvelle valeur de l'écart
     * @throws ExceptionParametreNonValide Exception envoyée si la valeur d'un paramètre n'est pas valide
     */
    public void modifieLesParametres(int delai, int ecart) throws ExceptionParametreNonValide{
        //Si l'écart n'est pas bon, le délai est quand même modifié
        modifieLeDelai(delai);
        notifierObservateurs();
        modifieLEcart(ecart);
        effacerSelection();
        notifierObservateurs();
    }
    /**
     * Modifie le nom de l'activité sélectionnée par le nom donné en paramètre
     * @param nom Nouveau nom de l'activité
     */
    public void modifieLeNom (String nom){
        EtapeIG etape = getEtapeSelectionnee();
        etape.setNom(nom);
        effacerSelection();
        notifierObservateurs();
    }

    /**
     *Modifie le nombre de jetons
     * @param nbJetons Nouveau nombre de jetons
     * @throws ExceptionParametreNonValide Exception lancée quand le nombre de jetons n'est pas valide
     */
    public void modifieNbJetons(int nbJetons) throws ExceptionParametreNonValide {
        EtapeIG etape = getEtapeSelectionnee();
        if(nbJetons < 0){
            effacerSelection();
            notifierObservateurs();
            throw new ExceptionParametreNonValide("Nombre de jetons invalide.");
        }else{
            ((GuichetIG)etape).setNbJetons(nbJetons);
            effacerSelection();
            notifierObservateurs();
        }
    }

    /**
     * Verifier la validité du mondeIG
     * @throws MondeException dans le cas où la création du monde est incorrecte
     */
    private void verifierMondeIG() throws MondeException {
        boolean guichetAvantEntree = false;
        boolean guichetSucc = false;
        boolean aucunSuccEtapes = false; //toutes les étapes (activités et guichets) doivent avoir un successeurs
        int guichetPred = 0;
        boolean activitePred = false;
        int nbAct  = 0;
        int nbEntree = 0;
        int nbSortie = 0;
        Iterator<EtapeIG> it = this.iterator();
        while(it.hasNext() && !guichetSucc && !guichetAvantEntree){
            activitePred = false;
            guichetPred = 0;
            EtapeIG e = it.next();
            if (e.estUneSortie()) {     //si c'est une sortie
                ++nbSortie;

            } else{

                if(e.getNbSuccesseurs() == 0 ) {
                    aucunSuccEtapes = true;      //toutes les étapes doivent avoir au moins un successeur
                }
            }
            if (e.estUneEntree()) {
                ++nbEntree;
            }
            if (e.estUneActivite() || e.estUneActiviteRestreinte()) {
                ++nbAct;

                //On vérifie qu'une activité, si elle est précédée d'un guichet, n'a pas d'autres prédecesseurs
                Iterator<EtapeIG> iterVerifSuccesseur = this.iterator();
                while(iterVerifSuccesseur.hasNext()){
                    EtapeIG etapeVerification = iterVerifSuccesseur.next();
                    if (etapeVerification.aCommeSuccesseur(e) && etapeVerification.estUnGuichet()){
                        guichetPred++;
                    } else if (etapeVerification.aCommeSuccesseur(e) && etapeVerification.estUneActivite()){
                        activitePred = true;
                    }

                }
            } else{
                //si c'est un guichet, suivi par une activité ça donne lieu à la création d'une activité restreinte
                if(e.estUnGuichet()){
                    if(e.getNbSuccesseurs()== 1){ //on s'assure que le guichet a un successeur
                        Iterator<EtapeIG> iter = e.iteratorSuccesseurs();
                        //On vérifie tous les successeurs
                        if(iter.hasNext()) {
                            EtapeIG eSucc = iter.next();
                            if (eSucc.estUneActivite()) {
                                nbAct++;
                                if (eSucc.estUneEntree()){
                                    guichetAvantEntree = true;
                                } else {

                                    ((ActiviteIG) eSucc).setEstUneActiviteRestreinte(true);
                                }

                            }
                        }
                    } else{
                        guichetSucc = true;
                    }

                }
            }
        }
        if (guichetSucc){
            setSimulationEnCours(false);
            throw new MondeException("Un guichet doit être suivi par une activité.");
        }
        if (guichetAvantEntree){
            setSimulationEnCours(false);
            throw new MondeException("Un guichet ne peut pas précéder\nune entrée.");
        }
        if(!(nbAct >0 && nbEntree>0 && nbSortie>0)){
            setSimulationEnCours(false);
            throw new MondeException("Un monde a au moins une entrée, une activité et \nune sortie.");
        }

        if(aucunSuccEtapes) {
            setSimulationEnCours(false);
            throw new MondeException("Toutes les étapes doivent avoir au moins un \nsuccesseur.");
        }
        if ((activitePred && guichetPred > 0)|| (!activitePred && guichetPred > 1)) {
            setSimulationEnCours(false);
            throw new MondeException("Une activité restreinte ne peut pas avoir\n d'autre prédecesseur qu'un guichet.");
        }

    }

    /**
     * défini la nouvelle loi
     * @param loi nouvelle loi
     */
    public void setLoi(String loi) {
        this.loi = loi;
        notifierObservateurs();
    }

    /**
     * Crée le monde
     * @return Le monde
     */
    private Monde creerMonde(){
        correspondance = new CorrespondanceEtapes();
        Monde monde = new Monde();
        monde.definirLoi(loi);
        Iterator<EtapeIG> etapes = iterator();
        //On crée toutes les étapes dans le monde
        while(etapes.hasNext()) {
            EtapeIG etapeIG = etapes.next();
            Etape etape = null;//On initialise à null pour
            if(etapeIG.estUnGuichet()){
                etape = new Guichet(etapeIG.getNom(),((GuichetIG) etapeIG).getNbJetons());
            } else if (etapeIG.estUneActiviteRestreinte()){
                etape = new ActiviteRestreinte(etapeIG.getNom(),((ActiviteIG) etapeIG).getDelai(),((ActiviteIG) etapeIG).getEcart());
            }else if (etapeIG.estUneActivite()) {//Si l'activité n'est pas restreinte on passe ici
                etape = new Activite(etapeIG.getNom(),((ActiviteIG) etapeIG).getDelai(),((ActiviteIG) etapeIG).getEcart());
            }
            monde.ajouter(etape);
            //On crée le lien entre l'étape et l'étapeIG
            correspondance.ajouter(etapeIG,etape);
        }
        //On
        etapes = iterator();
        Iterator<EtapeIG> iteratorSuccesseurs;
        while(etapes.hasNext()) {
            EtapeIG etapeIG = etapes.next();
            Etape etape = correspondance.get(etapeIG);
            //On parcourt les successeurs de l'étapeIG ajouter le lien dans le monde
            iteratorSuccesseurs = etapeIG.iteratorSuccesseurs();
            while(iteratorSuccesseurs.hasNext()) {
                EtapeIG successeurIG = iteratorSuccesseurs.next();
                Etape successeur = correspondance.get(successeurIG);
                etape.ajouterSuccesseur(successeur);
            }

            if (etapeIG.estUneEntree()){
                monde.aCommeEntree(etape);
            }

            if (etapeIG.estUneSortie()){
                monde.aCommeSortie(etape);
            }
        }
        return monde;
    }

    /**
     * Lance une simulation
     * @throws MondeException Exception lancée lorsque le monde n'est pas valide
     */
    public void simuler() throws MondeException{

        verifierMondeIG();

        Monde monde = creerMonde();
        //Appel de simuler de simulation
        ClassLoaderPerso loader = new ClassLoaderPerso(this.getClass().getClassLoader());
        try {
            Class<?> classeSimulation = loader.loadClass("twisk.simulation.Simulation");
            Class<?> classeSimulation1 = loader.loadClass("twisk.simulation.Simulation$1");
            Constructor<?> constructeur = classeSimulation.getDeclaredConstructor();
            //instance de la classe simulation
            Object instance = constructeur.newInstance();
            Method setNbClients = classeSimulation.getDeclaredMethod("setNbClients", int.class);
            setNbClients.invoke(instance, getNbDeClients());

            //récupérer le gestionnaireClient
            Method getGestionnaireClients = classeSimulation.getDeclaredMethod("getGestionnaireClients");
            gestionnaireClients = (GestionnaireClients) getGestionnaireClients.invoke(instance);

            //on ajoute les observateurs de mondeIG à Simulation pour mettre à jour l'affichage des clients
            Method ajouterObs = classeSimulation.getMethod("ajouterObservateur", Observateur.class);
            ajouterObs.invoke(instance,this);


            Method sim = classeSimulation.getDeclaredMethod("simuler", Monde.class);
            sim.invoke(instance, monde);
            temps.startTimer();
        } catch (ClassNotFoundException e) {
            System.out.println("Classe Simulation non trouvée.");
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException |
                InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    /**
        Change la valeur indiquant si la simulation est en cours
     */
    public void setSimulationEnCours(Boolean b) {
        this.simulationEnCours = b;
    }

    /**
     *  retourne la valeur de simulationEnCours
     * @return vrai si la simulation est lancée, faux sinon
     */
    public boolean isSimulationEnCours() {
        return simulationEnCours;
    }

    /**
     * Iterator sur les clients du monde
     * @return itérateur des clients, s'ils existent.
     */
    public Iterator<Client> iteratorClients(){
        if(gestionnaireClients!= null)
        return gestionnaireClients.iterator();
        else{
            return null;
        }
    }
    /**
     * fonction pour détruire tous les threads
     */
    public void detruireThreads(){
        gestionnaireThreads.detruireTout();
        temps.detruireTimer();
    }
    /**
     * Retourne l'étapeIG d'une étape
     * @param e Etape (modèle)
     * @return EtapeIG (vue)
     */
    public EtapeIG getEtapeIGDeLEtape(Etape e){
        return correspondance.getEtapeIG(e);
    }

    @Override
    public void reagir() {
        if(!isSimulationEnCours()){
            temps.detruireTimer();
        }
        //propagation de l'affichage pendant la simulation
        notifierObservateurs();
    }
    @Override
    public void etatDeLASimulation() {
        //fin de la simulation
        setSimulationEnCours(false);
        notifierObservateurs();
    }

    /**
     * Retourne le nombre de clients du monde
     * @return Le nombre de clients
     */
    public int getNbDeClients() {
        return nbDeClients;
    }

    /**
     * Vide le monde de ses étapes et le remet à son stade initial sans l'activité de départ
     */
    public void supprimerLeMonde(){
        // On efface tout
        gestionnaireClients = null;
        correspondance = null;
        simulationEnCours = false;
        premierPointSelectionne = null;
        selectionEtapes.clear();
        selectionArcs.clear();
        pointsSelectionnes.clear();
        arcs.clear();
        hm.clear();
        //On replace les valeurs des singletons à leur valeur initial
        FabriqueIdentifiant.getInstance().reset();
        FabriqueNumero.getInstance().reset();
    }

    /**
     * Retourne l'étapeIG dont la clé (le nom de départ) est passée en paramètre
     * @param cle Clé de l'étape ( et nom de départ)
     * @return L'étape IG
     */
    public EtapeIG getEtapeIG (String cle){
        return hm.get(cle);
    }

    /**
     * Charge un monde dans l'interface graphique
     * @param i numéro du monde à charger
     */
    public void chargerMonde(int i){
        switch (i){
            case 1 :
                exemples.chargerLeMonde1();
                break;
            case 2 :
                exemples.chargerLeMonde2();
                break;
            case 3 :
                exemples.chargerLeMonde3();
                break;
            case 4:
                exemples.chargerLeMonde4();
                break;

        }
        notifierObservateurs();
    }

    public void sauvegarder(String cheminFichier){
        FileOutputStream fichier = null;
        try {
            fichier = new FileOutputStream(cheminFichier);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(fichier);
            out.writeObject(this);
            out.close();
            fichier.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Modifie le nombre de Clients
     * @param nbDeClients Nouveau nombre de clients
     * @throws ExceptionParametreNonValide Exception envoyée si le nombre de clients est invalide
     */
    public void modifieNbClients(int nbDeClients) throws ExceptionParametreNonValide {
        if (nbDeClients > 0){
            this.nbDeClients = nbDeClients;
        } else {
            throw new ExceptionParametreNonValide("Le nombre de clients ne peut pas être négatif ou nul.");
        }
    }

    /**
     * Charger un monde
     * @param fichierChoisi monde à charger
     */
    public void charger(String fichierChoisi){
        supprimerLeMonde();

        try {
            FileInputStream fichier = new FileInputStream(fichierChoisi);
            ObjectInputStream in = new ObjectInputStream(fichier);
            MondeIG save = (MondeIG) in.readObject();
            Iterator<Map.Entry<String, EtapeIG>> iterator = save.hm.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, EtapeIG> map = iterator.next();
                this.hm.put(map.getKey(),map.getValue());
            }
            Iterator<ArcIG> iteratorArcs = save.iteratorArcs();

            while (iteratorArcs.hasNext()) {
                this.arcs.add(iteratorArcs.next());
            }
            in.close();
            fichier.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        notifierObservateurs();
    }

    /**
     * getter
     * @return le temps écoulé de la simulation
     */
    public int getTimer(){
        return temps.getSecondsPassed();
    }

    /**
     * getter
     * @return la loi d'arrivée des clients
     */
    public String getLoi() {
        return loi;
    }

}
