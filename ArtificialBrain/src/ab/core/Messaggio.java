package ab.core;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Formato comune con cui ogni nodo cerebrale comunica sul BUS.
 * Viene serializzato/deserializzato in JSON da {@link JsonUtil}.
 *
 * Esempio di JSON prodotto:
 * {
 *   "id": "a1b2c3...",
 *   "mittente": "Amigdala",
 *   "tipo": "EMOTIVO",
 *   "timestamp": 1737100000000,
 *   "payload": { "intensita": 0.8, "stimolo": "rumore improvviso" }
 * }
 */
public class Messaggio {

    private final String id;
    private final String mittente;
    private final String tipo;
    private final long timestamp;
    private final Map<String, Object> payload;

    public Messaggio(String mittente, String tipo, Map<String, Object> payload) {
        this(UUID.randomUUID().toString(), mittente, tipo, System.currentTimeMillis(), payload);
    }

    public Messaggio(String id, String mittente, String tipo, long timestamp, Map<String, Object> payload) {
        this.id = id;
        this.mittente = mittente;
        this.tipo = tipo;
        this.timestamp = timestamp;
        this.payload = payload != null ? payload : new LinkedHashMap<>();
    }

    public String getId() {
        return id;
    }

    public String getMittente() {
        return mittente;
    }

    public String getTipo() {
        return tipo;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public Map<String, Object> getPayload() {
        return payload;
    }

    /** Serializza il messaggio in una stringa JSON. */
    public String toJson() {
        Map<String, Object> radice = new LinkedHashMap<>();
        radice.put("id", id);
        radice.put("mittente", mittente);
        radice.put("tipo", tipo);
        radice.put("timestamp", timestamp);
        radice.put("payload", payload);
        return JsonUtil.toJson(radice);
    }

    /** Ricostruisce un Messaggio a partire dal suo JSON. */
    @SuppressWarnings("unchecked")
    public static Messaggio fromJson(String json) {
        Map<String, Object> radice = JsonUtil.parseObject(json);
        String id = (String) radice.get("id");
        String mittente = (String) radice.get("mittente");
        String tipo = (String) radice.get("tipo");
        long timestamp = ((Number) radice.get("timestamp")).longValue();
        Object payloadObj = radice.get("payload");
        Map<String, Object> payload = payloadObj instanceof Map
                ? (Map<String, Object>) payloadObj
                : new LinkedHashMap<>();
        return new Messaggio(id, mittente, tipo, timestamp, payload);
    }

    @Override
    public String toString() {
        return "Messaggio{" + "mittente='" + mittente + "', tipo='" + tipo + "', payload=" + payload + '}';
    }
}
