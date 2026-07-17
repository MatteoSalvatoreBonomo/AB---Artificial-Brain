package ab.lobolimbico;

import ab.core.NodoCerebrale;
import ab.core.Messaggio;

/**
 * Elaborazione emotiva, gusto, funzioni autonomiche, regolazione viscerale e olfatto
 *
 * Aree di Brodmann: 13-14-16-43-52
 */
public class Insula extends NodoCerebrale {

    public Insula() {
        super("Insula", "13-14-16-43-52", "Elaborazione emotiva, gusto, funzioni autonomiche, regolazione viscerale e olfatto");
    }

    @Override
    public void ricevi(Messaggio msg) {
        System.out.println("[" + getNome() + "] ricevuto messaggio di tipo '" + msg.getTipo()
                + "' da " + msg.getMittente() + " -> payload: " + msg.getPayload());
    }
}
