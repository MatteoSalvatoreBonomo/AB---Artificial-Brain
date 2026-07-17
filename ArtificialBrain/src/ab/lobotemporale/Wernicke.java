package ab.lobotemporale;

import ab.core.NodoCerebrale;
import ab.core.Messaggio;

/**
 * Comprensione semantica del linguaggio parlato e scritto, formulazione di discorsi coerenti
 *
 * Aree di Brodmann: 22
 */
public class Wernicke extends NodoCerebrale {

    public Wernicke() {
        super("Wernicke", "22", "Comprensione semantica del linguaggio parlato e scritto, formulazione di discorsi coerenti");
    }

    @Override
    public void ricevi(Messaggio msg) {
        System.out.println("[" + getNome() + "] ricevuto messaggio di tipo '" + msg.getTipo()
                + "' da " + msg.getMittente() + " -> payload: " + msg.getPayload());
    }
}
