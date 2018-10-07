import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Scanner;

public class Trie {
    char c;
    Trie[] children;
    boolean endOfWord;

    public Trie(){
        this.c = 0;
        // 26 children, one for each letter in the alphabet
        this.children = new Trie[26];
        this.endOfWord = false;
    }

    public void add(String s){
        // If string is empty the word is formed
        if(s.isEmpty()){
            this.endOfWord = true;
            return;
        }

        // 'a' has the value of 97 (A-A = 0) (B-A = 1)
        int index = s.charAt(0) - 'a';

        if (this.children[index] == null){
            this.children[index] = new Trie();
            this.children[index].c = s.charAt(0);
        }

        this.children[index].add(s.substring(1));
    }

    public boolean isWord(String s){
        /*
         If s is empty, but endOfWord is false, the word is not in the trie
         If s is empty and endOfWord is true, the work exists in the trie
        */
        if(s.isEmpty()){
            return this.endOfWord;
        }

        int index = s.charAt(0) - 'a';

        if (this.children[index] == null){
            return false;
        }

        // Traversing recursivly through the word
        return this.children[index].isWord(s.substring(1));
    }

    static String result = "";
    public String getSubStrings(String word, String currentString){
        // Adds the current letter to the current string if its not the first (0)
        if (this.c != 0) {
            currentString += this.c;
        }
        // Adds all alternative paths in a array
        ArrayList<Trie> trieChildren = new ArrayList<>();
        for (Trie trie : this.children) {
            if (trie != null) {
                trieChildren.add(trie);
            }
        }
        /*
         If there are 1 path just continue adding letters from that path
         If there are more than 1 path, add children from each path until end of word
         If there are no paths, add the current string (all letters added from a path) to the result
          */
        if (trieChildren.size() == 1) {
            for (Trie trie : trieChildren) {
                trie.getSubStrings(Character.toString(trie.c), currentString);
            }
        } else if (trieChildren.size() > 1) {
            result += "("+currentString;
            currentString = "";
            for (Trie trie : trieChildren) {
                trie.getSubStrings(Character.toString(trie.c), currentString);
            }
            result += ")";
        } else if (trieChildren.size() == 0) {
            result += "("+currentString+")";
        }
        return result;
    }

    public static void main(String[] args) throws FileNotFoundException {
        // Printer for printing to file
        PrintWriter writer = null;
        try {
            writer = new PrintWriter("outputFile.txt", "UTF-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Trie trie = new Trie();
        File file = new File("test2.txt");
        try {
            Scanner scanner = new Scanner(file);
            int N = scanner.nextInt();
            int counter = 0;
            while (scanner.hasNextLine()) {
                String string = scanner.nextLine();
                String[] C = string.split("[\\n]+");
                // Printing each word to file
                for (String word : C){
                    if (counter<=N && counter!=0){
                        trie.add(word);
                        writer.println(word + ": ");
                        String currentString = "";
                        trie.getSubStrings(word, currentString);
                        System.out.println(result);
                        writer.println(result);
                    } else if(counter>N) {
                        if (trie.isWord(word) == true){
                            writer.println(word + " YES");
                        } else {
                            writer.println(word + " NO");
                        }
                    }
                    result = "";
                    counter++;
                }
            }
        }
        catch(FileNotFoundException e) {
            e.printStackTrace();
        }
        writer.close();
    }
}
