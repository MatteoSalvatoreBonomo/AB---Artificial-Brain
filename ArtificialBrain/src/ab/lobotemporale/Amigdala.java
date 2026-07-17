package ab.lobotemporale;

import ab.core.NodoCerebrale;
import ab.core.Messaggio;

/**
 * Elaborazione delle emozioni, in particolare paura e minaccia; innesca risposte di allarme rapide
 *
 * Aree di Brodmann: -
 */
public class Amigdala extends NodoCerebrale {

    public Amigdala() {
        super("Amigdala", "-", "Elaborazione delle emozioni, in particolare paura e minaccia; innesca risposte di allarme rapide");
    }

    @Override
    public void ricevi(Messaggio msg) {
        System.out.println("[" + getNome() + "] ricevuto messaggio di tipo '" + msg.getTipo()
                + "' da " + msg.getMittente() + " -> payload: " + msg.getPayload());
    }
}
