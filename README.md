# SpellChecker
Spellchecker reads a file and spell checks the words in it.  Suggests words via algorithm to replace misspelled words in document.

Assignment 4: Spell Checker

In this assignment, you will practice reading and writing from files using Java (aka file I/O) and
manipulating strings, skills that form the foundation of working with databases, data science,
big data, and more.
The activities in Week 6 and 7 Recitation will help you practice for this assignment, so it is highly
recommended that you attend the live sessions or review the recordings and be sure to attempt
the recitation activities.

Goal

In this assignment, you will be building a simple spell checker. The spell checker that you build
will perform three tasks:
1. Spot the misspelled words in a file by checking each word in the file against a provided
dictionary.
2. Provide the user with a list of alternative words to replace any misspelled word.
3. Write a new file with the corrected words as selected by the user. Please note that it
would take some work to maintain the original file’s punctuation. Do not worry about
that for this assignment.

Specifications

A list of correctly spelled English words is provided as a reference in the file called
engDictionary.txt, one word per line of the file. The user file to be checked is a text file
where each space-separated word of the file is to be examined.

For now, we are going to ignore punctuation completely (i.e., input files have no punctuation),
and assume words that are entirely uppercase (such as MCIT or NBA) are correctly spelled. If
you want to add logic that deals with these in a special manner, you may, just make sure that it
does not break the specifications outlined below.

If a word from the file does not exist in the provided list, then it is assumed to be misspelled
and a set of alternatives provided to the user. The user has three options for the provided
candidate list:

1
MCIT Online - 591 - Introduction to Software Engineering

● The user types the letter ‘r’, indicating that they want to replace the word with one of
the words provided as suggested spellings. They then select, by number, one of the
candidates. The word in the file is replaced in the output file by the selected number.
● Indicate that they wish to leave the word as is. This is done by the user just typing the
letter ‘a’.
● Indicate that they wish to provide the alternative by typing it in directly. The program
replaces the word in the file with the user-provided replacement in the output file. The
user does this by first entering the letter ‘t’. They then proceed to type the word.

Example User Interaction

Typical interaction will be something like the following. The blue text indicates what is actually
displayed to the user. The normal (black) text is just for explanation purposes
The word ‘morbit’ is misspelled.
The following suggestions are available
1. ‘morbid’
2. ‘orbit’.
Press ‘r’ for replace, ‘a’ for accept as is, ‘t’ for type in manually.
User presses ‘r’
Your word will now be replaced with one of the suggestions
Enter the number corresponding to the word that you want to use for replacement.
User enters 1
In the output file, morbit will be replaced with morbid.
The word ‘automagically’ is misspelled.
The following suggestions are available
1. automatically.
Press ‘r’ for replace, ‘a’ for accept as is, ‘t’ for type in manually.
User presses ‘a’ and in the output file, ‘automagically’ stays the same.
The word ‘ewook’ is misspelled. The following suggestions are available
1. wok
2. woo
3. awoke
Press ‘r’ for replace, ‘a’ for accept as is, ‘t’ for type in manually.
User presses ‘t’
Please type the word that will be used as the replacement in the output file.

2
MCIT Online - 591 - Introduction to Software Engineering

User types ’ewok’
In the output file, this word is changed to ewok.
The rare case is when the spell checker cannot come up with anything at all. Note that this case
is slightly different so be aware of it when you program.
The word ‘sleepyyyyyyyy’ is misspelled.
There are 0 suggestions in our dictionary for this word.
Press ‘a’ for accept as is, ‘t’ for type in manually.
User presses ‘t’
Please type the word that will be used as the replacement in the output file.
 User types ‘sleepy’
If the user is trying to be silly, and enters something other than ‘r’, ‘a’ or ‘t’ and/or a number
that is outside of the range of options when they have decided to do a replace, please tell the
user politely that they have to try again. Do not let the program exit or crash in a
user-unfriendly manner.

Breaking Down the Assignment

We would like you to break down this assignment into the following pieces.
1. Prompt the user for the name of a file to spell check. The program will spell check each
word and then write a new file with the name of the original file plus the suffix ‘_chk’.
Thus if the file being checked is shopping.txt, the spell checked output will be
shopping_chk.txt. Note that the file suffix is preserved!
2. Create a method that checks a word of the user file against words from the dictionary
file and see if that word is there (more details below in the functions list).
3. Write a loop which reads from the input file and goes through it a word at a time. Each
word of the file is checked to see if it is spelled correctly based on the reference list. If it
is spelled correctly, just write it directly to the output file. If it is not spelled correctly,
then provide the user with the options discussed above. Depending upon their
responses, write the corresponding word to the output file.
4. Output the name of the updated spell-checked file. The user can then open up that file
via Finder/Windows Explorer (your program does not need to do this) and see the result
of the spell checking.

3

MCIT Online - 591 - Introduction to Software Engineering
Breaking Down the Assignment Even Further
(Methods that need to be written)
In order to help you out, here are some classes along with some methods that we want you to
write. You have to combine these classes and methods in a manner that makes the most sense.
We will first have you make a class called WordRecommender which has at least one instance
variable – a String filename that has a dictionary of words. This should be set with by
the constructor, which takes one argument:
public WordRecommender(String fileName)
When you call this constructor elsewhere in your program, you may hard-code it to be
“engDictionary.txt” (you do not need to ask for user input).
The WordRecommender Class will have the following methods in it:
1. public ArrayList<String> getWordsWithCommonLetters(String word,
ArrayList<String> listOfWords, int n)
Given a word and a list of words from a dictionary, return the list of words in the dictionary that
have at least (>=) n letters in common. For the purposes of this method, we will only consider
the distinct letters in the word.
Consider a wordList to contain [‘ban’, ‘bang’, ‘mange’, ‘gang’, ‘cling’, ‘loo’] and the word we
have is ‘cloong’.
Then we want the result of
getWordsWithCommonLetters(‘cloong’, wordList, 2) will return [‘bang’,
‘mange’, ‘gang’, ‘cling’, ‘loo’]
and
getWordsWithCommonLetters(‘cloong’, wordList, 3) will return
[‘cling’].
Note that we only count distinct letters, which is why the word ‘loo’ does not appear in the
second example.
2. public double getSimilarityMetric(String word1, String word2)
given two words, this function computes two measures of similarity and returns the average.

4

MCIT Online - 591 - Introduction to Software Engineering
leftSimilarity (a made-up metric) – the number of letters that match up between word1 and
word2 as we go from left to right and compare character by character.
So the leftSimilarity for ’oblige’ and ’oblivion’ is 4, the leftSimilarity for ’aghast’ and ’gross’ is 1.
For the “oblige” and “oblivion” example the first character is an o for both strings, the second
character is a b for both, the third character is an l, the fourth character is an i and then nothing
else lines up.
rightSimilarity (another made-up metric) – the number of letters that match up, but this time
going from right to left.
So the rightSimilarity for ‘oblige’ and ‘oblivion’ is 1.
the rightSimilarity for ‘aghast’ and ‘gross’ is 2.
For the aghast and gross example, the last character is a t and an s respectively so those do not
count, the second last character is an s in both cases, the a and the o do not line up, the h and
the r do not line up, and finally the fifth character from the end is a g in both cases and
therefore that contributes to the score as well.
Finally to get the similarity score, take the average of leftSimilarity and rightSimilarity and
return that value. So getSimilarityMetric(‘oblige’, ‘oblivion’) will return (4+1)/2.0 = 2.5
3. public ArrayList<String> getWordSuggestions(String word, int
n, double commonPercent, int topN) –
given an incorrect word, return a list of legal word suggestions as per an algorithm given below.
You can safely assume this function will only be called with a word that is not already present in
the dictionary. commonPercent should be a double between 0.0, corresponding to 0%, and
1.0, corresponding to 100%.
To come up with a list of candidate words, we first come up with a list of candidate words that
satisfy both of these two criteria:
a) candidate word length is word length +/- n characters .
b) have at least commonPercent% of the letters in common.
For the letters in common, please consider only the distinct letters in each word. So a word like
committee (c,o, m, i, t, e) and comet (c, o, m, e, t) have ⅚ = 83% in common.

5

MCIT Online - 591 - Introduction to Software Engineering
Next, for all the words that satisfy these two criteria, order them based on the similarity metric
(see above) and return the topN number of them.
This method involves more work so please ensure that you have written the previous methods
first.
4. public String prettyPrint(ArrayList<String> list)
Finally, here is a method that you need to write purely for display purposes. This method takes
an ArrayList and returns a String which when printed will have the list elements with a number
in front of them
prettyPrint([“biker”, “tiger”, “bigger”]) returns the string “1. biker\n2.
tiger\n3. bigger\n” so that when you print it you get
1. biker
2. tiger
3. bigger
Finally, in your README, you should assess the above design, and provide one recommendation
for how it could be made better. Think about the principles we’ve discussed in class such as
cohesion, coupling, and DRY, among other considerations.
