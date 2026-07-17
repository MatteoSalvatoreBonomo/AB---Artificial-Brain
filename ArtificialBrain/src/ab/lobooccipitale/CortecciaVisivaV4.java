package ab.lobooccipitale;

import ab.core.NodoCerebrale;
import ab.core.Messaggio;

/**
 * Percezione del colore costante e della forma statica complessa
 *
 * Aree di Brodmann: 19/V4
 */
public class CortecciaVisivaV4 extends NodoCerebrale {

    public CortecciaVisivaV4() {
        super("CortecciaVisivaV4", "19/V4", "Percezione del colore costante e della forma statica complessa");
    }

    @Override
    public void ricevi(Messaggio msg) {
        System.out.println("[" + getNome() + "] ricevuto messaggio di tipo '" + msg.getTipo()
                + "' da " + msg.getMittente() + " -> payload: " + msg.getPayload());
    }
}
