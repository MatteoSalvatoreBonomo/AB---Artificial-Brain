package ab.lobofrontale;

import ab.core.NodoCerebrale;
import ab.core.Messaggio;

/**
 * Proietta all'area 4 e ai nuclei della base; contiene neuroni specchio
 *
 * Aree di Brodmann: 6
 */
public class CortecciaPremotoria extends NodoCerebrale {

    public CortecciaPremotoria() {
        super("CortecciaPremotoria", "6", "Proietta all'area 4 e ai nuclei della base; contiene neuroni specchio");
    }

    @Override
    public void ricevi(Messaggio msg) {
        System.out.println("[" + getNome() + "] ricevuto messaggio di tipo '" + msg.getTipo()
                + "' da " + msg.getMittente() + " -> payload: " + msg.getPayload());
    }
}
