package ab.core.ml;

import java.io.IOException;
import java.util.Map;

/**
 * Interfaccia comune per qualunque "cervello artificiale" esterno che i nodi
 * cerebrali possano interpellare per un'inferenza (es. generare una risposta,
 * classificare uno stimolo, valutare un'emozione...).
 *
 * Il contratto e' volutamente minimale: si passa un input strutturato
 * (chiave/valore) e si ottiene un output strutturato dello stesso tipo.
 *
 * Le implementazioni concrete di questo progetto comunicano SEMPRE
 * attraverso file JSON su disco (vedi {@link ModelloMLMock} e
 * {@link ModelloMLOllama}), in coerenza con il resto dell'architettura
 * (Messaggio/BUS), cosi' da avere in ogni momento una traccia ispezionabile
 * di ogni richiesta/risposta scambiata con il "modello".
 */
public interface ModelloML {

    /**
     * Esegue un'inferenza sul modello.
     *
     * @param nodoMittente nome del nodo cerebrale che effettua la richiesta
     * @param tipo         tipo/contesto della richiesta (es. "EMOTIVO", "LINGUAGGIO"...)
     * @param input        payload di ingresso
     * @return payload di uscita prodotto dal modello
     * @throws IOException in caso di errori di I/O su file o di comunicazione con il server
     */
    Map<String, Object> elabora(String nodoMittente, String tipo, Map<String, Object> input) throws IOException;
}
