package ab.lobotemporale;

import ab.core.NodoCerebrale;
import ab.core.Messaggio;

/**
 * Hub di integrazione multisensoriale: memoria episodica, familiarita', concetti astratti e sociali, deja vu
 *
 * Aree di Brodmann: 38
 */
public class AreaTemporopolare extends NodoCerebrale {

    public AreaTemporopolare() {
        super("AreaTemporopolare", "38", "Hub di integrazione multisensoriale: memoria episodica, familiarita', concetti astratti e sociali, deja vu");
    }

    @Override
    public void ricevi(Messaggio msg) {
        System.out.println("[" + getNome() + "] ricevuto messaggio di tipo '" + msg.getTipo()
                + "' da " + msg.getMittente() + " -> payload: " + msg.getPayload());
    }
}
