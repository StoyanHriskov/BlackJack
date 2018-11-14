package blackjack;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Blackjack {

	static Scanner userInput = new Scanner(System.in);

	// ========== win/lose ===================
	private static double decideTheWiner(Deck playDeck, Deck dealerCards, Deck playerHand, boolean endRound,
			double currentMoney, double playerBet) {

		String handStatus;

		// Dealer draw

		while ((dealerCards.cardsValue() < 17) && endRound == false) {
			dealerCards.draw(playDeck);
		}

		// if dealer busted
		if ((dealerCards.cardsValue() > 21) && endRound == false) {
			handStatus = "Dealer hand score is: " + dealerCards.cardsValue() + ". " + "Dealer Busts. You win!";
			playerHand.setStatus(handStatus);
			currentMoney += playerBet;
			endRound = true;
		}

		// Determine if push
		if ((dealerCards.cardsValue() == playerHand.cardsValue()) && endRound == false) {
			handStatus = "Push. No one wins!";
			playerHand.setStatus(handStatus);
			endRound = true;
		}

		// Determine a winer
		if ((playerHand.cardsValue() > dealerCards.cardsValue()) && endRound == false) {
			handStatus = "Your score is: " + playerHand.cardsValue() + ", dealer score is: " + dealerCards.cardsValue()
					+ "." + " You win the hand";
			playerHand.setStatus(handStatus);
			currentMoney += playerBet;
			endRound = true;
		} else if (dealerCards.cardsValue() < 22 && (playerHand.cardsValue() < dealerCards.cardsValue())
				&& endRound == false) {
			handStatus = "Your score is: " + playerHand.cardsValue() + ", dealer score is: " + dealerCards.cardsValue()
					+ "." + " Dealer wins the hand!";
			playerHand.setStatus(handStatus);
			currentMoney -= playerBet;
		} else if ((dealerCards.cardsValue() > 21) && endRound == false) {
			handStatus = "Dealer hand score is: " + dealerCards.cardsValue() + ". " + "Dealer Busts. You win!";
			playerHand.setStatus(handStatus);
			currentMoney += playerBet;
			endRound = true;
		}

		return currentMoney;

	}

	// ============= hit or stand ==================
	private static double HitOrStand(double currentMoney, double playerBet, Deck playerHand, Deck dealerCards,
			Deck playingDeck, boolean endRound, int i) {
		String handStatus;
		while (true) {

			System.out.println("Your Hand#" + (i + 1) + ":" + playerHand.toString());

			System.out.println("Your hand is currently valued at: " + playerHand.cardsValue());

			System.out.println("Dealer Hand: " + dealerCards.getCard(0).toString() + " and [hidden]");

			System.out.println("Would you like to (1)Draw or (2)Stand");
			int response = userInput.nextInt();

			if (response == 1) {
				playerHand.draw(playingDeck);
				System.out.println("You draw a:" + playerHand.getCard(playerHand.getDeckSize() - 1).toString());

				if (playerHand.cardsValue() > 21) {

					handStatus = "Bust. Currently valued at: " + playerHand.cardsValue();
					playerHand.setStatus(handStatus);
					currentMoney -= playerBet;
					endRound = true;
					break;
				}
			}

			if (response == 2) {
				currentMoney = decideTheWiner(playingDeck, dealerCards, playerHand, endRound, currentMoney, playerBet);
				break;
			}
		}
		return currentMoney;
	}

	public static void main(String[] args) {
		Scanner uI = new Scanner(System.in);
		do {
			boolean WantToPlay = true;
			System.out.println("Welcome to Blackjack!");
			System.out.println("1. Play a Blackjack game!");
			System.out.println("2. Show High score!");
			System.out.println("4. Exit!");

			System.out.println("Enter the number of your choise:");
			int playerChoise = userInput.nextInt();

			switch (playerChoise) {
			case 1:
				boolean lostAllMoney = false;
				//gamesPlayed counter
				int gamesPlayed=0;

				while (WantToPlay == true && lostAllMoney == false) {

					Player playerHands = new Player();

					// SET PLAYER NAME

					do {
						System.out.println("Enter your name:");
						String name = uI.nextLine();
						playerHands.setName(name);
						if ((playerHands.correctName(playerHands.getName())) != true) {
							System.err.print("\n Invalid entry!\n The name cannot contain DIGITS or SYMBOLS!\n");

						}
					} while ((playerHands.correctName(playerHands.getName())) != true);

					// DEPOSTI MONEY
					double currentMoney;
					do {
						System.out.println(
								"Hello " + playerHands.getName() + ", how much money would you like to deposit?");
						currentMoney = userInput.nextDouble();
						if (currentMoney < 1) {
							System.err.println("\n Invalid entry!\n Cannot deposit less than 1$!");
						}
					} while (currentMoney < 1);

					double startMoney = currentMoney;

					Deck playingDeck = new Deck();
					playingDeck.createFullDeck();

					playingDeck.shuffle();

					Deck firstHand = new Deck();

					Deck dealerCards = new Deck();

					playerHands.addHand(firstHand);

					while (currentMoney > 0 && WantToPlay == true) {

						System.out.println("You have $" + currentMoney + ", how much would you like to bet?");
						double playerBet = userInput.nextDouble();
						boolean endRound = false;
						if (playerBet > currentMoney) {

							System.out.println("You cannot bet more than you have.");
							break;
						}

						System.out.println("Dealing...");

						// testing split
//						 firstHand.draw(playingDeck);
//						 firstHand.addCard(firstHand.getCard2(Suit.DIAMOND, Value.ACE));
//						 firstHand.addCard(firstHand.getCard(0));

						firstHand.draw(playingDeck);
						firstHand.draw(playingDeck);

						

						dealerCards.draw(playingDeck);
						dealerCards.draw(playingDeck);


						// ================== split ==================
						if (playerHands.getHand(0).getCard(0).getValue() == playerHands.getHand(0).getCard(1)
								.getValue()) {
							System.out.println(firstHand.toString());
							System.out.println("You have cards with the same VALUE!");
							System.out.println("Do you want to split them to separate hands? [Y/N]");
							String makeSplit = userInput.next();
							while (makeSplit.equalsIgnoreCase("Y") != true && makeSplit.equalsIgnoreCase("N") != true) {
								System.out.println("Invalide choise!\nTry Again!");
								System.out.println("Do you want to split them to separate hands? [Y/N]");
								makeSplit = userInput.next();
							}
							if (makeSplit.equalsIgnoreCase("Y")) {
								// ====================== 2 hands ===================

								Deck secondHand = new Deck();
								playerHands.addHand(secondHand);
								playerHands.getHand(playerHands.getHandSize() - 1).split(firstHand);
								playerHands.printHands();
								System.out.println("-------------------------------------------------");

								
								for (int i = 0; i < playerHands.getHandSize(); i++) {
									Deck currentHand = playerHands.getHand(i);

									currentMoney = HitOrStand(currentMoney, playerBet, currentHand, dealerCards,
											playingDeck, endRound, i);
									System.out.println();

								}

								System.out.println(dealerCards.toString());

								for (int i = 0; i < playerHands.getHandSize(); i++) {
									System.out.println("Hand #" + (i + 1));
									System.out.println(playerHands.getHand(i).getStatus());
								}
								// ===================== end 2 hands ======================
							} else {

								// =================================1 hand =================
								currentMoney = HitOrStand(currentMoney, playerBet, firstHand, dealerCards, playingDeck,
										endRound, 0);
								System.out.println(playerHands.getHand(0).getStatus());

							}
						} else {
							currentMoney = HitOrStand(currentMoney, playerBet, firstHand, dealerCards, playingDeck,
									endRound, 0);
							System.out.println("-----------------------------\nDealer Cards\n"+dealerCards.toString()+"\n-----------------------------\n");
							System.out.println(playerHands.getHand(0).getStatus());
						}
						System.out.println("End of Hand.");
						// return all cards to playingDeck
						firstHand.moveAllToDeck(playingDeck);
						dealerCards.moveAllToDeck(playingDeck);
						//count games played
						gamesPlayed++;
						//============== shaping the final score
						// get date/time
						DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
						Date date = new Date();

						String scoreToAdd = "";
						scoreToAdd += "  " + dateFormat.format(date);
						scoreToAdd += "\t" + playerHands.getName();
						scoreToAdd += "\t\t  " + startMoney;
						scoreToAdd += "\t\t   " + currentMoney;
						scoreToAdd += "\t\t  " + gamesPlayed;
						//===============
						String path1 = "D:\\JavaCurs\\Blackjack\\src\\blackjack\\resources\\Score.txt";
						// String path2 =
						// "C:\\User\\shrisc\\Desktop\\Blackjack\\src\\blackjack\\resources\\Score.txt";
						
						//check if player lost all his money
						System.out.println();
						if (currentMoney == 0) {
							lostAllMoney = true;
							
						}

						// play again
						System.out.println("Another game ? [Y/N]");
						String playAgain = userInput.next();
						while (playAgain.equalsIgnoreCase("Y") != true && playAgain.equalsIgnoreCase("N") != true) {
							System.out.println("Invalide choise!\nTry Again!");
							System.out.println("Another game ? [Y/N]");
							playAgain = userInput.next();
						}
						// yes!
						if (playAgain.equalsIgnoreCase("Y")) {
							// remove added hands
							for (int i = 1; i < playerHands.getHandSize(); i++) {
								playerHands.removeHand(i);
							}
							WantToPlay = true;
							
						} // no!
						else {

							WantToPlay = false;
														
							// write to file
							if (currentMoney < startMoney) {
								scoreToAdd += "\t\t\t" + "-" + (startMoney - currentMoney) + " \n";
								try {
									Files.write(Paths.get(path1), scoreToAdd.getBytes(), StandardOpenOption.APPEND);
								} catch (IOException e) {
									System.err.println("No file found!");
								}
							} else {
								scoreToAdd += "\t\t\t" + "+" + (currentMoney - startMoney) + " \n";
								try {
									Files.write(Paths.get(path1), scoreToAdd.getBytes(), StandardOpenOption.APPEND);
								} catch (IOException e) {
									System.err.println("No file found!");
								}
							}

						}

					}

				}

				break;
			case 2:

				// print score
				try {
					BufferedReader br = new BufferedReader(
							new FileReader("D:\\JavaCurs\\Blackjack\\src\\blackjack\\resources\\Score.txt"));
					String line = "";
					System.out.println(
							"============================================================== Score ==============================================================");
					System.out.println("| Date |\t\t| Name |\t\t| Deposit |\t\t| End Money |\t\t| Games Played |\t| Balance |");
					System.out.println(
							"===================================================================================================================================");
					try {
						while ((line = br.readLine()) != null) {
							System.out.println(line);
						}
					} catch (IOException e) {
						System.err.println("Ooops!");
					}
					System.out.println(
							"===================================================================================================================================\n");
					// close BufferReader
					try {
						br.close();
					} catch (IOException e) {

						e.printStackTrace();
					}

				} catch (FileNotFoundException e) {

					System.out.println("No high score!");
					System.out.println("==================================");

				}
				break;
				
			case 4:
				
				uI.close();
				userInput.close();				
				System.out.println("Good bye and have a nice day!");
				System.out.println("Exiting...");
				System.exit(0);
				
			}

		} while (true);
		// userInput.close();
	}

}
