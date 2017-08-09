import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.zip.GZIPInputStream;

class PageParser {
    private String pageURL;

    PageParser(String url) {
        this.pageURL = url;
    }

    String getWebPabeSource() throws IOException {
        URL url;
        try {
            url = new URL(pageURL);
        } catch (MalformedURLException e) {
            url = new URL("http://" + pageURL);
        }
        URLConnection urlCon = url.openConnection();
        BufferedReader in = null;

        if (urlCon.getHeaderField("Content-Encoding") != null
                && urlCon.getHeaderField("Content-Encoding").equals("gzip")) {
            in = new BufferedReader(new InputStreamReader(new GZIPInputStream(
                    urlCon.getInputStream())));
        } else {
            in = new BufferedReader(new InputStreamReader(
                    urlCon.getInputStream()));
        }

        String inputLine;
        StringBuilder sb = new StringBuilder();

        while ((inputLine = in.readLine()) != null)
            sb.append(inputLine);
        in.close();

        return sb.toString();
    }
}
