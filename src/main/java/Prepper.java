import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.List;

public class Prepper {
  private static final Logger log = LoggerFactory.getLogger(Prepper.class);

  public static void main(String[] args) throws IOException {
    Path inputDir = Path.of("noji_decks");
    Path outputDir = Path.of("anki_decks");
    Files.createDirectories(outputDir);

    List<Path> csvFiles;
    try (var stream = Files.list(inputDir)) {
      csvFiles = stream.filter(p -> p.toString().endsWith(".csv")).sorted().toList();
    }

    if (csvFiles.isEmpty()) {
      log.warn("No CSV files found in {}", inputDir);
      return;
    }

    for (Path inputFile : csvFiles) {
      String baseName = stripTimestamp(inputFile.getFileName().toString().replace(".csv", ""));
      String prefix = baseName.toUpperCase();
      Path outputFile = outputDir.resolve(baseName + ".csv");

      if (Files.exists(outputFile)) {
        log.info("{}: already processed, skipping", prefix);
        continue;
      }

      int count = processFile(inputFile, outputFile, prefix);
      log.info("{}: {} rows written to {}", prefix, count, outputFile);
    }
  }

  private static int processFile(Path inputFile, Path outputFile, String prefix) throws IOException {
    try (BufferedReader br = new BufferedReader(new InputStreamReader(Files.newInputStream(inputFile), StandardCharsets.UTF_8));
         PrintWriter pw = new PrintWriter(new OutputStreamWriter(Files.newOutputStream(outputFile), StandardCharsets.UTF_8))) {

      String line;
      int count = 0;
      while ((line = br.readLine()) != null) {
        pw.println(String.format("%s_%04d,%s", prefix, ++count, line));
      }
      return count;
    }
  }

  // Strips trailing _YYYY_MM_DD_HHMMSS from Noji export filenames
  private static String stripTimestamp(String name) {
    return name.replaceAll("_\\d{4}_\\d{2}_\\d{2}_\\d{6}$", "");
  }
}