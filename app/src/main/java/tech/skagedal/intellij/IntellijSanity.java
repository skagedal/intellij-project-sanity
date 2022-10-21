package tech.skagedal.intellij;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class IntellijSanity {
  private final Transformer xmlTransformer;
  private final GradleXmlConverter gradleXmlConverter;

  public static IntellijSanity create() {
    try {
      final var xmlTransformer = TransformerFactory.newInstance().newTransformer();
      xmlTransformer.setOutputProperty(OutputKeys.INDENT, "yes");
      xmlTransformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
      return new IntellijSanity(
          xmlTransformer,
          new GradleXmlConverter()
      );
    } catch (TransformerConfigurationException e) {
      throw new RuntimeException(e);
    }
  }

  public IntellijSanity(
      Transformer transformer,
      GradleXmlConverter gradleXmlConverter
  ) {
    this.xmlTransformer = transformer;
    this.gradleXmlConverter = gradleXmlConverter;
  }

  public void sanitizeGradleXmlFile(Path path)  {
    final var document = readAndConvert(openInputStream(path));
    final var writer = openWriter(path);
    write(document, writer);
  }

  public Document readAndConvert(InputStream inputStream) {
    final var document = readDocument(inputStream);
    gradleXmlConverter.convert(document);
    return document;
  }

  public String toText(Document document) {
    StringWriter writer = new StringWriter();
    write(document, writer);
    return writer.getBuffer().toString();
  }

  private void write(Document document, Writer writer) {
    final var target = new StreamResult(writer);
    try {
      xmlTransformer.transform(new DOMSource(document), target);
    } catch (TransformerException e) {
      throw new RuntimeException(e);
    }
  }

  private static Document readDocument(InputStream inputStream) {
    final var documentBuilderFactory = DocumentBuilderFactory.newInstance();
    try {
      final var documentBuilder = documentBuilderFactory.newDocumentBuilder();
      return documentBuilder.parse(inputStream);
    } catch (ParserConfigurationException | SAXException | IOException e) {
      throw new RuntimeException(e);
    }
  }

  // Files helpers
  private FileInputStream openInputStream(Path path) {
    try {
      return new FileInputStream(path.toFile());
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  private static BufferedWriter openWriter(Path path) {
    try {
      return Files.newBufferedWriter(path);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }


}
