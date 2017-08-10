import java.io.IOException;

/**
 * <h1>StepStone project</h1>
 * This program is parsing "wawalove.pl"
 * web page and getting amount of "href"
 * attribute in "a" tag.
 *
 * @author  Andrii Savchuk
 * @version 1.0
 * @since   2017-08-09
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
        PageParser pagePar = new PageParser("http://wawalove.pl");
        try {
            pagePar.getWebPageSource();
        } catch (IOException exception) {
            System.out.println("Problem with downloading page: " + exception);
        }
        pagePar.printResult();
    }
}
