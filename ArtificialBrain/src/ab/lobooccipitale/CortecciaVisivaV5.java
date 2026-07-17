package ab.lobooccipitale;

import ab.core.NodoCerebrale;
import ab.core.Messaggio;

/**
 * Analisi del movimento e della direzione degli stimoli visivi
 *
 * Aree di Brodmann: 19/V5
 */
public class CortecciaVisivaV5 extends NodoCerebrale {

    public CortecciaVisivaV5() {
        super("CortecciaVisivaV5", "19/V5", "Analisi del movimento e della direzione degli stimoli visivi");
    }

    @Override
    public void ricevi(Messaggio msg) {
        System.out.println("[" + getNome() + "] ricevuto messaggio di tipo '" + msg.getTipo()
                + "' da " + msg.getMittente() + " -> payload: " + msg.getPayload());
    }
}
