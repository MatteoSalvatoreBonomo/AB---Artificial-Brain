package ab.lobooccipitale;

import ab.core.NodoCerebrale;
import ab.core.Messaggio;

/**
 * Riceve input diretti dalla V1; integra contorni illusori, profondita' stereoscopica, flussi distinti
 *
 * Aree di Brodmann: 18/V2
 */
public class CortecciaVisivaSecondaria extends NodoCerebrale {

    public CortecciaVisivaSecondaria() {
        super("CortecciaVisivaSecondaria", "18/V2", "Riceve input diretti dalla V1; integra contorni illusori, profondita' stereoscopica, flussi distinti");
    }

    @Override
    public void ricevi(Messaggio msg) {
        System.out.println("[" + getNome() + "] ricevuto messaggio di tipo '" + msg.getTipo()
                + "' da " + msg.getMittente() + " -> payload: " + msg.getPayload());
    }
}
