package ab.lobotemporale;

import ab.core.NodoCerebrale;
import ab.core.Messaggio;

/**
 * Contiene l'Area Fusiforme dei Volti (FFA), essenziale per il riconoscimento individuale dei volti
 *
 * Aree di Brodmann: 37
 */
public class GiroFusiforme extends NodoCerebrale {

    public GiroFusiforme() {
        super("GiroFusiforme", "37", "Contiene l'Area Fusiforme dei Volti (FFA), essenziale per il riconoscimento individuale dei volti");
    }

    @Override
    public void ricevi(Messaggio msg) {
        System.out.println("[" + getNome() + "] ricevuto messaggio di tipo '" + msg.getTipo()
                + "' da " + msg.getMittente() + " -> payload: " + msg.getPayload());
    }
}
