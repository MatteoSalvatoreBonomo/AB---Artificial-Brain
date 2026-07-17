package ab.lobofrontale;

import ab.core.NodoCerebrale;
import ab.core.Messaggio;

/**
 * Controlla i movimenti volontari e coniugati degli occhi verso stimoli visivi o uditivi rilevanti
 *
 * Aree di Brodmann: 8
 */
public class CampoOculareFrontale extends NodoCerebrale {

    public CampoOculareFrontale() {
        super("CampoOculareFrontale", "8", "Controlla i movimenti volontari e coniugati degli occhi verso stimoli visivi o uditivi rilevanti");
    }

    @Override
    public void ricevi(Messaggio msg) {
        System.out.println("[" + getNome() + "] ricevuto messaggio di tipo '" + msg.getTipo()
                + "' da " + msg.getMittente() + " -> payload: " + msg.getPayload());
    }
}
