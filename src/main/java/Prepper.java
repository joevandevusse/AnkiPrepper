import org.apache.commons.csv.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Prepper {
  private static final Logger log = LoggerFactory.getLogger(Prepper.class);
  private static final Path MANIFEST = Path.of("processed.log");

  public static void main(String[] args) throws IOException {
    Path inputDir = Path.of("noji_decks");
    Path outputDir = Path.of("anki_decks");
    Files.createDirectories(outputDir);

    Set<String> processed = loadManifest();

    List<Path> csvFiles;
    try (var stream = Files.list(inputDir)) {
      csvFiles = stream.filter(p -> p.toString().endsWith(".csv")).sorted().toList();
    }

    if (csvFiles.isEmpty()) {
      log.warn("No CSV files found in {}", inputDir);
      return;
    }

    for (Path inputFile : csvFiles) {
      String filename = inputFile.getFileName().toString();
      String baseName = stripTimestamp(filename.replace(".csv", ""));
      String prefix = baseName.toUpperCase();

      if (processed.contains(filename)) {
        log.info("{}: already processed, skipping", prefix);
        continue;
      }

      Path outputFile = outputDir.resolve(baseName + ".csv");
      int count = processFile(inputFile, outputFile, prefix);
      appendManifest(filename);
      log.info("{}: {} rows written to {}", prefix, count, outputFile);
    }
  }

  private static Set<String> loadManifest() throws IOException {
    if (!Files.exists(MANIFEST)) return new HashSet<>();
    return new HashSet<>(Files.readAllLines(MANIFEST, StandardCharsets.UTF_8));
  }

  private static void appendManifest(String filename) throws IOException {
    Files.writeString(MANIFEST, filename + "\n", StandardCharsets.UTF_8,
        StandardOpenOption.CREATE, StandardOpenOption.APPEND);
  }

  private static int processFile(Path inputFile, Path outputFile, String prefix) throws IOException {
    try (var reader = new InputStreamReader(Files.newInputStream(inputFile), StandardCharsets.UTF_8);
         var writer = new OutputStreamWriter(Files.newOutputStream(outputFile), StandardCharsets.UTF_8);
         var parser = CSVFormat.DEFAULT.parse(reader);
         var printer = new CSVPrinter(writer, CSVFormat.DEFAULT)) {

      int count = 0;
      for (CSVRecord record : parser) {
        printer.printRecord(prepend(String.format("%s_%04d", prefix, ++count), record));
      }
      return count;
    }
  }

  private static Object[] prepend(String id, CSVRecord record) {
    Object[] row = new Object[record.size() + 1];
    row[0] = id;
    for (int i = 0; i < record.size(); i++) {
      row[i + 1] = record.get(i);
    }
    return row;
  }

  // Strips trailing _YYYY_MM_DD_HHMMSS from Noji export filenames
  private static String stripTimestamp(String name) {
    return name.replaceAll("_\\d{4}_\\d{2}_\\d{2}_\\d{6}$", "");
  }
}