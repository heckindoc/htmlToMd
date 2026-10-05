package org.heckindoc.htmltomdconverter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;

public class HtmlPlayground {
    public static void main(String[] args) throws IOException {
        Document doc = Jsoup.parse("""
                <pre> A simple test.
                                <code>function greetUser() {
                                    console.log("Hello, World!");
                                    return true;
                                }</code>
                                      A new paragraph after the code</pre>
                
                <a href="www.thesite.com">Visit Example Website</a>
                <h1>Heading 1</h1>
                """);

        Elements children = doc.children();
        List<Node> childNodes = doc.childNodes();
        Element next = doc.firstElementChild();
        System.out.println(next);
        System.out.println("-----------------");
        System.out.println(next.firstElementChild());
        System.out.println("-----------------");
        System.out.println(next.child(1));
        System.out.println("-----------------");
        Element body = next.child(1);
        System.out.println(body.firstElementChild());
        System.out.println("-----------------");
        Element pre = body.firstElementChild();
        System.out.println(pre.firstElementChild().nodeName());
        System.out.println(pre.firstElementChild().isBlock());
        System.out.println("-----------------");
        System.out.println(pre.childNode(0));
        System.out.println(pre.childNode(0).nodeName());
        System.out.println("-----------------");
        System.out.println(pre.wholeOwnText());
        System.out.println("-----------------");
        System.out.println(pre.childNodeSize());
        for (Node n : pre.childNodes()) {
            System.out.println(n.nodeName());
            System.out.println(n);
        }
        System.out.println("-----------------");
        Element code = pre.firstElementChild();
        System.out.println(code.wholeOwnText());
        System.out.println("-----------------");
        System.out.println(pre.nextElementSibling());
        System.out.println(pre.nextElementSibling().attr("href"));
        System.out.println(pre.nextElementSibling().wholeOwnText());
        System.out.println("-----------------");
        System.out.println(next.nextElementSibling());
    }


    public static void iterateNodes(Element element) {
//        System.out.println("--- " + element.nodeName() + " Children ___");
        for (Element e : element.children()) {
            System.out.println(e.nodeName());
            System.out.print(e.wholeOwnText());
            System.out.println("Child --> " + e);
            System.out.println(e.nodeName());
            System.out.println("- Attributes --> " + e.attributes());
            iterateNodes(e);
        }
    }
}


