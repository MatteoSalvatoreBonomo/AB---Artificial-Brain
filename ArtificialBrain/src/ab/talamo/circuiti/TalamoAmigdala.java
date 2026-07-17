package ab.talamo.circuiti;

import ab.core.Messaggio;
import ab.core.RegioneCerebrale;

import java.util.Map;

/**
 * Via BREVE della paura (LeDoux): il talamo manda il segnale grezzo
 * DIRETTAMENTE all'amigdala, saltando la corteccia. E' una via veloce ma
 * "imprecisa": permette una reazione di allarme immediata (es. scattare
 * indietro da un oggetto che sembra un serpente) prima ancora che la
 * corteccia abbia finito di elaborare lo stimolo in dettaglio.
 */
public class TalamoAmigdala implements CircuitoTalamico {

    @Override
    public String getNome() {
        return "Via breve talamo-amigdala (rapida e grezza)";
    }

    @Override
    public void propaga(Messaggio msg, Map<String, RegioneCerebrale> registro) {
        RegioneCerebrale amigdala = registro.get("Amigdala");
        if (amigdala != null) {
            amigdala.ricevi(msg);
        } else {
            System.out.println("[" + getNome() + "] Amigdala non registrata: impossibile consegnare il messaggio "
                    + msg.getId());
        }
    }
}
