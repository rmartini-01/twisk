package twisk.outils;

import twisk.exceptions.ExceptionAjoutDArcImpossible;
import twisk.mondeIG.*;

public class GestionnaireExemples {
    private MondeIG monde;

    public GestionnaireExemples(MondeIG monde) {
        this.monde = monde;
    }

    public void chargerLeMonde1(){
        //Monde avec deux activités
        monde.supprimerLeMonde();
        monde.ajouter("Activite");
        monde.ajouter("Activite");

        ActiviteIG act1 = (ActiviteIG) monde.getEtapeIG("0");
        ActiviteIG act2 = (ActiviteIG) monde.getEtapeIG("1");

        act1.setNom("Salle 1");
        act1.setDelai(7);
        act1.setEcart(3);
        act2.setNom("Salle 2");
        act2.setEcart(1);
        try {
            monde.ajouter(act1.getPointDeControle(1),act2.getPointDeControle(3));
        } catch (ExceptionAjoutDArcImpossible exceptionAjoutDArcImpossible) {
            exceptionAjoutDArcImpossible.printStackTrace();
        }
        act1.changeEntree();
        act2.changeSortie();
    }

    public void chargerLeMonde2(){
        //Monde avec un guichet
        monde.supprimerLeMonde();
        monde.ajouter("Activite");
        monde.ajouter("Guichet");
        ActiviteIG act = (ActiviteIG) monde.getEtapeIG("0");
        GuichetIG guichet = (GuichetIG) monde.getEtapeIG("1");
        guichet.setNom("File d'attente");
        guichet.setNbJetons(1);
        act.setNom("Vaccination");
        try {
            monde.ajouter(guichet.getPointDeControle(3),act.getPointDeControle(1));
        } catch (ExceptionAjoutDArcImpossible exceptionAjoutDArcImpossible) {
            exceptionAjoutDArcImpossible.printStackTrace();
        }
        guichet.changeEntree();
        act.changeSortie();
    }

    public void chargerLeMonde3(){
        //Monde avec bijection et plusieurs sorties
        monde.supprimerLeMonde();
        monde.ajouter("Activite");//0
        monde.ajouter("Guichet");//1
        monde.ajouter("Activite");//2
        monde.ajouter("Guichet");//3
        monde.ajouter("Activite");//4
        monde.ajouter("Activite");//5

        ActiviteIG act1 = (ActiviteIG) monde.getEtapeIG("0");
        act1.setNom("Snack 1");
        act1.setDelai(4);
        act1.setEcart(3);
        GuichetIG guichet1 = (GuichetIG) monde.getEtapeIG("1");
        guichet1.setNom("File du snack 1");
        guichet1.setNbJetons(2);
        ActiviteIG act2 = (ActiviteIG) monde.getEtapeIG("2");
        act2.setNom("Snack 2");
        act2.setDelai(6);
        act2.setEcart(4);
        GuichetIG guichet2 = (GuichetIG) monde.getEtapeIG("3");
        guichet2.setNom("File du snack 2");
        guichet2.setNbJetons(2);
        ActiviteIG act3 = (ActiviteIG) monde.getEtapeIG("4");
        act3.setNom("Grand huit");
        act3.setDelai(3);
        act3.setEcart(1);
        ActiviteIG act4 = (ActiviteIG) monde.getEtapeIG("5");
        act4.setNom("Montagnes russes");
        act4.setDelai(3);
        act4.setEcart(1);

        try {
            monde.ajouter(guichet1.getPointDeControle(0),act1.getPointDeControle(1));
            monde.ajouter(guichet2.getPointDeControle(1),act2.getPointDeControle(3));
            monde.ajouter(act3.getPointDeControle(1),guichet1.getPointDeControle(2));
            monde.ajouter(act3.getPointDeControle(2),guichet2.getPointDeControle(2));
            monde.ajouter(act2.getPointDeControle(1),act4.getPointDeControle(0));
        } catch (ExceptionAjoutDArcImpossible exceptionAjoutDArcImpossible) {
            exceptionAjoutDArcImpossible.printStackTrace();
        }
        act3.changeEntree();
        act1.changeSortie();
        act4.changeSortie();
    }

    public void chargerLeMonde4() {
        monde.supprimerLeMonde();
        monde.ajouter("Activite");//0
        monde.ajouter("Activite");//1
        monde.ajouter("Activite");//2

        monde.ajouter("Guichet");//3
        monde.ajouter("Activite");//4

        monde.ajouter("Guichet");//5
        monde.ajouter("Activite");//6

        monde.ajouter("Guichet"); //7
        monde.ajouter("Activite");//8


        monde.ajouter("Activite"); //9
        monde.ajouter("Activite"); //10


        ActiviteIG act0 = (ActiviteIG) monde.getEtapeIG("0");
        ActiviteIG act1 =  (ActiviteIG) monde.getEtapeIG("1");
        ActiviteIG act2 =  (ActiviteIG) monde.getEtapeIG("2");
        act0.setNom("Zoo");
        act1.setNom("Parc");
        act2.setNom("Piscine");

        GuichetIG guichet3 = (GuichetIG) monde.getEtapeIG("3");
        ActiviteIG act4 = (ActiviteIG) monde.getEtapeIG("4");//restreinte
        guichet3.setNom("Guichet de l'accrobranche");
        guichet3.setNbJetons(4);
        act4.setNom("Accrobranche");
        GuichetIG guichet5 = (GuichetIG) monde.getEtapeIG("5");
        ActiviteIG act6 = (ActiviteIG) monde.getEtapeIG("6");//restreinte
        guichet5.setNom("File du toboggan");
        act6.setNom("Toboggan");
        GuichetIG guichet7 = (GuichetIG) monde.getEtapeIG("7");
        ActiviteIG act8 = (ActiviteIG) monde.getEtapeIG("8"); //restreinte
        guichet7.setNom("File du bateau");
        act8.setNom("Bateau");
        act8.setDelai(14);
        act8.setEcart(5);
        ActiviteIG act9 = (ActiviteIG) monde.getEtapeIG("9");
        act9.setNom("Deltaplane");
        ActiviteIG act10 = (ActiviteIG) monde.getEtapeIG("10");
        act10.setNom("Restaurant");
        try {

            monde.ajouter(guichet3.getPointDeControle(1),act4.getPointDeControle(3));
            monde.ajouter(guichet5.getPointDeControle(1),act6.getPointDeControle(3));
            monde.ajouter(guichet7.getPointDeControle(3),act8.getPointDeControle(1));

            monde.ajouter(act4.getPointDeControle(2),guichet5.getPointDeControle(0)); //sortie

            //entrée
            monde.ajouter(act0.getPointDeControle(2),guichet7.getPointDeControle(1));
            monde.ajouter(act8.getPointDeControle(1),act10.getPointDeControle(3));
            monde.ajouter(act6.getPointDeControle(0),act9.getPointDeControle(2)); //sortie

            //entrée
            monde.ajouter(act1.getPointDeControle(1),guichet3.getPointDeControle(0));
            monde.ajouter(act1.getPointDeControle(3),act2.getPointDeControle(2));
            monde.ajouter(act4.getPointDeControle(1),act10.getPointDeControle(2));//sortie
            monde.ajouter(act2.getPointDeControle(2),act10.getPointDeControle(1)); //sortie

        } catch (ExceptionAjoutDArcImpossible exceptionAjoutDArcImpossible) {
            exceptionAjoutDArcImpossible.printStackTrace();
        }

        act0.changeEntree();
        act1.changeEntree();
        act9.changeSortie();
        act10.changeSortie();

    }

}
