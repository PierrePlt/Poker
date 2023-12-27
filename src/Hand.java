import java.util.ArrayList;
import java.util.List;

public class Hand implements Comparable<Hand>{
    private static final int SINGLE = 0;
    private static final int PAIR = 1;
    private static final int DOUBLE_PAIR = 2;
    private static final int THREE = 3;
    private static final int STRAIGHT = 4;
    private static final int FLUSH = 5;
    private static final int FULL_HOUSE = 6;
    private static final int FOUR = 7;
    private static final int STRAIGHT_FLUSH = 8;
    private static final int ROYAL_FLUSH = 9;

    private int type = -1;
    private int value = -1;
    private int[] values = null;

    private Hand(int type, int value) {
        this.type = type;
        this.value  = value;
    }

    private Hand(int type, int values[]) {
        this.type = type;
        this.values = values;
    }

    public static List<Hand> evaluate(List<Card> cards) {
        List<Hand> hands = new ArrayList<>();
        
        hands.add(royalFlushValue(cards));
        hands.add(straightFlushValue(cards));
        hands.add(fourValue(cards));
        hands.add(fullHouseValue(cards));
        hands.add(flushValue(cards));
        hands.add(straightValue(cards));
        hands.add(threeValue(cards));
        hands.add(doublePairValue(cards));
        hands.add(singleValue(cards));

        return hands.stream().filter(h -> h != null).toList();
    }

    private static Hand singleValue(List<Card> cards) {
        int value = 0;
        for (Card c : cards) {
            value = max(c.getValue().ordinal(), value);
        }
        return new Hand(SINGLE, value);
    }

    private static Hand doublePairValue(List<Card> cards) {
        List<Card> copiedList = new ArrayList<>(cards);
        copiedList.sort((a, b) -> b.compareTo(a));
        int[] pairValues = new int[2];
        int pairCount = 0;
        for (int i = 0; i < copiedList.size(); i++) {
            for (int j = i+1; j < copiedList.size(); j++) {
                Card c1 = copiedList.get(i);
                Card c2 = copiedList.get(j);
                if (c1.getValue() == c2.getValue() && !c1.equals(c2)) {
                    pairValues[pairCount] = c1.getValue().ordinal();
                    pairCount++;
                    copiedList.remove(c1);
                    copiedList.remove(c2);
                    j--;
                    i--;
                    break;
                }
            }

        }
        if (pairCount == 0)
            return null;
        if (pairCount == 1)
            return new Hand(PAIR, pairValues[0]);
        return new Hand(DOUBLE_PAIR, pairValues);
    }

    private static Hand threeValue(List<Card> cards) {
        int value = 0;
        for (Card c1 : cards) {
            int matches = 1;
            for (Card c2 : cards) {
                if (!c1.equals(c2) && c1.getValue() == c2.getValue())
                    matches += 1;
            }
            if (matches == 3) {
                value = c1.getValue().ordinal();
                break;
            }
        }
        return value == 0 ? null : new Hand(THREE, value);
    }

    private static Hand fourValue(List<Card> cards) {
        int value = 0;
        for (Card c1 : cards) {
            int matches = 1;
            for (Card c2 : cards) {
                if (!c1.equals(c2) && c1.getValue() == c2.getValue())
                    matches += 1;
            }
            if (matches == 4) {
                value = c1.getValue().ordinal();
                break;
            }
        }
        return value == 0 ? null : new Hand(FOUR, value);
    }

    private static Hand flushValue(List<Card> cards) {
        int value = 0;
        for (Card c1 : cards.stream().sorted((a,b) -> b.compareTo(a)).toList()) {
            int matches = 1;
            for (Card c2 : cards) {
                if (!c1.equals(c2) && c1.getColor() == c2.getColor())
                    matches += 1;
            }
            if (matches == 5) {
                value = c1.getValue().ordinal();
            }
        }
        return value == 0 ? null : new Hand(FLUSH, value);
    }

    private static Hand straightValue(List<Card> cards) {
        int value = 0;
        int counter = 0;
        List<Card> hand = cards.stream().sorted((a, b) -> b.compareTo(a)).toList();
        int i;
        for (i = 1; i < hand.size(); i++) {
            if (hand.get(i).getValue().ordinal() == hand.get(i - 1).getValue().ordinal()) {
                counter++;
            }
            if (counter == 5)
                break;
        }
        value = counter == 5 ? hand.get(i).getValue().ordinal() : 0;
        return value == 0 ? null : new Hand(STRAIGHT, value);
    }

    private static Hand fullHouseValue(List<Card> cards) {
        List<Card> hand = new ArrayList<>(cards);
        Hand threeValue = threeValue(hand);
        if (threeValue == null)
            return null;
        hand = hand.stream().filter(c -> c.getValue().ordinal() == threeValue.value).toList();
        if (doublePairValue(hand) != null)
            return new Hand(FULL_HOUSE, threeValue(cards).value);
        return null;
    }

    private static Hand straightFlushValue(List<Card> cards) {
        Hand valueHand = straightValue(cards);
        if (valueHand != null && flushValue(cards) != null)
            return new Hand(STRAIGHT_FLUSH, valueHand.value);
        return null;
    }

    private static Hand royalFlushValue(List<Card> cards) {
        Hand hand = straightFlushValue(cards);
        if (hand == null)
            return null;
        if (hand.value == CardValue.ACE.ordinal())
            return new Hand(ROYAL_FLUSH, hand.value);
        return null;
    }

    private static int max(int a, int b) {
        return a > b ? a : b;
    }

    @Override
    public int compareTo(Hand o) {
        if (this.type > o.type)
            return 1;
        else if (this.type < o.type)
            return -1;
        else {
            if (this.value > o.value)
                return 1;
            else if (this.value < o.value)
                return -1;
            else {
                if (this.values == null)
                    return 0;
                int valSum1 = 0;
                int valSum2 = 0;
                for (int i = 0; i < this.values.length; i++) {
                    valSum1 += this.values[i];
                    valSum2 += o.values[i];
                }
                if (valSum1 > valSum2)
                    return 1;
                else if (valSum1 < valSum2)
                    return -1;
            }
        }
        return 0;
    }

    @Override
    public String toString() {
        String str = "";
        switch (this.type) {
            case SINGLE:
                str += "Single";
                break;
            case PAIR:
                str += "Pair";
                break;
            case DOUBLE_PAIR:
                str += "Double pair";
                break;
            case THREE:
                str += "Three";
                break;
            case STRAIGHT:
                str += "Straight";
                break;
            case FLUSH:
                str += "Flush";
                break;
            case FULL_HOUSE:
                str += "Full house";
                break;
            case FOUR:
                str += "Four";
                break;
            case STRAIGHT_FLUSH:
                str += "Straight flush";
                break;
            case ROYAL_FLUSH:
                str += "Royal flush";
                break;
        }
        return str;
    }
}
