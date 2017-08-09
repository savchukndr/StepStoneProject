import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

class PageParser {
    private String pageURL;
    private String content;

    PageParser(String url) {
        pageURL = url;
    }

    /**
     * Method for getting HTML source code from current page URL
     * using HtmlUnit framework
     */
    void getWebPageSource() throws IOException {
        WebClient webClient = new WebClient();
        webClient.getOptions().setJavaScriptEnabled(true);
        Page page;
        try {
            page = webClient.getPage(pageURL);
        } catch (MalformedURLException e) {
            page = webClient.getPage("http://" + pageURL);
        }
        WebResponse response = page.getWebResponse();
        content = response.getContentAsString();
        System.out.println(content);
        PageParser.writeIntoFile("C:\\temp\\1111.txt", content);
    }

    void fileParse(){
        Document doc = Jsoup.parse(content);
        Elements links = doc.select("a[href]");
        List<String> lst = new ArrayList<>();
        List<String> lst2 = new ArrayList<>();
        Set<String> set1;
        Map<String, Integer> map1 = new HashMap<>();
        for (Element link : links) {
            if(link.attr("href") != null && link.attr("href").contains("http://")) {
                lst.add(link.attr("href"));
                try {
                    lst2.add(getHostName(link.attr("href")));
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        }
        set1 = new HashSet<>(lst2);
        for (String x: set1) {
            int occurrences = Collections.frequency(lst2, x);
            map1.put(x, occurrences);
        }
        System.out.println(lst);
        System.out.println("lst size: " + lst.size());
        System.out.println(lst2);
        System.out.println(set1);
        System.out.println(map1);
    }

    private static String getHostName(String url) throws URISyntaxException {
        URI uri = new URI(url);
        String hostname = uri.getHost();
        // to provide faultproof result, check if not null then return only hostname, without www.
        if (hostname != null) {
            return hostname.startsWith("www.") ? hostname.substring(4) : hostname;
        }
        return null;
    }

    /**
     * Method for writing HTML source code int text file
     */
    private static void writeIntoFile(String sourceHtmlFileName, String st) {
        BufferedWriter bw = null;
        FileWriter fw = null;
        try {
            fw = new FileWriter(sourceHtmlFileName);

            bw = new BufferedWriter(fw);
            bw.write(st);
        } catch (FileNotFoundException e) {
            System.out.print("File " + sourceHtmlFileName + " not found!");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bw != null)
                    bw.close();
                if (fw != null)
                    fw.close();
            } catch (IOException ex) {
                System.out.print("Problem with write into file: " + ex);
            }
        }
    }
}
