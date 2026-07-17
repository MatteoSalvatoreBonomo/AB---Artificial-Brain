package ab.lobooccipitale;

import ab.core.NodoCerebrale;
import ab.core.Messaggio;

/**
 * Elaborazione della forma dinamica e del movimento globale degli oggetti
 *
 * Aree di Brodmann: 19/V3
 */
public class CortecciaVisivaV3 extends NodoCerebrale {

    public CortecciaVisivaV3() {
        super("CortecciaVisivaV3", "19/V3", "Elaborazione della forma dinamica e del movimento globale degli oggetti");
    }

    @Override
    public void ricevi(Messaggio msg) {
        System.out.println("[" + getNome() + "] ricevuto messaggio di tipo '" + msg.getTipo()
                + "' da " + msg.getMittente() + " -> payload: " + msg.getPayload());
    }
}
