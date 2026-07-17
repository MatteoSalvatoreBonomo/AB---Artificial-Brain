package ab.lobotemporale;

import ab.core.NodoCerebrale;
import ab.core.Messaggio;

/**
 * Memoria di riconoscimento degli oggetti e associazione stimolo-ricompensa
 *
 * Aree di Brodmann: 35-36
 */
public class CortecciaPerirenale extends NodoCerebrale {

    public CortecciaPerirenale() {
        super("CortecciaPerirenale", "35-36", "Memoria di riconoscimento degli oggetti e associazione stimolo-ricompensa");
    }

    @Override
    public void ricevi(Messaggio msg) {
        System.out.println("[" + getNome() + "] ricevuto messaggio di tipo '" + msg.getTipo()
                + "' da " + msg.getMittente() + " -> payload: " + msg.getPayload());
    }
}
