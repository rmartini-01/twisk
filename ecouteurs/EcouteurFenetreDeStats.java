package twisk.ecouteurs;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import twisk.mondeIG.MondeIG;
import twisk.mondeIG.SujetObserve;
import twisk.simulation.Client;
import java.util.Iterator;

public class EcouteurFenetreDeStats implements EventHandler<ActionEvent> {
    private SujetObserve sujet;

    /**
     *Constructeur
     * @param sujet Données du monde
     */
    public EcouteurFenetreDeStats(SujetObserve sujet) {
        this.sujet = sujet;
    }

    @Override
    public void handle(ActionEvent actionEvent) {

        GridPane pane = new GridPane();
        Iterator<Client> iteratorClients = ((MondeIG) sujet).iteratorClients();
        Client c;
        int cptClient = 0;
        if(iteratorClients!=null){
            while(iteratorClients.hasNext()){
                c = iteratorClients.next();
                Rectangle rect = new Rectangle(20,20);
                rect.setFill(Color.color(c.getCouleurs(0), c.getCouleurs(1), c.getCouleurs(2)));
                pane.add(rect,0,cptClient);
                pane.add(new Label((""+c.getTempsJusquAArrivee()) +" secondes."),1,cptClient);
                cptClient++;
            }
        }else{
            Label empty = new Label("Aucune simulation n'a été lancée ");
            empty.setStyle("-fx-font-weight: bold; -fx-font-family:Arial;-fx-font-size:13px; -fx-Alignment: Center;");
            pane.add(empty, 1, 1);
        }
        pane.setVgap(3);
        pane.setHgap(3);
        pane.setPadding(new Insets(5, 5, 5, 5));
        pane.setAlignment(Pos.CENTER_LEFT);
        Scene scene = new Scene(pane , 300, 250);
        Stage stage = new Stage();
        stage.setTitle("Temps d'arrivée des clients");
        stage.setScene(scene);

        stage.show();
    }
}
