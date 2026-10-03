import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.commonmark.renderer.markdown.MarkdownRenderer;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class markdownToHTML {
    public static void main(String[] args) {
        System.out.println(mdToHTML("/mdtest.md"));

    }

    // TODO: better error logging...
    public static String mdToHTML (String path) {
        Parser parser = Parser.builder().build();
        InputStream input = markdownToHTML.class.getResourceAsStream(path);
        if (input == null) {
            return "error - file not found";
        }
        try (InputStreamReader reader = new InputStreamReader(input, StandardCharsets.UTF_8)) {
            Node document = parser.parseReader(reader);
            HtmlRenderer renderer = HtmlRenderer.builder().build();
            return renderer.render(document);
        } catch (IOException e) {
            return "unable to read file";
        }

    }


}
