import java.util.*;
import java.io.*;
import java.util.Scanner;
public class WordRecommender {
	//public access variables
	public String filename;
	public ArrayList<String> DictionaryWordList = new ArrayList<String>();
	public ArrayList<Double> SimilarityScore = new ArrayList<Double>();
	public ArrayList<Integer> SizeDifference = new ArrayList<Integer>();
	public ArrayList<String> ListOfWords = new ArrayList<String>();
	public Scanner userSelection = new Scanner(System.in);
	//constructor 
	public WordRecommender(String fileName) {
		this.filename = fileName;

		
	}
	
	//opens new scanner to read input form user.
	public String readScanner(Scanner scan) {
		return scan.next();
	}
	
	//closes scanner that was opened.
	public void closeScanner(Scanner scan) {
		scan.close();
	}
	
	//creates puts words from dictionary in to an arraylist.
	public ArrayList<String> getDictionaryWordList(){
		return DictionaryWordList;
	}
	
	//gets the user inputted filename
	public String getFilename() {
		return filename;
	}
	
	//take in a word and removes duplicate (makes the letters in the word distinct), returns word without duplicate letters
	public String RemoveDuplicates(String word) {
		char[] listofChar = word.toCharArray();
		char[] commonChar = new char[word.length()];
		commonChar[0] = listofChar[0];
		int count=1;
		
		outerloop:
		for(int i =0; i<word.length(); i++) {
			for(int j=0; j<commonChar.length; j++) {
				if(commonChar[j] == listofChar[i]) {
					continue outerloop;
				}else if(j==commonChar.length-1&&commonChar[j]!=listofChar[i]){
					commonChar[count] = listofChar[i];
					count++;
				}else {
					continue;
				}
			}
		}
		
		String noDuplicates = new String(commonChar);
		noDuplicates = noDuplicates.trim();
		return noDuplicates;
	}
	
	//Takes in a an ArrayList and a word and an allowable 
	//tolerance for string length all words as a list that meet criteria.
	public ArrayList<String> getWordsWithCommonLetters(String word, ArrayList<String> listofWords,int n ){

		ArrayList<String> WordsWithCommonLetters = new ArrayList<>();
		
		for(int i=0; i<listofWords.size(); i++) {
			if(listofWords.get(i).length() >= word.length()-n && listofWords.get(i).length() <= word.length()+n) {
				WordsWithCommonLetters.add(listofWords.get(i));
			}
		}
		return WordsWithCommonLetters;
	}
	
	//determines the similarity of two words
	public double returnSimilarity(String word1, String word2) {
		int SimilarityScore=0;
		for(int i=0; i<(word1.length()>word2.length() ? word2.length() : word1.length()); i++) {
			if(word1.charAt(i)==word2.charAt(i)) {
				SimilarityScore++;
			}
		}
		return SimilarityScore*1.0;
				
	}
	
	
	//returns the average similarity of two words going forwards as well as in reverse
	public double getSimilarityMetric(String word1, String word2) {
		StringBuilder word1SB = new StringBuilder();
		word1SB.append(word1);
		StringBuilder word2SB = new StringBuilder();
		word2SB.append(word2);
		double averageScore;
		double leftSimilarity = 0.0;
		double rightSimilarity = 0.0;
		leftSimilarity = returnSimilarity(word1SB.toString(), word2SB.toString());
		rightSimilarity = returnSimilarity(word1SB.reverse().toString(), word2SB.reverse().toString());
		averageScore = (leftSimilarity + rightSimilarity)/2.0;
		
		return averageScore;
	}
	
	//Calculates the number of distinct letters two words have in common
	public double CalculateCommonPercent(String Largerword, String Smallerword) {
		char[] LargerWordArr = Largerword.toCharArray();
		char[] SmallerWordArr = Smallerword.toCharArray();
		int count = 0;
		for(int i=0; i<LargerWordArr.length; i++) {
			for(int j=0; j<SmallerWordArr.length; j++) {
				if(LargerWordArr[i]==SmallerWordArr[j]) {
					count++;
				}
			}
		}
		return (count*1.0)/(LargerWordArr.length*1.0);
	}
	
	//Sorts a similarity List from largest to smallest, and sorts at the same time the 
	//corresponding list of words from largest t
	public ArrayList<String> SortList(ArrayList<String> listofwords, ArrayList<Double> similarityList){
		int indexOfLargest=0;		
		for(int i = 0; i<listofwords.size(); i++) {
			double tempLargest = 0.0;
			for(int j = i; j<listofwords.size(); j++) {
				if(similarityList.get(j)>tempLargest) {
					tempLargest = similarityList.get(j);
					indexOfLargest = j;
				}
			}
			Collections.swap(similarityList, indexOfLargest, i);
			Collections.swap(listofwords, indexOfLargest, i);
		}
		return listofwords;
		
	}
	
	//bulk of work done here, given input parameters returns a 
	//list of topN similar words to incorrect word.
	public ArrayList<String> getWordSuggestions(String word1, int n, double commonPercent, int topN){
		//gets words that are +/- in length to word being checked.
		ArrayList<String> WrdWithCommonLetters = getWordsWithCommonLetters(word1, DictionaryWordList, n);
		
		//method variables
		double averageScore;
		StringBuilder wordFromFile = new StringBuilder();
		wordFromFile.append(word1);
		String TruncWordFromFile = new String();
		ArrayList<String> wordFromDiction = new ArrayList<String>();
		String TruncWordFromDiction = new String();
		ArrayList<String> SortedWords = new ArrayList<String>();
		ArrayList<String> TopNumWords = new ArrayList<String>();
		double percent;
		ArrayList<Double> metricArray = new ArrayList<Double>();
		TruncWordFromFile = RemoveDuplicates(wordFromFile.toString());
		
		//loops through list of words that have +/- common letters and determines 
		//the percent of distinct letters between two words
		for(int i=0; i<WrdWithCommonLetters.size(); i++) {
			TruncWordFromDiction = RemoveDuplicates(WrdWithCommonLetters.get(i));
			if(TruncWordFromDiction.length() >= TruncWordFromFile.length()) {
				percent = CalculateCommonPercent(TruncWordFromDiction, TruncWordFromFile);
			}else {
				percent = CalculateCommonPercent(TruncWordFromFile, TruncWordFromDiction);
			}
			if(percent>=commonPercent) {
				wordFromDiction.add(WrdWithCommonLetters.get(i));
			}			
		}
		
		//loops through list of words from dictionary that have a tolerance percent in common with 
		//wrong words and determines the similarity between the two words (not distinct, but full)
		for(int k=0; k<wordFromDiction.size(); k++) {
			averageScore = getSimilarityMetric(wordFromDiction.get(k), wordFromFile.toString());
			metricArray.add(averageScore);
		}
		
		//sends list of words and similarity score list to be sorted
		SortedWords = SortList(wordFromDiction, metricArray);
		
		//gets topN words from sorted list
		if(SortedWords.size()!=0) {
			for(int j =0; j<(topN<SortedWords.size() ? topN : SortedWords.size()); j++) {
				TopNumWords.add(SortedWords.get(j));
			}
		}
		
		return TopNumWords;
	}
	
	
	//places dictionary in an ArrayList
	public ArrayList<String> getDictionary(String filename){
		File dictionaryFile = new File(filename);
		
		int count =0;
		ArrayList<String> CreateDictionary = new ArrayList<String>();
		try {
			Scanner scanDictionary = new Scanner(dictionaryFile);
			while(scanDictionary.hasNextLine()) {
				CreateDictionary.add(scanDictionary.next().toLowerCase());
				count++;
			}
			scanDictionary.close();
		}catch (FileNotFoundException e){
			e.printStackTrace();
		}
		return CreateDictionary;
	}
	
	
	//puts the file that is being checked in to a list array
	public void getWordList(String filename) {
		
		File FiletoCorrect = new File(filename);
		try {
			Scanner scanFile = new Scanner(FiletoCorrect);
			while(scanFile.hasNextLine()) {
				Scanner scanWords = new Scanner(scanFile.nextLine());
					while(scanWords.hasNext()) {
						ListOfWords.add(scanWords.next());
					}
					ListOfWords.add("\n");
					scanWords.close();
				}
			scanFile.close();
		}catch(FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	//checks the list of words against the dictionary, runs getWordSuggetions method, 
	//and replaces incorrect words from the list of words from file being checked with correct words 
	public void getcompareWords() {
		DictionaryWordList = getDictionary(this.filename);
		String WordToCheck = new String();
		String WordReplacement = new String();
		ArrayList<String> listOfSuggestedWords = new ArrayList<String>();
		
		for(int i =0; i<ListOfWords.size(); i++) {
			if(DictionaryWordList.contains(ListOfWords.get(i).toLowerCase()) || ListOfWords.get(i).toLowerCase().equals("i")|| ListOfWords.get(i).equals("a")) {
				continue;
			}else if(ListOfWords.get(i) == "\n"){
				continue;
			}else {
				WordToCheck = ListOfWords.get(i).toLowerCase();
				System.out.println("You have a mis-spelled word: " + WordToCheck);
				System.out.println("Please select a word to replace it with:");
				listOfSuggestedWords = getWordSuggestions(WordToCheck, 2, 0.5, 15);
				WordReplacement = prettyPrint(listOfSuggestedWords);
				if(WordReplacement.length()>0) {
					ListOfWords.set(i, WordReplacement);
				}else {
					continue;
				}
			}
		}
		userSelection.close();
	}
	
	//prints the word suggestions to the console
	public String prettyPrint(ArrayList<String> list) {
		
		String userSelectionOfWord = new String();
		int listSelectionNumber;
		String userInput = "";
		
		//asks user to accept a word as it, replace, or type in a new word
		if(list.size()== 0) {
			System.out.println("No suitable match was not found, please press 'a' to accept as it, or 't' to type a manual replacement:");
			userInput = userSelection.next();
		}else{
			System.out.println("The following matches were found:");
			for(int i=1; i<=list.size(); i++) {
				System.out.println(i+")" +list.get(i-1));
			}
			System.out.println("Please enter 'r to replace, please press 'a' to accept as it, or 't' to enter a type replacement:");
			userInput = userSelection.next();
		}
		
		//checks what user wants to do (r, a, t) and executes appropriately.
		if(userInput.equals("r")) {
			System.out.println("Please enter a number to replace your word with:");
			listSelectionNumber = userSelection.nextInt();
			if(listSelectionNumber<=list.size() && listSelectionNumber>0) {
				userSelectionOfWord = list.get(listSelectionNumber-1);
			}
			else {
				System.out.println("You have entered an inncorrect choice, word will not change");
				userSelectionOfWord = "";
			}
		}else if(userInput.equals("a")) {
			System.out.println("Word will not change");
			userSelectionOfWord = "";
		}else if(userInput.contentEquals("t")) {
			System.out.println("Please enter a word to replace your incorrect word with:");
			userSelectionOfWord = userSelection.next();
		}else {
			System.out.println("You did not enter a correct choice, word will not change.");
		}
		
		return userSelectionOfWord;
		
	}
	
	//creates a new file with corrected words.
	public void makeCorrectedFile (String filename) {
		File newFile = new File(filename+"_chk");
		
		try {
			FileWriter filewrite = new FileWriter(newFile, true);
			for(int i=0; i<ListOfWords.size(); i++) {
				filewrite.write(ListOfWords.get(i));
				filewrite.write(" ");
				
			}
			filewrite.close();
		}catch(IOException e){
			e.printStackTrace();
		}
		System.out.println("Your file has been spell checked!  Thank you!");
		System.out.println("Your file can be found with the name: "+filename+"_chk");
		
	}
	
	//main method.
	public static void main(String[] args) {
		String FiletoSpellCheck = new String();
		String FileName = new String();
		System.out.println("Please enter the filename of the file you would like to check:");
		Scanner userFileName = new Scanner(System.in);
		FileName = userFileName.nextLine();
		FiletoSpellCheck = FileName;
		//"/Users/Pradeep/Coursera/MCIT591/SpellChecker/SpellChecker/src/" + 
		WordRecommender dictionaryRef = new WordRecommender("engDictionary.txt");
		
		dictionaryRef.getWordList(FiletoSpellCheck);
		
		dictionaryRef.getcompareWords();
		
		dictionaryRef.makeCorrectedFile(FileName);
		
		userFileName.close();
	
		
	}
	
	
}
	


