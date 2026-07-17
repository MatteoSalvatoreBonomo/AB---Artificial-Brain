package ab.core.ml;

import ab.core.JsonUtil;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Implementazione reale di {@link ModelloML} che parla con un server di
 * inferenza locale compatibile con l'API di Ollama (es. "ollama serve" in
 * ascolto su http://localhost:11434).
 *
 * Protocollo di comunicazione (file JSON, come richiesto):
 *  1) la richiesta per il modello viene prima scritta su un file JSON in
 *     "ml/richieste/" (questo file È l'unica cosa che viene poi spedita
 *     via HTTP: il corpo della richiesta e' letto dal file appena scritto);
 *  2) il corpo HTTP di risposta del server (JSON) viene scritto cosi' com'e'
 *     su un file JSON in "ml/risposte/";
 *  3) tale file viene riletto da disco e trasformato in una Map Java che
 *     torna al chiamante.
 *
 * In questo modo il nodo cerebrale e il "modello" non si scambiano mai
 * oggetti Java direttamente: comunicano sempre attraverso file JSON,
 * esattamente come il resto dell'architettura (BUS/Messaggio).
 */
public class ModelloMLOllama implements ModelloML {

    private static final Path CARTELLA_RICHIESTE = Paths.get("ml", "richieste");
    private static final Path CARTELLA_RISPOSTE = Paths.get("ml", "risposte");

    private final String baseUrl;
    private final String modello;
    private final HttpClient client;
    private final Duration timeoutRisposta;

    public ModelloMLOllama() {
        this("http://localhost:11434", "llama3");
    }

    public ModelloMLOllama(String baseUrl, String modello) {
        this(baseUrl, modello, Duration.ofSeconds(3), Duration.ofSeconds(60));
    }

    /**
     * @param baseUrl          es. "http://localhost:11434"
     * @param modello          nome del modello Ollama (es. "llama3")
     * @param timeoutConnessione tempo massimo per stabilire la connessione TCP: tenuto
     *                         breve apposta, cosi' se il server locale non e' avviato lo
     *                         si scopre subito, senza bloccare a lungo il nodo chiamante
     * @param timeoutRisposta  tempo massimo di attesa della risposta completa una volta
     *                         connessi: tenuto piu' ampio perche' una vera inferenza puo'
     *                         richiedere diversi secondi
     */
    public ModelloMLOllama(String baseUrl, String modello, Duration timeoutConnessione, Duration timeoutRisposta) {
        this.baseUrl = baseUrl;
        this.modello = modello;
        this.timeoutRisposta = timeoutRisposta;
        this.client = HttpClient.newBuilder()
                .connectTimeout(timeoutConnessione)
                .build();
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

        // 1) costruisci la richiesta in formato Ollama e SCRIVILA su file JSON
        Map<String, Object> richiestaOllama = new LinkedHashMap<>();
        richiestaOllama.put("model", modello);
        richiestaOllama.put("prompt", costruisciPrompt(nodoMittente, tipo, input));
        richiestaOllama.put("stream", false);

        Path fileRichiesta = CARTELLA_RICHIESTE.resolve(idScambio + ".json");
        Files.writeString(fileRichiesta, JsonUtil.toJson(richiestaOllama));

        // 2) il corpo HTTP e' letto ESATTAMENTE dal file appena scritto su disco
        String corpoRichiesta = Files.readString(fileRichiesta);

        HttpRequest richiestaHttp = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/api/generate"))
                .timeout(timeoutRisposta)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(corpoRichiesta))
                .build();

        String corpoRisposta;
        try {
            HttpResponse<String> rispostaHttp = client.send(richiestaHttp, HttpResponse.BodyHandlers.ofString());
            if (rispostaHttp.statusCode() / 100 != 2) {
                throw new IOException("Il server Ollama (" + baseUrl + ") ha risposto con codice HTTP "
                        + rispostaHttp.statusCode() + ": " + rispostaHttp.body());
            }
            corpoRisposta = rispostaHttp.body();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Chiamata al server Ollama interrotta", e);
        }

        // 3) la risposta grezza del server viene scritta su file JSON...
        Path fileRisposta = CARTELLA_RISPOSTE.resolve(idScambio + ".json");
        Files.writeString(fileRisposta, corpoRisposta);

        // 4) ...e riletta da disco per essere trasformata in Map: il round-trip
        //    tramite file e' l'unico canale di comunicazione fra IA e classe Java.
        String contenutoRisposta = Files.readString(fileRisposta);
        Map<String, Object> rispostaOllama = JsonUtil.parseObject(contenutoRisposta);

        Object testoGenerato = rispostaOllama.get("response");

        Map<String, Object> risultato = new LinkedHashMap<>();
        risultato.put("nodo", nodoMittente);
        risultato.put("tipo_richiesta", tipo);
        risultato.put("esito", "ollama");
        risultato.put("modello", modello);
        risultato.put("messaggio", testoGenerato != null ? testoGenerato.toString() : "");
        risultato.put("risposta_completa", rispostaOllama);
        return risultato;
    }

    /** Traduce input strutturato del nodo cerebrale in un prompt testuale per il LLM. */
    private String costruisciPrompt(String nodoMittente, String tipo, Map<String, Object> input) {
        return "Sei il nodo cerebrale artificiale '" + nodoMittente + "'. "
                + "Hai ricevuto uno stimolo di tipo '" + tipo + "' con i seguenti dati: "
                + JsonUtil.toJson(input) + ". "
                + "Rispondi in modo sintetico e coerente con il tuo ruolo funzionale.";
    }
}
