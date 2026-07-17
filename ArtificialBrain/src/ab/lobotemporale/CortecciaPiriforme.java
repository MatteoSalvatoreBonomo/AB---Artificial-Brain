package ab.lobotemporale;

import ab.core.NodoCerebrale;
import ab.core.Messaggio;

/**
 * Corteccia olfattiva primaria
 *
 * Aree di Brodmann: 27
 */
public class CortecciaPiriforme extends NodoCerebrale {

    public CortecciaPiriforme() {
        super("CortecciaPiriforme", "27", "Corteccia olfattiva primaria");
    }

    @Override
    public void ricevi(Messaggio msg) {
        System.out.println("[" + getNome() + "] ricevuto messaggio di tipo '" + msg.getTipo()
                + "' da " + msg.getMittente() + " -> payload: " + msg.getPayload());
    }
}
