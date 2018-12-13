package blackjack;

import java.util.ArrayList;
import java.util.Random;

public class Deck {

	private ArrayList<Card> cards;
	private String handStatus;

	public Deck() {
		this.cards = new ArrayList<Card>();

	}

	public void createFullDeck() {

		for (Suit cardSuit : Suit.values()) {

			for (Value cardValue : Value.values()) {

				this.cards.add(new Card(cardSuit, cardValue));
			}
		}
	}

	public void shuffle() {

		ArrayList<Card> tmpDeck = new ArrayList<Card>();

		Random random = new Random();
		int randomCardIndex = 0;
		int originalSize = this.cards.size();
		for (int i = 0; i < originalSize; i++) {

			randomCardIndex = random.nextInt((this.cards.size() - 1 - 0) + 1) + 0;

			tmpDeck.add(this.cards.get(randomCardIndex));

			this.cards.remove(randomCardIndex);
		}

		this.cards = tmpDeck;
	}

	public String setStatus(String newStatus) {
		return this.handStatus = newStatus;
	}

	public String getStatus() {
		return this.handStatus;
	}

	public void removeCard(int i) {
		this.cards.remove(i);
	}

	public Card getCard(int i) {
		return this.cards.get(i);
	}

	// for testing
	public Card getCard2(Suit suit, Value value) {
		Card testCard = new Card(suit, value);
		return testCard;
	}

	public void addCard(Card addCard) {
		this.cards.add(addCard);
	}

	public void draw(Deck comingFrom) {

		this.cards.add(comingFrom.getCard(0));

		comingFrom.removeCard(0);
	}

	public String toString() {
		String cardListOutput = "";

		for (Card aCard : this.cards) {
			cardListOutput += "\n" + aCard.toString();
		}
		return cardListOutput;
	}

	public void split(Deck DeckToGetCardFrom) {

		this.addCard(DeckToGetCardFrom.getCard(1));
		DeckToGetCardFrom.removeCard(1);

	}

	public void moveAllToDeck(Deck moveTo) {
		int thisDeckSize = this.cards.size();

		for (int i = 0; i < thisDeckSize; i++) {
			moveTo.addCard(this.getCard(i));
		}

		for (int i = 0; i < thisDeckSize; i++) {
			this.removeCard(0);
		}
	}

	public int getDeckSize() {
		return this.cards.size();
	}

	public int cardsValue() {
		int totalValue = 0;
		int aces = 0;

		for (Card aCard : this.cards) {

			switch (aCard.getValue()) {
			case TWO:
				totalValue += 2;
				break;
			case THREE:
				totalValue += 3;
				break;
			case FOUR:
				totalValue += 4;
				break;
			case FIVE:
				totalValue += 5;
				break;
			case SIX:
				totalValue += 6;
				break;
			case SEVEN:
				totalValue += 7;
				break;
			case EIGHT:
				totalValue += 8;
				break;
			case NINE:
				totalValue += 9;
				break;
			case TEN:
				totalValue += 10;
				break;
			case JACK:
				totalValue += 10;
				break;
			case QUEEN:
				totalValue += 10;
				break;
			case KING:
				totalValue += 10;
				break;
			case ACE:
				aces += 1;
				break;
			}
		}

		for (int i = 0; i < aces; i++) {


			if (this.getCard(0).getValue() == Value.ACE && this.getCard(i) == this.getCard(0)) {
				totalValue += 11;
				continue;
			}
			if (totalValue < 11 && this.getCard(1).getValue() == Value.ACE && this.getCard(i) == this.getCard(1)) {
				totalValue += 11;
			} else 
			{
				totalValue += 1;
			}
			if (this.getCard(i) != this.getCard(0) || this.getCard(i) != this.getCard(1)) {
				if (totalValue > 10) {
					{
						totalValue += 1;
					}
				} else {
					totalValue += 11;
				}
			}
		}

		return totalValue;

	}

}
