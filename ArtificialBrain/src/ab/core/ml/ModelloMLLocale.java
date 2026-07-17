package ab.core.ml;

import java.io.IOException;
import java.util.Map;

/**
 * Implementazione "di default" per l'intero cervello artificiale: garantisce
 * l'indipendenza da terzi e la possibilita' di funzionare sempre, anche
 * offline, pur sfruttando un vero modello ML quando disponibile.
 *
 * Strategia:
 *  1) prova prima il modello {@link ModelloMLOllama}, che parla via HTTP con
 *     un server di inferenza locale (es. Ollama in ascolto su
 *     http://localhost:11434) — nessuna chiamata verso internet o servizi
 *     cloud di terze parti, resta tutto sulla macchina;
 *  2) se quel server non e' raggiungibile (non installato, non avviato,
 *     macchina offline...) ripiega IN MODO TRASPARENTE sul motore
 *     completamente autonomo {@link ModelloMLOffline}, che non ha alcuna
 *     dipendenza esterna.
 *
 * Il chiamante (un {@link ab.core.NodoCerebrale}) non deve mai preoccuparsi
 * di quale dei due sia stato effettivamente usato: ottiene sempre una
 * risposta valida. Il campo "esito" nella risposta ("ollama" oppure
 * "offline") permette comunque di distinguere a posteriori quale motore ha
 * effettivamente elaborato la richiesta.
 */
public class ModelloMLLocale implements ModelloML {

    private final ModelloML apiLocale;
    private final ModelloML motoreOffline;

    /** Configurazione di default: Ollama su localhost, con riserva offline integrata. */
    public ModelloMLLocale() {
        this(new ModelloMLOllama(), new ModelloMLOffline());
    }

    /** Configurazione di default con URL/modello Ollama personalizzati. */
    public ModelloMLLocale(String urlApiLocale, String nomeModello) {
        this(new ModelloMLOllama(urlApiLocale, nomeModello), new ModelloMLOffline());
    }

    /** Costruttore avanzato: permette di sostituire liberamente sia il motore primario sia la riserva. */
    public ModelloMLLocale(ModelloML apiLocale, ModelloML motoreOffline) {
        this.apiLocale = apiLocale;
        this.motoreOffline = motoreOffline;
    }

    @Override
    public Map<String, Object> elabora(String nodoMittente, String tipo, Map<String, Object> input) throws IOException {
        try {
            return apiLocale.elabora(nodoMittente, tipo, input);
        } catch (IOException e) {
            // Nessun server locale raggiungibile (offline, non avviato, ecc.):
            // si ripiega sul motore integrato, senza mai far fallire il nodo chiamante.
            System.err.println("[" + nodoMittente + "] API ML locale non raggiungibile (" + e.getMessage()
                    + "): uso il motore offline integrato");
            return motoreOffline.elabora(nodoMittente, tipo, input);
        }
    }
}
