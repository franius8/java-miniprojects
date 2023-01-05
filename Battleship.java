package battleship;

import java.util.Arrays;
import java.util.Scanner;

public class Battleship {

    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        Player player1 = new Player(1);
        Player player2 = new Player(2);

        collectCoordinates(player1);
        System.out.print("Press Enter and pass the move to another player");
        scanner.nextLine();
        collectCoordinates(player2);
        System.out.print("Press Enter and pass the move to another player");
        scanner.nextLine();

        while (true) {
            if (turn(player2, player1)) break;
            System.out.println("Press Enter and pass the move to another player");
            scanner.nextLine();
            if (turn(player1, player2)) break;
            System.out.println("Press Enter and pass the move to another player");
            scanner.nextLine();
        }
    }

    public static void collectCoordinates(Player player) {
        Board boardObject = player.getBoard();
        Ship[] ships = player.getShips();
        System.out.println("Player " + player.number + ", place your ships on the game field");
        boardObject.printVisibleBoard();
        for (Ship ship: ships) {
            char[][] board = boardObject.getBoard();
            System.out.println("Enter the coordinates of the " + ship.name + " (" + ship.length + " cells):");
            outerloop:
            while (true) {
                String coordinates = scanner.nextLine();
                String begin = coordinates.split(" ")[0];
                String end = coordinates.split(" ")[1].replace("\n", "");
                char beginRow = begin.charAt(0);
                int beginColumn = Integer.parseInt(begin.substring(1));
                char endRow = end.charAt(0);
                int endColumn = Integer.parseInt(end.substring(1));
                boolean vertical = beginRow != endRow;
                if (beginColumn > endColumn) {
                    int temp = beginColumn;
                    beginColumn = endColumn;
                    endColumn = temp;

                }
                if (beginRow > endRow) {
                    char temp = beginRow;
                    beginRow = endRow;
                    endRow = temp;
                }
                if (vertical) {
                    if (endRow - beginRow + 1 != ship.length) {
                        System.out.println("Error! Wrong length of the " + ship.name + "! Try again:");
                    } else if (beginColumn != endColumn) {
                        System.out.println("Error! Wrong ship location! Try again:");
                    } else {
                        for (char i = beginRow; i <= endRow; i++) {
                            if (boardObject.shipNearby(i - 65, beginColumn - 1)) {
                                System.out.println("Error! You placed it too close to another one. Try again:");
                                continue outerloop;
                            }
                        }
                        for (char i = beginRow; i <= endRow; i++) {
                            board[i - 65][beginColumn - 1] = 'O';
                            String coordinate = Character.toString(i) + beginColumn;
                            int index = i - beginRow;
                            ship.setPosition(index, coordinate);
                            player.setShips(ships);
                        }
                        break;
                    }
                } else {
                    if (endColumn - beginColumn + 1 != ship.length) {
                        System.out.println("Error! Wrong length of the " + ship.name + "! Try again:");
                    } else {
                        for (int i = beginColumn; i <= endColumn; i++) {
                            if (boardObject.shipNearby(beginRow - 65, i - 1)) {
                                System.out.println("Error! You placed it too close to another one. Try again:");
                                continue outerloop;
                            }
                        }
                        for (int i = beginColumn; i <= endColumn; i++) {
                            board[beginRow - 65][i - 1] = 'O';
                            String coordinate = Character.toString(beginRow) + i;
                            ship.setPosition(i - beginColumn, coordinate);
                            player.setShips(ships);
                        }
                        break;
                    }
                }
            }
            boardObject.setBoard(board);
            boardObject.printVisibleBoard();
        }

    }
    private static boolean turn(Player opponent, Player player) {
        Board boardObject = opponent.getBoard();
        char[][] board = boardObject.getBoard();
        Ship[] ships = opponent.getShips();
        boardObject.printHiddenBoard();
        System.out.println("---------------------");
        player.getBoard().printVisibleBoard();
        System.out.println("Player " + player.number + ", it's your turn:");

        String shot;
        char row;
        int column;
        while (true) {
            shot = scanner.nextLine();
            row = shot.charAt(0);
            column = Integer.parseInt(shot.replace("\n", "").substring(1)) - 1;
            if (row - 65 > 10 || column > 9) {
                System.out.println("Error! You entered the wrong coordinates! Try again:");
            } else {
                break;
            }
        }
        if (board[row - 65][column] == '~') {
            board[row - 65][column] = 'M';
            boardObject.printHiddenBoard();
            System.out.println("You missed!");
        } else {
            board[row - 65][column] = 'X';
            boardObject.printHiddenBoard();
            Ship hitShip = null;
            for (Ship ship: ships) {
                if (Arrays.asList(ship.getPosition()).contains(shot)) {
                    hitShip = ship;
                    break;
                }
            }
            assert hitShip != null;
            if (hitShip.hit()) {
                boolean allSunk = true;
                for (Ship ship: ships) {
                    if (!ship.isSunk) {
                        allSunk = false;
                        break;
                    }
                }
                if (allSunk) {
                    System.out.println("You sank the last ship. You won. Congratulations!");
                    return true;
                } else {
                    System.out.println("You sank a ship! Specify a new target:");
                }
            } else {
                System.out.println("You hit a ship!");
            }
        }
        return false;
    }
}

class Board {
    char[][] board;

    public Board() {
        board = new char[10][10];
        for (char[] c:board) {
            Arrays.fill(c, '~');
        }
    }

    public char[][] getBoard() {
        return board;
    }

    public void setBoard(char[][] board) {
        this.board = board;
    }

    public void printVisibleBoard() {
        _printBoard(false);
    }

    public void printHiddenBoard() {
        _printBoard(true);
    }
    private void _printBoard(boolean hidden) {
        System.out.print(" ");
        for (int i = 1; i <= board.length; i++) {
            System.out.print(" " + i);
        }
        System.out.print("\n");
        char rows = 'A';
        for (char[] chars: board) {
            System.out.print(rows);
            for (char field: chars) {
                if (hidden && field == 'O') {
                    System.out.print(" ~");
                } else {
                    System.out.print(" " + field);
                }
            }
            System.out.print("\n");
            rows++;
        }
    }

    public boolean shipNearby (int y, int x) {
        for(int i = -1; i <= 1; i++) {
            try {
                if (board[y - 1][x + i] != '~') {
                    return true;
                }
            } catch (Exception ignored) { }
            try {
                if (board[y + 1][x + i] != '~') {
                    return true;
                }
            } catch (Exception ignored) { }
            if (i != 0) {
                try {
                    if (board[y][x + i] != '~') {
                        return true;
                    }
                } catch (Exception ignored) { }
            }
        }
        return false;
    }
}

class Player {
    int number;
    Board board;
    Ship[] ships;

    public Player(int number) {
        this.number = number;
        board = new Board();
        ships = new Ship[5];
        ships[0] = new Ship(ShipEnum.CARRIER.name, ShipEnum.CARRIER.length);
        ships[1] = new Ship(ShipEnum.BATTLESHIP.name, ShipEnum.BATTLESHIP.length);
        ships[2] = new Ship(ShipEnum.SUBMARINE.name, ShipEnum.SUBMARINE.length);
        ships[3] = new Ship(ShipEnum.CRUISER.name, ShipEnum.CRUISER.length);
        ships[4] = new Ship(ShipEnum.DESTROYER.name, ShipEnum.DESTROYER.length);
    }

    public Board getBoard() {
        return board;
    }

    public Ship[] getShips() {
        return ships;
    }

    public void setShips(Ship[] ships) {
        this.ships = ships;
    }
}

class Ship {
    final String name;
    final int length;
    final String[] position;
    int timesHit = 0;
    boolean isSunk = false;

    Ship(String name, int length) {
        this.name = name;
        this.length = length;
        this.position = new String[length];
    }

    public boolean hit() {
        timesHit++;
        if (timesHit == length) {
            isSunk = true;
            return true;
        } else {
            return false;
        }
    }

    public void setPosition(int index, String coordinate) {
        position[index] = coordinate;
    }

    public String[] getPosition() {
        return position;
    }
}

enum ShipEnum {
    CARRIER("Aircraft Carrier", 5),
    BATTLESHIP("Battleship", 4),
    SUBMARINE("Submarine", 3),
    CRUISER("Cruiser", 3),
    DESTROYER("Destroyer", 2);

    final String name;
    final int length;

    ShipEnum(String name, int length) {
        this.name = name;
        this.length = length;
    }
}
