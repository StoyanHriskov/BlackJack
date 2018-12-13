package blackjack;

import java.util.ArrayList;

public class Player {
	
	private String name;
	private ArrayList<Deck> hands;
	
	
	public Player()
	{
		this.hands=new ArrayList<Deck>();
	}
	public Player(String name) {
		this();
		setName(name);
	}
	
	public void setName(String name) {
		if(correctName(name)) {
			this.name=name;
		}else {
			System.out.println("Invalid entry!\n The name cannot contain DIGITS or SYMBOLS!");
		}
	}
	
	public String getName() {
		return this.name;
	}
	
	public void printHands() {
		
		int handNumber=0;
		for (Deck aDeck : this.hands) {
			System.out.print("Hand #"+ ++handNumber +": ");
			System.out.println(aDeck.toString());
		}
		
	}
	
	public Deck getHand(int i) {
		return this.hands.get(i);
		
	}
	
	public void addHand(Deck newHand) {
		this.hands.add(newHand);
	}
	
	public int getHandSize() {
		return this.hands.size();
	}
	public void removeHand(int i) {
		this.hands.remove(i);
	}
	
	boolean correctName(String name) {

		if(name==null || name=="") {
			return false;
		}
		for(char c : name.toCharArray()) {
			
			if(Character.isDigit(c)) {
				return false;
			}
			if(Character.isWhitespace(c)) {
				continue;
			}
			if(!Character.isLetter(c)) {
				return false;
			}
		}
		
		return true;
	}
	
	
}
