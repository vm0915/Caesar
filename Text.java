import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * Created by Raymond on 27.10.2018.
 */
public class Text {
    // возвращает текст из файла
    public static String readText(final String fileName)  {
        final StringBuilder stringBuilder = new StringBuilder();
        final BufferedReader reader = new BufferedReader(new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName), Charset.forName("UTF-8")));
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
        }catch (Exception e) {System.exit(2);}
        return stringBuilder.toString();
    }
}
