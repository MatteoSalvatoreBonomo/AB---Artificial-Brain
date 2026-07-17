package ab.core;

import ab.bus.BUS;
import ab.core.ml.ModelloML;
import ab.core.ml.ModelloMLLocale;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Implementazione di base condivisa da tutte le aree cerebrali del progetto.
 * Ogni sottoclasse rappresenta un nodo reale (es. Broca, Amigdala, V1...) e
 * eredita gratuitamente:
 *  - i metadati (nome, aree di Brodmann, descrizione funzionale)
 *  - il metodo invia(...) per pubblicare un messaggio JSON sul BUS
 *  - un comportamento di default per ricevi(...) (override libero nelle sottoclassi)
 *  - l'accesso a un modello ML locale tramite il metodo pensa(...)
 */
public abstract class NodoCerebrale implements RegioneCerebrale {

    /**
     * Modello ML condiviso di default da tutti i nodi che non ne impostano
     * uno proprio: {@link ModelloMLLocale}, che prova un'API locale reale
     * (Ollama su localhost) e ripiega automaticamente su un motore offline
     * integrato se quella non e' raggiungibile. Nessuna dipendenza da
     * servizi di terze parti: il cervello funziona sempre, anche offline.
     * Puo' comunque essere sostituito globalmente con
     * {@link #impostaModelloGlobale(ModelloML)}.
     */
    private static volatile ModelloML modelloGlobale = new ModelloMLLocale();

    private final String nome;
    private final String areeBrodmann;
    private final String descrizione;

    /** Modello ML specifico di questo nodo; se null si usa il modello globale. */
    private ModelloML modelloML;

    protected NodoCerebrale(String nome, String areeBrodmann, String descrizione) {
        this.nome = nome;
        this.areeBrodmann = areeBrodmann;
        this.descrizione = descrizione;
    }

    /** Imposta il modello ML di default per TUTTI i nodi cerebrali (a meno di override puntuali). */
    public static void impostaModelloGlobale(ModelloML modello) {
        modelloGlobale = modello;
    }

    /** Imposta un modello ML specifico per questo singolo nodo, indipendente da quello globale. */
    public void setModelloML(ModelloML modello) {
        this.modelloML = modello;
    }

    /** Modello ML effettivamente usato da questo nodo (quello proprio se presente, altrimenti quello globale). */
    public ModelloML getModelloML() {
        return modelloML != null ? modelloML : modelloGlobale;
    }

    @Override
    public String getNome() {
        return nome;
    }

    @Override
    public String getAreeBrodmann() {
        return areeBrodmann;
    }

    @Override
    public String getDescrizione() {
        return descrizione;
    }

    /**
     * Costruisce un Messaggio con questo nodo come mittente e lo pubblica
     * sul BUS (che si occupa di scriverne il JSON su disco e di inoltrarlo
     * al Talamo per il routing).
     *
     * @param tipo    tipo di segnale (es. "VISIVO", "EMOTIVO", "MOTORIO"...)
     * @param payload dati specifici del messaggio
     * @return il messaggio pubblicato
     */
    public Messaggio invia(String tipo, Map<String, Object> payload) {
        Messaggio msg = new Messaggio(nome, tipo, payload);
        BUS.getInstance().invia(msg);
        return msg;
    }

    /**
     * Interpella il modello ML (locale/remoto) associato a questo nodo per
     * elaborare un input, secondo il contratto {@link ModelloML}: la
     * comunicazione avviene sempre tramite file JSON su disco.
     * In caso di errore (es. server Ollama non raggiungibile) l'eccezione
     * viene loggata e restituita una mappa vuota, cosi' da non bloccare mai
     * il resto del cervello artificiale.
     *
     * @param tipo  tipo/contesto della richiesta (es. "EMOTIVO", "LINGUAGGIO"...)
     * @param input dati di ingresso per il modello
     * @return l'output prodotto dal modello (mappa vuota in caso di errore)
     */
    public Map<String, Object> pensa(String tipo, Map<String, Object> input) {
        try {
            return getModelloML().elabora(nome, tipo, input);
        } catch (IOException e) {
            System.err.println("[" + nome + "] Errore durante l'inferenza ML (" + tipo + "): " + e.getMessage());
            return new LinkedHashMap<>();
        }
    }

    /**
     * Come {@link #pensa(String, Map)}, ma pubblica automaticamente il
     * risultato dell'inferenza come nuovo Messaggio sul BUS, con tipo
     * "&lt;tipo&gt;_ML", cosi' che il Talamo possa instradarlo agli altri nodi.
     *
     * @return il messaggio pubblicato, oppure null se il modello non ha prodotto output
     */
    public Messaggio pensaEInvia(String tipo, Map<String, Object> input) {
        Map<String, Object> risultato = pensa(tipo, input);
        if (risultato.isEmpty()) {
            return null;
        }
        return invia(tipo + "_ML", risultato);
    }

    /** Comportamento di default alla ricezione: log su console. Le sottoclassi possono fare override. */
    @Override
    public void ricevi(Messaggio msg) {
        System.out.println("[" + nome + "] ha ricevuto: " + msg);
    }

    /** Utility per costruire rapidamente un payload chiave/valore. */
    protected static Map<String, Object> payload(Object... coppieChiaveValore) {
        Map<String, Object> mappa = new LinkedHashMap<>();
        for (int i = 0; i + 1 < coppieChiaveValore.length; i += 2) {
            mappa.put(String.valueOf(coppieChiaveValore[i]), coppieChiaveValore[i + 1]);
        }
        return mappa;
    }

    @Override
    public String toString() {
        return nome + " (BA " + areeBrodmann + ")";
    }
}
