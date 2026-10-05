
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.LeafNode;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HtmlToMarkdownConverter {

    public HtmlToMarkdownConverter() {
    }

    public String convertToMarkdown(String html) {
        if (html.isBlank()) {
            return "";
        } else {
            Document doc = Jsoup.parse(html);
            Element next = doc.firstElementChild();
            if (next != null) {
                return iterateNodes(next, 0);
            } else return "";
        }
    }

    private String iterateNodes(Node node, int indentLevel) {
        StringBuilder text = new StringBuilder();
        Set<String> ignoreList = new HashSet<>(List.of("body", "head"));
        List<Node> nodes = node.childNodes();
        for (Node n : nodes) {
            if (n instanceof LeafNode) {
                if (n instanceof TextNode) {
                    if (((TextNode) n).isBlank()) {
                        if (isInlineNeighbor(n.previousSibling()) && isInlineNeighbor(n.nextSibling())) {
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
                    // TODO: add <code>, <div>
                    // The following tags are not supported by Commonmark, for that reason they are not included here:
                    // <mark>, <sup>, <sub>

                    // Block cases

                    // headings
                    case "h1" ->
                            text.append(indentSpaces(determineIndent(indentLevel))).append(wrapBlock(iterateNodes(n, indentLevel),
                                    "# ", addLineSeparators((Element) n, n.nextElementSibling())));
                    case "h2" ->
                            text.append(indentSpaces(determineIndent(indentLevel))).append(wrapBlock(iterateNodes(n, indentLevel),
                                    "## ", addLineSeparators((Element) n, n.nextElementSibling())));
                    case "h3" ->
                            text.append(indentSpaces(determineIndent(indentLevel))).append(wrapBlock(iterateNodes(n, indentLevel),
                                    "### ", addLineSeparators((Element) n, n.nextElementSibling())));
                    case "h4" ->
                            text.append(indentSpaces(determineIndent(indentLevel))).append(wrapBlock(iterateNodes(n, indentLevel),
                                    "#### ", addLineSeparators((Element) n, n.nextElementSibling())));
                    case "h5" ->
                            text.append(indentSpaces(determineIndent(indentLevel))).append(wrapBlock(iterateNodes(n, indentLevel),
                                    "##### ", addLineSeparators((Element) n, n.nextElementSibling())));
                    case "h6" ->
                            text.append(indentSpaces(determineIndent(indentLevel))).append(wrapBlock(iterateNodes(n, indentLevel),
                                    "###### ", addLineSeparators((Element) n, n.nextElementSibling())));

                    // blockquotes
                    case "blockquote" -> text.append(indentSpaces(determineIndent(indentLevel)))
                            .append(wrapBlock(blockQuotePrefix(iterateNodes(n, indentLevel)),
                                    "", addLineSeparators((Element) n, n.nextElementSibling())));

                    // paragraphs
                    case "p" ->
                            text.append(indentSpaces(determineIndent(indentLevel))).append(wrapBlock(iterateNodes(n, indentLevel),
                                    "", addLineSeparators((Element) n, n.nextElementSibling())));

                    // lists
                    case "li" ->
                            text.append(indentSpaces(determineIndent(indentLevel))).append(wrapBlock(iterateNodes(n, indentLevel),
                                    listItemPrefix(((Element) n)), addLineSeparators((Element) n, n.nextElementSibling())));
                    case "ul", "ol" ->
                            text.append(indentSpaces(determineIndent(indentLevel))).append(wrapBlock(iterateNodes(n, indentLevel + 1),
                                    "", addLineSeparators((Element) n, n.nextElementSibling())));

                    // preformatted

                    // TODO: Do we need to determine an indent level before a pre tag? I think no, but not sure.
                    case "pre" ->
                            text.append(preFormattedBuilder((Element) n)).append(addLineSeparators((Element) n, n.nextElementSibling()));


                    // In-Line cases
                    case "img" -> {
                        String img = imageBuilder(n);
                        text.append(wrapBlock(img.strip(),
                                trailingSubstring(findLeadingWhiteSpaces(img), img),
                                trailingSubstring(findTrailingWhiteSpaces(img), img)));
                    }
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
                    case "code" -> {
                        String code = ((Element) n).wholeOwnText();
                        text.append(wrapBlock(code.strip(),
                                trailingSubstring(findLeadingWhiteSpaces(code), code) + "`",
                                "`" + trailingSubstring(findTrailingWhiteSpaces(code), code)));
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

    private String wrapBlock(String innerContent, String prefix, String suffix) {
        return prefix + innerContent + suffix;
    }

    private boolean isInlineNeighbor(Node neighbor) {
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

    private int findLeadingWhiteSpaces(String string) {
        return (string.length() - string.stripLeading().length());
    }

    private int findTrailingWhiteSpaces(String string) {
        return (string.length() - string.stripTrailing().length());
    }

    private String trailingSubstring(int numberToAdd, String text) {
        if (numberToAdd == 0) {
            return "";
        }
        text = text.substring(text.length() - numberToAdd);
        return text;
    }

    private String addLineSeparators(Element thisElement, Element nextElement) {
        if (nextElement == null) {
            return "";
        }
        if ((thisElement.isBlock() && nextElement.is("li")) ||
                (thisElement.isBlock() && nextElement.is("ul")) ||
                (thisElement.isBlock() && nextElement.is("ol")))
            return "\n";
        if (thisElement.isBlock() && nextElement.isBlock()) {
            return "\n\n";
        } else return "";
    }

    private int determineIndent(int level) {
        if (level <= 0) {
            return 0;
        }
        return (level - 1) * 2;
    }

    private String indentSpaces(int level) {
        String text = "";
        if (level == 0) {
            return text;
        }
        for (int i = 0; i < level; i++) {
            text = text.concat(" ");
        }
        return text;
    }

    private String listItemPrefix(Element element) {
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

    private String linkBuilder(Node node, int level) {
        if (!node.hasAttr("href") || node.attr("href").isBlank()) {
            return "URL Missing: " + iterateNodes(node, level);
        } else {
            return "[" + iterateNodes(node, level) + "]" + "(" + node.attr("href") + ")";
        }
    }

    // TODO: We could end up removing a <br> tag in a case like this: <blockquote><p>last line<br></p></blockquote> --> needs fixing?
    private String blockQuotePrefix(String string) {
        String[] stringArray = string.split("\n");
        StringBuilder outText = new StringBuilder();
        for (String s : stringArray) {
            outText.append("> ").append(s).append("\n");
        }
        return outText.toString().stripTrailing();
    }

    private String imageBuilder(Node node) {
        String src = !node.attr("src").isBlank() ? node.attr("src") : "Image Missing";
        String alt = node.hasAttr("alt") ? node.attr("alt") : "";
        String title = node.hasAttr("title") ? node.attr("title") : "";

        if (src.equals("Image Missing")) {
            if (title.isBlank()) {
                title = "none";
            }
            return (src + ": Title - " + title + " | Alt - " + alt);
        } else if (title.isBlank()) {
            return ("![" + alt + "]" + "(" + src + ")");
        } else return ("![" + alt + "]" + "(" + src + " \"" + title + "\")");
    }

    // <pre> tags will only handle code blocks. This is by design as Commonmark has no implementation
    // to generate <pre> tags outside of this.
    private String preFormattedBuilder(Element element) {
        StringBuilder text = new StringBuilder();
        // If the <code> tag ends up on a second line below the <pre> tag, this if statement will be false.
        if (element.childNode(0).nameIs("code") && element.childrenSize() == 1) {
            text.append("```").append(element.wholeText()).append("\n```");
            return text.toString();
        } else {
            text.append("Unhandled HTML tag within <pre>: ").append(element.wholeText());
            return text.toString();
        }
    }
}



