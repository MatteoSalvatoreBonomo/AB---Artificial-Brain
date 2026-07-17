package ab.lobotemporale;

import ab.core.NodoCerebrale;
import ab.core.Messaggio;

/**
 * Formazione ippocampale: consolidamento della memoria a lungo termine e memoria episodica
 *
 * Aree di Brodmann: -
 */
public class Ippocampo extends NodoCerebrale {

    public Ippocampo() {
        super("Ippocampo", "-", "Formazione ippocampale: consolidamento della memoria a lungo termine e memoria episodica");
    }

    @Override
    public void ricevi(Messaggio msg) {
        System.out.println("[" + getNome() + "] ricevuto messaggio di tipo '" + msg.getTipo()
                + "' da " + msg.getMittente() + " -> payload: " + msg.getPayload());
    }
}
