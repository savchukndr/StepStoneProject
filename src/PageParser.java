import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
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

        PageParser.writeIntoFile("C:\\temp\\1111.txt", content);
    }

    void fileParse(){
        String[] st = content.split("\\n");
        System.out.println(st.length);
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
