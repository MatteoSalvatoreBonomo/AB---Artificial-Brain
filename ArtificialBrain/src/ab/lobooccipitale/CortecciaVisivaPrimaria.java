package ab.lobooccipitale;

import ab.core.NodoCerebrale;
import ab.core.Messaggio;

/**
 * Riceve input visivi grezzi dal corpo genicolato laterale del talamo; elabora le caratteristiche elementari dello stimolo visivo
 *
 * Aree di Brodmann: 17/V1
 */
public class CortecciaVisivaPrimaria extends NodoCerebrale {

    public CortecciaVisivaPrimaria() {
        super("CortecciaVisivaPrimaria", "17/V1", "Riceve input visivi grezzi dal corpo genicolato laterale del talamo; elabora le caratteristiche elementari dello stimolo visivo");
    }

    @Override
    public void ricevi(Messaggio msg) {
        System.out.println("[" + getNome() + "] ricevuto messaggio di tipo '" + msg.getTipo()
                + "' da " + msg.getMittente() + " -> payload: " + msg.getPayload());
    }
}
