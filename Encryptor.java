import java.util.HashMap;

/**
 * Created by Raymond on 26.10.2018.
 */
public class Encryptor {
    private static String alphabet = Main.alphabet;

    public static String encryptText(String text, String key){
        key = key.toLowerCase();
        StringBuilder encryptedText = new StringBuilder();
        HashMap<Character, Character> accordanceTable;
        accordanceTable = Table.makeAccordanceTable(key);

        for(int i = 0; i < text.length(); i++){
            if(alphabet.contains(Character.toString(Character.toLowerCase(text.charAt(i))))) {
                if (Character.isUpperCase(text.charAt(i))) {
                    encryptedText.append(Character.toUpperCase(accordanceTable.get(Character.toLowerCase(text.charAt(i)))));
                } else {
                    encryptedText.append(accordanceTable.get(text.charAt(i)));
                }
            }
            else {
                encryptedText.append(text.charAt(i));
            }
        }
        return encryptedText.toString();
    }
}
