package ab.lobofrontale;

import ab.core.NodoCerebrale;
import ab.core.Messaggio;

/**
 * Produzione motoria del linguaggio, fluenza verbale e articolazione delle parole
 *
 * Aree di Brodmann: 44-45
 */
public class Broca extends NodoCerebrale {

    public Broca() {
        super("Broca", "44-45", "Produzione motoria del linguaggio, fluenza verbale e articolazione delle parole");
    }

    @Override
    public void ricevi(Messaggio msg) {
        System.out.println("[" + getNome() + "] ricevuto messaggio di tipo '" + msg.getTipo()
                + "' da " + msg.getMittente() + " -> payload: " + msg.getPayload());
    }
}
