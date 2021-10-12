

/**
 * COMP 2140   		SECTION A01
 * INSTRUCTOR   	Cuneyt Akcora
 * Assignment       4
 * @author			Saif Mahmud
 * @id				7808507
 * @version      	03/23/2020
 *
 * PURPOSE: Practicing Usage of HashTable to spellcheck a doc.txt file with a dictionary.txt
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JFileChooser;

public class A4MahmudSaif {

	public static void main(String[] args) {
		Scanner dict = loadDictionary();
		Scanner doc = loadDocument();
		
		spellChecker(dict, doc);
		
		System.out.println();
		System.out.println("//------------END OF PROGRAM--------------//");
	} //main
	/**
	 * PURPOSE : Checks a document file with a dictionary file for spelling errors
	 * @param dict Takes a Scanner pointer of the dictionary file
	 * @param doc Takes a Scanner pointer of the document file
	 */
	private static void spellChecker(Scanner dict, Scanner doc) {
		
		int docTableSize = 94321;
		int errorTableSize = 2729;
		
		Table dictTable = new Table(docTableSize);
		Table errorTable = new Table(errorTableSize);
		
		dictTable.populateTable(dict);
		dictTable.crossCheck(doc, errorTable);
	}
	/**
	 * PURPOSE: Promts to select a dictionary of type .txt file and
	 * @return the Scanner pointer of the dictionary file.
	 */
	private static Scanner loadDictionary() 
	{
		Scanner dict = null ;
		JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(new File("."));
		fc.setDialogTitle("Load Dictionary File of type *.txt)");
		int selected = fc.showOpenDialog(fc);
		if(selected == JFileChooser.APPROVE_OPTION)
		{
			File f = fc.getSelectedFile();	
			
			try {
				dict = new Scanner(new BufferedReader(new FileReader(f)));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("No Dictionary File Selected");
			System.exit(0);
		}
		return dict;
	}
	/**
	 * PURPOSE: Promts to select a document of type .txt file and
	 * @return the Scanner pointer of the document file.
	 */
	private static Scanner loadDocument()
	{
		Scanner doc = null ;
		JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(new File("."));
		fc.setDialogTitle("Load Document File of type *.txt)");
		int selected = fc.showOpenDialog(fc);
		if(selected == JFileChooser.APPROVE_OPTION)
		{
			File f = fc.getSelectedFile();	
			try {
				doc = new Scanner(new BufferedReader(new FileReader(f)));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("No Document File Selected");
			System.exit(0);
		}
		return doc;
	}
}// end A4MahmudSaif class
	class Table {
		Node[] hashArray;
		public static final int PRIME = 13;
		/**
		 * Constructs a Table
		 * @param size of the Table to be constructed
		 */
		public Table(int size) {
			hashArray = new Node[size];
			for(@SuppressWarnings("unused") Node x : hashArray)
			{
				x = null;
			}
		}
		/**
		 * PURPOSE: Cross checks the document file with the dictionary table
		 * and if an error word is found, put that into a table containing all the 
		 * error words
		 * @param doc is the Scanner of document.txt file.
		 * @param errorTable is of type Table which contains all the wrong spelled words.
		 */
		public void crossCheck(Scanner doc, Table errorTable) {
			int lineCounter = 0;
			String line;
			String[] oneLine;
			int invalidWordCounter = 0;
			//cross every word of document with the dictionary
			while(doc.hasNext())
			{
				lineCounter++;
				line = doc.nextLine();
				line = line.toLowerCase();
				oneLine = line.trim().split( "[^a-z]+" );
				
				for(int i = 0; i < oneLine.length; i++)
				{
					String myLine = oneLine[i];
					if(!searchIndex(myLine, this) && !myLine.equals("")) //Not found in the dictionary
					{
						invalidWordCounter++;
						errorTable.addErrorNode(myLine, lineCounter); // add to the errorTable
					}
				}// main for
			}//while
			doc.close();
			System.out.printf("There are a total of %d invalid words:\n",invalidWordCounter);
			printErrorTable(errorTable);
		}
		/**
		 * Adds wrong spelled words to the table, if it's already there, it just adds the line
		 * number of the wrong spelled word was found.
		 * @param key is the wrong spelled word
		 * @param lineCounter is the lineNumber where this word was found
		 */
		public void addErrorNode(String key, int lineCounter) {
			int hashIndex = hashCode(key);
			if(this.hashArray[hashIndex] == null)
			{
				hashArray[hashIndex] = new Node(key, null);
				hashArray[hashIndex].lineList.add(lineCounter);
			} else { // node not empty
				if(!searchIndex( key, this)) // didn't find key in this chain, add to the top
				{
					Node newNode = new Node(key, hashArray[hashIndex]);
					newNode.lineList.add(lineCounter);
					hashArray[hashIndex] = newNode;		
				} else { // key is already in the chain, just add the line number to the node containing the key value
					Node myNode = getNode(hashArray[hashIndex], key);
					myNode.lineList.add(lineCounter);
				}
			}//outer else
		}// method ends
		/**
		 * Search for a node and returns it.
		 * @param top is the first node of the appropiate index of the table where the search begins
		 * @param key is the to be searched String value
		 * @return the node which has the searched String value
		 */
		public Node getNode(Node top, String key) {
			Node out = null;
			Node curr = top;
			Boolean isFound = false;
			while(curr != null && !isFound) 
			{
				if(curr.item.equals(key))
				{
					isFound = true;
					out = curr;
				}
				curr = curr.next;
			}
			return out;
		}
		/**
		 * Populate the instance table with words at the appropiate indexs
		 * @param in is the Scanner type pointer of the file to be parsed.
		 */
		public void populateTable(Scanner in) {
			String[] lineWords;
			while(in.hasNext()) 
			{
				String s = in.nextLine();
				s = s.toLowerCase();
				lineWords = s.trim().split( "[^a-z]+" );
				for (int i = 0; i < lineWords.length; i++)
				{
					int hashIndex = hashCode(lineWords[i]);
					if(hashArray[hashIndex] == null) // empty index. just add
					{
						hashArray[hashIndex] = new Node (lineWords[i], null);
					} else { // not empty
						if(!searchIndex( lineWords[i], this )) // key not found in this index chain
						{
							Node newNode = new Node(lineWords[i], hashArray[hashIndex]);
							hashArray[hashIndex] = newNode; // add to the top of the chain
						}
					}// else
				}// for
			}// while
			in.close();	
		}
		/**
		 * Converts a String to an int which will be the index location of the table
		 * Using Horner's method to convert, it will convert based on a polynomial function
		 * @param word the String value to be converted to an int
		 * @return the index value of the table, the converted int value of the String. 
		 */
		public int hashCode(String word)
		{	
			int hashIndex = 0;
			for ( int i = 0; i < word.length(); i++ ) 
			{
			    hashIndex = (hashIndex * PRIME) % hashArray.length + (int)word.charAt(i);
			    hashIndex = (hashIndex % hashArray.length);
			}
			return hashIndex;
		}
		/**
		 * Prints all the value of the table
		 * @param table is the identifier of the table to be printed
		 */
		public void printErrorTable(Table table) {
			Node curr;
			for(int i = 0; i < table.hashArray.length; i++)
			{
				if(table.hashArray[i] != null) 
				{
					curr = table.hashArray[i];
					while(curr != null)
					{
						System.out.printf("Invalid word \"%s\" found on lines ", curr.item);
						for(int k = 0; curr.lineList.size()> 0 && k < curr.lineList.size(); k++)
						{  
							System.out.print(curr.lineList.get(k) + " ");
						}
						System.out.println();
						curr = curr.next;
					}//while
				}// main if
			}// HashArray for loop
		}//printError method
		/**
		 * Search a node chain for the given String value
		 * @param key is the value to be searched inside the nodes
		 * @param table is the table to be searched
		 * @return true if the table/hashIndex containing the String value was found, false otherwise.
		 */
		public Boolean searchIndex(String key, Table table)
		{
			Boolean isFound = false;
			int hashIndex = hashCode(key);
			Node curr = table.hashArray[hashIndex];
			while(curr != null && !isFound)
			{
				if(curr.item.equals(key))
				{
					isFound = true;
				}
				curr = curr.next;
			}
			return isFound;
		}
	}// table class
	class Node {
		public String item;
		public Node next;
		public ArrayList<Integer> lineList;
		/**
		 * Contructs a Node
		 * @param item is the String value of the Node
		 * @param next is the Next pointer of the Node
		 */
		public Node( String item, Node next)
		{
			this.item = item;
			this.next = next;
			lineList = new ArrayList<Integer>();
		}
	}//node class

