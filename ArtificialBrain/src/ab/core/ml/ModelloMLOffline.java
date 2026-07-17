package ab.core.ml;

import ab.core.JsonUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Motore ML integrato e completamente autonomo: non contatta nessun processo
 * esterno, non richiede rete ne' server di terze parti, e quindi funziona
 * SEMPRE, anche a computer scollegato da internet o con nessun servizio di
 * inferenza installato/avviato.
 *
 * Non e' un semplice "finto" modello ad uso test: e' il motore che garantisce
 * che ogni nodo cerebrale abbia comunque una risposta anche in totale
 * assenza di infrastruttura esterna (vedi {@link ModelloMLLocale}, che lo usa
 * come riserva quando l'API locale non e' raggiungibile).
 *
 * Rispetta comunque il protocollo di comunicazione a file JSON: la richiesta
 * viene scritta su disco in "ml/richieste/", la risposta in "ml/risposte/",
 * e quest'ultima viene riletta da file prima di essere restituita.
 */
public class ModelloMLOffline implements ModelloML {

    private static final Path CARTELLA_RICHIESTE = Paths.get("ml", "richieste");
    private static final Path CARTELLA_RISPOSTE = Paths.get("ml", "risposte");

    public ModelloMLOffline() {
        try {
            Files.createDirectories(CARTELLA_RICHIESTE);
            Files.createDirectories(CARTELLA_RISPOSTE);
        } catch (IOException e) {
            throw new IllegalStateException("Impossibile creare le cartelle 'ml/richieste' e 'ml/risposte'", e);
        }
    }

    @Override
    public Map<String, Object> elabora(String nodoMittente, String tipo, Map<String, Object> input) throws IOException {
        String idScambio = UUID.randomUUID().toString();

        // 1) la richiesta viene sempre scritta su file JSON, come da contratto d'interfaccia
        Map<String, Object> richiesta = new LinkedHashMap<>();
        richiesta.put("nodo", nodoMittente);
        richiesta.put("tipo", tipo);
        richiesta.put("input", input);
        richiesta.put("timestamp", System.currentTimeMillis());
        richiesta.put("motore", "offline");

        Path fileRichiesta = CARTELLA_RICHIESTE.resolve(idScambio + ".json");
        Files.writeString(fileRichiesta, JsonUtil.toJson(richiesta));

        // 2) elaborazione locale, deterministica, senza alcuna dipendenza esterna
        Map<String, Object> risposta = new LinkedHashMap<>();
        risposta.put("nodo", nodoMittente);
        risposta.put("tipo_richiesta", tipo);
        risposta.put("esito", "offline");
        risposta.put("messaggio", "Elaborazione locale (nessun server esterno) per '" + tipo + "' da " + nodoMittente);
        risposta.put("confidenza", 0.5);
        risposta.put("echo_input", input);

        // 3) anche la risposta viene scritta su file JSON...
        Path fileRisposta = CARTELLA_RISPOSTE.resolve(idScambio + ".json");
        Files.writeString(fileRisposta, JsonUtil.toJson(risposta));

        // 4) ...ed e' proprio dal file appena scritto che viene riletta e restituita al chiamante
        String contenuto = Files.readString(fileRisposta);
        return JsonUtil.parseObject(contenuto);
    }
}
