import java.io.IOException;

public class Main {
    private static String fileName = "C:\\temp\\1111.txt";

    public static void main(String[] args) {
        PageParser page2 = new PageParser("wawalove.pl/");
        try {
            page2.getWebPageSource();
        } catch (IOException e) {
            System.out.println("Problem with downloading page: " + e);
        }
        page2.writeIntoFile(fileName);
    }
}
