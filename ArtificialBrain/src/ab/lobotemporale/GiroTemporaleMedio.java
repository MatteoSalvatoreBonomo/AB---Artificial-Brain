package ab.lobotemporale;

import ab.core.NodoCerebrale;
import ab.core.Messaggio;

/**
 * Comprensione del linguaggio a livello superiore, identificazione di categorie di oggetti
 *
 * Aree di Brodmann: 21
 */
public class GiroTemporaleMedio extends NodoCerebrale {

    public GiroTemporaleMedio() {
        super("GiroTemporaleMedio", "21", "Comprensione del linguaggio a livello superiore, identificazione di categorie di oggetti");
    }

    @Override
    public void ricevi(Messaggio msg) {
        System.out.println("[" + getNome() + "] ricevuto messaggio di tipo '" + msg.getTipo()
                + "' da " + msg.getMittente() + " -> payload: " + msg.getPayload());
    }
}
