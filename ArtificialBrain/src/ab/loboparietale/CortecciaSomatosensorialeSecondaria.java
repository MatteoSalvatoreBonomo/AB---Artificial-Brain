package ab.loboparietale;

import ab.core.NodoCerebrale;
import ab.core.Messaggio;

/**
 * Percezione tattile bilaterale e stereognosia
 *
 * Aree di Brodmann: 40
 */
public class CortecciaSomatosensorialeSecondaria extends NodoCerebrale {

    public CortecciaSomatosensorialeSecondaria() {
        super("CortecciaSomatosensorialeSecondaria", "40", "Percezione tattile bilaterale e stereognosia");
    }

    @Override
    public void ricevi(Messaggio msg) {
        System.out.println("[" + getNome() + "] ricevuto messaggio di tipo '" + msg.getTipo()
                + "' da " + msg.getMittente() + " -> payload: " + msg.getPayload());
    }
}
