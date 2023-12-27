import java.util.ArrayList;
import java.util.List;

public class Player {
    private List<Card> cards = new ArrayList<>();
    private int money;
    private boolean inGame = true;
    private String name;


    public Player(String name, int money) {
        this.money = money;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getMoney() {
        return money;
    }

    public boolean inGame() {
        return inGame;
    }

    public void startGame() {
        inGame = true;
    }

    public void giveCard(Card c) {
        this.cards.add(c);
    }

    public int call(Game g) {
        int bid = g.getCurrentBid();
        if (bid > this.money) {
            this.inGame = false;
            System.out.println("You don't have enough money to call, you're out.");
            return -2;
        }
        this.money -= bid;
        return bid;
    } 

    public int raise(Game g) {
        int bid = CLI.askForBid(this);
        if (bid > this.money) {
            System.out.println("You don't have enough money.");
            if (g.getCurrentBid() > this.money) {
                this.inGame = false;
                System.out.println("You don't have enough money to call, you're out.");
                return -2;
            }
            else
                raise(g); // Theorically we could cause the program to stack overflow by calling that too much
        }
        this.money -= bid;
        return bid;
    }

    public int turn(Game g, boolean canCheck) {
        System.out.println("\033[48;5;42mPlayer: " + this.name + "\033[0m");
        CLI.gameStatus(g);
        System.out.println("Your hand:");
        CLI.printCardList(CLI.cardStrings(cards, 2));
        int selection = CLI.inGameOptions(this, canCheck);
        switch (selection) {
            case 1:
                return call(g);
            case 2:
                return raise(g);
            case 3:
                this.inGame = false;
                return -2;
            case 4:
                return 0;
            default:
                return -1;

        }
    }

    public int placeBlind(int value) {
        if (value > this.money) {
            this.inGame = false;
            return 0;
        }
        this.money -= value;
        return value;
    }

    public void win(int value) {
        this.money += value;
    }

    public void lose() {
        this.inGame = false;
    }

    public List<Card> getCards() {
        return this.cards;
    }
}
