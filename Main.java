package twisk;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import twisk.mondeIG.MondeIG;
import twisk.outils.TailleComposants;
import twisk.vues.VueMenu;
import twisk.vues.VueMondeIG;
import twisk.vues.VueOutils;
import twisk.vues.VueTemps;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage){

        BorderPane root = new BorderPane();
        MondeIG monde = new MondeIG();
        root.setTop(new VueMenu(monde));
        root.setBottom(new VueOutils(monde));
        root.setRight(new VueTemps(monde));
        VueMondeIG vueMonde = new VueMondeIG(monde);
        root.setCenter(vueMonde);

        primaryStage.setScene(new Scene(root, TailleComposants.getInstance().getLargeurFenetre(),
                TailleComposants.getInstance().getHauteurFenetre()));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
