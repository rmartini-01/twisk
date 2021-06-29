package twisk.vues;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import twisk.mondeIG.EtapeIG;
import twisk.mondeIG.MondeIG;
import twisk.mondeIG.SujetObserve;
import twisk.outils.TailleComposants;

public abstract class VueEtapeIG extends VBox implements Observateur {
    protected SujetObserve sujet;
    protected EtapeIG etape;
    protected BorderPane bp;
    protected Label titre;

    /**
     * Constructeur d'une vue sur une étape
     * @param sujet Sujet à observer
     * @param etape Etape associée à la vue
     */
    protected VueEtapeIG(SujetObserve sujet, EtapeIG etape) {
        super();
        setPrefHeight(TailleComposants.getInstance().getHauteurGuichet());
        setPrefWidth(TailleComposants.getInstance().getLargeurGuichet());
        this.sujet = sujet;
        this.etape = etape;
        bp = new BorderPane(); //On utilise un borderpane pour afficher les icones si c'est une entrée et/ou une sortie
        titre = new Label(etape.getNom());//On affiche le nom de l'étape
        bp.setCenter(titre);
        this.getChildren().add(bp);
        this.setAlignment(Pos.CENTER);
        modifierAffichage();

        //Ecouteur lorsqu'on sélectionne l'étape
        this.setOnMouseClicked(event-> ((MondeIG) sujet).selectionEtape(etape));

        definirLaVueCommeSource(); //Pour le déplacement de l'activité
        afficherIconeEntree();
        afficherIconeSortie();
    }

    /**
     * Indique que la vue est une source pour le déplacement
     */
    private void definirLaVueCommeSource(){
        this.setId(etape.getIdentifiant());//On attribue l'identifiant à la source
        //Ecouteur lorsqu'on déplace l'étape
        this.setOnDragDetected(event ->{
            Dragboard db = this.startDragAndDrop(TransferMode.MOVE);//On déplace seulement l'étape (pas de copie,...)
            ClipboardContent content = new ClipboardContent(); //Le contenu lors du transfert
            content.putString(etape.getIdentifiant());//On ajoute l'identifiant de l'étape au contenu à transférer
            //Ajout image du composant
            content.putImage(this.snapshot(null,null));
            db.setContent(content);
            event.consume();
        });
    }

    /**
     * Modifie le style de l'affichage d'une étape
     */
    private void modifierAffichage(){
        this.relocate(this.etape.getPosX(),this.etape.getPosY()); //Déplace la vue à la bonne position
        titre.setStyle("-fx-font-weight: bold; -fx-font-family:Arial;-fx-font-size:13px;");
        this.setSpacing(1);
        this.setStyle("-fx-border-width: 3;-fx-border-radius:10; -fx-background-radius: 10;-fx-background-color: white; -fx-padding: 5 10;");
        if (etape.isSelectionnee()){
            this.setStyle(getStyle()+"-fx-border-color: red;");
        }
    }

    /**
     * Affiche le symbole d'entrée si l'étape est une entrée
     */
    private void afficherIconeEntree(){
        if (etape.estUneEntree()) {
            Image imageEntree = new Image(getClass().getResourceAsStream("/twisk/ressources/images/Fleche.png"),15,15,true,true);
            ImageView vueImageEntree = new ImageView(imageEntree);
            bp.setLeft(vueImageEntree);
            bp.setPadding(new Insets(0, 0, 0, 5));
        }
    }

    /**
     * Affiche le symbole de sortie si l'étape est une sortie
     */
    private void afficherIconeSortie(){
        if (etape.estUneSortie()){
            Image imageSortie = new Image(getClass().getResourceAsStream("/twisk/ressources/images/Drapeau.png"),15,15,true,true);
            ImageView vueImageSortie = new ImageView(imageSortie);
            bp.setRight(vueImageSortie);
            bp.setPadding(new Insets(0, 5, 0, 0));
        }
    }
}
