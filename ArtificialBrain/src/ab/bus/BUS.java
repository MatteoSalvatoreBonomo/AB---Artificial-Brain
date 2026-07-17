package ab.bus;

import ab.core.Messaggio;
import ab.core.RegioneCerebrale;
import ab.talamo.Talamo;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * BUS: canale di comunicazione condiviso da tutte le aree cerebrali.
 *
 * Responsabilita':
 *  1) ricevere i Messaggio pubblicati dai nodi (invia(...))
 *  2) serializzarli in JSON e scriverli su disco (una entry per nodo/messaggio)
 *  3) inoltrarli al Talamo, che si occupa del routing verso i destinatari
 *
 * E' implementato come singleton (un solo bus condiviso da tutto il "cervello")
 * e processa i messaggi in modo asincrono tramite una coda + un thread dedicato,
 * cosi' un nodo che pubblica un messaggio non resta bloccato in attesa che
 * arrivi a destinazione.
 */
public final class BUS {

    /** Cartella dove viene scritto il JSON di ogni messaggio transitato sul bus. */
    private static final Path CARTELLA_MESSAGGI = Paths.get("messaggi");

    private static final BUS INSTANCE = new BUS();

    private final Talamo talamo = new Talamo();
    private final Map<String, RegioneCerebrale> regioniRegistrate = new ConcurrentHashMap<>();
    private final BlockingQueue<Messaggio> coda = new LinkedBlockingQueue<>();
    private Thread threadConsumo;
    private volatile boolean attivo = false;

    private BUS() {
        try {
            Files.createDirectories(CARTELLA_MESSAGGI);
        } catch (IOException e) {
            throw new IllegalStateException("Impossibile creare la cartella dei messaggi: " + CARTELLA_MESSAGGI, e);
        }
    }

    public static BUS getInstance() {
        return INSTANCE;
    }

    public Talamo getTalamo() {
        return talamo;
    }

    /** Registra un nodo cerebrale, rendendolo raggiungibile dal Talamo per il routing. */
    public void registra(RegioneCerebrale regione) {
        regioniRegistrate.put(regione.getNome(), regione);
        talamo.registraRegione(regione);
    }

    public RegioneCerebrale getRegione(String nome) {
        return regioniRegistrate.get(nome);
    }

    /**
     * Punto di ingresso dei messaggi: viene chiamato da NodoCerebrale.invia(...).
     * Scrive subito il JSON su disco, poi mette il messaggio in coda per il routing.
     */
    public void invia(Messaggio msg) {
        scriviJsonSuDisco(msg);
        coda.add(msg);
    }

    private void scriviJsonSuDisco(Messaggio msg) {
        String nomeFile = msg.getTimestamp() + "_" + msg.getMittente() + "_" + msg.getId() + ".json";
        Path percorso = CARTELLA_MESSAGGI.resolve(nomeFile);
        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(percorso))) {
            writer.print(msg.toJson());
        } catch (IOException e) {
            System.err.println("[BUS] Impossibile scrivere il file JSON per il messaggio " + msg.getId() + ": " + e.getMessage());
        }
    }

    /** Legge e ricostruisce un Messaggio da un file JSON precedentemente scritto dal bus. */
    public Messaggio leggiMessaggioDaFile(Path percorsoFile) throws IOException {
        String contenuto = Files.readString(percorsoFile);
        return Messaggio.fromJson(contenuto);
    }

    /** Avvia il thread che consuma la coda e passa i messaggi al Talamo per il routing. */
    public synchronized void avvia() {
        if (attivo) {
            return;
        }
        attivo = true;
        threadConsumo = new Thread(() -> {
            while (attivo) {
                try {
                    Messaggio msg = coda.take();
                    talamo.instrada(msg);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }, "bus-thread");
        threadConsumo.setDaemon(true);
        threadConsumo.start();
    }

    /** Ferma il consumo asincrono della coda (utile per test/demo). */
    public synchronized void ferma() {
        attivo = false;
        if (threadConsumo != null) {
            threadConsumo.interrupt();
        }
    }
}
