import java.util.*;

class Position {


    static final int DIM = 4;
    static final int SIZE = DIM * DIM;
    char turn;
    private char[] board;
    private Map<Integer, Integer> cache = new HashMap<>(); //gyorsítás

    Position() {
        turn = 'x';
        board = new char[SIZE];
        for (int i = 0; i < SIZE; i++) {
            board[i] = ' ';
        }
    }

    Position move(int index) {
        board[index] = turn;
        turn = turn == 'x' ? 'o' : 'x';
        return this;
    }

    private void unMove(int index) {
        board[index] = ' ';
        turn = turn == 'x' ? 'o' : 'x';
    }

    private List<Integer> possibleMoves() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < board.length; i++) {
            if (board[i] == ' ') {
                list.add(i);
            }
        }
        return list;
    }

    boolean isWinFor(char turn) {
        boolean isWin = false;
        for (int i = 0; i < SIZE; i += DIM) {
            if (lineMatch(turn, i, i + DIM, 1)) {
                isWin = true;
            }
        }
        for (int i = 0; i < DIM; i++) {
            if (lineMatch(turn, i, SIZE, DIM)) {
                isWin = true;
            }
        }
        isWin = isWin || lineMatch(turn, 0, SIZE, DIM + 1);
        isWin = isWin || lineMatch(turn, DIM - 1, SIZE - 1, DIM - 1);
        return isWin;
    }

    private boolean lineMatch(char turn, int start, int end, int step) {
        for (int i = start; i < end; i += step) {
            if (board[i] != turn) {
                return false;
            }
        }
        return true;
    }

    private int blanks() {
        int total = 0;
        for (int i = 0; i < SIZE; i++) {
            if (board[i] == ' ') {
                total++;
            }
        }
        return total;
    }

    private int code() {
        int value = 0;
        for (int i = 0; i < SIZE; i++) {
            value = value * 3;
            if (board[i] == 'x') {
                value += 1;
            } else if (board[i] == 'o')
                value += 2;
        }
        return value;
    }

    private int miniMax() {
        Integer key = code();
        Integer value = cache.get(key);
        //@formatter:off
        if (value!=null)return value;
        if (isWinFor('x'))return blanks();
        if (isWinFor('o'))return -blanks();
        if (blanks() == 0)return 0;
        //@formatter:on

        List<Integer> list = new ArrayList<>();
        for (Integer i : possibleMoves()) {
            list.add(move(i).miniMax());
            unMove(i);
        }

        value = turn == 'x' ? Collections.max(list) : Collections.min(list);
        cache.put(key, value);
        return value;
    }

    int bestMove() {
        Comparator<Integer> comp = (first, second) -> {
            int a = move(first).miniMax();
            unMove(first);
            int b = move(second).miniMax();
            unMove(second);
            return a - b;
        };
        List<Integer> list = possibleMoves();
        return turn == 'x' ? Collections.max(list, comp) : Collections.min(list, comp);
    }

    boolean isGameEnd() {
        return isWinFor('x') || isWinFor('o') || blanks() == 0;
    }

}