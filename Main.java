
/**
 * Created by Raymond on 26.10.2018.
 */
public class Main {
    private static String text30Symbols = "Табличку можно определить как физически прочный, надёжный носитель письменной информации, относительно удобный в повседневном использовании и транспортировке";
    private static String key = "скользящий";
    public static String alphabet = "абвгдеёжзийклмнопрстуфхцчшщъыьэюя";
    public static String textForLearning = "vim.txt";

    public static void main(String[] args){
        String text = Text.readText(textForLearning);
        String textOfChapter = Text.readText("vimChapter.txt");

        System.out.println(Encryptor.encryptText(text30Symbols, key)); //зашифровать текст
        System.out.println(Decipher.decipherText(Encryptor.encryptText(text30Symbols, key), key)); //расшифровать
        //System.out.print(Table.makeFrequencyTable(false,text)); //вывести таблицу частотности для грамм
        //System.out.print(Table.makeFrequencyTable(true,text)); //вывести таблицу частотности для биграмм
        System.out.println(Decrtyptor.decryptText(Encryptor.encryptText(textOfChapter, key), false)); //дешифровать главу без биграмм
        //System.out.println(Decrtyptor.decryptText(Encryptor.encryptText(textOfChapter, key), true)); //дешифровать главу с биграммами
    }
}
