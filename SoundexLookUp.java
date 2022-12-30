import java.io.*;
import java.util.*;
/**
 * A class to make the Soundex for top 15000 surnames and to lookup names  
 * entered by the user
 *
 * @author Aaron Johnson
 * @version 5/15/22
 */
public class SoundexLookUp{
    public static void main(String [] args){
        Scanner keyboard = new Scanner(System.in); 
        System.out.println("\f"); 
        File nameFile = new File("name15k.txt"); 
        TreeMap<String,ArrayList<String>> names = NameList(nameFile); // creates a treemap of the names and soundex keys
        System.out.println("Please enter a surname to look up: "); 
        String search = keyboard.next(); 
        String keyToSearch = keyMaker(search); // makes key of the name for searching
        if (names.get(keyToSearch) != null){ // makes sure the key exists in the treemap
            Object[] keys = names.keySet().toArray(); // makes an array of the keys
            for (int i = 0; i<keys.length; i++){ 
                if (((String)keys[i]).equals(keyToSearch)){ // finds the key in the key array
                    int location = 0; // initialize the index of the key in the key array
                    if (i+5>keys.length){ // checks if the index of keyToSearch+5 is over the end
                        location = i-5; // starts the location 5 before the keyToSearch
                        for (int k = location; k<keys.length; k++) // goes from the start location till the end of the array
                            System.out.println(keys[k] + " - " + names.get(keys[k])); 
                    }else if (i<5){// checks if the index of keyToSearch is less than 5 away from the start(index of 0)
                        for (int k = 0; k<i+6; k++) // starts at 0 and goes till 5 after the keyToSearch
                            System.out.println(keys[location+k] + " - " + names.get(keys[location+k]));
                    } else { // otherwise it is in the middle of the array
                        location = i-5; // starts 5 before the keyToSearch index
                        for (int k = 0; k<11; k++) // goes till 5 after the keyToSearch
                            System.out.println(keys[location+k] + " - " + names.get(keys[location+k]));
                    } // end printing names and keys
                } // ends finding the key in the key array
            } // end traversal of key array
        } else { // keyToSearch does not exist in the names hastable keys
            System.out.println("Name does not exist in list"); 
        }
        boolean done = false; // to check if the user wants to look uo another name
        while (!done){
            System.out.println("Would you like to look up another surname? (yes or no)");
            String redo = keyboard.next(); // takes input 
            if (redo.equalsIgnoreCase("yes")){
                done = true;
                main(new String[] {}); // calls main method again if user wants to look up another name
            } else if (redo.equalsIgnoreCase("no")){
                System.out.println("Ok, Goodbye");
                done = true; // ends program
            } else { // must enter yes or no to continue
                System.out.println("Not a valid answer");
            }
        } // end of while
    }// end main
    
    public static TreeMap<String,ArrayList<String>> NameList(File list){ // for making the names hashtable with soundex keys and names that match them
        boolean succeeded = false; // checking if the file is read
        Scanner inputFile = null; // scanner for file to be put in later
        Hashtable<String,ArrayList<String>> names = new Hashtable<String,ArrayList<String>>(); // hashtable that will take the names and soundex keys
        while (!succeeded){
            try{     
                inputFile = new Scanner(list);// takes the file and makes a scanner for it
                inputFile.nextLine(); // skip the first line in the file 
                while(inputFile.hasNextLine()){ // goes till the end of the file
                    String name = inputFile.next(); // takes the name from each line in file
                    ArrayList<String> nameV = new ArrayList<String>(); // creates a list for the names 
                    String key = keyMaker(name); // creates a key for the name
                    if (names.get(key) != null){ // checks if the key already exists 
                        names.get(key).add(name); // adds name to the name array for a key that already exists
                    } else { // key does not already exist
                        nameV.add(name); // adds new name to a new list
                        names.put(key, nameV); // adds name list and key to a new spot in the hashtable
                    } // end adding name and key to hashtable
                    inputFile.nextLine(); // goes to next line in file
                } // end of the file
                succeeded = true; // file was read
            }catch (FileNotFoundException e){ // if file was not found
                System.out.print("File Not Found: " + list);
                succeeded = false; // file was not read
            }
        }//file was read
        TreeMap<String,ArrayList<String>> n = new TreeMap<String,ArrayList<String>>(names); // converst hashtable to treemap to sort the keys
        return n; // returns the hashtable that was made
    } // end NameList maker
    
    public static String keyMaker(String name){ // computes soundex key from a name passed through this method
       char[] word = name.toUpperCase().toCharArray(); // makes an array of char of the word that is given
       char firstLetter = word[0];// takes the first letter of the word as a char to keep as the first character in the soundex key
       for (int i = 0; i < word.length; i++) { // traveres the word
          switch (word[i]) { // checks each letter and gives it a number
             case 'B':case 'F':case 'P':case 'V': {word[i] = '1';break;}
             case 'C':case 'G':case 'J':case 'K':case 'Q':
             case 'S':case 'X':case 'Z': {word[i] = '2';break;}
             case 'D':case 'T': {word[i] = '3';break;}
             case 'L': {word[i] = '4';break;}
             case 'M':case 'N': {word[i] = '5';break;}
             case 'R': {word[i] = '6';break;}
             default: {word[i] = '0';break;} 
          }
       } // end traversal
       String soundex = "" + firstLetter; // makes a string for the soundex key starting with the first letter
       for (int i = 1; i < word.length; i++){ // traverses the char array of the word again because they have been changed to numbers
         if (word[i] != word[i - 1] && word[i] != '0'){ //checks for repetative numbers and skips them along with zeros(zeros are like 7's)
           soundex += word[i]; // adds non repetative numbers to the soundex key string
         } // end check
       }// end traversal
       soundex = soundex + "0000"; // adds zeros to fill space
       return soundex.substring(0, 4); // makes the string length 4 and returns the soundex key
    } // end key maker
} // end class
