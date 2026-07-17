package ab.lobotemporale;

import ab.core.NodoCerebrale;
import ab.core.Messaggio;

/**
 * Integrazione degli stimoli uditivi grezzi; riconoscimento di pattern sonori complessi e sequenze temporali
 *
 * Aree di Brodmann: 42
 */
public class CortecciaUditivaSecondaria extends NodoCerebrale {

    public CortecciaUditivaSecondaria() {
        super("CortecciaUditivaSecondaria", "42", "Integrazione degli stimoli uditivi grezzi; riconoscimento di pattern sonori complessi e sequenze temporali");
    }

    @Override
    public void ricevi(Messaggio msg) {
        System.out.println("[" + getNome() + "] ricevuto messaggio di tipo '" + msg.getTipo()
                + "' da " + msg.getMittente() + " -> payload: " + msg.getPayload());
    }
}
