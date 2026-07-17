# ArtificialBrain — BUS / Talamo

Progetto Java che modella un "cervello artificiale" a nodi: ogni area
cerebrale e' una classe che pubblica messaggi JSON su un **BUS**, il quale
li inoltra al **Talamo** per il routing verso i nodi destinatari corretti.

Ogni nodo eredita da `NodoCerebrale`:
- `invia(tipo, payload)` costruisce un `Messaggio`, lo passa al `BUS`
- il `BUS` lo serializza in JSON e lo scrive nella cartella `messaggi/`
  (un file per messaggio: `<timestamp>_<mittente>_<id>.json`)
- il `BUS` lo mette in coda e un thread dedicato lo passa al `Talamo`
- il `Talamo` guarda il `tipo` del messaggio nella sua tabella di routing
  e chiama `ricevi(msg)` sui nodi destinatari corretti

## Come compilare ed eseguire

Essere sicuri di avere un JDK 17+ installato, dalla cartella del progetto.

Se preferisci un IDE (IntelliJ, Eclipse, VS Code), basta aprire la cartella `ArtificialBrain`, definire la cartella
`src` come sorgente, andare su `http://localhost:11434` per far partire il server ollama, e lanciare `ab.demo.Main`.

## Estendere il progetto

- **Nuovo nodo**: crea una classe che estende `NodoCerebrale`, chiama
  `super(nome, areeBrodmann, descrizione)` nel costruttore, ed
  eventualmente fai override di `ricevi(Messaggio msg)`.
- **Nuova regola di routing**: nel costruttore di `Talamo`
  (`caricaRoutingDiDefault()`), aggiungi una riga
  `aggiungiRegola("TIPO_MESSAGGIO", "NomeNodo1", "NomeNodo2", ...)`.
- **Nuovo circuito speciale** (routing non 1:1, es. percorsi multipli o
  broadcast): implementa `CircuitoTalamico` e registralo in
  `caricaCircuitiSpeciali()`.

## Nota sul parsing JSON

Non e' stata usata nessuna libreria esterna (Jackson/Gson). `ab.core.JsonUtil` e' un encoder/decoder JSON scritto da
zero, sufficiente per i tipi usati nei messaggi (stringhe, numeri, booleani,
null, oggetti, array).
