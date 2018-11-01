import java.util.HashMap;

/**
 * Created by Raymond on 27.10.2018.
 */
public class Decipher {
    private static String alphabet = Main.alphabet;

    public static String decipherText(String encryptedText, String key){
        key = key.toLowerCase();
        StringBuilder decryptedText = new StringBuilder();
        HashMap<Character, Character> accordanceTable;
        accordanceTable = Table.makeAccordanceTable(key);

        HashMap<Character, Character> reversedAccordanceTable = new HashMap<>();
        for(HashMap.Entry<Character, Character> entry : accordanceTable.entrySet()) {
            reversedAccordanceTable.put(entry.getValue(), entry.getKey());
        }
        accordanceTable = reversedAccordanceTable;
        for(int i = 0; i < encryptedText.length(); i++){
            if(alphabet.contains(Character.toString(Character.toLowerCase(encryptedText.charAt(i))))) {
                if (Character.isUpperCase(encryptedText.charAt(i))) {
                    decryptedText.append(Character.toUpperCase(accordanceTable.get(Character.toLowerCase(encryptedText.charAt(i)))));
                } else {
                    decryptedText.append(accordanceTable.get(encryptedText.charAt(i)));
                }
            }
            else {
                decryptedText.append(encryptedText.charAt(i));
            }
        }

        return decryptedText.toString();
    }
}
