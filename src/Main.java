import java.io.IOException;

/**
 * <h1>StepStone project</h1>
 * This program is parsing "wawalove.pl"
 * web page and getting amount of "href"
 * attribute in "a" tag.
 *
 * @author Andrii Savchuk
 * @version 1.0
 * @since 2017-08-09
 */

public class Main {

    /**
     * This is the main method which makes use of
     * getWebPageSource() and printResult() methods.
     *
     * @param args Unused.
     * @return Nothing.
     * @see IOException
     */
    public static void main(String[] args) {
        String webPage = "http://wawalove.pl"; /* input here web page link */
        PageParser pagePar = new PageParser(webPage);
        try {
            System.out.println("Start parsing web page: " + webPage + ".");
            pagePar.getWebPageSource(); /* start web page parsing */
            System.out.println("Web page " + webPage + " has already parsed.\n");
        } catch (IOException exception) {
            System.out.println("Problem with downloading page: " + exception);
        }
        System.out.println("Results:");
        pagePar.printResult(); /* printing results */
    }
}
