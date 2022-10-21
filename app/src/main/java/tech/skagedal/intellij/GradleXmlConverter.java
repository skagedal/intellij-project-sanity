package tech.skagedal.intellij;

import java.awt.event.WindowStateListener;
import java.util.Objects;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class GradleXmlConverter {
  public void convert(Document document) {
    removeTextNodes(document.getDocumentElement());

    if (getFirstNodeByTagName(document) instanceof Element settingsElement) {
      // Setting "delegatedBuild" to "false" makes the setting "Build and run using:" to be "IntelliJ"
      findOrCreateOptionElement(document, settingsElement, "delegatedBuild")
          .setAttribute("value", "false");
    } else {
      throw new GradleXmlException("Did not find element GradleProjectSettings");
    }
  }

  private static Node getFirstNodeByTagName(Document document) {
    return document.getElementsByTagName("GradleProjectSettings").item(0);
  }

  private Element findOrCreateOptionElement(Document document, Element gradleProjectSettings, String name) {
    final var option = findOptionElement(gradleProjectSettings, name);
    return (option != null) ? option : createOptionElement(document, gradleProjectSettings, name);
  }

  private Element findOptionElement(Element element, String name) {
    final var optionElements = element.getElementsByTagName("option");
    for (var i = 0; i < optionElements.getLength(); i++) {
      final var node = optionElements.item(i);
      if (Objects.equals(node.getNodeName(), name)) {
        if (node instanceof Element element1) {
          return element1;
        } else {
          throw new RuntimeException("Found the option node but it wasn't an element");
        }
      }
    }
    return null;
  }

  private Element createOptionElement(Document document, Element gradleProjectSettings, String name) {
    final var optionElement = document.createElement("option");
    optionElement.setAttribute("name", name);
    gradleProjectSettings.appendChild(optionElement);
    return optionElement;
  }

  private void removeTextNodes(Node node) {
    final var childNodes = node.getChildNodes();
    for (var i = childNodes.getLength() - 1; i >= 0; i--) {
      final var child = childNodes.item(i);
      if (child.getNodeType() == Node.TEXT_NODE) {
        node.removeChild(child);
      } else {
        removeTextNodes(child);
      }
    }
  }
}
