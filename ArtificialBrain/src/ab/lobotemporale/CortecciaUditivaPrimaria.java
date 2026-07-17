package ab.lobotemporale;

import ab.core.NodoCerebrale;
import ab.core.Messaggio;

/**
 * Riceve input diretti dal corpo genicolato mediale del talamo; decodifica le caratteristiche fisiche del suono
 *
 * Aree di Brodmann: 41
 */
public class CortecciaUditivaPrimaria extends NodoCerebrale {

    public CortecciaUditivaPrimaria() {
        super("CortecciaUditivaPrimaria", "41", "Riceve input diretti dal corpo genicolato mediale del talamo; decodifica le caratteristiche fisiche del suono");
    }

    @Override
    public void ricevi(Messaggio msg) {
        System.out.println("[" + getNome() + "] ricevuto messaggio di tipo '" + msg.getTipo()
                + "' da " + msg.getMittente() + " -> payload: " + msg.getPayload());
    }
}
