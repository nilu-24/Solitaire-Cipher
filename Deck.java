package assignment2;

import java.util.Random;

public class Deck {
    public static String[] suitsInOrder = {"clubs", "diamonds", "hearts", "spades"};
    public static Random gen = new Random();

    public int numOfCards; // contains the total number of cards in the deck
    public Card head; // contains a pointer to the card on the top of the deck

    /*
     * TODO: Initializes a Deck object using the inputs provided
     */
    public Deck(int numOfCardsPerSuit, int numOfSuits) {

        if (numOfCardsPerSuit < 1 || numOfCardsPerSuit > 13) {
            throw new IllegalArgumentException("numOfCardsPerSuit is invalid!");
        }
        if (numOfSuits < 1 || numOfSuits > suitsInOrder.length) {
            throw new IllegalArgumentException("numOfSuits is invalid!");
        }
        Deck d = new Deck();
        Card Rj = new Joker("red"); //creating red joker
        Card Bj = new Joker("black");//creating black joker

        head = new Deck.PlayingCard(suitsInOrder[0], 1);
        Card c = head;
        for (int i = 0; i < numOfSuits; i++) {
            for (int j = 0; j < numOfCardsPerSuit; j++) {
                c.next = new Deck.PlayingCard(suitsInOrder[i], j + 1);
                Card prevC = c;
                c = c.next;
                c.prev = prevC;
            }
        }
        c.next = Rj; //c.next points to Rj
        Rj.prev = c;
        Rj.next = Bj;
        Bj.prev = Rj;

        head = head.next;//cutting off the extra head
        Bj.next = head; // making it a doubly linked list
        head.prev = Bj;

        numOfCards = (numOfCardsPerSuit * numOfSuits) + 2;
    }


    /*
     * TODO: Implements a copy constructor for Deck using Card.getCopy().
     * This method runs in O(n), where n is the number of cards in d.
     */
    public Deck(Deck d) {
        this.numOfCards = d.numOfCards;

        this.head = d.head.getCopy();
        Card c = this.head;
        Card x = d.head;


        for (int i = 1; i < d.numOfCards; i++) {
            c.next = x.next.getCopy();
            Card p = c;
            c = c.next;
            x = x.next;
            c.prev = p;

        }
        //making it a doubly linked list
        c.next = this.head;
        this.head.prev = c;

        if (d.numOfCards == 0) return;

    }

    /*
     * For testing purposes we need a default constructor.
     */
    public Deck() {
    }

    /*
     * TODO: Adds the specified card at the bottom of the deck. This
     * method runs in $O(1)$.
     */
    public void addCard(Card c) {

        if (numOfCards == 0) {
            head = c;
            c.next = c;
            c.prev = c;
        }
        else{
            c.prev = head.prev;
            head.prev.next = c;
            c.next = head;
            head.prev = c;
        }
        numOfCards++;
    }

    /*
     * TODO: Shuffles the deck using the algorithm described in the pdf.
     * This method runs in O(n) and uses O(n) space, where n is the total
     * number of cards in the deck.
     */
    public void shuffle() {
        Card h = head;

        if (numOfCards == 1 || numOfCards == 0) { //edge case
            return;
        }

        Card[] array = new Card[numOfCards];
        for (int i = 0; i < numOfCards; i++) {
            array[i] = h;
            h = h.next;
        }
        for (int i = numOfCards - 1; i > 0; i--) {
            int j = gen.nextInt(i + 1); //picking a random j from 0 to i (inclusive)
            //to swap,
            Card swapper = array[i]; //storing card at i
            array[i] = array[j];//swapping with j
            array[j] = swapper;//swapping with i
        }
        head = array[0];
        Card c = head;
        for (int i = 1; i < numOfCards; i++) {
            c.next = array[i];
            Card p = c;
            c = c.next;
            c.prev = p;
        }
        c.next = head;
        head.prev = c;
    }

    /*
     * TODO: Returns a reference to the joker with the specified color in
     * the deck. This method runs in O(n), where n is the total number of
     * cards in the deck.
     */
    public Joker locateJoker(String color) {

        Card c = head;
        for (int i = 0; i < numOfCards; i++) {
            if (c instanceof Joker && ((Joker) c).getColor() == color) {
                return (Joker) c;
            }
            c = c.next;
        }
//might be edge cases so check for those

        return null;
    }

    /*
     * TODO: Moved the specified Card, p positions down the deck. You can
     * assume that the input Card does belong to the deck (hence the deck is
     * not empty). This method runs in O(p).
     */
    public void moveCard(Card c, int p) {

        Card c2 = c;

        if (p == 0) { //no change if p = 0
            return;
        }
        for (int i = 0; i < p; i++) {
            if (c2.next != c){ //if c doesn't repeat (normal)
                c2 = c2.next;
            }
            else {
                c2 = c2.next.next; //skip itself if c repeats
            }
            c.prev.next =c.next;
            c.next.prev = c.prev;
            c.prev = c2;
            c2.next.prev = c;
            c.next = c2.next;
            c2.next = c;
        }
    }


    /*
     * TODO: Performs a triple cut on the deck using the two input cards. You
     * can assume that the input cards belong to the deck and the first one is
     * nearest to the top of the deck. This method runs in O(1)
     */
    public void tripleCut(Card firstCard, Card secondCard) {
        //if two cards at ends
        if(head == firstCard && head.prev == secondCard){
            return;
        }
        if(head ==secondCard && head.prev == firstCard){
            return;
        }
        if(head == firstCard){ //edge case 1

            Card rightDeck = secondCard.next; //nearest to the bottom

            head = rightDeck; //just update head as circular list is in order
            head.prev = secondCard;

        }
        else if (head.prev == secondCard){ //edge case 2

            Card leftDeck = firstCard.prev;

            head = firstCard;
            head.prev = firstCard.prev;
        }

        else {
            //general case
            Card leftDeck = firstCard.prev; //nearest to the top
            Card rightDeck = secondCard.next; //nearest to the bottom
            Card bottom = head.prev;

            //isolating the top(head) and bottom(tail) and left and right decks
            bottom.next = null; //cutting tail's link
            head.prev = null; //cutting head's link

            firstCard.prev = null; //cutting first card from left
            secondCard.next = null; //cutting second card from right
            leftDeck.next = null; //cutting left from first card
            rightDeck.prev = null; //cutting right from second card

            //now we have to join the pieces appropriately

            firstCard.prev = bottom; //making the switch in the left
            bottom.next = firstCard;

            secondCard.next = head; //making the switch in the right
            head.prev = secondCard;

            head = rightDeck; //updating head
            bottom = leftDeck; //updating the tail

            head.prev = bottom; //connecting head's prev to new tail
            bottom.next = head; //connecting tail's next to new head
        }


    }

    /*
     * TODO: Performs a count cut on the deck. Note that if the value of the
     * bottom card is equal to a multiple of the number of cards in the deck,
     * then the method should not do anything. This method runs in O(n).
     */
    public void countCut() {
        Card h = head;
        Card lastCard = head.prev;
        Card secondLast = head.prev.prev;

        int cut = (head.prev.getValue())%(numOfCards);

        if(head.prev instanceof Joker){ //having joker at bottom has no change
            return;
        }
        if(cut==0){
            return;
        }
        if(cut==numOfCards-1){
            return;
        }

        for(int i=1;i<cut;i++){
            h=h.next;
        }
        Card top = h.next;

        //cutting
        top.prev = null;
        h.next = null;
        head.prev = null;
        lastCard.next = null;
        lastCard.prev = null;
        secondLast.next = null;

        //joining
        secondLast.next = head;
        head.prev = secondLast;

        h.next = lastCard;
        lastCard.prev = h;

        head = top;
        head.prev = lastCard;

        lastCard.next = top;

    }

    /*
     * TODO: Returns the card that can be found by looking at the value of the
     * card on the top of the deck, and counting down that many cards. If the
     * card found is a Joker, then the method returns null, otherwise it returns
     * the Card found. This method runs in O(n).
     */
    public Card lookUpCard() {

        Card h = head;
        int num = head.getValue();
        for(int i=1;i<num;i++){
            h=h.next;
        }
        Card key = h.next;
        if(!(key instanceof Joker)){
            return key;
        }
        else
            return null;
    }

    /*
     * TODO: Uses the Solitaire algorithm to generate one value for the keystream
     * using this deck. This method runs in O(n).
     */
    public int generateNextKeystreamValue() {
        Card h = head;
        int key;
        Card first;
        Card second;
        Card rj = locateJoker("red");
        Card bj = locateJoker("black");
        moveCard(rj,1);
        moveCard(bj,2);

        //need to locate which joker is first and which one is second
        for (int i =0;i<numOfCards;i++) {
            if (!(h instanceof Joker)) {
                h = h.next;
            }
        }
        first = h; //h is the first instance of joker in the deck
        if(first==rj){
            second =bj; //if h is rj then second has to be bj
            tripleCut(first,second);
        }

        else if(first==bj){
            second = rj; //if h is bj then second has to be rj
            tripleCut(first,second);
        }
        countCut();
        lookUpCard();

        if(lookUpCard()==null){
            generateNextKeystreamValue(); //calling the function again recursively if joker
        }
        key= lookUpCard().getValue();

        if(key!=numOfCards-1){
            return  key;
        }
        return 0;

    }

    public abstract class Card {
        public Card next;
        public Card prev;

        public abstract Card getCopy();

        public abstract int getValue();

    }

    public class PlayingCard extends Card {
        public String suit;
        public int rank;

        public PlayingCard(String s, int r) {
            this.suit = s.toLowerCase();
            this.rank = r;
        }

        public String toString() {
            String info = "";
            if (this.rank == 1) {
                //info += "Ace";
                info += "A";
            } else if (this.rank > 10) {
                String[] cards = {"Jack", "Queen", "King"};
                //info += cards[this.rank - 11];
                info += cards[this.rank - 11].charAt(0);
            } else {
                info += this.rank;
            }
            //info += " of " + this.suit;
            info = (info + this.suit.charAt(0)).toUpperCase();
            return info;
        }

        public PlayingCard getCopy() {
            return new PlayingCard(this.suit, this.rank);
        }

        public int getValue() {
            int i;
            for (i = 0; i < suitsInOrder.length; i++) {
                if (this.suit.equals(suitsInOrder[i]))
                    break;
            }

            return this.rank + 13 * i;
        }

    }

    public class Joker extends Card {
        public String redOrBlack;

        public Joker(String c) {
            if (!c.equalsIgnoreCase("red") && !c.equalsIgnoreCase("black"))
                throw new IllegalArgumentException("Jokers can only be red or black");

            this.redOrBlack = c.toLowerCase();
        }

        public String toString() {
            //return this.redOrBlack + " Joker";
            return (this.redOrBlack.charAt(0) + "J").toUpperCase();
        }

        public Joker getCopy() {
            return new Joker(this.redOrBlack);
        }

        public int getValue() {
            return numOfCards - 1;
        }

        public String getColor() {
            return this.redOrBlack;
        }
    }



}
