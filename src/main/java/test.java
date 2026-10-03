import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.io.InputStream;

public class test {
    public static void main(String[] args) {
        try (InputStream inputStream = markdownToHTML.class.getResourceAsStream("/htmlTest2.html")) {
            Document doc = Jsoup.parse(inputStream, "UTF-8", "");
            Element next = doc.firstElementChild();



        } catch (IOException e) {
            System.out.println("IO EXCEPTION ON INPUT STREAM");
        }
    }
}
