package ab.core;

/**
 * Contratto comune che ogni area/nodo cerebrale deve rispettare per poter
 * essere registrata sul BUS e ricevere messaggi instradati dal Talamo.
 */
public interface RegioneCerebrale {

    /** Nome univoco del nodo, usato come chiave di routing dal Talamo. */
    String getNome();

    /** Aree di Brodmann associate al nodo (es. "44-45"), a scopo informativo. */
    String getAreeBrodmann();

    /** Breve descrizione funzionale del nodo. */
    String getDescrizione();

    /**
     * Callback invocata dal Talamo quando un messaggio instradato
     * raggiunge questo nodo.
     */
    void ricevi(Messaggio msg);
}
