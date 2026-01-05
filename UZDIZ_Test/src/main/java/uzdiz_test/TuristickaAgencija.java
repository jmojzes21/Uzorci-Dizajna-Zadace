package uzdiz_test;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class TuristickaAgencija {

  private Properties properties = new Properties();
  private Process process;
  private PrintWriter writer;
  private BufferedReader reader;

  private Queue<String> inputQueue = new ConcurrentLinkedQueue<>();
  private Thread readInputThread;
  private AtomicBoolean readingInput = new AtomicBoolean(true);

  public TuristickaAgencija() {

  }

  public List<String> izvrsiKomandu(String komanda) {
    writer.println(komanda);
    writer.flush();
    return readLines();
  }
  
  public List<String> readLines() {

    List<String> lines = new ArrayList<>();

    do {
      while (!inputQueue.isEmpty()) {
        lines.add(inputQueue.poll());
      }

      sleep(20);

    } while (!inputQueue.isEmpty());

    return lines;

  }

  private void sleep(int ms) {
    try {
      Thread.sleep(ms);
    } catch (InterruptedException ignored) {}
  }

  public void pokreni() throws Exception {

    readProperties();

    var p = properties.entrySet();
    for (var e : p) {
      System.out.println(e.getKey() + ": " + e.getValue());
    }

    String zadaca = properties.getProperty("ZADACA_DATOTEKA");
    String wdir = properties.getProperty("RADNI_DIREKTORIJ");

    var builder = new ProcessBuilder("java", "-Dfile.encoding=UTF-8", "-Dsun.stdout.encoding=UTF-8", "-jar",
        zadaca, "--ta", "aranzmani.csv", "--rta", "rezervacije.csv");
    builder.directory(new File(wdir));
    process = builder.start();

    writer = new PrintWriter(new OutputStreamWriter(process.getOutputStream(), StandardCharsets.UTF_8));
    reader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));

    readInputThread = new Thread(this::readInputThreadTask);
    readInputThread.start();

    sleep(200);
    readLines();

  }

  private void readInputThreadTask() {

    while (readingInput.get()) {
      try {
        String line = reader.readLine();
        System.out.println(line);
        if (line == null) {return;}
        inputQueue.add(line);
      } catch (Exception e) {
        System.out.println(e.getMessage());
      }
    }

  }

  public void zaustavi() throws Exception {

    readingInput.set(false);

    writer.println("Q");
    writer.flush();

    if (!process.waitFor(2, TimeUnit.SECONDS)) {
      process.destroy();
    }

  }

  private void readProperties() throws IOException {

    var lines = Files.readAllLines(Path.of("postavke.txt"));
    lines = lines.stream().map(e -> e.trim()).filter(e -> !e.isEmpty()).toList();

    for (String line : lines) {
      int i = line.indexOf('=');
      if (i == -1) {continue;}
      String key = line.substring(0, i).trim();
      String value = line.substring(i + 1).trim();
      properties.put(key, value);
    }

  }

}
