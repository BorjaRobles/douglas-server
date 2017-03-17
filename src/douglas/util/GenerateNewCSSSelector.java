package douglas.util;

import org.json.simple.JSONObject;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import java.io.*;
import java.util.Scanner;

public class GenerateNewCSSSelector {

    private String fetchCssLib() {

        // Try to locate the library in two different ways depending on whether it's packed as a JAR or not
        if(!new File(ClassLoader.getSystemResource("css-selector-generator.min.js").getFile()).exists()) {

            StringBuilder cssGenerator = new StringBuilder();
            try {
                InputStream is = getClass().getResourceAsStream("css-selector-generator.min.js");
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String line;
                while ((line = br.readLine()) != null) {
                    cssGenerator.append(line);
                    cssGenerator.append(System.getProperty("line.separator"));
                }
            } catch(IOException e) {
                e.printStackTrace();
            }

            return cssGenerator.toString();
        } else {
            String cssGenerator = "";
            try {
                File ioStream = new File(ClassLoader.getSystemResource("css-selector-generator.min.js").getFile());
                Scanner sc = new Scanner(new FileInputStream(ioStream));
                while (sc.hasNext()) {
                    String[] s = sc.next().split("\r\n");
                    for (int i = 0; i < s.length; i++) {
                        cssGenerator += s[i];
                        cssGenerator += " ";
                    }
                }
            } catch(FileNotFoundException e) {
                e.printStackTrace();
            }
            return cssGenerator;
        }

    }

    // JavaScript lines used for travese the DOM nodes in order to locate elements that contains a certain text-value
    private String traverseByContent = "" +
            "function walkTheDOM(node, func) {\n" +
            "    func(node);\n" +
            "    node = node.firstChild;\n" +
            "    while (node) {\n" +
            "        walkTheDOM(node, func);\n" +
            "        node = node.nextSibling;\n" +
            "    }\n" +
            "}" +
            "var deepestChild;" +
            "walkTheDOM(document.body, function(node) { " +
            "   if(node.innerText === \"%s\") { " +
            "       deepestChild = node " +
            "   } " +
            "});";

    private String generate(WebDriver driver, String jsScript) {

        // We are injecting the CSS Selector Generator library into the currently
        // tested page in order to generate a new updated CSS selector
        JavascriptExecutor jsExecutor = (JavascriptExecutor)driver;
        jsExecutor.executeScript(fetchCssLib());
        jsExecutor.executeScript("document.CssSelectorGenerator = window.CssSelectorGenerator;");
        jsExecutor.executeScript("document.generator = new CssSelectorGenerator;");

        // Execute the JavaScript lines we passed as argument
        String newCssSelector = (String)jsExecutor.executeScript(jsScript);
        return newCssSelector;
    }

    // Craft a JavaScript string that uses elementFromPoint() and returns a CSS-selector of the located node
    public String generateFromLocation(WebDriver driver, JSONObject location) {
        int x = Math.toIntExact((Long)location.get("x"));
        int y = Math.toIntExact((Long)location.get("y"));
        String jsScript = 
            String.format("return document.generator.getSelector(document.elementFromPoint(%d,%d));", x, y);
        return this.generate(driver, jsScript);
    }

    // Craft a JavaScript string that uses "traverseByContent" and returns a CSS-selector of the located node
    public String generateFromContent(WebDriver driver, String content) {
        String jsScript = String.format(traverseByContent, content) + 
            "document.walkTheDOM = window.walkTheDOM; return document.generator.getSelector(deepestChild);";
        return this.generate(driver, jsScript);
    }

}
