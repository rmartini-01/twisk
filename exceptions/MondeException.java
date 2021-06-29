package twisk.exceptions;

public class MondeException extends Exception{
    /**
     * Constructeur d'une exception du monde
     * @param message Message que l'on veut envoyer
     */
    public MondeException(String message){
        super(message);
    }
}
