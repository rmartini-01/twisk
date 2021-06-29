package twisk.exceptions;

public class ExceptionParametreNonValide extends TwiskException{

    /**
     * Constructeur d'une exception Twisk lorsqu'un des paramètres entrés n'est pas valide
     * @param message Message que l'on veut envoyer
     */
    public ExceptionParametreNonValide(String message) {
        super(message);
    }
}
