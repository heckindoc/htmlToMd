import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class test {


    public static void main(String[] args) {
        MarkdownToHtmlConverter mdConverter = new MarkdownToHtmlConverter();
        HtmlToMarkdownConverter htmlConverter = new HtmlToMarkdownConverter();

//        try (InputStream inputStream = test.class.getResourceAsStream("/htmlTest2.html")) {
//            String input = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
//
//            System.out.println(htmlConverter.convertToMarkdown(input));

        try (InputStream inputStream = test.class.getResourceAsStream("/mdtest.md")) {
            String input = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

            System.out.println(mdConverter.convertToHtml(input));

                        Document doc = Jsoup.parse(inputStream, "UTF-8", "");
            Element next = doc.firstElementChild();





        } catch (IOException e) {
            System.out.println("IO EXCEPTION ON INPUT STREAM");
        }
    }
}
