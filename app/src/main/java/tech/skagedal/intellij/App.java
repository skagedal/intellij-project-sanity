package tech.skagedal.intellij;

import java.nio.file.Files;
import java.nio.file.Paths;

public class App {


    public static void main(String[] args) {
        new App().run(args);
    }

    private void run(String[] args) {
        final var gradleXmlPath = Paths.get(".idea", "gradle.xml");
        if (Files.exists(gradleXmlPath)) {
            final var converter = new GradleXmlConverter();

            final var intellijSanity = IntellijSanity.create();
            intellijSanity.sanitizeGradleXmlFile(gradleXmlPath);
            System.err.println("File modified.");
        } else {
            System.err.println("You are not in an IntelliJ project.");
            System.exit(1);
        }
    }

}
