//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Scanner;

public class TextIterator implements Iterator<String> {
    private LinkedList<String> queue = new LinkedList();

    public TextIterator() {
    }

    public boolean hasNext() {
        return !this.queue.isEmpty();
    }

    public String next() {
            return (String) this.queue.remove();
    }

    public String peekNext() {
        return (String) this.queue.peek();
    }

    public void remove() {
    }

    public void readTextFile(String var1) {
        File var2 = new File(var1);

        try {
            Locale loc = new Locale("es", "ES");
            Scanner var3 = new Scanner(new FileInputStream(var1), "UTF-8");
            var3.useLocale(loc);
            while(var3.hasNext()) {
                String var4 = var3.nextLine();

                this.process_words(var4);
            }

        } catch (FileNotFoundException var5) {
            System.out.println(var5);
        }
    }

    public void readTextString(String var1) {
        try {
            Locale loc = new Locale("es", "ES");
            Scanner var3 = new Scanner(var1);
            var3.useLocale(loc);
            while(var3.hasNext()) {
                String var4 = var3.nextLine();

                this.process_words(var4);
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void process_words(String var1) {
        boolean var2 = false;
        int var3 = 0;

        while(!var2) {
            //Cycle through if we keep encountering nonletters (Added: treat - as a letter)
            while(var3 + 1 <= var1.length() && (!Character.isLetter(var1.charAt(var3)) && var1.charAt(var3) != '-' ) ) {
                ++var3;
            }

            if(var3 == var1.length()) {
                return;
            }

            int var4;

            for(var4 = var3++; this.inword(var1, var3); ++var3) {
                ;
            }

            int var5 = var3 - 1;

            String var7 = this.wordCopy(var1, var4, var5);

            //There's a case where it thinks - is a word.
            if(!var7.equals("-")) {
                this.queue.add(var7);
            }
        }

    }

    //return true/false if var2 is in the word, treating "-" as a letter.
    private boolean inword(String var1, int var2) {
        return var2 >= var1.length()?false:((Character.isLetter(var1.charAt(var2)) || var1.charAt(var2) == '-')?true:var1.charAt(var2) == 39 && var2 + 1 < var1.length() && Character.isLetter(var1.charAt(var2 + 1)));
    }

    private String wordCopy(String var1, int var2, int var3) {
        StringBuffer var4 = new StringBuffer();
        var4.append(var1.substring(var2, var3 + 1));

        for(int var5 = 0; var5 < var4.length(); ++var5) {
            var4.setCharAt(var5, Character.toLowerCase(var4.charAt(var5)));
        }

        return var4.toString();
    }
}
