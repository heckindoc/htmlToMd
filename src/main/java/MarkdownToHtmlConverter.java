import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

public class MarkdownToHtmlConverter {
    public MarkdownToHtmlConverter() {}

    public String convertToHtml(String markdown) {
        if (markdown.isBlank()) {
            return "";
        }
        else return convert(markdown);
    }

    private String convert(String input) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(input);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(document);
    }
}
