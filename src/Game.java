import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

public class Game {
    private List<Player> players = new ArrayList<>();
    private List<Card> cards = new ArrayList<>();
    private Deck deck = Deck.getDeck();
    private int totalBet = 0;
    private int blindValue = 0;
    private int currentBid = 0;

    public Game(List<Player> players) {
        this.players = players;

    }

    public int getCurrentBid() {
        return currentBid;
    }

    public int getTotalBet() {
        return totalBet;
    }

    public void startGame() {
        this.totalBet = 0;
        this.deck.mix();

        System.out.print("Small blind: ");
        blindValue = (new Scanner(System.in)).nextInt();

        this.setup();
        if (players.size() > 2)
            this.roundOne();
        this.roundTwo();
        this.roundThreeFour();
        this.roundThreeFour();
        this.endGame();
    }

    public List<Card> getCards() {
        return this.cards;
    }

    private void setup() {
        for (Player p : players) {
            p.giveCard(this.deck.unstack());
            p.giveCard(this.deck.unstack());
        }
        for (int i = 0; i < 2; i++) {
            this.totalBet += players.get(i).placeBlind(blindValue * (i + 1));
        }
    }

    private boolean placeBid(Player p, boolean canCheck) { // True if player has placed a bid
        int bid = p.turn(this, canCheck);
        if (bid > this.currentBid)
            currentBid = bid;
        this.totalBet += bid;
        return bid > 0;
    }

    private void roundOne() {
        boolean canCheck = false;
        boolean firstRound = true;
        this.currentBid = this.blindValue*2;
        while (!canCheck ) {
            canCheck = true;
            for (int i = firstRound ? 2 : 0; i < players.size(); i++) {
                if (players.get(i).inGame()) {
                    canCheck = !placeBid(players.get(i), canCheck && !firstRound) && canCheck;
                }
            }
            firstRound = false;

        }
        CLI.gameStatus(this);
    }

    private void roundTwo() {
        CLI.clear();
        this.currentBid = this.blindValue * 2;
        for (int i = 0; i < 3; i++) {
            this.cards.add(this.deck.unstack());
        }
        boolean canCheck;
        do {
            canCheck = true;
            for (int i = 0; i < players.size(); i++) {
                if (players.get(i).inGame()) {
                    CLI.clear();
                    canCheck = !placeBid(players.get(i), canCheck) && canCheck;
                }
            }
        } while (!canCheck);
    }

    private void roundThreeFour() {
        CLI.clear();
        this.cards.add(this.deck.unstack());
        CLI.gameStatus(this);
        boolean canCheck;
        do {
            canCheck = true;
            for (int i = 0; i < players.size(); i++) {
                if (players.get(i).inGame()) {
                    CLI.clear();
                    canCheck = !placeBid(players.get(i), canCheck) && canCheck;
                }
            }
        } while (!canCheck);
        CLI.gameStatus(this);
    }

    private void endGame() {
        Player winner = getWinner();
        CLI.clear();
        CLI.winnerScreen(this, winner,
                Hand.evaluate(Stream.concat(this.cards.stream(), winner.getCards().stream()).toList()).get(0));
    }

    public Player getWinner() {
        Player winner = null;
        for (Player p1 : players) {
            boolean wonAll = true;
            List<Card> hand1 = Stream.concat(this.cards.stream(), p1.getCards().stream()).toList();
            for (Player p2 : players) {
                if (p1.getName() == p2.getName())
                    continue;
                List<Card> hand2 = Stream.concat(this.cards.stream(), p2.getCards().stream()).toList();
                if (Hand.evaluate(hand1).get(0).compareTo(Hand.evaluate(hand2).get(0)) < 0) {
                    wonAll = false;
                    break;
                }
                if (!wonAll)
                    break;
            }
            if (wonAll) {
                winner = p1;
                break;
            }
        }
        return winner;
    }
}
