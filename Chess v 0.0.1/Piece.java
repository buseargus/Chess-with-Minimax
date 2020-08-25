package Chess;

public class Piece {
    private int line, column, oldLine, oldColumn;
    private final String color, name;
    private final int point;

    Piece(int line, int column, String color, String name, int point) {
        this.line = line;
        this.column = column;
        this.color = color;
        this.name = name;
        this.point = point;
    }

    Piece(Piece piece) {
        this.line = piece.getLine();
        this.column = piece.getColumn();
        this.color = piece.getColor();
        this.name = piece.getName();
        this.point = piece.getPoint();
        this.oldLine = piece.getOldLine();
        this.oldColumn = piece.getOldColumn();
    }

    public boolean pieceMove(int newLine, int newColumn, Piece[][] board, String color) {
        this.setOldLine(line);
        this.setOldColumn(column);
        this.setLine(newLine);
        this.setColumn(newColumn);
        return true;
    }

    boolean plusConflict(int newLine, int newColumn, Piece[][] board) {
        // Aynı satırda çakışma
        if (newColumn == column) {
            // Kuzey kontrolü
            if (newLine > line) {
                for (int i = line + 1; i < newLine; i++) {
                    Piece p = board[i][column];
                    if (p != null) return false;
                }
            }
            // Güney kontrolü
            else if (newLine < line) {
                for (int i = line - 1; i > newLine; i--) {
                    Piece p = board[i][column];
                    if (p != null) return false;
                }
            }
        }
        // Aynı sütunda çakışma
        else if (newLine == line) {
            // Doğu kontrolü
            if (newColumn > column) {
                for (int i = column + 1; i < newColumn; i++) {
                    Piece p = board[line][i];
                    if (p != null) return false;
                }
            }
            // Batı kontrolü
            else {
                for (int i = column - 1; i > newColumn; i--) {
                    Piece p = board[line][i];
                    if (p != null) return false;
                }
            }
        }
        return true;
    }

    boolean crossConflict(int newLine, int newColumn, Piece[][] board) {
        int x = column;
        // Kuzey doğu yönü için çakışma
        if (newLine - line > 0 && newColumn - column > 0) {
            for (int i = line + 1; i < newLine; i++) {
                Piece p = board[i][++x];
                if (p != null) return false;
            }
        }
        // Kuzey batı yönü için çakışma
        else if (newLine - line > 0 && newColumn - column < 0) {
            for (int i = line + 1; i < newLine; i++) {
                Piece p = board[i][--x];
                if (p != null) return false;
            }
        }
        // Güney doğu yönü için çakışma
        else if (newLine - line < 0 && newColumn - column > 0) {
            for (int i = line - 1; i > newLine; i--) {
                Piece p = board[i][++x];
                if (p != null) return false;
            }
        }
        // Güney batı yönü için çakışma
        else if (newLine - line < 0 && newColumn - column < 0) {
            for (int i = line - 1; i > newLine; i--) {
                Piece p = board[i][--x];
                if (p != null) return false;
            }
        }

        return true;
    }

    int getLine() {
        return line;
    }

    int getColumn() {
        return column;
    }

    int getOldLine() {
        return oldLine;
    }

    int getOldColumn() {
        return oldColumn;
    }

    int getPoint() {
        return point;
    }

    String getColor() {
        return color;
    }

    String getName() {
        return name;
    }

    void setLine(int line) {
        this.line = line;
    }

    private void setColumn(int column) {
        this.column = column;
    }

    private void setOldLine(int oldLine) {
        this.oldLine = oldLine;
    }

    private void setOldColumn(int oldColumn) {
        this.oldColumn = oldColumn;
    }
}

class Pawn extends Piece {
    private boolean firstMove = true;

    Pawn(int line, int column, String color, String name) {
        super(line, column, color, name, 10);
    }

    Pawn(Piece piece) {
        super(piece);
    }

    public boolean pieceMove(int newLine, int newColumn, Piece[][] board, String color) {
        int line = super.getLine();
        int column = super.getColumn();

        // Aynı sütunda hareket
        if (newColumn == column && board[newLine][newColumn] == null) {
            if (newLine == line + 1 || (firstMove && newLine == line + 2 && board[line + 1][column] == null)) {
                firstMove = false;
                return super.pieceMove(newLine, newColumn, board, color);
            }

            // Karşı taşı yeme (çapraz hareket)
        } else if (Math.abs(newColumn - column) == 1 && board[newLine][newColumn] != null) {
            if (newLine == line + 1 && !board[newLine][newColumn].getColor().equals(color))
                return super.pieceMove(newLine, newColumn, board, color);
        }

        return false;
    }

}

class Rook extends Piece {

    Rook(int line, int column, String color, String name) {
        super(line, column, color, name, 50);
    }

    Rook(Piece piece) {
        super(piece);
    }

    // Rook + şeklinde hareket edebilir.
    public boolean pieceMove(int newLine, int newColumn, Piece[][] board, String color) {
        int line = this.getLine();
        int column = this.getColumn();

        if (newLine == line || newColumn == column) {
            if (super.plusConflict(newLine, newColumn, board)) {
                Piece p = board[newLine][newColumn];
                if (p == null || !p.getColor().equals(color))
                    return super.pieceMove(newLine, newColumn, board, color);
            }
        }
        return false;
    }
}

class Bishop extends Piece {

    Bishop(int line, int column, String color, String name) {
        super(line, column, color, name, 30);
    }

    Bishop(Piece piece) {
        super(piece);
    }

    public boolean pieceMove(int newLine, int newColumn, Piece[][] board, String color) {
        int line = super.getLine();
        int column = super.getColumn();

        if (Math.abs(newLine - line) == Math.abs(newColumn - column)) {
            // Çapraz çakışma kontrolü yapılır
            if (super.crossConflict(newLine, newColumn, board)) {
                Piece p = board[newLine][newColumn];
                if (p == null || !p.getColor().equals(color))
                    return super.pieceMove(newLine, newColumn, board, color);
            }
        }

        return false;
    }

}

class Queen extends Piece {

    Queen(int line, int column, String color, String name) {
        super(line, column, color, name, 90);
    }

    Queen(Piece piece) {
        super(piece);
    }

    public boolean pieceMove(int newLine, int newColumn, Piece[][] board, String color) {
        int line = super.getLine();
        int column = super.getColumn();

        // Çapraz hareket durumu
        if (Math.abs(newLine - line) == Math.abs(newColumn - column)) {
            // Çapraz çakışma kontrolü
            if (super.crossConflict(newLine, newColumn, board)) {
                Piece p = board[newLine][newColumn];
                if (p == null || !p.getColor().equals(color))
                    return super.pieceMove(newLine, newColumn, board, color);
            }
        }
        // Aynı satır/sütun hareket durumu
        else if (newColumn == column || newLine == line) {
            // Aynı satır/sütun çakışma kontrolü
            if (super.plusConflict(newLine, newColumn, board)) {
                Piece p = board[newLine][newColumn];
                if (p == null || !p.getColor().equals(color))
                    return super.pieceMove(newLine, newColumn, board, color);
            }
        }

        return false;
    }
}

class King extends Piece {
    King(int line, int column, String color, String name) {
        super(line, column, color, name, 900);
    }

    King(Piece piece) {
        super(piece);
    }

    // King etrafındaki 1 mesafeli her konuma hareket edebilir
    public boolean pieceMove(int newLine, int newColumn, Piece[][] board, String color) {
        int lineDif = Math.abs(newLine - super.getLine());
        int columnDif = Math.abs(newColumn - super.getColumn());

        // Girilen newLine ve newColumn mesafeleri -1, 0 veya 1 ise harekete izin verilir
        if ((lineDif == 0 || lineDif == 1) && (columnDif == 0 || columnDif == 1)) {
            Piece p = board[newLine][newColumn];
            if (p == null || !p.getColor().equals(color))
                return super.pieceMove(newLine, newColumn, board, color);
        }

        return false;
    }
}

class Knight extends Piece {

    Knight(int line, int column, String color, String name) {
        super(line, column, color, name, 30);
    }

    Knight(Piece piece) {
        super(piece);
    }

    // Knight L şeklinde hareket edebilir.
    public boolean pieceMove(int newLine, int newColumn, Piece[][] board, String color) {
        int line = super.getLine();
        int column = super.getColumn();

        // Satırda 2 sütunda 1 hareket koşulu
        boolean Line2Column1 = (Math.abs(newLine - line) == 2 && Math.abs(newColumn - column) == 1);

        // Sütunda 2 satırda 1 hareket koşulu
        boolean Line1Column2 = (Math.abs(newLine - line) == 1 && Math.abs(newColumn - column) == 2);

        // İki koşuldan birini sağlıyorsa gideceği konuma bakılır
        if (Line2Column1 || Line1Column2) {
            Piece p = board[newLine][newColumn];
            if (p == null || !p.getColor().equals(color))
                return super.pieceMove(newLine, newColumn, board, color);
        }

        return false;
    }
}