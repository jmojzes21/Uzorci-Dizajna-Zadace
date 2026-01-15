package edu.unizg.foi.uzdiz.jmojzes21.lib.facade;

import edu.unizg.foi.uzdiz.jmojzes21.lib.ucitavac.UcitavacAranzmana;
import edu.unizg.foi.uzdiz.jmojzes21.lib.ucitavac.UcitavacCsvPodataka;
import edu.unizg.foi.uzdiz.jmojzes21.lib.ucitavac.UcitavacRezervacija;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class UcitavacPodatakaFacade {

  public List<List<String>> ucitajAranzmane(Path putanja) throws IOException {
    UcitavacCsvPodataka ucitavacPodataka = new UcitavacAranzmana();
    return ucitavacPodataka.ucitaj(putanja);
  }

  public List<List<String>> ucitajRezervacije(Path putanja) throws IOException {
    UcitavacCsvPodataka ucitavacPodataka = new UcitavacRezervacija();
    return ucitavacPodataka.ucitaj(putanja);
  }

}
