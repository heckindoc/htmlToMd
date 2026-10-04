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
                <pre> A simple test.
                <code>function greetUser() {
                    console.log("Hello, World!");
                    return true;
                }</code>
                <p>      A new paragraph after the code</p></pre>
               
                """);

        Elements children = doc.children();
        List<Node> childNodes = doc.childNodes();
        Element next = doc.body();

        next.select("pre");
//        System.out.println("WHOLE TEXT EXAMPLE");
//        System.out.println(next.wholeText());
//        System.out.println("-------------");
//        System.out.println(next.wholeOwnText());
//        System.out.println(next.html());

        for (Element e : next.children()) {
            System.out.println(e.nodeName());
            System.out.print(e.wholeOwnText());
//            System.out.printf("--- %s Children ___\n", e.normalName());
//            System.out.println("Child --> " + e);
//            System.out.println(e.nodeName());
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
//        System.out.println("--- " + element.nodeName() + " Children ___");
        for (Element e : element.children()) {
            System.out.println(e.nodeName());
            System.out.print(e.wholeOwnText());
//            System.out.println("Child --> " + e);
//            System.out.println(e.nodeName());
//            System.out.println("- Attributes --> " + e.attributes());
            iterateNodes(e);
        }
    }
}
