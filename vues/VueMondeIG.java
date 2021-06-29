package twisk.vues;

import javafx.application.Platform;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import twisk.mondeIG.*;
import twisk.simulation.Client;

import java.util.Iterator;

public class VueMondeIG extends Pane implements Observateur {
    private SujetObserve sujet;
    /**
     * Constructeur de l'affichage d'un monde
     * @param sujet Sujet à observer
     */
    public VueMondeIG(SujetObserve sujet) {
        super();
        this.sujet = sujet;
        sujet.ajouterObservateur(this);//La vue s'inscrit comme vue auprès du monde
        ajouterVuesDesEtapes();
        //on ajoute l'écouteur qui permettra d'appuyer sur un point de l'écran pour dessiner des courbes
        this.setOnMouseClicked(mouseEvent -> {
            //s'il y a un premier point de contrôle sélectionné
            if(((MondeIG) sujet).premierPointEstSelectionne()) {
                //si on appuie sur un point de contrôle
                ((MondeIG) sujet).ajouterPoint(mouseEvent.getX(), mouseEvent.getY());
            }
        });
        definirLaVueCommeDestination();
        activeLeDepotDUneActivite();
    }

    @Override
    public void reagir() {
        VueMondeIG vue = this;
        Runnable command = new Runnable() {
            @Override
            public void run() {
                vue.getChildren().clear(); //On supprime les anciens composants
                //On affiche les arcs au monde (avant les activités pour que les écouteurs soient sûrs de fonctionner)
                ajouterVuesDesArcs();
                ajouterVuesDesEtapes();
                //Si la simulation est en cours on affiche les clients
                if (((MondeIG) sujet).isSimulationEnCours()){
                    ajouterVuesClients();
                }
                sensDeCirculation();
            }
        };

        if(Platform.isFxApplicationThread()){
            command.run();
        } else {
            Platform.runLater(command);
        }
    }

    @Override
    public void etatDeLASimulation() {

    }


    /**
     * Ajoute toutes les vues des étapes à la vue du monde
     */
    private void ajouterVuesDesEtapes(){
        //On affiche étape du monde (avec l'itérateur)
        for (EtapeIG etape : (MondeIG) sujet) {
            if (etape.estUneActivite() || etape.estUneActiviteRestreinte()) {
                getChildren().add(new VueActiviteIG(sujet, etape)); //On ajoute la vue d'une activité du monde
            }
            if (etape.estUnGuichet()) {
                getChildren().add(new VueGuichetIG(sujet, etape)); //On ajoute la vue d'un guichet du monde
            }
            //On ajoute les points de contrôle à l'affichage du monde
            for (PointDeControleIG pointDeC : etape) {
                getChildren().add(new VuePointDeControleIG(pointDeC, sujet));
            }
        }
    }
    /**
     * Ajoute les vues des arcs à la vue du monde
     */
    private void ajouterVuesDesArcs(){
        Iterator<ArcIG> it = ((MondeIG) sujet).iteratorArcs();
        while( it.hasNext() ) {
            ArcIG arc = it.next();
            if(arc.estUneLigne()){
                VueArcIG vueArc = new VueLigneDroiteIG(sujet , arc);
                this.getChildren().add(vueArc);
            }else {
                VueArcIG vueArc = new VueCourbeIG( sujet, arc);
                this.getChildren().add(vueArc);
            }
        }

    }

    /**
     * Modifie le sens de simulation entre deux étapes
     */
    public void sensDeCirculation(){
        Iterator<ArcIG> it = ((MondeIG) sujet).iteratorArcs();
        for(EtapeIG e : (MondeIG) sujet){
            if(e.estUnGuichet()){
                while(it.hasNext()) {
                    ArcIG arc = it.next();
                    if (arc.getPoint1().getEtape().estUnGuichet()) {
                        ((GuichetIG)arc.getPoint1().getEtape()).quelSensDeCirculation(arc.getPoint1().getEtape(), arc.getPoint2().getEtape());
                    }
                }
            }
        }

    }
    /**
     * afficher les clients
     */
    private void ajouterVuesClients(){
        double x,y;
        MondeIG monde =(MondeIG) sujet;
        Iterator<Client> itClients = monde.iteratorClients();
        if(itClients!= null) {
            while (itClients.hasNext()) {
                Client client = itClients.next();
                EtapeIG etape = monde.getEtapeIGDeLEtape(client.getEtape());
                if (!client.getEtape().estUnSasDEntree() && !client.getEtape().estUnSasDeSortie()) {
                    Circle circleClient = new Circle(5.0);
                    circleClient.setFill(Color.color(client.getCouleurs(0), client.getCouleurs(1), client.getCouleurs(2)));
                    getChildren().add(circleClient);
                    //affichage dans le cas où c'est une activité (ou une activité restreinte)
                    if(etape.estUneActivite() || etape.estUneActiviteRestreinte()){
                         x = Math.random() * ((etape.getPosX() +etape.getLargeur()-20) - (etape.getPosX()+20)) + (etape.getPosX()+20);
                         y = Math.random() * ((etape.getPosY() +etape.getHauteur()-15) - (etape.getPosY() +50)) + (etape.getPosY() +50);
                    }else {
                        //affichage dans le cas où c'est une guichet.
                        if(((GuichetIG) etape).isSensDeCirculation()){
                            x = (etape.getPosX() +etape.getLargeur())+(2.5*client.getRang()*-7);
                        }else{
                            x = (etape.getPosX())+(2.5*client.getRang()*7.5);
                        }
                        y = (etape.getPosY() +etape.getHauteur()-19);
                        if (client.getRang() > 10){
                            circleClient.setVisible(false);
                        }
                    }
                    circleClient.setCenterX(x);
                    circleClient.setCenterY(y);
                }
            }
        }
    }
    /**
     * Définit la vue comme destination pour le déplacement d'une étape
     */
    private void definirLaVueCommeDestination(){
        //On définit la vue monde comme une destination (où on peut déplacer les activités)
        this.setOnDragOver(dragEvent->{
            if( dragEvent.getGestureSource() != this && dragEvent.getDragboard().hasString()){
                dragEvent.acceptTransferModes(TransferMode.MOVE);
            }
            dragEvent.consume();
        });
    }

    /**
     * Fait réagir la vue et le monde lorsqu'une activité est déposée
     */
    private void activeLeDepotDUneActivite(){
        //On fait réagir la vue lorsqu'une activité est déposée dans la vue
        this.setOnDragDropped(dragEvent->{
            Dragboard dragBoard = dragEvent.getDragboard();
            boolean success = false;
            if (dragBoard.hasString()){
                String id = dragBoard.getString();
                ((MondeIG) sujet).deplacerEtape(id,(int) Math.round(dragEvent.getSceneX()),(int) Math.round(dragEvent.getSceneY())-29);
                success = true;
            }
            dragEvent.setDropCompleted(success);
            dragEvent.consume();
        });
    }


}
