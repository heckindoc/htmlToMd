import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class htmlPlayground {
    public static void main(String[] args) throws IOException {
        Document doc = Jsoup.parse("""
                <h1>this is a heading</h1>
                <p>This is a new paragraph with a line of text
                <br>in case you missed it there was a break that just happened
                <br>did you see that? That was another one. </p>
                
                <ol>
                    <li> first </li>
                    <li> number 2 </li>
                    <li> number three </li>
                </ol>
                """);

        Elements children = doc.children();
        List<Node> childNodes = doc.childNodes();
        Element next = doc.firstElementChild();

        System.out.println("--- Body Children ___");
        for (Element e : next.children()) {
            System.out.println("Child --> " + e);
            System.out.println(e.nodeName());
            iterateNodes(e);
        }
//
//        System.out.println();
//        System.out.println();
//
//        System.out.println("--- Attributes ---");
//        for (Attribute a : body.child(1).attributes()) {
//           System.out.println(a);
//        }
    }

    public static void iterateNodes(Element element) {
        System.out.println("--- " + element.nodeName() + " Children ___");
        for (Element e : element.children()) {
            System.out.println("Child --> " + e);
            System.out.println(e.nodeName());
            iterateNodes(e);
        }
    }
}
