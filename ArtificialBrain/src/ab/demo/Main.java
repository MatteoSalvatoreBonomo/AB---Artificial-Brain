package ab.demo;

import ab.bus.BUS;
import ab.core.Messaggio;
import ab.core.NodoCerebrale;
import ab.core.ml.ModelloML;
import ab.core.ml.ModelloMLLocale;
import ab.core.ml.ModelloMLOffline;

import ab.lobofrontale.*;
import ab.loboparietale.*;
import ab.lobooccipitale.*;
import ab.lobotemporale.*;
import ab.lobolimbico.*;
import ab.cervelletto.Cervelletto;
import ab.ganglidellabase.GangliDellaBase;
import ab.dmn.DMN;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Punto di ingresso della demo: crea e registra tutte le aree cerebrali,
 * avvia il BUS e simula alcuni segnali per mostrare il routing effettuato
 * dal Talamo, oltre alla creazione/lettura dei file JSON su disco.
 */
public final class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        // --- 0) scelta del modello ML globale da riga di comando ---
        //   java ab.demo.Main                        -> API locale (Ollama su http://localhost:11434, modello "llama3"),
        //                                                con fallback AUTOMATICO al motore offline integrato se il
        //                                                server non e' raggiungibile: funziona sempre, anche offline.
        //   java ab.demo.Main URL MODELLO             -> API locale su URL/modello custom, stesso fallback automatico
        //   java ab.demo.Main --solo-offline          -> nessun tentativo di rete, solo motore offline integrato
        ModelloML modelloGlobale = configuraModelloGlobale(args);
        NodoCerebrale.impostaModelloGlobale(modelloGlobale);
        System.out.println("Modello ML globale attivo: " + modelloGlobale.getClass().getSimpleName());

        BUS bus = BUS.getInstance();

        // --- 1) creazione e registrazione di tutti i nodi cerebrali ---
        List<NodoCerebrale> nodi = List.of(
                // lobo frontale
                new CortecciaMotoriaPrimaria(),
                new CortecciaPremotoria(),
                new CampoOculareFrontale(),
                new CortecciaFrontaleDorsolaterale(),
                new CortecciaFrontaleVentromediale(),
                new CortecciaFrontaleOrbitale(),
                new CortecciaCingolataAnteriore(),
                new Broca(),
                // lobo parietale
                new CortecciaSomatosensorialePrimaria(),
                new CortecciaSomatosensorialeSecondaria(),
                new LobuloParietaleSuperiore(),
                new GiroAngolare(),
                new GiroSovramarginale(),
                new Precuneo(),
                new CortecciaCingolataPosteriore(),
                // lobo occipitale
                new CortecciaVisivaPrimaria(),
                new CortecciaVisivaSecondaria(),
                new CortecciaVisivaV3(),
                new CortecciaVisivaV4(),
                new CortecciaVisivaV5(),
                // lobo temporale
                new CortecciaUditivaPrimaria(),
                new CortecciaUditivaSecondaria(),
                new Wernicke(),
                new GiroTemporaleInferiore(),
                new GiroTemporaleMedio(),
                new AreaTemporopolare(),
                new GiroFusiforme(),
                new CortecciaEntorinale(),
                new CortecciaPerirenale(),
                new CortecciaPiriforme(),
                new Ippocampo(),
                new Amigdala(),
                // lobo limbico
                new Insula(),
                // altre strutture
                new Cervelletto(),
                new GangliDellaBase(),
                new DMN()
        );

        nodi.forEach(bus::registra);
        System.out.println("Registrati " + nodi.size() + " nodi cerebrali sul BUS.\n");

        // --- 2) avvio del bus (thread asincrono che consuma la coda e instrada via Talamo) ---
        bus.avvia();

        // --- 3) alcuni nodi pubblicano messaggi, il BUS li scrive su JSON e li instrada ---
        NodoCerebrale occhio = nodi.stream()
                .filter(n -> n.getNome().equals("CortecciaVisivaPrimaria"))
                .findFirst().orElseThrow();
        invia(occhio, "VISIVO_GREZZO", "stimolo", "ombra in movimento nella stanza", "intensita", 0.6);

        NodoCerebrale orecchio = nodi.stream()
                .filter(n -> n.getNome().equals("Wernicke"))
                .findFirst().orElseThrow();
        invia(orecchio, "LINGUAGGIO_COMPRENSIONE", "frase", "dove hai lasciato le chiavi?", "lingua", "it");

        NodoCerebrale amigdala = nodi.stream()
                .filter(n -> n.getNome().equals("Amigdala"))
                .findFirst().orElseThrow();
        // segnale di potenziale minaccia: attiva sia la via breve che la via lunga (vedi Talamo -> circuiti PAURA)
        invia(amigdala, "PAURA", "stimolo", "rumore improvviso alle spalle", "intensita", 0.9);

        NodoCerebrale corteccia = nodi.stream()
                .filter(n -> n.getNome().equals("CortecciaFrontaleDorsolaterale"))
                .findFirst().orElseThrow();
        invia(corteccia, "COGNITIVO", "compito", "pianificazione settimanale", "difficolta", "media");

        // segnale modulatorio/attenzionale: in broadcast a tutti i nodi (vedi Talamo -> tipiBroadcast)
        invia(corteccia, "ATTENZIONE", "focus", "priorita alta", "durata_ms", 500);

        // diamo tempo al thread del bus di consumare la coda prima di proseguire
        Thread.sleep(300);

        // --- 4) rilettura dei file JSON scritti su disco dal BUS, a riprova del round-trip ---
        stampaMessaggiSuDisco();

        // --- 5) ciclo continuo: il "cervello" resta vivo e continua a pensare/comunicare
        //         finche' non viene fermato (Ctrl+C) ---
        eseguiCicloContinuo(bus, nodi);

        bus.ferma();
    }

    /**
     * Analizza gli argomenti da riga di comando e costruisce il modello ML
     * globale da usare per tutti i nodi cerebrali.
     */
    private static ModelloML configuraModelloGlobale(String[] args) {
        if (args.length > 0 && "--solo-offline".equalsIgnoreCase(args[0])) {
            // nessun tentativo di rete: utile per test rapidi o macchine sicuramente offline
            return new ModelloMLOffline();
        }
        if (args.length >= 2) {
            // URL e modello dell'API locale personalizzati, con fallback offline comunque garantito
            return new ModelloMLLocale(args[0], args[1]);
        }
        // default: prova l'API locale, ripiega da sola sull'offline se non disponibile
        return new ModelloMLLocale();
    }

    /**
     * Ciclo di vita continuo del cervello artificiale: a intervalli regolari
     * un nodo viene "svegliato", interpella il proprio modello ML tramite
     * pensa(...)/pensaEInvia(...) e pubblica il risultato sul BUS, che lo
     * instrada agli altri nodi tramite il Talamo. Il ciclo prosegue
     * indefinitamente finche' il processo non viene interrotto (Ctrl+C),
     * intercettato con uno shutdown hook che ferma il BUS in modo pulito.
     */
    private static void eseguiCicloContinuo(BUS bus, List<NodoCerebrale> nodi) throws InterruptedException {
        AtomicBoolean inEsecuzione = new AtomicBoolean(true);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> inEsecuzione.set(false), "shutdown-hook"));

        Random random = new Random();
        long intervalloMs = 2000L;
        int ciclo = 0;

        System.out.println("\n--- Avvio del ciclo continuo del cervello (Ctrl+C per fermare) ---");

        while (inEsecuzione.get()) {
            ciclo++;
            NodoCerebrale nodo = nodi.get(random.nextInt(nodi.size()));

            Map<String, Object> stimolo = Map.of(
                    "ciclo", ciclo,
                    "osservazione", "attivita' spontanea di fondo",
                    "istante", System.currentTimeMillis()
            );

            System.out.println("[ciclo " + ciclo + "] " + nodo.getNome() + " interpella il modello ML...");
            Messaggio msgProdotto = nodo.pensaEInvia("PENSIERO_SPONTANEO", stimolo);
            if (msgProdotto != null) {
                System.out.println("[ciclo " + ciclo + "] " + nodo.getNome()
                        + " ha pubblicato: " + msgProdotto.getTipo() + " -> " + msgProdotto.getPayload());
            }

            Thread.sleep(intervalloMs);
        }

        System.out.println("--- Ciclo continuo interrotto, arresto in corso ---");
    }

    /** Helper: costruisce un payload al volo e lo invia tramite il nodo mittente. */
    private static void invia(NodoCerebrale mittente, String tipo, Object... coppieChiaveValore) {
        java.util.Map<String, Object> payload = new java.util.LinkedHashMap<>();
        for (int i = 0; i + 1 < coppieChiaveValore.length; i += 2) {
            payload.put(String.valueOf(coppieChiaveValore[i]), coppieChiaveValore[i + 1]);
        }
        mittente.invia(tipo, payload);
    }

    private static void stampaMessaggiSuDisco() throws IOException {
        Path cartella = Path.of("messaggi");
        if (!Files.exists(cartella)) {
            return;
        }
        System.out.println("\n--- File JSON scritti su disco dal BUS (cartella 'messaggi/') ---");
        try (Stream<Path> file = Files.list(cartella)) {
            List<Path> ordinati = file.sorted().collect(Collectors.toList());
            for (Path p : ordinati) {
                Messaggio msg = BUS.getInstance().leggiMessaggioDaFile(p);
                System.out.println(p.getFileName() + "  ->  " + msg);
            }
        }
    }
}
