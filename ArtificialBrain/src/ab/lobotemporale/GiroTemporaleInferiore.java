package ab.lobotemporale;

import ab.core.NodoCerebrale;
import ab.core.Messaggio;

/**
 * Elaborazione di stimoli visivi complessi e memoria semantica
 *
 * Aree di Brodmann: 20
 */
public class GiroTemporaleInferiore extends NodoCerebrale {

    public GiroTemporaleInferiore() {
        super("GiroTemporaleInferiore", "20", "Elaborazione di stimoli visivi complessi e memoria semantica");
    }

    @Override
    public void ricevi(Messaggio msg) {
        System.out.println("[" + getNome() + "] ricevuto messaggio di tipo '" + msg.getTipo()
                + "' da " + msg.getMittente() + " -> payload: " + msg.getPayload());
    }
}
