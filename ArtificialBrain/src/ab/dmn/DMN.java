package ab.dmn;

import ab.core.NodoCerebrale;
import ab.core.Messaggio;

/**
 * Default Mode Network: rete attiva a riposo, coinvolta nel pensiero autoreferenziale e nel vagare della mente
 *
 * Aree di Brodmann: -
 */
public class DMN extends NodoCerebrale {

    public DMN() {
        super("DMN", "-", "Default Mode Network: rete attiva a riposo, coinvolta nel pensiero autoreferenziale e nel vagare della mente");
    }

    @Override
    public void ricevi(Messaggio msg) {
        System.out.println("[" + getNome() + "] ricevuto messaggio di tipo '" + msg.getTipo()
                + "' da " + msg.getMittente() + " -> payload: " + msg.getPayload());
    }
}
