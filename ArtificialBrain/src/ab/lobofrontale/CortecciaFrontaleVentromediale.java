package ab.lobofrontale;

import ab.core.NodoCerebrale;
import ab.core.Messaggio;

/**
 * Controllo emotivo caldo e selezione delle risposte in base a stimoli sensoriali
 *
 * Aree di Brodmann: 11-12
 */
public class CortecciaFrontaleVentromediale extends NodoCerebrale {

    public CortecciaFrontaleVentromediale() {
        super("CortecciaFrontaleVentromediale", "11-12", "Controllo emotivo caldo e selezione delle risposte in base a stimoli sensoriali");
    }

    @Override
    public void ricevi(Messaggio msg) {
        System.out.println("[" + getNome() + "] ricevuto messaggio di tipo '" + msg.getTipo()
                + "' da " + msg.getMittente() + " -> payload: " + msg.getPayload());
    }
}
