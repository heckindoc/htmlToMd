
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.LeafNode;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class htmlToMarkdown {
    public static void main(String[] args) {
        try (InputStream inputStream = markdownToHTML.class.getResourceAsStream("/htmlTest2.html")) {
            Document doc = Jsoup.parse(inputStream, "UTF-8", "");
            Element next = doc.firstElementChild();


            System.out.println(iterateNodes(next, 0));

        } catch (IOException e) {
            System.out.println("IO EXCEPTION ON INPUT STREAM");
        }
    }

    // TODO: Build unified warning message for use in linkBuilder and default.
    public static String iterateNodes(Node node, int indentLevel) {
        StringBuilder text = new StringBuilder();
        Set<String> ignoreList = new HashSet<>(List.of("body", "head"));
        List<Node> nodes = node.childNodes();
        for (Node n : nodes) {
            if (n instanceof LeafNode) {
                if (n instanceof TextNode) {
                    if (((TextNode) n).isBlank()) {
                        if(isInlineNeighbor(n.previousSibling()) && isInlineNeighbor(n.nextSibling())) {
                            text.append(" ");
                        }
                    } else {
                        // TODO: Need to fix trailing whitespaces --> can't do stripTrailing() with following inline elements
                        text.append(((TextNode) n).text().stripLeading());
                        if (!isInlineNeighbor(n.nextSibling())) {
                            text.append("\n");
                        }
                    }
                }
            } else if (n instanceof Element) {
                switch (n.nodeName()) {
                    // TODO: add <blockquote>, <code>, <pre>, <div>, <h4>, <mark>, <sup>, <sub>
                    // Block cases

                    // headings
                    case "h1" -> text.append(indentSpaces(determineIndent(indentLevel))).append(wrapBlock(iterateNodes(n, indentLevel),
                            "# ", addLineSeparators((Element) n, n.nextElementSibling())));
                    case "h2" -> text.append(indentSpaces(determineIndent(indentLevel))).append(wrapBlock(iterateNodes(n, indentLevel),
                            "## ", addLineSeparators((Element) n, n.nextElementSibling())));
                    case "h3" -> text.append(indentSpaces(determineIndent(indentLevel))).append(wrapBlock(iterateNodes(n, indentLevel),
                            "### ", addLineSeparators((Element) n, n.nextElementSibling())));
                    case "h4" -> text.append(indentSpaces(determineIndent(indentLevel))).append(wrapBlock(iterateNodes(n, indentLevel),
                            "#### ", addLineSeparators((Element) n, n.nextElementSibling())));
                    case "h5" -> text.append(indentSpaces(determineIndent(indentLevel))).append(wrapBlock(iterateNodes(n, indentLevel),
                            "##### ", addLineSeparators((Element) n, n.nextElementSibling())));
                    case "h6" -> text.append(indentSpaces(determineIndent(indentLevel))).append(wrapBlock(iterateNodes(n, indentLevel),
                            "###### ", addLineSeparators((Element) n, n.nextElementSibling())));

                    // paragraphs
                    case "p" -> text.append(indentSpaces(determineIndent(indentLevel))).append(wrapBlock(iterateNodes(n, indentLevel),
                            "", addLineSeparators((Element) n, n.nextElementSibling())));

                    // lists
                    case "li" -> text.append(indentSpaces(determineIndent(indentLevel))).append(wrapBlock(iterateNodes(n, indentLevel),
                            listItemPrefix(((Element) n)), addLineSeparators((Element) n, n.nextElementSibling())));
                    case "ul", "ol" -> text.append(indentSpaces(determineIndent(indentLevel))).append(wrapBlock(iterateNodes(n, indentLevel + 1),
                            "", addLineSeparators((Element) n, n.nextElementSibling())));

                    // In-Line cases
                    case "b", "strong" -> {
                        String boldedText = iterateNodes(n, indentLevel);
                        text.append(wrapBlock(boldedText.strip(),
                            trailingSubstring(findLeadingWhiteSpaces(boldedText), boldedText) + "**",
                            "**" + trailingSubstring(findTrailingWhiteSpaces(boldedText), boldedText)));
                    }
                    case "em", "i" -> {
                        String emphasizedText = iterateNodes(n, indentLevel);
                        text.append(wrapBlock(emphasizedText.strip(),
                                trailingSubstring(findLeadingWhiteSpaces(emphasizedText), emphasizedText) + "*",
                                "*" + trailingSubstring(findTrailingWhiteSpaces(emphasizedText), emphasizedText)));
                    }
                    case "br" -> text.append("  \n");
                    case "a" -> {
                        String link = linkBuilder(n, indentLevel);
                        text.append(wrapBlock(link.strip(),
                                trailingSubstring(findLeadingWhiteSpaces(link), link),
                                 trailingSubstring(findTrailingWhiteSpaces(link), link)));
                    }
                    default -> {
                        //TODO: How to handle missing nodes --> log vs put into .md
                        if (!ignoreList.contains(n.nodeName())) {
                            text.append("Unhandled HTML Node: ").append(n.nodeName()).append("\n");
                        }
                        text.append(iterateNodes(n, indentLevel));
                    }
                }
            }
        }
        return text.toString();
    }

    private static String wrapBlock(String innerContent, String prefix, String suffix) {
        return prefix + innerContent + suffix;
    }

    private static boolean isInlineNeighbor (Node neighbor) {
        if (neighbor == null) {
            return true;
        }
        if (neighbor instanceof Element && !((Element) neighbor).isBlock()) {
            return true;
        }
        if (neighbor instanceof TextNode) {
            return true;
        }
        return false;
    }

    private static int findLeadingWhiteSpaces (String string) {
        return (string.length() - string.stripLeading().length());
    }

    private static int findTrailingWhiteSpaces (String string) {
        return (string.length() - string.stripTrailing().length());
    }

    private static String trailingSubstring(int numberToAdd, String text) {
        if (numberToAdd == 0) {
            return "";
        }
            text = text.substring(text.length() - numberToAdd);
        return text;
    }

    private static String addLineSeparators (Element thisElement, Element nextElement) {
        if (nextElement == null) {
            return "";
        }
        if ((thisElement.isBlock() && nextElement.is("li")) ||
                (thisElement.isBlock() && nextElement.is("ul")) ||
                (thisElement.isBlock() && nextElement.is("ol")))
            return "\n";
        if (thisElement.isBlock() && nextElement.isBlock()) {
            return "\n\n";
        }
        else return "";
    }

    private static int determineIndent (int level) {
        if (level <= 0) {
            return 0;
        }
        return (level - 1) * 2;
    }

    private static String indentSpaces (int level) {
        String text = "";
        if (level == 0) {
            return text;
        }
        for (int i = 0 ; i < level ; i++) {
            text = text.concat(" ");
        }
        return text;
    }

    private static String listItemPrefix (Element element) {
        Element parent = element.parent();
        if (parent != null) {
            if (parent.nodeName().equals("ul")) {
                return "- ";
            } else if (parent.nodeName().equals("ol")) {
                return (String.valueOf(element.elementSiblingIndex() + 1) + ". ");
            }
        }
        return "";
    }

    private static String linkBuilder (Node node, int level) {
        if (!node.hasAttr("href") || node.attr("href").isBlank()) {
            return "URL Missing: " + iterateNodes(node, level);
        } else
        {
            return "[" + iterateNodes(node, level) + "]" + "(" + node.attr("href") + ")";
        }
    }

}
