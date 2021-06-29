package twisk.mondeIG;

import twisk.monde.Etape;

import java.util.HashMap;
import java.util.Map;

public class CorrespondanceEtapes {
    private HashMap<EtapeIG, Etape> etapes;

    /**
     * Constructeur
     */
    public CorrespondanceEtapes(){
        etapes = new HashMap<>();
    }

    /**
     * Ajoute une correspondance entre l'étapeIG et l'étape passées en paramètre
     * @param etig L'étapeIG à associer
     * @param et L'étape à associer
     */
    public void ajouter(EtapeIG etig,Etape et){
        etapes.put(etig,et);
    }

    /**
     * Retourne l'étape associée à l'étapeIG en paramètre
     * @param e Une étape
     * @return L'étape associée à l'étapeIG en paramètre
     */
    public Etape get(EtapeIG e){
        return etapes.get(e);
    }

    /**
     *
     * @param e Etape du client
     * @return EtapeIG du client
     */
    public EtapeIG getEtapeIG(Etape e){
        EtapeIG etapeIG = null;
        for (Map.Entry<EtapeIG, Etape> entry : etapes.entrySet()) {
            if (entry.getValue().equals(e)) {
                etapeIG =  entry.getKey();
            }
        }
        return etapeIG;
    }
}
