import java.util.List;


public class Main {
    public static void main(String[] args) {
        CLI.clear();
        List<Player> players = CLI.getPlayers();
        Game g = new Game(players);

        g.startGame();
    }
}