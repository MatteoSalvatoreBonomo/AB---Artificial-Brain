package ab.loboparietale;

import ab.core.NodoCerebrale;
import ab.core.Messaggio;

/**
 * Associazione somatosensoriale e percezione dello spazio peripersonale
 *
 * Aree di Brodmann: 5-7
 */
public class LobuloParietaleSuperiore extends NodoCerebrale {

    public LobuloParietaleSuperiore() {
        super("LobuloParietaleSuperiore", "5-7", "Associazione somatosensoriale e percezione dello spazio peripersonale");
    }

    @Override
    public void ricevi(Messaggio msg) {
        System.out.println("[" + getNome() + "] ricevuto messaggio di tipo '" + msg.getTipo()
                + "' da " + msg.getMittente() + " -> payload: " + msg.getPayload());
    }
}
