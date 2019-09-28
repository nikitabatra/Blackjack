/** 
 * 
 * @author Nikita Batra
 * Bachelor of Engineering, Computer Science; Minor in Business 
 * Hong Kong University of Science and Technology, 2013-17
 * 
 **/

import java.util.Scanner;
import java.util.ArrayList;
import java.util.Random;
import java.util.List;

public class blackjack {

	  // Statics declared
	public static final int initMoney = 100;	// this is the money user has before game starts
	public static final String[] names = {"Ace", "King", "Queen", "Jack", "10", "9", "8", "7", "6", "5", "4", "3", "2"};
	public static final String[] types = {"Hearts", "Spades", "Clubs", "Diamonds"};

	// Main function starts 
	public static void main(String[] arg)	{
		
		// Intro text 
		int money =	initMoney;
		System.out.println("Solitaire BlackJack");
		System.out.println("");
		System.out.println("Value of Jack, Queen, King = 10");
		System.out.println("Value of Ace = 11");
		System.out.println("In total you have $" + money);
		
		// Setup of bet input value
		Scanner inputValue = new Scanner(System.in);
		
		// Setup of card deck
		List<Card> deck = new ArrayList<Card>();
		for (int i = 0; i < types.length; i++){
			for (int j = 0; j < names.length; j++){
				Card x = new Card(names[j], types[i]);
				deck.add(x);
			}
		}

		boolean playGame = true;

		while (money > 0 && playGame == true) {

			int playersTotal = 0;
			int dealersTotal = 0;

			List<Card> playersCards = new ArrayList<Card>(); 
			List<Card> dealersCards = new ArrayList<Card>();

			deck = randomizeDeck(deck);
		
			System.out.println("");
			System.out.println("Enter bet amount: ");
			int bet = Math.abs(inputValue.nextInt());
			
			while(bet > money){
				System.out.println("Short on money, try again");
				System.out.println("Enter bet amount: ");
				bet = inputValue.nextInt();
			}
			
			System.out.println("");
			System.out.print("Card pulled from Deck: ");
			playersTotal += drawCard(deck, playersTotal, playersCards);
			System.out.println("");
			
			System.out.print("Dealer's Card: ");
			dealersTotal += drawCard(deck, dealersTotal, dealersCards);

			boolean nextCard = true;
			while (playersTotal < 21 && nextCard){
					nextCard = Hit(playersTotal, inputValue);
					if (playersTotal > 21 || playersTotal == 21 || !nextCard) {
						break;
					} else {
						playersTotal += drawCard(deck, dealersTotal, dealersCards);
					}
					
					for(int i = 0; i < playersCards.size(); i++){
						if (playersCards.get(i).Ace && playersTotal > 21) {
							playersTotal -= 10;
						}
					}		
			}
			
			while (dealersTotal < 17 && playersTotal < 21) {
				System.out.print("Dealer's Card: " + dealersTotal);
				Card dealerCard = deck.remove(0);
				System.out.println("");
				
				System.out.print("Dealer gets: ");
				dealerCard.printCard();
				dealersTotal += dealerCard.giveValue(dealersTotal);
				dealersCards.add(dealerCard);
				
				for(int i = 0; i < dealersCards.size(); i++){
					if (dealersCards.get(i).Ace && dealersTotal > 21) {	
						playersTotal -= 10;
					}
				}
			}
			
			//Check winner
			//Another round or not
			System.out.println();
			money += checkWinner(playersTotal, dealersTotal, bet);
			playGame = playAgain(inputValue, money);
		}
	}
	
	// ^Main function ends 
	
	// Drawing a card and removing it from the deck
	public static int drawCard(List<Card> deck, int playersTotal, List<Card> playersCards){
		int total = 0;
		Card playerCard1 = deck.remove(0);
		playerCard1.printCard();
		total += playerCard1.giveValue(playersTotal);
		playersCards.add(playerCard1);
		return total;
	}
	
	// Randomizer
	public static List<Card> randomizeDeck(List<Card> deck){
		List<Card> randDeck = new ArrayList<Card>();
		int rand = 0;
		while (deck.size() > 0){
			Random card = new Random();
			rand = card.nextInt(deck.size());
			Card randomGenerated = deck.remove(rand);
			randDeck.add(randomGenerated);
		}
		return randDeck;
	}

	// Who won?
	public static int checkWinner(int userTotal, int dealerTotal, int betPlayed) {
		
		int moneyWonOrLost = 0;
		
		//WIN
		if (userTotal == 21) {
			System.out.println("All your cards added up: " + userTotal);
			System.out.println("IT'S A BLACKJACK, YOU WIN!");
			moneyWonOrLost = betPlayed;
		} 
		
		else if (dealerTotal > 21) {
			System.out.println("Dealer has: " + dealerTotal);
			System.out.println("Dealer Busts, YOU WIN!");
			moneyWonOrLost = betPlayed;
		} 
		
		// LOSE
		else if (userTotal > 21) {
			System.out.println("All your cards added up: " + userTotal);
			System.out.println("You bust");
			moneyWonOrLost = -1 * betPlayed;
		} 
		
		else if (userTotal < dealerTotal) {
			System.out.println("All your cards added up: " + userTotal);
			System.out.println("Dealer has: " + dealerTotal);
			System.out.println("Dealer wins");
			moneyWonOrLost = -1 * betPlayed;
		} 
		
		// TIE
		else if (userTotal == dealerTotal) {
			System.out.println("All your cards added up: " + userTotal);
			System.out.println("Dealer has: " + dealerTotal);
			System.out.println("It's a tie!");
			moneyWonOrLost = 0;
		} 
		
		else {
			System.out.println("All your cards added up: " + userTotal);
			System.out.println("Dealer has: " + dealerTotal);
			System.out.println("YOU WIN!");
			moneyWonOrLost = betPlayed;
		}
		return moneyWonOrLost;
	}
   	
   // Hit or Stand
	public static boolean Hit(int total, Scanner inputValue){
		boolean ans = false;
		System.out.println("All your cards added up: " + total); 
		System.out.println("Hit or Stand? (H/S)");
      String answer = inputValue.next();
        if (answer.indexOf("H") == 0 || answer.indexOf("h") == 0) {
            ans = true;
        } else if (answer.indexOf("S") == 0 || answer.indexOf("s") == 0) {
            ans = false;
        }	else {
            System.out.println();
            ans = false;
        }
		return ans;
	}	

	//User plays again or not 
	public static boolean playAgain(Scanner inputValue, int userAmountLeft){
		boolean ans;
		System.out.println("In total you have $" + userAmountLeft);
		if (userAmountLeft == 0) {
			System.out.println("Oops! You're out of money!");
			ans = false;
			return ans;
		} 
		
		System.out.println("Play again?");
		String answer = inputValue.next();
      if (answer.indexOf("y") == 0 || answer.indexOf("Y") == 0 || answer.indexOf("yes") == 0 || answer.indexOf("YES") == 0) {
      	ans = true;
			return ans;
      } else if (answer.indexOf("n") == 0 || answer.indexOf("N") == 0 || answer.indexOf("no") == 0 || answer.indexOf("NO") == 0) {
            ans = false;
				if (userAmountLeft > 100) {
					System.out.println("In total you have $" + userAmountLeft);
				} else {
					System.out.println("In total you have $" + userAmountLeft);
				}
				return ans;
      } else {
      	System.out.println();
         ans = false;
      }
		return ans;
	}


   public static class Card{
	   private int value;
	   private String name; // king, queen etc 
	   private String types; // diamond, spade etc
	   public boolean Ace;
	   
	   //this. = using constructor
	   public Card(String name, String types){
		   this.name = name;
		   this.types = types;
		   this.value = cardValue(name);
	   }
	   
	   public void printCard(){
		   System.out.println(this.name + " of " + this.types);
	   	}
	   
   		public int giveValue(int playersTotal){
   			return this.value;
   		}
   		
		public int cardValue(String name){
			int value = 0;
				if (name == "King" || name == "Queen" || name == "Jack"){
				value = 10;
				return value;
				} 
				else if(name == "Ace"){
					value = 11; 
					this.Ace = true;
					return value;
				}
				else{
					value = Integer.parseInt(name.substring(0,1));
					return value;
				}
			}	
   }
}