package twisk;

import twisk.monde.*;
import twisk.outils.ClassLoaderPerso;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ClientTwisk {
    /**
     * Constructeur de la classe ClientTwisk
     */
    public ClientTwisk(){
        Monde monde1 = monde1();
        Monde monde2 = monde2();
        Monde monde3 = monde3();
        //On instancie le ClassLoader perso
        ClassLoaderPerso loader = new ClassLoaderPerso(this.getClass().getClassLoader());
        try {
            Class<?> classeSimulation = loader.loadClass("twisk.simulation.Simulation");
            Object instance = classeSimulation.newInstance();

            Method setNbClients = classeSimulation.getDeclaredMethod("setNbClients", int.class);
            setNbClients.invoke(instance , 5);

            Method sim = classeSimulation.getDeclaredMethod("simuler", Monde.class);
            sim.invoke(instance, monde1);

        } catch (ClassNotFoundException e) {
            System.out.println("Classe Simulation non trouvée.");
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
        //nouvelle instance de ClassLoaderPerso
        loader = new ClassLoaderPerso(this.getClass().getClassLoader());
        try {
            Class<?> classeSimulation = loader.loadClass("twisk.simulation.Simulation");
            Object instance = classeSimulation.newInstance();
            Method sim = classeSimulation.getDeclaredMethod("simuler", Monde.class);
            sim.invoke(instance, monde2);
        } catch (ClassNotFoundException e) {
            System.out.println("Classe Simulation non trouvée.");
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        ClientTwisk clientTwisk = new ClientTwisk();
    }
    private static Monde monde1(){
        //4 étapes
        //0 bifurcation
        //au cinéma
        Monde monde = new Monde() ;

        Guichet guichet = new Guichet("ticket", 2) ;
        Activite act1 = new ActiviteRestreinte("Salle Cinéma", 3, 1) ;

        Etape etape1 = new Activite("Accueil") ;
        Etape etape2 = new Activite("snacks") ;

        etape1.ajouterSuccesseur(guichet) ; //accueil ->ticket (guichet)
        guichet.ajouterSuccesseur(act1) ; //ticket -> bar/snack
        act1.ajouterSuccesseur(etape2);

        monde.ajouter(etape1, etape2) ;
        monde.ajouter(guichet, act1) ;
        monde.aCommeEntree(etape1);
        monde.aCommeSortie(etape2) ;

        return monde;
    }
    private static Monde monde2() {
        //6 étapes
        //1 bifurcation
     Monde monde = new Monde();
     Etape grand8 = new ActiviteRestreinte("Grand huit", 5, 1);
     Etape fileRoue = new Guichet("File grande Roue", 2);
     Etape fileGrand8 = new Guichet("File grand 8", 3);
     Etape grandeRoue = new ActiviteRestreinte("Grande Roue");

     Etape snack1 = new Activite("snack 1");
     Etape snack2 = new Activite("snack 2");
     fileGrand8.ajouterSuccesseur(grand8);
     grand8.ajouterSuccesseur(fileRoue);
     fileRoue.ajouterSuccesseur(grandeRoue);
     grandeRoue.ajouterSuccesseur(snack1, snack2);
     monde.aCommeEntree(fileGrand8);
     monde.aCommeSortie(snack1, snack2);


     monde.ajouter(fileGrand8, grand8, fileRoue,grandeRoue, snack1, snack2);

     return monde;
    }
    private static Monde monde3(){
        //6 étapes
        // 2 bifurcations
        Monde monde = new Monde();

        Etape act1 = new Activite("act 1");
        Etape act2 = new Activite("act 2", 5, 3);

        Etape guichet1 = new Guichet("file Act Restreinte ",2);
        Etape actRes = new ActiviteRestreinte("act Restreinte", 6, 3);

        Etape act3 = new Activite("act 3");
        Etape act4 = new Activite("act 4");


        monde.aCommeEntree(act1);
        monde.aCommeSortie(act4);

        act1.ajouterSuccesseur(guichet1,act2);
        act2.ajouterSuccesseur(act3);
        act3.ajouterSuccesseur(act4);

        guichet1.ajouterSuccesseur(actRes);
        actRes.ajouterSuccesseur(act3, act4);

        monde.ajouter(act1, act2, guichet1, actRes, act3, act4);
        return monde;
    }

}
