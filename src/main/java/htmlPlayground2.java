import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

public class htmlPlayground2 {
    public static void main(String[] args) {
        Document doc = Jsoup.parse(
                """
        <pre>
                <code>
                function greetUser() {
            console.log("Hello, World!");
            Code block within pre tag.
            return true;
        }</code></pre>
        """);

        Element start = doc.selectFirst("pre");
        System.out.println(start.nodeName());
        System.out.println("--------------------");
        System.out.println(start.firstElementChild());
        Element code = start.firstElementChild();
        System.out.println("--------------------");
        System.out.println(start.child(0).nameIs("code"));
        System.out.println(start.childrenSize());
        System.out.println("--------------------");
        System.out.println(code.childrenSize());
        System.out.println(code.childNodeSize());
        System.out.println(code.childNode(0));
        System.out.println("--------------------");
        System.out.println(code.wholeOwnText());

    }
}
