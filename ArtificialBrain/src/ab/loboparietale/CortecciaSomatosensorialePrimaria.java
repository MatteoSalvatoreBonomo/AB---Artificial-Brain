package ab.loboparietale;

import ab.core.NodoCerebrale;
import ab.core.Messaggio;

/**
 * Riceve la maggior parte degli input talamici grezzi; percezione tattile elementare, texture, forma, propriocezione
 *
 * Aree di Brodmann: 1-2-3
 */
public class CortecciaSomatosensorialePrimaria extends NodoCerebrale {

    public CortecciaSomatosensorialePrimaria() {
        super("CortecciaSomatosensorialePrimaria", "1-2-3", "Riceve la maggior parte degli input talamici grezzi; percezione tattile elementare, texture, forma, propriocezione");
    }

    @Override
    public void ricevi(Messaggio msg) {
        System.out.println("[" + getNome() + "] ricevuto messaggio di tipo '" + msg.getTipo()
                + "' da " + msg.getMittente() + " -> payload: " + msg.getPayload());
    }
}
