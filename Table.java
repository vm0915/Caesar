import java.util.HashMap;

/**
 * Created by Raymond on 27.10.2018.
 */
public class Table {
    private static String alphabet = Main.alphabet;

    //возвращает таблицу буква = частота или биграмма = частота
    public static HashMap<String, Double> makeFrequencyTable(boolean needBigrams, String text){
        HashMap<String, Double> frequencyTable = new HashMap<>();

        if(!needBigrams) {
            for (Character c : alphabet.toCharArray()) {
                c = Character.toLowerCase(c);
                frequencyTable.put(c.toString(), (double) 0);
            }
            for (Character c : text.toCharArray()) {
                c = Character.toLowerCase(c);
                if (alphabet.contains(Character.toString(c))) {
                    frequencyTable.replace(c.toString(), frequencyTable.get(c.toString()) + 1);
                }
            }
        }
        //для биграмм
        //заполнение биграммами таблицы
        if(needBigrams) {
            HashMap<String, Double> tempMap = new HashMap<>();
            for (Character c : alphabet.toCharArray()) {
                c = Character.toLowerCase(c);
                //frequencyTable.put(c.toString(), (double) 0);
                tempMap.put(c.toString(), (double) 0);
            }
            for(HashMap.Entry<String, Double> entry : tempMap.entrySet()){
                for(Character c : alphabet.toCharArray()){
                    frequencyTable.put(entry.getKey() + c.toString(),  (double) 0 );
                }
                //tempMap.remove(entry.getKey());
            }
            //тут должен быть поиск самих биграмм в тексте
            char[] textArr = text.toCharArray();
            StringBuilder strb = new StringBuilder();
            for(int i = 0; i < textArr.length - 1; i++){
                if(alphabet.contains(Character.toString(Character.toLowerCase(textArr[i+1])))){
                    strb.append(textArr[i]);
                    strb.append(textArr[i+1]);
                    for(HashMap.Entry<String, Double> entry : frequencyTable.entrySet()){
                        if(entry.getKey().equals(strb.toString())){
                            frequencyTable.replace(entry.getKey(), entry.getValue() + 1);
                            break;
                        }
                    }
                    strb.delete(0,strb.length());
                }
            }
        }

        //рассчет частоты в таблицу
        int numberOfAllLettersInText = 0;
        for(HashMap.Entry<String, Double> entry: frequencyTable.entrySet()){
            numberOfAllLettersInText += entry.getValue();
        }
        for(HashMap.Entry<String, Double> entry: frequencyTable.entrySet()){
            if(numberOfAllLettersInText != 0) {
                frequencyTable.replace(entry.getKey(), entry.getValue() / numberOfAllLettersInText);
            }
        }
        return frequencyTable;
    }

    //возвращает таблицу соответствий для шифрования БУКВА - ШИФРОБУКВА
    public static HashMap<Character, Character> makeAccordanceTable(String key){
        HashMap<Character, Character> accordanceTable = new HashMap<>();
        int k = 0;
        for(int i =0; i < key.length(); i++){
            accordanceTable.put(alphabet.charAt(i),key.charAt(i));
        }
        for(int i = key.length(); i < alphabet.length(); i++){
            do {
                if (!key.contains(String.valueOf(alphabet.charAt(k)))) {
                    accordanceTable.put(alphabet.charAt(i), alphabet.charAt(k));
                    k++;
                } else {
                    k++;
                }
            }
            while (accordanceTable.get(alphabet.charAt(i)) == null);
        }
        return accordanceTable;
    }
}
