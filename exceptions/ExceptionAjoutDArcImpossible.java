package twisk.exceptions;

public class ExceptionAjoutDArcImpossible extends TwiskException{

    private int code;
    /**
     * Constructeur d'une exception lorsque qu'on veut créer un arc qui n'est pas valide
     * @param message Message que l'on veut envoyer
     */
    public ExceptionAjoutDArcImpossible(String message, int code) {
        super(message);
        this.code = code;
    }

    /**
     * Retourne le code de l'exception : 1 pour un arc sur la même étape, 2 pour un arc entre deux guichets, 3 pour un arc créant une boucle
     * @return Le code de l'exception
     */
    public int getCode() {
        return code;
    }
}
