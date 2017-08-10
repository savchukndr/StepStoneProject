import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.HTMLParserListener;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptErrorListener;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.css.sac.CSSException;
import org.w3c.css.sac.CSSParseException;
import org.w3c.css.sac.ErrorHandler;

import java.io.*;
import java.net.*;
import java.util.*;

class PageParser {
    private String pageURL;
    private String contentHtml;
    private Map<String, Integer> map1;

    PageParser(String url) {
        pageURL = url;
    }

    /**
     * Method for getting HTML source code from current page URL
     * using HtmlUnit framework
     */
    void getWebPageSource() throws IOException {
        WebClient webClient;
        webClient = new WebClient();


        webClient.setIncorrectnessListener((arg0, arg1) -> {

        });
        webClient.setCssErrorHandler(new ErrorHandler() {

            @Override
            public void warning(CSSParseException exception) throws CSSException {}

            @Override
            public void fatalError(CSSParseException exception) throws CSSException {}

            @Override
            public void error(CSSParseException exception) throws CSSException {}
        });
        webClient.setJavaScriptErrorListener(new JavaScriptErrorListener() {

            @Override
            public void timeoutError(HtmlPage arg0, long arg1, long arg2) {}

            @Override
            public void scriptException(HtmlPage arg0, ScriptException exception) {}

            @Override
            public void malformedScriptURL(HtmlPage arg0, String arg1, MalformedURLException exception) {}

            @Override
            public void loadScriptError(HtmlPage arg0, URL arg1, Exception exception) {}
        });
        webClient.setHTMLParserListener(new HTMLParserListener() {

            @Override
            public void error(String s, URL url, String s1, int i, int i1, String s2) {}

            @Override
            public void warning(String s, URL url, String s1, int i, int i1, String s2) {}
        });

        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setPrintContentOnFailingStatusCode(false);
        webClient.getOptions().setThrowExceptionOnScriptError(false);


        Page page;
        try {
            page = webClient.getPage(pageURL);
        } catch (MalformedURLException e) {
            page = webClient.getPage("http://" + pageURL);
        }
        WebResponse response = page.getWebResponse();
        contentHtml = response.getContentAsString();

    }

    void contentParse(){
        List<String> hrefLinksList;
        Set<String> set1;

        Document doc = Jsoup.parse(contentHtml);
        Elements links = doc.select("a[href]");

        hrefLinksList = new ArrayList<>();
        map1 = new HashMap<>();
        for (Element link : links) {
            if(link.attr("href") != null && link.attr("href").contains("http://")) {
                try {
                    hrefLinksList.add(getHostName(link.attr("href")));
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        }
        set1 = new HashSet<>(hrefLinksList);
        for (String mapElem: set1) {
            int occurrences = Collections.frequency(hrefLinksList, mapElem);
            map1.put(mapElem, occurrences);
        }
    }

    private static String getHostName(String url) throws URISyntaxException {
        URI uri = new URI(url);
        String hostname = uri.getHost();
        if (hostname != null) {
            return hostname.startsWith("www.") ? hostname.substring(4) : hostname;
        }
        return null;
    }

    void printResult(){
        for (Map.Entry entry : map1.entrySet()) {
            System.out.println("• " + entry.getKey() + " - " + entry.getValue());
        }
    }
}
