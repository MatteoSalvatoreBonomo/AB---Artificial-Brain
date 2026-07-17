package ab.loboparietale;

import ab.core.NodoCerebrale;
import ab.core.Messaggio;

/**
 * Nodo chiave del Default Mode Network: autoconsapevolezza e memoria episodica
 *
 * Aree di Brodmann: 7/31
 */
public class Precuneo extends NodoCerebrale {

    public Precuneo() {
        super("Precuneo", "7/31", "Nodo chiave del Default Mode Network: autoconsapevolezza e memoria episodica");
    }

    @Override
    public void ricevi(Messaggio msg) {
        System.out.println("[" + getNome() + "] ricevuto messaggio di tipo '" + msg.getTipo()
                + "' da " + msg.getMittente() + " -> payload: " + msg.getPayload());
    }
}
