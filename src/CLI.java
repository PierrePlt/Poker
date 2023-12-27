import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CLI {
    private static String cardTemplate = ".------.\n" +
            "|??    |\n" +
            "|      |\n" +
            "|      |\n" +
            "|    ??|\n" +
            "'------'";

    public static List<Player> getPlayers() {
        List<Player> players = new ArrayList<>();
        System.out.println("How many players?");
        int input = (new Scanner(System.in)).nextInt();
        for (int i = 0; i < input; i++) {
            System.out.println("Player " + i + " name?");
            String name = (new Scanner(System.in)).nextLine();
            System.out.println("Player " + i + " money?");
            int money = (new Scanner(System.in)).nextInt();
            players.add(new Player(name, money));
        }
        return players;
    }

    public static List<String> cardStrings(List<Card> cards, int nbCards) {
        List<String> cardList = new ArrayList<>(nbCards);
        for (int i = 0; i < nbCards; i++) {
            if (cards.size() > i) {
                String template = new String(cardTemplate);
                if (cards.get(i).getValue() == CardValue.TEN) {
                    template = template.replaceFirst("\\? ", "?");
                    template = template.replaceFirst(" \\?", "?");
                }
                template = template.replaceAll("\\?\\?", cards.get(i).getSymbol());
                cardList.add(template);
            }
            else {
                cardList.add(cardTemplate);
            }
        }
        return cardList;
    }

    public static void printCardList(List<String> cards) {
        for (int line = 0; line < 6; line++) {
            for (String card : cards) {
                System.out.print(card.split("\n")[line] + " ");
            }
            System.out.println();
        }
    }

    public static void gameStatus(Game g) {
        List<String> cards = cardStrings(g.getCards(), 5);
        printCardList(cards);
        System.out.println("Total bet: " + g.getTotalBet());
        System.out.println("Current bid: " + g.getCurrentBid());
    }

    public static int inGameOptions(Player p, boolean canCheck) {
        System.out.println("1. Call");
        System.out.println("2. Raise");
        System.out.println("3. Fold");
        if (canCheck)
            System.out.println("4. Check");
        Scanner scanner = new Scanner(System.in);
        int input = scanner.nextInt();
        return input;
    }

    public static int askForBid(Player p) {
        System.out.println("How much do you want to bid?");
        System.out.println("You have " + p.getMoney() + "€.");
        Scanner scanner = new Scanner(System.in);
        int input = scanner.nextInt();
        return input;
    }

    public static void winnerScreen(Game game, Player winner, Hand winningHand) {
        clear();
        gameStatus(game);
        System.out.println("Winner hand:");
        printCardList(cardStrings(winner.getCards(), 2));
        System.out.println("It's a " + winningHand);
        System.out.println("Winner: \033[48;5;42m" + winner.getName() + "\033[0m");
        System.out.println("Money: " + winner.getMoney());
        System.out.println("Press enter to continue.");
    }

    public static void clear() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

}
