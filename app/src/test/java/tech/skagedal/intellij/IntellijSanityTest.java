package tech.skagedal.intellij;

import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;

class IntellijSanityTest {
  final IntellijSanity intellijSanity = IntellijSanity.create();

  @Test
  void convert_project_run() {
    try {
      InputStream resourceStream = getResourceStream("/initial-gradle.xml");
      final var document = intellijSanity.readAndConvert(resourceStream);
      System.out.println(intellijSanity.toText(document));
    } catch (Exception exception) {
      throw new RuntimeException(exception);
    }
  }

  private InputStream getResourceStream(String name) {
    return getClass().getResourceAsStream(name);
  }
}