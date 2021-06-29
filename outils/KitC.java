package twisk.outils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class KitC {
    private static String OS  = System.getProperty("os.name").toLowerCase();
    protected int numlib;
    protected Runtime runtime = Runtime.getRuntime();
    /**
     * Constructeur
     */
    public KitC() {
    }

    public int getNumlib() {
        return numlib;
    }
    /**
     * Copie les fichiers ressources de C au niveau du dossier tmp pour créer les fichiers temporaires
     */
    public void creerEnvironnement(){

        try{
            //Crée le répertoire /tmp/twisk/
            Path directories = Files.createDirectories(Paths.get("/tmp/twisk"));

            String[] liste = {"programmeC.o","def.h", "codeNatif.o", "lois.h", "lois.o"};

            for(String nom : liste) {
                    /*Path source = Paths.get(getClass().getResource("/codeCMac/"+ nom).getPath());
                    Path newdir = Paths.get("/tmp/twisk/");
                    Files.copy(source, newdir.resolve(source.getFileName()),REPLACE_EXISTING);
                    */
                InputStream source;
                //  Pour système MacOS
                if(OS.contains("mac os")) {
                    // copie des deux fichiers programmeC.o et def.h depuis le projet sous /tmp/twisk
                    source = getClass().getResource("/codeCMac/" + nom).openStream();
                } else{  // Pour système Linux
                    // copie des deux fichiers programmeC.o et def.h depuis le projet sous /tmp/twisk
                    source = getClass().getResource("/codeC/" + nom).openStream();
                }
                    File destination = new File("/tmp/twisk/" + nom);
                    copier(source, destination);
                }

            }catch(IOException e) {
                e.printStackTrace();
            }
    }

    /**
     * Crée le fichier client.c dans le répertoire /tmp/twisk/ à partir des lignes de code C produites par le monde
     * @param codeC Code créé pas le monde
     */
    public void creerFichier(String codeC)  {
        try {
            File clientC = new File("/tmp/twisk/client.c");
            if (!clientC.createNewFile()) {
                //si le fichier existe déjà, on le recrée pour le mettre à jour.
                clientC = new File("/tmp/twisk/client.c");
            }
            //on écrit le code dans le fichier client.c
            FileWriter code = new FileWriter(clientC);
            code.write(codeC);
            code.close();
            //on ferme le fichier
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Construit la classe client.o dans le dossier /tmp/twisk/ à partir du fichier source de cette classe
     */
    public void compiler(){
        Runtime runtime = Runtime.getRuntime();
        try{
            Process p = runtime.exec("gcc -Wall -fPIC -c /tmp/twisk/client.c -o /tmp/twisk/client.o" );

            // récupération des messages sur la sortie standard et la sortie d’erreur de la commande exécutée
            BufferedReader output =new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader error = new BufferedReader(new InputStreamReader(p.getErrorStream()));

            String ligne ;
            while((ligne = output.readLine()) !=null) {
                System.out.println(ligne);
            }
            while((ligne = error.readLine()) !=null) {
                System.out.println(ligne);
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Construit la librairie libTwisk dans le répertoire /tmp/twisk/ à partir des fichiers du répertoire
     */
    public void construireLaLibrairie(){

        numlib = FabriqueNumero.getInstance().getNumeroLibrairie();
        try{

            if(OS.contains("mac os")){
                //partie de l'exécution pour mac :
                Process processMac = runtime.exec("gcc -dynamiclib -o /tmp/twisk/libTwisk"+numlib+".dylib /tmp/twisk/lois.o /tmp/twisk/client.o /tmp/twisk/codeNatif.o /tmp/twisk/programmeC.o ");
                processMac.waitFor();
                // récupération des messages sur la sortie standard et la sortie d’erreur de la commande exécutée
                BufferedReader output =new BufferedReader(new InputStreamReader(processMac.getInputStream()));
                BufferedReader error = new BufferedReader(new InputStreamReader(processMac.getErrorStream()));
                String ligne ;
                while((ligne = output.readLine()) !=null) {
                    System.out.println(ligne);
                }
                while((ligne = error.readLine()) !=null) {
                    System.out.println(ligne);
                }
            }else{
                //Partie de l'exécution pour linux:
                Process processLinux = runtime.exec("gcc -shared /tmp/twisk/programmeC.o /tmp/twisk/lois.o /tmp/twisk/codeNatif.o /tmp/twisk/client.o -o /tmp/twisk/libTwisk"+numlib+".so");
                processLinux.waitFor();
                // récupération des messages sur la sortie standard et la sortie d’erreur de la commande exécutée
                BufferedReader output =new BufferedReader(new InputStreamReader(processLinux.getInputStream()));
                BufferedReader error = new BufferedReader(new InputStreamReader(processLinux.getErrorStream()));
                String ligne ;
                while((ligne = output.readLine()) !=null) {
                    System.out.println(ligne);
                }
                while((ligne = error.readLine()) !=null) {
                    System.out.println(ligne);
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Copie le contenu d'un fichier dans un autre
     * @param source Le fichier source
     * @param dest Le fichier de destination
     * @throws IOException Erreur lors la lecture/écriture d'un fichier
     */
    private void copier(InputStream source,File dest) throws IOException {
        InputStream sourceFile = source;
        OutputStream destinationFile = new FileOutputStream(dest);

        // Lecture par segment de 0.5Mo
        byte buffer[] = new byte[512 * 1024];
        int nbLecture;
        while ((nbLecture = sourceFile.read(buffer)) != -1){
            destinationFile.write(buffer, 0, nbLecture);
        }
        //On ferme les deux fichiers lorsque la tâche est terminée
        destinationFile.close();
        sourceFile.close();
    }

    /**
     *
     * @param pidClient pid du client à arrêter
     */
    public void detruireClients(int pidClient){
        try {
            runtime.exec("kill -9"+ pidClient);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
