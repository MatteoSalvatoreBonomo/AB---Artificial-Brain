package ab.lobofrontale;

import ab.core.NodoCerebrale;
import ab.core.Messaggio;

/**
 * Rilevamento errori, monitoraggio conflitti, regolazione autonoma e dell'attenzione, controllo del dolore
 *
 * Aree di Brodmann: 24-25-32-33
 */
public class CortecciaCingolataAnteriore extends NodoCerebrale {

    public CortecciaCingolataAnteriore() {
        super("CortecciaCingolataAnteriore", "24-25-32-33", "Rilevamento errori, monitoraggio conflitti, regolazione autonoma e dell'attenzione, controllo del dolore");
    }

    @Override
    public void ricevi(Messaggio msg) {
        System.out.println("[" + getNome() + "] ricevuto messaggio di tipo '" + msg.getTipo()
                + "' da " + msg.getMittente() + " -> payload: " + msg.getPayload());
    }
}
