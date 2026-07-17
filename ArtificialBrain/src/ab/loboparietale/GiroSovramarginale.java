package ab.loboparietale;

import ab.core.NodoCerebrale;
import ab.core.Messaggio;

/**
 * Elaborazione fonologica delle parole udite, comprensione del linguaggio complesso, immagine corporea
 *
 * Aree di Brodmann: 40
 */
public class GiroSovramarginale extends NodoCerebrale {

    public GiroSovramarginale() {
        super("GiroSovramarginale", "40", "Elaborazione fonologica delle parole udite, comprensione del linguaggio complesso, immagine corporea");
    }

    @Override
    public void ricevi(Messaggio msg) {
        System.out.println("[" + getNome() + "] ricevuto messaggio di tipo '" + msg.getTipo()
                + "' da " + msg.getMittente() + " -> payload: " + msg.getPayload());
    }
}
