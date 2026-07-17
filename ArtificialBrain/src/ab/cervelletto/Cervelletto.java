package ab.cervelletto;

import ab.core.NodoCerebrale;
import ab.core.Messaggio;

/**
 * Coordinazione motoria fine, equilibrio ed apprendimento procedurale
 *
 * Aree di Brodmann: -
 */
public class Cervelletto extends NodoCerebrale {

    public Cervelletto() {
        super("Cervelletto", "-", "Coordinazione motoria fine, equilibrio ed apprendimento procedurale");
    }

    @Override
    public void ricevi(Messaggio msg) {
        System.out.println("[" + getNome() + "] ricevuto messaggio di tipo '" + msg.getTipo()
                + "' da " + msg.getMittente() + " -> payload: " + msg.getPayload());
    }
}
