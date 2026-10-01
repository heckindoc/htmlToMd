import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.commonmark.renderer.markdown.MarkdownRenderer;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args) {
        System.out.println(mdToHTML("/mdtest.md"));
//        System.out.println(fromHTML("/htmlTest.html"));

    }

    public static String fromHTML(String path) {
        InputStream input = Main.class.getResourceAsStream(path);

        try (var reader = new InputStreamReader(input, StandardCharsets.UTF_8)) {
            Parser parser = Parser.builder().build();
            Node document = parser.parseReader(reader);
            MarkdownRenderer renderer = MarkdownRenderer.builder().build();
            return renderer.render(document); // I need to figure out how to convert to Node
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String mdToHTML (String path) {
        Parser parser = Parser.builder().build();
        InputStream input = Main.class.getResourceAsStream(path);
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
