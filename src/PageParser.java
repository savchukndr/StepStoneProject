import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.ScriptException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HTMLParserListener;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptErrorListener;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.css.sac.CSSException;
import org.w3c.css.sac.CSSParseException;
import org.w3c.css.sac.ErrorHandler;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;

/**
 * The PageParser class consisted of methods for
 * parsing web site and getting links amount from
 * "href" attribute in "a" tag.
 */
class PageParser {
    private String mPageUrl; /* web page url */
    private String mContentHtml; /* html content */
    private Map<String, Integer> mHrefLinksMap; /* parsed links map */

    /**
     * Constructor for class PageParser.
     *
     * @param url Gets web site URL.
     */
    PageParser(String url) {
        mPageUrl = url;
    }

    /**
     * Method for getting host name from the
     * given URL.
     *
     * @param url Given URL.
     * @return Host name.
     * @throws URISyntaxException Throws exception if
     *                            there is a problem in
     *                            in URI syntax.
     * @see URISyntaxException
     */
    private String getHostName(String url) throws URISyntaxException {
        URI uri = new URI(url);
        String hostname = uri.getHost();
        if (hostname != null) {
            return hostname.startsWith("www.") ? hostname.substring(4) : hostname;
        }
        return null;
    }

    /**
     * Method for getting HTML source code from
     * current page URL using HtmlUnit framework.
     *
     * @return Nothing.
     * @throws IOException Throws exception if
     *                     if there are problems
     *                     with downloading pages.
     * @see IOException
     */
    void getWebPageSource() throws IOException {
        WebClient webClient;
        webClient = new WebClient();
        ignoreException(webClient);
        Page page;
        try {
            page = webClient.getPage(mPageUrl);
        } catch (MalformedURLException exception) {
            page = webClient.getPage("http://" + mPageUrl);
        }
        WebResponse response = page.getWebResponse();
        mContentHtml = response.getContentAsString();
    }

    /**
     * Method for getting HTML href links from
     * "a" tag.
     *
     * @return Nothing
     */
    private void contentParse() {
        Document doc;
        List<String> hrefLinksList;
        Set<String> hrefUniqueLinksSet;
        try {
            doc = Jsoup.parse(mContentHtml);
            Elements links = doc.select("a[href]"); /* get links from "href" attribute in a "a" teg */

            hrefLinksList = new ArrayList<>();
            mHrefLinksMap = new HashMap<>();
            for (Element link : links) {
                if (link.attr("href") != null && link.attr("href").contains("http://")) {
                    try {
                        hrefLinksList.add(getHostName(link.attr("href"))); /* add links into hrefLinksList */
                    } catch (URISyntaxException exception) {
                        System.out.println("URI Syntax exception: " + exception);
                    }
                }
            }
            hrefUniqueLinksSet = new HashSet<>(hrefLinksList); /* get a set of unique links */
            for (String mapElem : hrefUniqueLinksSet) {
                int occurrences = Collections.frequency(hrefLinksList, mapElem);
                mHrefLinksMap.put(mapElem, occurrences); /* mapping links and their amount */
            }
        } catch (IllegalArgumentException exception) {
            System.out.println("String cannot be null.");
        }
    }

    /**
     * Method for printing formatted output.
     *
     * @return Nothing
     */
    void printResult() {
        contentParse(); /* start parsing html content*/
        if (mHrefLinksMap != null) {
            for (Map.Entry entry : mHrefLinksMap.entrySet()) {
                System.out.println("• " + entry.getKey() + " - " + entry.getValue());
            }
        }
    }

    /**
     * Method for ignoring unnecessary exceptions
     * and warnings while HtmlUnit is executing.
     *
     * @param webClient All exceptions are ignored
     *                  for this web client.
     * @return Nothing.
     */
    private void ignoreException(WebClient webClient) {

        /* HtmlUnit exception and warning ignore code */
        webClient.setIncorrectnessListener((arg0, arg1) -> {
        });
        webClient.setCssErrorHandler(new ErrorHandler() {

            @Override
            public void warning(CSSParseException exception) throws CSSException {
                // Warning is ignored because of
                // not overload user with not
                // necessary warning information.
            }

            @Override
            public void fatalError(CSSParseException exception) throws CSSException {
                // Fatal error is ignored because of
                // not overload user with not
                // necessary exception information.
            }

            @Override
            public void error(CSSParseException exception) throws CSSException {
                // Fatal error is ignored because of
                // not overload user with not
                // necessary exception information.
            }
        });
        webClient.setJavaScriptErrorListener(new JavaScriptErrorListener() {

            @Override
            public void timeoutError(HtmlPage arg0, long arg1, long arg2) {
                // Timeout error is ignored because of
                // not overload user with not
                // necessary exception information.
            }

            @Override
            public void scriptException(HtmlPage arg0, ScriptException exception) {
                // Script Exception is ignored because of
                // not overload user with not
                // necessary exception information.
            }

            @Override
            public void malformedScriptURL(HtmlPage arg0, String arg1, MalformedURLException exception) {
                // Exception is ignored because of
                // not overload user with not
                // necessary exception information.
            }

            @Override
            public void loadScriptError(HtmlPage arg0, URL arg1, Exception exception) {
                // Exception is ignored because of
                // not overload user with not
                // necessary exception information.
            }
        });
        webClient.setHTMLParserListener(new HTMLParserListener() {

            @Override
            public void error(String s, URL url, String s1, int i, int i1, String s2) {
                // Error is ignored because of
                // not overload user with not
                // necessary exception information.
            }

            @Override
            public void warning(String s, URL url, String s1, int i, int i1, String s2) {
                // Warning is ignored because of
                // not overload user with not
                // necessary warning information.
            }
        });
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setPrintContentOnFailingStatusCode(false);
        webClient.getOptions().setThrowExceptionOnScriptError(false);

        /* HtmlUnit logs turning off code */
        LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
        java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);
        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit.javascript.StrictErrorReporter").setLevel(Level.OFF);
        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit.javascript.host.ActiveXObject").setLevel(Level.OFF);
        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDocument").setLevel(Level.OFF);
        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit.html.HtmlScript").setLevel(Level.OFF);
        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit.javascript.host.WindowProxy").setLevel(Level.OFF);
        java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF);
        java.util.logging.Logger.getLogger("org.apache").setLevel(Level.OFF);
    }
}
