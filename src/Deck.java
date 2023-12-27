import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Deck {
    private final List<Card> cardList = new ArrayList<>(48);

    public static Deck getDeck() {
        Deck deck = new Deck();
        for (int i = 1; i < 14; i++) {
            for (int j = 0; j < 4; j++) {
                Card c = new Card(CardValue.values()[i], CardColor.values()[j]);
                deck.cardList.add(c);
            }
        }
        return deck;
    }

    public void mix() {
        Random r = new Random();
        for (int i = 0; i < 100; i++) {
            int pos1 = r.nextInt(52);
            int pos2 = r.nextInt(52);
            Card c = cardList.get(pos1);
            cardList.set(pos1, cardList.get(pos2));
            cardList.set(pos2, c);
        }
    }

    public Card unstack() {
        return cardList.removeLast();
    }
}
