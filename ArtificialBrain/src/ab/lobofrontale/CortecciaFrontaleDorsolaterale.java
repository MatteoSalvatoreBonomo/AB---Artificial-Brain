package ab.lobofrontale;

import ab.core.NodoCerebrale;
import ab.core.Messaggio;

/**
 * Ragionamento astratto, controllo emotivo freddo, organizzazione temporale delle azioni
 *
 * Aree di Brodmann: 9-10-46
 */
public class CortecciaFrontaleDorsolaterale extends NodoCerebrale {

    public CortecciaFrontaleDorsolaterale() {
        super("CortecciaFrontaleDorsolaterale", "9-10-46", "Ragionamento astratto, controllo emotivo freddo, organizzazione temporale delle azioni");
    }

    @Override
    public void ricevi(Messaggio msg) {
        System.out.println("[" + getNome() + "] ricevuto messaggio di tipo '" + msg.getTipo()
                + "' da " + msg.getMittente() + " -> payload: " + msg.getPayload());
    }
}
