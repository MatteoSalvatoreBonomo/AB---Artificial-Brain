package ab.talamo.circuiti;

import ab.core.Messaggio;
import ab.core.RegioneCerebrale;

import java.util.Map;

/**
 * Rappresenta un percorso di routing "speciale", alternativo alla normale
 * tabella tipo-messaggio -> destinatari del Talamo. Utile per modellare
 * circuiti neurali noti che non sono un semplice instradamento 1:1,
 * come la doppia via della paura descritta da LeDoux.
 */
public interface CircuitoTalamico {

    /** Nome descrittivo del circuito, per log/debug. */
    String getNome();

    /**
     * Esegue il percorso del circuito per il messaggio dato, consegnandolo
     * ai nodi coinvolti nell'ordine e con i tempi che il circuito prevede.
     *
     * @param msg      il messaggio da propagare
     * @param registro mappa nome->nodo da cui recuperare i destinatari registrati
     */
    void propaga(Messaggio msg, Map<String, RegioneCerebrale> registro);
}
