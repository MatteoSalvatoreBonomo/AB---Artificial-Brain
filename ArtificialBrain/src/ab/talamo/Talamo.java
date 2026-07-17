package ab.talamo;

import ab.core.Messaggio;
import ab.core.RegioneCerebrale;
import ab.talamo.circuiti.CircuitoTalamico;
import ab.talamo.circuiti.TalamoAmigdala;
import ab.talamo.circuiti.TalamoCortecciaAmigdala;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Talamo: il "router" del cervello. Riceve dal BUS i messaggi e decide a
 * quali nodi vanno consegnati, in base al TIPO di messaggio.
 *
 * La tabella di routing di default rispecchia (in forma semplificata) le
 * proiezioni dei principali nuclei talamici verso la corteccia, cosi' come
 * mappate nel diagramma TALAMO.pdf:
 *
 *   - Corpo genicolato laterale  -> corteccia visiva primaria (V1 / BA17)
 *   - Corpo genicolato mediale   -> corteccia uditiva primaria (BA41)
 *   - Nuclei ventrali postero-L/M -> corteccia somatosensoriale primaria (BA1-2-3)
 *   - Nuclei ventrale anteriore/laterale -> corteccia motoria + gangli della base
 *   - Nucleo dorsomediale        -> corteccia prefrontale (elaborazione cognitiva)
 *   - Nucleo anteriore (circuito di Papez) -> ippocampo / cingolo (memoria)
 *   - Via talamica per la paura (LeDoux)   -> amigdala (via breve) e insula/corteccia (via lunga)
 *
 * La tabella e' liberamente estendibile con aggiungiRegola(...).
 */
public class Talamo {

    /** tipo di messaggio -> elenco di nomi di nodi destinatari */
    private final Map<String, List<String>> tabellaRouting = new LinkedHashMap<>();

    /** tipi di messaggio che vengono sempre inviati in broadcast a tutti i nodi registrati
     *  (es. segnali modulatori/attenzionali, analoghi al nucleo reticolare del talamo,
     *  che non proietta alla corteccia ma regola gli altri nuclei). */
    private final List<String> tipiBroadcast = new ArrayList<>();

    private final Map<String, RegioneCerebrale> regioniRegistrate = new ConcurrentHashMap<>();

    /** Circuiti speciali per tipo di messaggio (es. "PAURA" -> doppia via di LeDoux). */
    private final Map<String, List<CircuitoTalamico>> circuitiSpeciali = new LinkedHashMap<>();

    public Talamo() {
        caricaRoutingDiDefault();
        caricaCircuitiSpeciali();
    }

    private void caricaCircuitiSpeciali() {
        // segnale di potenziale minaccia: viaggia sia sulla via breve (talamo->amigdala)
        // sia sulla via lunga (talamo->corteccia->amigdala), come nel modello di LeDoux
        circuitiSpeciali.put("PAURA", java.util.Arrays.asList(
                new TalamoAmigdala(),
                new TalamoCortecciaAmigdala("Insula")
        ));
    }

    /** Espone il registro dei nodi (usato dai CircuitoTalamico per recuperare i destinatari). */
    public Map<String, RegioneCerebrale> getRegistro() {
        return regioniRegistrate;
    }

    private void caricaRoutingDiDefault() {
        aggiungiRegola("VISIVO_GREZZO", "CortecciaVisivaPrimaria");
        aggiungiRegola("UDITIVO_GREZZO", "CortecciaUditivaPrimaria");
        aggiungiRegola("SOMATOSENSORIALE", "CortecciaSomatosensorialePrimaria");
        aggiungiRegola("MOTORIO", "CortecciaMotoriaPrimaria", "GangliDellaBase");
        aggiungiRegola("COGNITIVO", "CortecciaFrontaleDorsolaterale");
        aggiungiRegola("MEMORIA", "Ippocampo", "AreaTemporopolare");
        aggiungiRegola("LINGUAGGIO_COMPRENSIONE", "Wernicke");
        aggiungiRegola("LINGUAGGIO_PRODUZIONE", "Broca");
        aggiungiRegola("EMOTIVO", "Amigdala", "Insula");

        // segnale modulatorio/attenzionale: va in broadcast a tutti i nodi registrati,
        // come il nucleo reticolare che regola l'intero talamo invece di proiettare
        // ad una singola area corticale
        tipiBroadcast.add("ATTENZIONE");
    }

    /** Registra un nodo, rendendolo raggiungibile per nome dal router. */
    public void registraRegione(RegioneCerebrale regione) {
        regioniRegistrate.put(regione.getNome(), regione);
    }

    /** Aggiunge (o estende) una regola di routing: tipoMessaggio -> uno o piu' nodi destinatari. */
    public void aggiungiRegola(String tipoMessaggio, String... nomiDestinatari) {
        tabellaRouting
                .computeIfAbsent(tipoMessaggio, k -> new ArrayList<>())
                .addAll(java.util.Arrays.asList(nomiDestinatari));
    }

    /** Instrada un messaggio verso i nodi corretti in base al suo tipo. */
    public void instrada(Messaggio msg) {
        List<CircuitoTalamico> circuiti = circuitiSpeciali.get(msg.getTipo());
        if (circuiti != null) {
            for (CircuitoTalamico circuito : circuiti) {
                circuito.propaga(msg, regioniRegistrate);
            }
            return;
        }

        if (tipiBroadcast.contains(msg.getTipo())) {
            for (RegioneCerebrale regione : regioniRegistrate.values()) {
                regione.ricevi(msg);
            }
            return;
        }

        List<String> destinatari = tabellaRouting.get(msg.getTipo());
        if (destinatari == null || destinatari.isEmpty()) {
            System.out.println("[Talamo] Nessuna regola di routing per il tipo '" + msg.getTipo()
                    + "' (messaggio da " + msg.getMittente() + " scartato/non instradato)");
            return;
        }

        for (String nomeDestinatario : destinatari) {
            RegioneCerebrale destinatario = regioniRegistrate.get(nomeDestinatario);
            if (destinatario != null) {
                destinatario.ricevi(msg);
            } else {
                System.out.println("[Talamo] Destinatario '" + nomeDestinatario
                        + "' non registrato: messaggio " + msg.getId() + " non consegnato a quel nodo");
            }
        }
    }
}
