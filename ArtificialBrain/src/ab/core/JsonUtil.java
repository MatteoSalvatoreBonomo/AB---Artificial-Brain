package ab.core;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility JSON minimale, scritta senza dipendenze esterne (Jackson/Gson)
 * perche' l'ambiente di build potrebbe non avere accesso a internet per
 * scaricare librerie. Supporta i tipi base necessari al progetto:
 * String, Number, Boolean, null, Map (oggetto) e List (array).
 *
 * Non e' un parser JSON completo per ogni caso limite dello standard,
 * ma copre tutto cio' che serve per i messaggi scambiati sul Bus.
 */
public final class JsonUtil {

    private JsonUtil() {
    }

    // ---------------------------------------------------------------
    // ENCODING (oggetto Java -> stringa JSON)
    // ---------------------------------------------------------------

    public static String toJson(Object valore) {
        StringBuilder sb = new StringBuilder();
        scriviValore(valore, sb);
        return sb.toString();
    }

    @SuppressWarnings("unchecked")
    private static void scriviValore(Object valore, StringBuilder sb) {
        if (valore == null) {
            sb.append("null");
        } else if (valore instanceof String) {
            scriviStringa((String) valore, sb);
        } else if (valore instanceof Number || valore instanceof Boolean) {
            sb.append(valore.toString());
        } else if (valore instanceof Map) {
            scriviOggetto((Map<String, Object>) valore, sb);
        } else if (valore instanceof List) {
            scriviArray((List<Object>) valore, sb);
        } else {
            // fallback: trattalo come stringa
            scriviStringa(valore.toString(), sb);
        }
    }

    private static void scriviOggetto(Map<String, Object> mappa, StringBuilder sb) {
        sb.append('{');
        boolean primo = true;
        for (Map.Entry<String, Object> entry : mappa.entrySet()) {
            if (!primo) {
                sb.append(',');
            }
            primo = false;
            scriviStringa(entry.getKey(), sb);
            sb.append(':');
            scriviValore(entry.getValue(), sb);
        }
        sb.append('}');
    }

    private static void scriviArray(List<Object> lista, StringBuilder sb) {
        sb.append('[');
        boolean primo = true;
        for (Object elemento : lista) {
            if (!primo) {
                sb.append(',');
            }
            primo = false;
            scriviValore(elemento, sb);
        }
        sb.append(']');
    }

    private static void scriviStringa(String s, StringBuilder sb) {
        sb.append('"');
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '"':
                    sb.append("\\\"");
                    break;
                case '\\':
                    sb.append("\\\\");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                default:
                    if (c < 0x20) {
                        sb.append(String.format("\\u%04x", (int) c));
                    } else {
                        sb.append(c);
                    }
            }
        }
        sb.append('"');
    }

    // ---------------------------------------------------------------
    // PARSING (stringa JSON -> Map/List/String/Number/Boolean/null)
    // ---------------------------------------------------------------

    public static Object parse(String json) {
        Parser p = new Parser(json);
        Object valore = p.leggiValore();
        p.saltaSpazi();
        if (!p.finito()) {
            throw new IllegalArgumentException("JSON malformato: caratteri in eccesso dopo il valore principale");
        }
        return valore;
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> parseObject(String json) {
        Object v = parse(json);
        if (!(v instanceof Map)) {
            throw new IllegalArgumentException("Il JSON fornito non rappresenta un oggetto");
        }
        return (Map<String, Object>) v;
    }

    private static final class Parser {
        private final String s;
        private int pos;

        Parser(String s) {
            this.s = s;
            this.pos = 0;
        }

        boolean finito() {
            return pos >= s.length();
        }

        void saltaSpazi() {
            while (pos < s.length() && Character.isWhitespace(s.charAt(pos))) {
                pos++;
            }
        }

        char corrente() {
            return s.charAt(pos);
        }

        void attesa(char atteso) {
            saltaSpazi();
            if (finito() || corrente() != atteso) {
                throw new IllegalArgumentException(
                        "JSON malformato: atteso '" + atteso + "' alla posizione " + pos);
            }
            pos++;
        }

        Object leggiValore() {
            saltaSpazi();
            if (finito()) {
                throw new IllegalArgumentException("JSON malformato: fine inattesa dell'input");
            }
            char c = corrente();
            if (c == '{') {
                return leggiOggetto();
            } else if (c == '[') {
                return leggiArray();
            } else if (c == '"') {
                return leggiStringa();
            } else if (c == 't' || c == 'f') {
                return leggiBooleano();
            } else if (c == 'n') {
                leggiLetterale("null");
                return null;
            } else {
                return leggiNumero();
            }
        }

        Map<String, Object> leggiOggetto() {
            Map<String, Object> mappa = new LinkedHashMap<>();
            attesa('{');
            saltaSpazi();
            if (!finito() && corrente() == '}') {
                pos++;
                return mappa;
            }
            while (true) {
                saltaSpazi();
                String chiave = leggiStringa();
                attesa(':');
                Object valore = leggiValore();
                mappa.put(chiave, valore);
                saltaSpazi();
                if (!finito() && corrente() == ',') {
                    pos++;
                } else {
                    break;
                }
            }
            attesa('}');
            return mappa;
        }

        List<Object> leggiArray() {
            List<Object> lista = new ArrayList<>();
            attesa('[');
            saltaSpazi();
            if (!finito() && corrente() == ']') {
                pos++;
                return lista;
            }
            while (true) {
                Object valore = leggiValore();
                lista.add(valore);
                saltaSpazi();
                if (!finito() && corrente() == ',') {
                    pos++;
                } else {
                    break;
                }
            }
            attesa(']');
            return lista;
        }

        String leggiStringa() {
            attesa('"');
            StringBuilder sb = new StringBuilder();
            while (true) {
                if (finito()) {
                    throw new IllegalArgumentException("JSON malformato: stringa non terminata");
                }
                char c = s.charAt(pos++);
                if (c == '"') {
                    break;
                } else if (c == '\\') {
                    char esc = s.charAt(pos++);
                    switch (esc) {
                        case '"':
                            sb.append('"');
                            break;
                        case '\\':
                            sb.append('\\');
                            break;
                        case '/':
                            sb.append('/');
                            break;
                        case 'n':
                            sb.append('\n');
                            break;
                        case 'r':
                            sb.append('\r');
                            break;
                        case 't':
                            sb.append('\t');
                            break;
                        case 'b':
                            sb.append('\b');
                            break;
                        case 'f':
                            sb.append('\f');
                            break;
                        case 'u':
                            String hex = s.substring(pos, pos + 4);
                            sb.append((char) Integer.parseInt(hex, 16));
                            pos += 4;
                            break;
                        default:
                            throw new IllegalArgumentException("Sequenza di escape non valida: \\" + esc);
                    }
                } else {
                    sb.append(c);
                }
            }
            return sb.toString();
        }

        Boolean leggiBooleano() {
            if (s.startsWith("true", pos)) {
                pos += 4;
                return Boolean.TRUE;
            } else if (s.startsWith("false", pos)) {
                pos += 5;
                return Boolean.FALSE;
            }
            throw new IllegalArgumentException("JSON malformato: booleano non valido alla posizione " + pos);
        }

        void leggiLetterale(String letterale) {
            if (!s.startsWith(letterale, pos)) {
                throw new IllegalArgumentException("JSON malformato: atteso '" + letterale + "' alla posizione " + pos);
            }
            pos += letterale.length();
        }

        Number leggiNumero() {
            int inizio = pos;
            if (!finito() && (corrente() == '-' || corrente() == '+')) {
                pos++;
            }
            boolean isDouble = false;
            while (!finito() && (Character.isDigit(corrente()) || corrente() == '.'
                    || corrente() == 'e' || corrente() == 'E'
                    || corrente() == '+' || corrente() == '-')) {
                if (corrente() == '.' || corrente() == 'e' || corrente() == 'E') {
                    isDouble = true;
                }
                pos++;
            }
            String numeroStr = s.substring(inizio, pos);
            if (numeroStr.isEmpty()) {
                throw new IllegalArgumentException("JSON malformato: numero atteso alla posizione " + inizio);
            }
            return isDouble ? (Number) Double.parseDouble(numeroStr) : (Number) Long.parseLong(numeroStr);
        }
    }
}
