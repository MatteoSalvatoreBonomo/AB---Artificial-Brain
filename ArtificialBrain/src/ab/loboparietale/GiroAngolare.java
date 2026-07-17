package ab.loboparietale;

import ab.core.NodoCerebrale;
import ab.core.Messaggio;

/**
 * Integrazione visivo-uditivo-tattile; centro della lettura, scrittura e calcolo matematico
 *
 * Aree di Brodmann: 39
 */
public class GiroAngolare extends NodoCerebrale {

    public GiroAngolare() {
        super("GiroAngolare", "39", "Integrazione visivo-uditivo-tattile; centro della lettura, scrittura e calcolo matematico");
    }

    @Override
    public void ricevi(Messaggio msg) {
        System.out.println("[" + getNome() + "] ricevuto messaggio di tipo '" + msg.getTipo()
                + "' da " + msg.getMittente() + " -> payload: " + msg.getPayload());
    }
}
