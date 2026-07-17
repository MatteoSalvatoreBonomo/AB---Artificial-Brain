package ab.lobofrontale;

import ab.core.NodoCerebrale;
import ab.core.Messaggio;

/**
 * Genera impulsi nervosi per il movimento volontario dei muscoli scheletrici controlaterali
 *
 * Aree di Brodmann: 4
 */
public class CortecciaMotoriaPrimaria extends NodoCerebrale {

    public CortecciaMotoriaPrimaria() {
        super("CortecciaMotoriaPrimaria", "4", "Genera impulsi nervosi per il movimento volontario dei muscoli scheletrici controlaterali");
    }

    @Override
    public void ricevi(Messaggio msg) {
        System.out.println("[" + getNome() + "] ricevuto messaggio di tipo '" + msg.getTipo()
                + "' da " + msg.getMittente() + " -> payload: " + msg.getPayload());
    }
}
