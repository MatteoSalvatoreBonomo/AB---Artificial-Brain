package ab.loboparietale;

import ab.core.NodoCerebrale;
import ab.core.Messaggio;

/**
 * Coinvolta nella memoria episodica e nell'orientamento spaziale, parte del Default Mode Network
 *
 * Aree di Brodmann: 23-31
 */
public class CortecciaCingolataPosteriore extends NodoCerebrale {

    public CortecciaCingolataPosteriore() {
        super("CortecciaCingolataPosteriore", "23-31", "Coinvolta nella memoria episodica e nell'orientamento spaziale, parte del Default Mode Network");
    }

    @Override
    public void ricevi(Messaggio msg) {
        System.out.println("[" + getNome() + "] ricevuto messaggio di tipo '" + msg.getTipo()
                + "' da " + msg.getMittente() + " -> payload: " + msg.getPayload());
    }
}
