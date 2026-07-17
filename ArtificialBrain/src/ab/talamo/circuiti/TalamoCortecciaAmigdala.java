package ab.talamo.circuiti;

import ab.core.Messaggio;
import ab.core.RegioneCerebrale;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Via LUNGA della paura (LeDoux): il talamo manda il segnale alla corteccia
 * (che lo elabora in dettaglio) prima che il risultato raggiunga l'amigdala.
 * E' piu' lenta della via diretta talamo-amigdala, ma piu' accurata: permette
 * di "correggere" un allarme falso (es. capire che non era un serpente ma un
 * ramo) confermando o disinnescando la risposta emotiva innescata dalla via breve.
 */
public class TalamoCortecciaAmigdala implements CircuitoTalamico {

    /** Nome del nodo corticale che fa da tappa intermedia prima dell'amigdala. */
    private final String nodoCorticaleIntermedio;

    public TalamoCortecciaAmigdala(String nodoCorticaleIntermedio) {
        this.nodoCorticaleIntermedio = nodoCorticaleIntermedio;
    }

    @Override
    public String getNome() {
        return "Via lunga talamo-corteccia(" + nodoCorticaleIntermedio + ")-amigdala (lenta e accurata)";
    }

    @Override
    public void propaga(Messaggio msg, Map<String, RegioneCerebrale> registro) {
        RegioneCerebrale corteccia = registro.get(nodoCorticaleIntermedio);
        RegioneCerebrale amigdala = registro.get("Amigdala");

        if (corteccia == null) {
            System.out.println("[" + getNome() + "] Nodo corticale '" + nodoCorticaleIntermedio
                    + "' non registrato: via lunga interrotta per il messaggio " + msg.getId());
            return;
        }

        // 1) il segnale passa prima dalla corteccia, che lo elabora...
        corteccia.ricevi(msg);

        // 2) ... e solo dopo il risultato (qui semplificato: lo stesso messaggio)
        //    raggiunge l'amigdala per un'eventuale conferma/correzione della risposta emotiva
        if (amigdala != null) {
            Map<String, Object> payloadArricchito = new LinkedHashMap<>(msg.getPayload());
            payloadArricchito.put("confermatoDaCorteccia", nodoCorticaleIntermedio);
            Messaggio msgArricchito = new Messaggio(
                    msg.getId() + "-corretto",
                    nodoCorticaleIntermedio,
                    msg.getTipo(),
                    System.currentTimeMillis(),
                    payloadArricchito);
            amigdala.ricevi(msgArricchito);
        }
    }
}
