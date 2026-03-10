# Zadaće iz kolegija Uzorci dizajna

Ovaj repozitorij obuhvaća zadaće na kolegiju Uzorci dizajna, studij Informacijsko i programsko inženjerstvo.

U zadaćama se simulira ponašanje neke turističke
agencije koja obrađuje rezervacije korisnika
za razne turističke aranžmane.
Korisnici mogu dodati rezervacije, ali i otkazati.
Stanje svake rezervacije ovisi o broju putnika
na aranžmanu. Prilikom dodavanja ili otkazivanja
rezervacije, mogu se kaskadno dogoditi promjene
stanja drugih rezervacija.

U nastavku je opis korištenih uzoraka za posljednju zadaću.

|Naziv uzorka|Klase|Razlog odabira|
|-|-|-|
|Singleton|**PostavkeSustava** (Singleton)|Postavke sustava definiraju način sortiranja podataka u tablicama te postavke tabličnog ispisa (dodatna funkcionalnost). Postavke sustava su jedne koje se koristi kroz cijeli program.|
|Singleton|**Formati** (Singleton)|Klasa Formati sadrži sve formate datuma i brojeva koji se koristi u cijelom programu na različitim mjestima. Postoji jedna instanca da se objekti formator klasa ne bi ponovno stvarali.|
|Singleton|**EvidencijaGresaka** (Singleton)|Greške se trebaju moći evidentirati iz svih dijelova programa. Treba postoji samo jedan objekt kako bi se ispravno brojio broj grešaka i sinkronizacija u slučaju višedretvenog rada.|
|Builder|**Aranzman** (Product)<br />**AranzmanGraditelj** (Builder)<br />**AranzmanStvarniGraditelj** (ConcreteBuilder)<br />**AranzmanDirektor** (Director)|Aranžman je složena klasa s većim brojem atributa gdje nisu svi atributi obavezni. Nije preporučljivo da se svi ti atributi upisuju u konstruktor. Direktor omogućuje kreiranje aranžmana samo s obaveznim atributima.|
|Builder|**ITablicniIspis** (Product)<br />**TablicniIspisGraditelj** (ConcreteBuilder)|Tablični ispis je složenija klasa koja prima listu stupaca. Graditelj omogućuje jednostavniju i slijednu izgradnju stupaca, postavljanje opcija te definiranje poravnanja za svaki stupac. Omogućuje i korištenje dodatnih dekoratora (dodatna funkcionalnost).|
|Builder|**Pattern** (Product)<br />**RegexKomandeGraditelj** (ConcreteBuilder)|Regularni izrazi su komplicirani za izgradnju. Ovaj graditelj omogućuje jednostavnu izgradnju regularnog izraza koji se koristi prilikom provjere komandi.|
|Facade|**UcitavacPodatakaFacade** (Facade)<br />Klase podsustava:<br />**UcitavacCsvPodataka, UcitavacAranzmana, UcitavacRezervacija, CsvCitac, CsvRedak, CsvRedakIterator, CsvStupacIterator, EvidencijaGresaka**|Postoji više klasa koje sudjeluju u učitavanju i parsiranju csv podataka. Facade definira sučelje više razine koje omogućuje jednostavnije korištenje podsustava za učitavanje, parsiranje i provjeru ispravnosti podataka.|
|Iterator|**CsvRedakIterator** (ConcreteIterator)<br />**CsvCitac** (ConcreteAggregate)|Da omogući lakše iteriranje kroz csv retke.|
|Iterator|**CsvStupacIterator** (ConcreteIterator)<br />**CsvRedak** (ConcreteAggregate)|Da omogući lakše iteriranje kroz stupce csv retka. S obzirom da csv podaci za aranžmane imaju velik broj stupaca, lakše je koristiti iterator da dohvati sljedeći element, nego koristiti indekse.|
|State|**Rezervacija** (Context)<br />**RezervacijaStanje** (State)<br />ConcreateState klase:<br />**RezervacijaNova**<br />**RezervacijaAktivna**<br />**RezervacijaNaCekanju**<br />**RezervacijaOdgodjena**<br />**RezervacijaOtkazana**<br />**RezervacijaPrimljena**|Pojedina rezervacija može biti u različitim stanjima. Poslovna logika se može razlikovati s obzirom na stanje rezervacije.|
|State|**Aranzman** (Context)<br />**AranzmanStanje** (State)<br />ConcreteState klase:<br />**AranzmanUPripremi**<br />**AranzmanAktivan**<br />**AranzmanPopunjen**<br />**AranzmanOtkazan**|Aranžman može biti u različitim stanjima. Poslovna logika se razlikuje s obzirom na stanje aranžmana. State uzorak omogućuje lakše dodavanje novih stanja i lakše definiranje poslovne logike svakog stanja.|
|Composite|**PutovanjeComponent** (Component)<br />**PutovanjeComposite** (Composite)<br />**TuristickaAgencija** (ConcreteComposite)<br />**Aranzman** (ConcreteComposite)<br />**Rezervacija** (Leaf)|Turistička agencija obuhvaća različite aranžmane. Svaki aranžman sadrži različite rezervacije korisnika. Turistička agencija, aranžmani i rezervacije predstavljaju hijerarhijsku strukturu podataka.|
|Observer|**RezervacijaObserver** (Observer)<br />**RezervacijaSubject** (Subject)<br />**Aranzman** (ConcreteSubject, ConcreteObserver)<br />**Rezervacija** (ConcreteObserver)<br />**Korisnik** (ConcreteObserver)|Rezervacija tijekom svojeg životnog ciklusa mijenja različita stanja. Stanja ovise o korisničkim akcijama te ostalim rezervacijama vlastitog, ali i ostalih aranžmana. Promjena stanja jedne rezervacije može utjecati na promjenu stanja druge rezervacije. State omogućuje lakše praćenje promjena ostalih rezervacija.|
|Decorator|**ITablicniIspis** (Component)<br />**TablicniIspis** (ConcreteComponent)<br />**TablicniIspisDecorator** (Decorator)<br />**TablicniIspisDodatneCrte** (ConcreteDecorator)<br />**TablicniIspisPrelamanje** (ConcreteDecorator)|Decorator uzorak dodaje odgovornost postojećoj klasi bez unutarnjih izmjena postojeće klase. Ne želimo mijenjati već postojeći tablični ispis. Prelamanje teksta samo priprema i izmjenjuje ulazne podatke koje već postojeća klasa TablicniIspis koristi. Nadalje, dodatne crte samo pozivaju dodatne metode.|
|Prototype|**Prototype** (Prototype)<br />**Korisnik** (ConcretePrototype)<br />**Rezervacija** (ConcretePrototype)|Potrebno je napraviti potpunu kopiju nekih objekata koji se spremaju koristeći Memento uzorak dizajna. Memento objekt mora biti nepromjenjiv.|
|Template Method|**UcitavacCsvPodataka** (AbstractClass)<br />**UcitavacAranzmana** (ConcreteClass)<br />**UcitavacRezervacija** (ConcreteClass)|Koraci za učitavanje csv podataka za aranžmane i rezervacije su vrlo identični. Postoje samo neki koraci koji su različiti, a posebne klase mogu definirati te korake. S ovim uzorkom smanjujemo dupliciranje koda i pojednostavljujemo učitavanje csv podataka.|
|Strategy|**UpravljanjeRezervacijamaStrategy** (Strategy)<br />**UpravljanjeRezervacijamaJDR** (ConcreteStrategy)<br />**UpravljanjeRezervacijamaVDR** (ConcreteStrategy)|Postoji više načina na koji se upravlja dozvoljenim brojem rezervacija. Svaki način se može odvojiti u posebnu klasa te se tako postiže labava povezanost klasa i smanjenje odgovornosti.|
|Null Object|**UpravljanjeRezervacijamaNull** (Null Object)|Želimo izbjeći stalno provjeravanje ako je neki objekt null, a možemo definirati ponašanje „ne radi ništa“.|
|Factory Method|**IKomanda** (Product)<br />**KomandaKreator** (Creator)<br /><br />Concrete Creator (inner class) -> Concrete Product<br />**KomandaITAK.Kreator -> KomandaITAK**<br />**KomandaIRTA.Kreator -> KomandaIRTA**<br />**KomandaIRO.Kreator -> KomandaIRO**<br />**KomandaDRTA.Kreator -> KomandaDRTA**<br />**...**|Na temelju naziv komande, potrebno je pozvati odgovarajuću klasu koja sadržava funkcionalnost te komande. Nadalje, potrebno je parsirati parametre komande. Želimo odvojiti odgovornost na parsiranje te odabir komande.|
|Command|**IKomanda** (Command)<br />**ObradaKomandi** (Invoker)<br />**TuristickaAgencija** (Receiver)<br />ConcreteCommand klase:<br />**KomandaBP, KomandaDRTA, KomandaIP, KomandaIRO, KomandaIRTA, KomandaITAK, KomandaITAP, KomandaITAS, KomandaORTA, KomandaOTA, KomandaPOTI, KomandaPPTAR, KomandaPSTAR, KomandaPTAR, KomandaUP, KomandaUPTAR, KomandaVSTAR**|Program izvršava komande korisnika. Svaka komanda obavlja svoju funkcionalnost.|
|Memento|**Aranzman** (Originator)<br />**StanjeAranzmanaMemento** (Memento)<br />**StanjeAranzmanaCaretaker** (Caretaker)|Želimo moći pohraniti stanje aranžmana i svih njegovih rezervacija kako bi se stanje moglo obnoviti.|
|Chain of Responsibility|**FilterRezervacije** (Handler)<br />ConcreteHandler klase:<br />**RezervacijeAranzmanaFilter**<br />**RezervacijeKorisnikaFilter**<br />**StanjeRezervacijeFilter**|Potrebno je filtrirati rezervacije po određenim kriterijima. Svaka klasa određuje zadovoljava li neka rezervacije kriterij ili ne. Možemo povezati filtriranje u lanac gdje rezervacija prolazi kroz lanac i ako dođe do kraja, zadovoljava svim kriterijima.|
|Visitor|**PutovanjeVisitor** (Visitor)<br />**PretrazivanjePutovanjaVisitor** (ConcreteVisitor)<br />**PutovanjeComponent** (Element)<br />**Aranzman** (ConcreteElement)<br />**Rezervacija** (ConcreteElement)|Potrebno je pretražiti rezervacije i aranžmane na temelju riječi pretrage. Rezervacije pretražujemo na temelju imena i prezimena korisnika, a aranžmane na temelju naziva i opisa programa. Postoje različita sučelja tih klasa.|

## Dijagram klasa

![dijagram klasa](Dijagram\_klasa.png)
