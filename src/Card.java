public class Card implements Comparable<Card>{
    private final CardValue value;
    private final CardColor color;

    public Card(CardValue value, CardColor color) {
        this.value = value;
        this.color = color;
    }

    public CardColor getColor() {
        return color;
    }

    public CardValue getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString().toLowerCase() + " of " + color.toString().toLowerCase();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Card) {
            Card c = (Card) obj;
            return c.getColor() == this.getColor() && c.getValue() == this.getValue();
        }
        return false;
    }

    public String getSymbol() {
        String symbol = "";
        switch (this.color) {
            case HEARTS:
                symbol += "♥";
                break;
            case SPADES:
                symbol += "♠";
                break;
            case DIAMONDS:
                symbol += "♦";
                break;
            case CLUBS:
                symbol += "♣";
                break;
        }
        switch (this.value) {
            case ACE:
                symbol += "A";
                break;
            case KING:
                symbol += "K";
                break;
            case QUEEN:
                symbol += "Q";
                break;
            case JACK:
                symbol += "J";
                break;
            case TEN:
                symbol += "10";
                break;
            case NINE:
                symbol += "9";
                break;
            case EIGHT:
                symbol += "8";
                break;
            case SEVEN:
                symbol += "7";
                break;
            case SIX:
                symbol += "6";
                break;
            case FIVE:
                symbol += "5";
                break;
            case FOUR:
                symbol += "4";
                break;
            case THREE:
                symbol += "3";
                break;
            case TWO:
                symbol += "2";
                break;
            default:
                symbol += "?";
                break;
        }
        return symbol;
    }

    @Override
    public int compareTo(Card o) {
        return this.getValue().ordinal() - o.getValue().ordinal();
    }
}
