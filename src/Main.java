import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        PageParser page = new PageParser("http://wawalove.pl");
        try {
            page.getWebPageSource();
        } catch (IOException e) {
            System.out.println("Problem with downloading page: " + e);
        }
        page.contentParse();
        page.printResult();
    }
}
