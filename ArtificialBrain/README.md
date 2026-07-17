# ArtificialBrain — BUS / Talamo

Progetto Java che modella un "cervello artificiale" a nodi: ogni area
cerebrale e' una classe che pubblica messaggi JSON su un **BUS**, il quale
li inoltra al **Talamo** per il routing verso i nodi destinatari corretti.

## Struttura

```
src/ab/
  core/          Messaggio, JsonUtil (parsing/serializzazione JSON), NodoCerebrale (classe base), RegioneCerebrale (interfaccia)
  bus/           BUS.java — riceve i messaggi, li scrive su JSON, li mette in coda per il Talamo
  talamo/        Talamo.java — tabella di routing (tipo messaggio -> nodi destinatari)
  talamo/circuiti/  Circuiti speciali (via breve/lunga della paura, LeDoux)
  lobofrontale/  lobooccipitale/  loboparietale/  lobotemporale/  lobolimbico/
  cervelletto/   ganglidellabase/  dmn/
                 Una classe per ciascuna area, con le rispettive aree di Brodmann
  demo/          Main.java — esempio d'uso end-to-end
```

Ogni nodo eredita da `NodoCerebrale`:
- `invia(tipo, payload)` costruisce un `Messaggio`, lo passa al `BUS`
- il `BUS` lo serializza in JSON e lo scrive nella cartella `messaggi/`
  (un file per messaggio: `<timestamp>_<mittente>_<id>.json`)
- il `BUS` lo mette in coda e un thread dedicato lo passa al `Talamo`
- il `Talamo` guarda il `tipo` del messaggio nella sua tabella di routing
  e chiama `ricevi(msg)` sui nodi destinatari corretti

## Come compilare ed eseguire

Questo ambiente sandbox ha solo la JRE (nessun `javac`), quindi il codice
non e' stato compilato qui: e' stato scritto e controllato "a mano"
(bilanciamento parentesi, coerenza package/nome classe/nome file). Sul tuo
computer, con un JDK 17+ installato, dalla cartella del progetto:

```bash
# compilazione: tutti i .java dentro out/
find src -name "*.java" > sources.txt
javac -d out @sources.txt

# esecuzione (crea/legge la cartella messaggi/ nella directory corrente)
java -cp out ab.demo.Main
```

Se preferisci un IDE (IntelliJ, Eclipse, VS Code), basta aprire la cartella
`src` come sorgente e lanciare `ab.demo.Main`.

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

Non e' stata usata nessuna libreria esterna (Jackson/Gson) perche' il
sandbox di sviluppo non aveva accesso a internet per scaricare dipendenze
Maven/Gradle. `ab.core.JsonUtil` e' un encoder/decoder JSON scritto da
zero, sufficiente per i tipi usati nei messaggi (stringhe, numeri, booleani,
null, oggetti, array). Se in futuro vuoi usare Jackson o Gson in un
progetto Maven/Gradle con accesso a internet, `Messaggio.toJson()` /
`Messaggio.fromJson()` sono gli unici due punti da riscrivere: il resto
dell'architettura (BUS, Talamo, nodi) non cambia.
