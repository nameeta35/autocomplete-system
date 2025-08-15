/*
 * Click `Run` to execute the snippet below!
 */

import java.io.*;
import java.util.*;

/*
 * To execute Java, please define "static void main" on a class
 * named Solution.
 *
 * If you need more classes, simply define them inline.
 */
 
interface AutoCompleteProvider{
  void insert(String word);
  List<String> suggestWords(String prefix);
}

class TrieNode{
  Map<Character, TrieNode> children;
  String word = null;
  Map<String, Integer> freqTable;

  public TrieNode(){
    this.children = new HashMap<>();
    this.freqTable = new HashMap<>();
  }
  //setter
  //getters
}

class EnglishAutoCompleteProvider implements AutoCompleteProvider{

  TrieNode root; 
  int numberOfSuggestions;

  public EnglishAutoCompleteProvider(int numberOfSuggestions){
    root = new TrieNode();
    this.numberOfSuggestions = numberOfSuggestions;
  }

  @Override
  public void insert(String word){
    TrieNode node = root;
    for (Character c : word.toCharArray()){
      if (!node.children.containsKey(c)){
        TrieNode newNode = new TrieNode();
        node.children.put(c, newNode);
        node = newNode;
      } else {
         node = node.children.get(c);
      }     
       updateFreqTable(word , node); //updating every trieNode with frequency
    }
    node.word = word;
  }

  public void updateFreqTable(String word, TrieNode node){
    TrieNode trie = node;
    if (trie.freqTable.containsKey(word)){
      node.freqTable.put(word, node.freqTable.get(word) + 1);
    } else {
      node.freqTable.put(word,1);
    }
  }

  @Override
  public List<String> suggestWords(String prefix){
    List<String> popularWords = new ArrayList<>();
    //relevant & popular 
    //find words for the prefix input
    TrieNode node = root;
    for (Character ch: prefix.toCharArray()){
      if (!node.children.containsKey(ch)){
        return Collections.emptyList();
      } else {
        node = node.children.get(ch);
      }
    }
    final TrieNode freqNode = node;
    //pick top 5 words from words list
    PriorityQueue<String>  wordHeap = new PriorityQueue<>((a,b) 
    -> freqNode.freqTable.get(a) -  freqNode.freqTable.get(b));

    for (String word : node.freqTable.keySet()){
      wordHeap.add(word);
      if (wordHeap.size() > numberOfSuggestions){
        wordHeap.poll();
      }
    }
    while (!wordHeap.isEmpty()){
      popularWords.add(wordHeap.poll());
    }
    Collections.reverse(popularWords);
      return popularWords;
  }
}
class AutoCompleteSystem{

  int numberOfSuggestions;
  String type;
  StringBuilder currentString = new StringBuilder();
  List<String> suggestedWords = new ArrayList<>();
  AutoCompleteProvider provider;

  public AutoCompleteSystem(int numberOfSuggestions, String type){
    this.numberOfSuggestions = numberOfSuggestions;
    this.type = type;
    this.provider = createProvider(type);
  }

  public AutoCompleteProvider createProvider(String type){
    if (type.equals("English")){
      return new EnglishAutoCompleteProvider(numberOfSuggestions);
    }
    return new EnglishAutoCompleteProvider(numberOfSuggestions);
  }

  public void insertWords(List<String> words){
    for (String word : words){
      provider.insert(word);
    }
  }
  public List<String> suggestWords(Character input){
        if (input != '.'){
          currentString.append(input); //i
          return provider.suggestWords(currentString.toString()); //i
        } else {
          provider.insert(currentString.toString());
          currentString.setLength(0);
          return Collections.emptyList();
      }
  }
}


class Solution {
  public static void main(String[] args) throws Exception {

    AutoCompleteSystem autoCompleteSystem = new AutoCompleteSystem(5, "English");
    
    List<String> words = new ArrayList<>();
    words.add("iceland");
    words.add("ice");
    words.add("icecubes");
    words.add("iceland");
    words.add("icecubes");
    words.add("icetruckdriver");
    autoCompleteSystem.insertWords(words);
    
    String word = "icetruck";
    for (Character c : word.toCharArray()){
      System.out.println(autoCompleteSystem.suggestWords(c));
    }
  
  }

}


//AutoComplete System
//suggest words when user is typing, some delimiter ?
//suggest most frequently used and relevant words
//lowercase

//trie
//when user is done typing i.e they enter "." , save word to the trie
//when user is typing, depending on char, we find prefix and corresponding word in the trie to suggest as well as top 5 words

//class AutoCompleteSystem
//class TrieNode
//class TrieUpdateSystem
//class SuggestionSystem

//interface AutoCompleteProvider

