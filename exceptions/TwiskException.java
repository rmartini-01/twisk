package twisk.exceptions;

public class TwiskException extends Exception{

    /**
     * Constructeur d'une exception Twisk
     * @param message Message que l'on veut envoyer
     */
    public TwiskException(String message) {
        super(message);
    }
}
