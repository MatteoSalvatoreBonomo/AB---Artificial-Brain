package ab.lobofrontale;

import ab.core.NodoCerebrale;
import ab.core.Messaggio;

/**
 * Selezione delle risposte comportamentali; sistema di ricompensa/punizione
 *
 * Aree di Brodmann: 12-47
 */
public class CortecciaFrontaleOrbitale extends NodoCerebrale {

    public CortecciaFrontaleOrbitale() {
        super("CortecciaFrontaleOrbitale", "12-47", "Selezione delle risposte comportamentali; sistema di ricompensa/punizione");
    }

    @Override
    public void ricevi(Messaggio msg) {
        System.out.println("[" + getNome() + "] ricevuto messaggio di tipo '" + msg.getTipo()
                + "' da " + msg.getMittente() + " -> payload: " + msg.getPayload());
    }
}
