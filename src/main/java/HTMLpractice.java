import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;

public class HTMLpractice {
    public static void main(String[] args) throws IOException {
        Document doc = Jsoup.parse("""
                <body>
                <h1>Title</h1>
                
                <a href="www.thesite.com"><strong>Visit Example Website</strong></a>
                
                <ol>
                    <li> first </li>
                    <li> number 2 </li>
                    <li> number three </li>
                </ol>
                </body>""");

//        Elements headings = doc.select("h1");
//        Elements paragraphs = doc.select("p");

        Elements children = doc.children();
        List<Node> childNodes = doc.childNodes();
        Element body = doc.body();

        System.out.println("--- Body Children ___");
        for (Element e : body.children()) {
            System.out.println("Child --> " + e);
        }

        System.out.println();
        System.out.println();

        System.out.println("--- Attributes ---");
//        for (Attribute a : body.child(1).attributes()) {
//            System.out.println(a);
//        }
//
        Element url = body.child(1);
        for (Node n : url.childNodes()) {
            System.out.println(n.nodeName());
            System.out.println(n);
        }

        System.out.println(url.ownText());

        System.out.println(url.attribute("href"));

        System.out.println(url.attr("href"));

    }
}
