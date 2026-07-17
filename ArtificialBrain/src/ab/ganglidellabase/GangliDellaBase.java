package ab.ganglidellabase;

import ab.core.NodoCerebrale;
import ab.core.Messaggio;

/**
 * Selezione e avvio dei programmi motori; coinvolti anche in abitudini e apprendimento per rinforzo
 *
 * Aree di Brodmann: -
 */
public class GangliDellaBase extends NodoCerebrale {

    public GangliDellaBase() {
        super("GangliDellaBase", "-", "Selezione e avvio dei programmi motori; coinvolti anche in abitudini e apprendimento per rinforzo");
    }

    @Override
    public void ricevi(Messaggio msg) {
        System.out.println("[" + getNome() + "] ricevuto messaggio di tipo '" + msg.getTipo()
                + "' da " + msg.getMittente() + " -> payload: " + msg.getPayload());
    }
}
