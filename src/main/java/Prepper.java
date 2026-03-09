import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class Prepper {
  private static final Logger log = LoggerFactory.getLogger(Prepper.class);

  public static void main(String[] args) throws IOException {
    if (args.length < 2) {
      log.error("Usage: java Prepper <input.csv> <PREFIX>");
      System.exit(1);
    }

    String inputFileName = args[0];
    String prefix = args[1];
    String outputFileName = inputFileName.replace(".csv", "_prepped.csv");

    try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(inputFileName), StandardCharsets.UTF_8));
         PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(outputFileName), StandardCharsets.UTF_8))) {

      String line;
      int count = 0;
      while ((line = br.readLine()) != null) {
        pw.println(String.format("%s_%04d,%s", prefix, ++count, line));
      }
      log.info("{} rows written to {}", count, outputFileName);
    }
  }
}