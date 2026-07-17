package ab.lobotemporale;

import ab.core.NodoCerebrale;
import ab.core.Messaggio;

/**
 * Interfaccia principale tra ippocampo e neocorteccia; la 34 e' la prima area colpita nell'Alzheimer
 *
 * Aree di Brodmann: 28-34
 */
public class CortecciaEntorinale extends NodoCerebrale {

    public CortecciaEntorinale() {
        super("CortecciaEntorinale", "28-34", "Interfaccia principale tra ippocampo e neocorteccia; la 34 e' la prima area colpita nell'Alzheimer");
    }

    @Override
    public void ricevi(Messaggio msg) {
        System.out.println("[" + getNome() + "] ricevuto messaggio di tipo '" + msg.getTipo()
                + "' da " + msg.getMittente() + " -> payload: " + msg.getPayload());
    }
}
