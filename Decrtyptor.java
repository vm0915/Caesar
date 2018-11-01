import javafx.beans.binding.MapExpression;

import java.util.*;

/**
 * Created by Raymond on 27.10.2018.
 */
public class Decrtyptor {
    private static HashMap<String, Double> fqTalbeVimBi = new HashMap<>();
    private static HashMap<String, Double> fqTableChapVimBi = new HashMap<>();
    private static HashMap<String, Double> fqTalbeVim = new HashMap<>();
    private static HashMap<String, Double> fqTableChapVim = new HashMap<>();
    private static String alphabet = Main.alphabet;
    private static String fullVimPath = Main.textForLearning;
    private static int numberOfBigramms = 30;

    //создает таблицы частотности биграмм и монограмм для ВиМ и шифротекста
    private static void prepareTables(String encryptedText){
        String textViM = Text.readText(fullVimPath);
        fqTalbeVimBi = Table.makeFrequencyTable(true, textViM);
        fqTalbeVim = Table.makeFrequencyTable(false, textViM);
        fqTableChapVimBi = Table.makeFrequencyTable(true, encryptedText);
        fqTableChapVim = Table.makeFrequencyTable(false, encryptedText);
    }
    //делает таблицы соответствия между таблицами ВиМ и шифротекста
    private static HashMap<String, String> processTables(boolean useBigrams){
        ////отсортировать fqTableChapterBi и сделать ТОП10
        Map.Entry<String, Double>[] allEntrys = new Map.Entry[fqTableChapVimBi.size()];
        int counter = 0;
        for(Map.Entry<String, Double> entry : fqTableChapVimBi.entrySet()){
            allEntrys[counter] = entry;
            counter++;
        }
        for(int i = 0; i < allEntrys.length; i++){
            for(int k = 0; k < allEntrys.length - 1; k++){
                if(allEntrys[k].getValue() < allEntrys[k+1].getValue()){
                    Map.Entry<String, Double> temp = allEntrys[k];
                    allEntrys[k] = allEntrys[k+1];
                    allEntrys[k+1] = temp;
                }
            }
        }
        HashMap<String, Double> fqTop10Bi = new HashMap<>();
        for(int i = 0; i < numberOfBigramms; i++) {
            fqTop10Bi.put(allEntrys[i].getKey(), allEntrys[i].getValue());
        }
        ////

        //сравнение таблиц fqTableVimBi и fqTop10Bi
        HashMap<String, String> accordingTableBiGramms = new HashMap<>();
        String usedGramms = "";
        for(Map.Entry<String, Double> entry : fqTop10Bi.entrySet()){
            double delta = 2;
            String key = null;
            for(Map.Entry<String, Double> entry1 : fqTalbeVimBi.entrySet()){
                if(!usedGramms.contains(entry1.getKey())){
                    if(Math.abs(entry.getValue() - entry1.getValue()) < delta ){
                        delta = Math.abs(entry.getValue() - entry1.getValue());
                        key = entry1.getKey();
                    }
                }
            }
            usedGramms+=key + " ";
            accordingTableBiGramms.put(entry.getKey(), key);
        }
        if(useBigrams) {
            System.out.println("Соответствие биграмм: \n" + accordingTableBiGramms);
        }
        //взять букву из шифробиграммы, для всех биграмм
        // {если буква в зашифрованной биграмме, то взять ее соответствие и положить в Map<буква соответсвтия, количество соответствий>}
        // найти в мап наибольшее value и если оно не равно 1 (или единственно), то положить для этой буквы это соответсвие в карту для коррекции расшифровки
        String checkedLetters = "";
        String alreadyInCorrectionTable = "";
        String removeItFormCorrectionTable = "";
        HashMap<String, String> correctionTable = new HashMap<>();
        for(HashMap.Entry<String, String> entry : accordingTableBiGramms.entrySet()){
            for(int i = 0; i < entry.getKey().length(); i++){
                Character cryptoLetter = entry.getKey().charAt(i);
                if(!checkedLetters.contains(cryptoLetter.toString())) {
                    HashMap<String, Integer> topAccordance = new HashMap<>();
                    for (HashMap.Entry<String, String> entry1 : accordingTableBiGramms.entrySet()) { ///заменить эту карту на ее дубликат?
                        if (entry1.getKey().contains(cryptoLetter.toString())) {
                            //найти в entry1 соответствие этой букве и положить в карту
                            //узнать индекс буквы в ключе, взять по этому индексу букву в value

                            Character accordanceCharacter = entry1.getValue().charAt(entry1.getKey().indexOf(cryptoLetter));
                            if (!topAccordance.containsKey(accordanceCharacter.toString())) {
                                topAccordance.put(accordanceCharacter.toString(), 1);
                            } else {
                                topAccordance.put(accordanceCharacter.toString(), topAccordance.get(accordanceCharacter.toString()) + 1);
                            }
                        }
                    }
                    checkedLetters += cryptoLetter;
                    //найти наибольшее value
                    Integer maxValue = Collections.max(topAccordance.values());
                    //считаем количество букв с макс значением вхождения в топ10
                    int counter1 = 0;
                    String decryptLetter = null;
                    for (Map.Entry<String, Integer> entry1 : topAccordance.entrySet()){
                        if(entry1.getValue() == maxValue){
                            counter1++;
                            decryptLetter = entry1.getKey();
                        }
                    }
                    if(counter1 == 1){
                        if(!alreadyInCorrectionTable.contains(decryptLetter)) {
                            correctionTable.put(cryptoLetter.toString(), decryptLetter);
                            alreadyInCorrectionTable += decryptLetter;
                        }
                        else{
                          removeItFormCorrectionTable += decryptLetter;
                        }
                    }
                    //делаем обратную таблицу и if(значение единственно) {записываем в табл коррекции} else {if(таблицы разного рамера) скидываем ключ}else
                    HashMap<Integer, String> reverseTopAccordance = new HashMap<>();
                    for(HashMap.Entry<String, Integer> entry1 : topAccordance.entrySet()){
                        reverseTopAccordance.put(entry1.getValue(), entry1.getKey());
                    }


                    if(useBigrams) {
                        System.out.println("Дешифровка буквы -" + cryptoLetter + "- :" + topAccordance);
                    }


                }
            }
        }
        HashMap<String, String> tempCopyCorrectionTable = new HashMap<>();
        for(HashMap.Entry<String,String> entry : correctionTable.entrySet()){
            tempCopyCorrectionTable.put(entry.getKey(), entry.getValue());
        }
        for(HashMap.Entry<String,String> entry : tempCopyCorrectionTable.entrySet()){
            for(int i = 0; i < removeItFormCorrectionTable.length(); i++) {
                if (entry.getValue().contains(Character.toString(removeItFormCorrectionTable.charAt(i)))){
                    correctionTable.remove(entry.getKey());
                }
            }
        }


        //создаем таблицу соответствия для одиночных букв
        HashMap<String, String> accordanceTableForDecryption = new HashMap<>();
        String usedKeys = "";
        for(Map.Entry<String, Double> entry : fqTableChapVim.entrySet()){
            double delta = 2;
            String key = null;
            for(Map.Entry<String, Double> entry1 : fqTalbeVim.entrySet()){
                if(!usedKeys.contains(entry1.getKey()))
                    if(Math.abs(entry.getValue() - entry1.getValue()) < delta ){
                        delta = Math.abs(entry.getValue() - entry1.getValue());
                        key = entry1.getKey();
                    }
            }
            usedKeys+=key;
            accordanceTableForDecryption.put(entry.getKey(), key);
        }
        if(useBigrams) {
            System.out.println("Таблица корректировки:" + correctionTable + correctionTable.size());
            System.out.println("Таблица дешифровки:    " + accordanceTableForDecryption + accordanceTableForDecryption.size());
            System.out.println("таблица дешифроки кор: " + createCorrectedDecryptionTable(correctionTable) + createCorrectedDecryptionTable(correctionTable).size());
            //System.out.println("Результат CCT          " + createCorrectedDecryptionTable(correctionTable));
            //корректируем таблицу соответствия и возращаем
            return createCorrectedDecryptionTable(correctionTable);
        }
        else{
            System.out.println("Таблица дешифровки:    " + createUnCorrectedDecryptionTable() + createUnCorrectedDecryptionTable().size());
            return createUnCorrectedDecryptionTable();
        }
    }

    public static HashMap<String, String> correctTables(HashMap<String, String> correctionTable, HashMap<String, String> accordanceTableForDecryption){
        //for(HashMap.Entry<>)

        for(HashMap.Entry<String, String> entry : correctionTable.entrySet()){
            accordanceTableForDecryption.replace(entry.getKey(),entry.getValue());
        }
        return accordanceTableForDecryption;
    }

    public static String decryptText(String encryptedText, boolean useBigramms){
        prepareTables(encryptedText);
        HashMap<String, String> accordanceTableForDecryption = processTables(useBigramms);

        //дешифрируем текст
        StringBuilder decryptedText = new StringBuilder();
        for(int i = 0; i < encryptedText.length(); i++){
            if(alphabet.contains(Character.toString(Character.toLowerCase(encryptedText.charAt(i))))) {
                if (Character.isUpperCase(encryptedText.charAt(i))) {
                    Character character = Character.toLowerCase(encryptedText.charAt(i));
                    String decrLetter = (accordanceTableForDecryption.get(character.toString())).toUpperCase();
                    decryptedText.append(decrLetter);
                } else {
                    decryptedText.append(accordanceTableForDecryption.get(Character.toString(encryptedText.charAt(i))));
                }
            }
            else {
                decryptedText.append(encryptedText.charAt(i));
            }
        }
        return decryptedText.toString();
    }

    public static HashMap<String,String> createCorrectedDecryptionTable(HashMap<String, String> correctionTable){
        String keyInCT = "";//String для correct.key - зашифрованные
        String valueInCT = "";//String для correct.value - дешифрованные
        String usedKeys = "";
        HashMap<String,String> accordanceTableForDecryption = new HashMap<>();

        //заполнение строк буквами из correctionTable
        for (HashMap.Entry<String,String> entry : correctionTable.entrySet()){
            keyInCT += entry.getKey();
            valueInCT += entry.getValue();
        }


        for(Map.Entry<String, Double> entry : fqTableChapVim.entrySet()) {
            if (!keyInCT.contains(entry.getKey())) {
                double delta = 2;
                String key = null; //использованные буквы
                for (Map.Entry<String, Double> entry1 : fqTalbeVim.entrySet()) {
                    if (!valueInCT.contains(entry1.getKey())) {
                        if (!usedKeys.contains(entry1.getKey()))
                            if (Math.abs(entry.getValue() - entry1.getValue()) < delta) {
                                delta = Math.abs(entry.getValue() - entry1.getValue());
                                key = entry1.getKey();
                            }
                    }

                }
                usedKeys += key;
                accordanceTableForDecryption.put(entry.getKey(), key);
            }
        }
        //добавить таблицу кореектировки в конечную
        for(HashMap.Entry<String,String> entry : correctionTable.entrySet()){
            accordanceTableForDecryption.put(entry.getKey(), entry.getValue());
        }

        return accordanceTableForDecryption;
    }

    public static HashMap<String,String> createUnCorrectedDecryptionTable(){
        String usedKeys = "";
        HashMap<String,String> accordanceTableForDecryption = new HashMap<>();

        for(Map.Entry<String, Double> entry : fqTableChapVim.entrySet()) {
                double delta = 2;
                String key = null; //использованные буквы
                for (Map.Entry<String, Double> entry1 : fqTalbeVim.entrySet()) {
                        if (!usedKeys.contains(entry1.getKey()))
                            if (Math.abs(entry.getValue() - entry1.getValue()) < delta) {
                                delta = Math.abs(entry.getValue() - entry1.getValue());
                                key = entry1.getKey();
                            }

                }
                usedKeys += key;
                accordanceTableForDecryption.put(entry.getKey(), key);

        }
        return accordanceTableForDecryption;
    }

}
