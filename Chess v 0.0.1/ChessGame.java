package Chess;

import java.util.*;

public class ChessGame {

    private static String[] letters = {"a", "b", "c", "d", "e", "f", "g", "h"};

    public static void main(String[] args) {
        Piece[][] board = new Piece[8][8];
        beginning(board);

        boolean isWhite = true;
        int line, column, newLine, newColumn;
        StringTokenizer tokenizer;
        Hashtable<String, Integer> ht = new Hashtable<>();
        Scanner scanner = new Scanner(System.in);

        // sütunlar a'dan h'ye kadar olan harflerle ifade ediliyor.
        // bu harfler hashtable içinde sayılarla eşleştiriliyor.
        for (int i = 0; i < 8; i++)
            ht.put(letters[i], i);

        System.out.println("-----Satranç Oyununa Hoşgeldiniz-----");
        String mode = "";
        while (!mode.equals("1") && !mode.equals("2")) {
            System.out.println("Bilgisayara karşı oynamak için 1;\nİki kişi oynamak için 2 yazınız.");
            mode = scanner.nextLine();
        }


        while (true) {
            String color = isWhite ? "white" : "black";

            isWhite = !isWhite;
            printBoard(board);

            if (kingInCheck(board, color)) {
                if (checkmate(board, color)) {
                    String winner = isWhite ? "siyah" : "beyaz";
                    System.out.println("Şah ve mat! " + winner + " kazandı.");
                    break;
                }

                System.out.println("Şah!");
            }

            boolean flag = true;

            while(flag) {
                System.out.println("Oynanacak taşın ve hedefin konumu: ");

                // input "b 2 b 3" örneğindeki gibi (aralarda boşluk bırakarak) girilir.
                String input = scanner.nextLine();

                try {
                    // input ile alınanlar ayrıştırılıyor
                    if (input.equals("lCastle") || input.equals("sCastle")) {
                        castleMove(board, input);
                        flag = false;
                    } else {
                        tokenizer = new StringTokenizer(input.trim(), " ");
                        column = ht.get(tokenizer.nextToken());
                        line = Integer.parseInt(tokenizer.nextToken()) - 1;
                        newColumn = ht.get(tokenizer.nextToken());
                        newLine = Integer.parseInt(tokenizer.nextToken()) - 1;
                        // Piece classlarının içindeki hareket methodları
                        // taşın oynanıp oynanmadığına göre boolean değer döndürüyor.
                        Piece piece = board[line][column];

                        // Eğer taş oynanmışsa, taşın board içindeki konumu da değiştiriliyor
                        if (piece.pieceMove(newLine, newColumn, board, color)) {
                            move(board, piece);
                            flag = false;
                        } else
                            System.out.println("Hatalı hamle. Tekrar deneyin.");
                    }
                }
                catch(Exception e) {
                    System.out.println("Hatalı hamle. Tekrar deneyin.");
                }
            }

            if (mode.equals("1")) {
                // ai hamlesini yapıyor
                board = insertTree(board, 4);

                if (kingInCheck(board, color)) {
                    if (checkmate(board, color)) {
                        String winner = isWhite ? "siyah" : "beyaz";
                        System.out.println("Şah ve mat! " + winner + " kazandı.");
                        break;
                    }

                    System.out.println("Şah!");
                }
            } else if (mode.equals("2")) {
                board = rotate(board);
            }
        }

    }

    private static void printBoard(Piece[][] board) {
        // board ile satır ve sütun numaraları/harfleri yazdırılıyor.
        for (int i = 7; i >= 0; i--) {
            System.out.print(i + 1 + "  ");
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == null) {
                    System.out.print(" ---");
                } else {
                    System.out.print("  " + board[i][j].getName() + " ");
                }
            }
            System.out.println();
        }

        System.out.print("\n   ");

        for (int k = 0; k < 8; k++)
            System.out.print("  " + letters[k] + " ");

        System.out.println();
    }

    private static void beginning(Piece[][] board) {
        // Tahtanın ilk hali oluşturulur
        board[7][0] = new Rook(7, 0, "black", "R");
        board[7][1] = new Knight(7, 1, "black", "N");
        board[7][2] = new Bishop(7, 2, "black", "B");
        board[7][3] = new Queen(7, 3, "black", "Q");
        board[7][4] = new King(7, 4, "black", "K");
        board[7][5] = new Bishop(7, 5, "black", "B");
        board[7][6] = new Knight(7, 6, "black", "N");
        board[7][7] = new Rook(7, 7, "black", "R");
        for (int i = 0; i < 8; i++)
            board[6][i] = new Pawn(6, i, "black", "P");

        board[0][0] = new Rook(0, 0, "white", "r");
        board[0][1] = new Knight(0, 1, "white", "n");
        board[0][2] = new Bishop(0, 2, "white", "b");
        board[0][3] = new Queen(0, 3, "white", "q");
        board[0][4] = new King(0, 4, "white", "k");
        board[0][5] = new Bishop(0, 5, "white", "b");
        board[0][6] = new Knight(0, 6, "white", "n");
        board[0][7] = new Rook(0, 7, "white", "r");
        for (int i = 0; i < 8; i++)
            board[1][i] = new Pawn(1, i, "white", "p");
    }

    private static void castleMove(Piece[][] board, String token) {
        if (token.equals("lCastle") && board[0][4].getName().toLowerCase().equals("k")) {
            if (board[0][1] == null && board[0][2] == null && board[0][3] == null
                    && board[0][0].getName().toLowerCase().equals("r")) {
                board[0][2] = board[0][4];
                board[0][4] = null;
                board[0][3] = board[0][0];
                board[0][0] = null;
            } else System.out.println("Hatalı hamle.");
        } else if (token.equals("rCastle") && board[0][4].getName().toLowerCase().equals("k")) {
            if (board[0][5] == null && board[0][6] == null && board[0][7].getName().toLowerCase().equals("r")) {
                board[0][6] = board[0][4];
                board[0][4] = null;
                board[0][5] = board[0][7];
                board[0][7] = null;
            } else System.out.println("Hatalı hamle.");
        } else System.out.println("Hatalı hamle.");

    }

    private static void move(Piece[][] board, Piece piece) {
        int newLine = piece.getLine(), newColumn = piece.getColumn();
        int oldLine = piece.getOldLine(), oldColumn = piece.getOldColumn();

        board[newLine][newColumn] = piece;
        board[oldLine][oldColumn] = null;
    }

    private static boolean checkmate(Piece[][] board, String color) {
        ArrayList<Piece[][]> PossibleMoves = possibleMoves(board, color);

        for (Piece[][] move : PossibleMoves)
            if (!kingInCheck(move, color))
                return false;

        return true;
    }

    private static boolean kingInCheck(Piece[][] board, String color) {
        // Kralın tahtadaki konumu bulunur.
        int[] pos = kingPos(board, color);

        // Bulunan konumun sırasıyla Kuzey-Güney-Doğu-Batı sınırlarına uzaklıkları;
        int[] range = {7 - pos[1], pos[1], 7 - pos[0], pos[0]};

        // Şimdi Kralın çevresini kontrol eden bir gezme algoritması kurulur.
        {
            // Aynı satır ve sütun kontrolü
            {
                // Kuzey yönünün kontrolü
                for (int y = pos[1] + 1; y < 8; y++) {
                    Piece p = board[y][pos[0]];

                    int control = plusControl(p, color);
                    if (control == 2) return true;
                    else if (control == 1) break;
                }

                // Güney yönünün kontrolü
                for (int y = pos[1] - 1; y >= 0; y--) {
                    Piece p = board[y][pos[0]];

                    int control = plusControl(p, color);
                    if (control == 2) return true;
                    else if (control == 1) break;
                }

                // Doğu yönünün kontrolü
                for (int x = pos[0] + 1; x < 8; x++) {
                    Piece p = board[pos[1]][x];

                    int control = plusControl(p, color);
                    if (control == 2) return true;
                    else if (control == 1) break;
                }

                // Batı yönünün kontrolü
                for (int x = pos[0] - 1; x >= 0; x--) {
                    Piece p = board[pos[1]][x];

                    int control = plusControl(p, color);
                    if (control == 2) return true;
                    else if (control == 1) break;
                }
            }

            // Çapraz Kontrolleri
            {
                // Kuzey doğu yönünün kontrolü
                int x = pos[0];
                int y = pos[1];
                int loopcount = 0;
                for (int xy = Math.min(range[0], range[2]); xy > 0; xy--) {
                    loopcount++;
                    x++;
                    y++;
                    Piece p = board[y][x];

                    // İlk çarprazdaki taş rakibin piyonuysa şah durumu döner.
                    int control = xControl(p, color);
                    if (p != null && !p.getColor().equals(color)
                            && p.getName().toLowerCase().equals("p") && loopcount == 1) return true;
                    else if (control == 2) return true;
                    else if (control == 1) break;
                }

                // Kuzey batı yönünün kontrolü
                loopcount = 0;
                x = pos[0];
                y = pos[1];
                for (int xy = Math.min(range[0], range[3]); xy > 0; xy--) {
                    loopcount++;
                    x--;
                    y++;
                    Piece p = board[y][x];

                    // İlk çarprazdaki taş rakibin piyonuysa şah durumu döner.
                    int control = xControl(p, color);
                    if (p != null && !p.getColor().equals(color)
                            && p.getName().toLowerCase().equals("p") && loopcount == 1) return true;
                    else if (control == 2) return true;
                    else if (control == 1) break;
                }

                // Güney doğu yönünün kontrolü
                for (int xy = Math.min(range[1], range[2]); xy > 0; xy--) {
                    x++;
                    y--;
                    Piece p = board[y][x];

                    int control = xControl(p, color);
                    if (control == 2) return true;
                    else if (control == 1) break;
                }

                // Güney batı yönünün kontrolü
                for (int xy = Math.min(range[1], range[3]); xy > 0; xy--) {
                    x--;
                    y--;
                    Piece p = board[y][x];

                    int control = xControl(p, color);
                    if (control == 2) return true;
                    else if (control == 1) break;
                }
            }

            // At kontrolleri
            {
                // K >= 2 D & B >= 1
                if (range[0] >= 2) {
                    // D >= 1
                    if (range[2] >= 1) {
                        Piece p = board[pos[1] + 2][pos[0] + 1];
                        if (p != null) {
                            boolean nameCont = p.getName().toLowerCase().equals("n");
                            boolean colorCont = !p.getColor().equals(color);
                            if (nameCont && colorCont) return true;
                        }
                    }
                    // B >= 1
                    if (range[3] >= 1) {
                        Piece p = board[pos[1] + 2][pos[0] - 1];
                        if (p != null) {
                            boolean nameCont = p.getName().toLowerCase().equals("n");
                            boolean colorCont = !p.getColor().equals(color);
                            if (nameCont && colorCont) return true;
                        }
                    }
                }

                // G >= 2 D & B >= 1
                if (range[1] >= 2) {
                    // D >= 1
                    if (range[2] >= 1) {
                        Piece p = board[pos[1] - 2][pos[0] + 1];
                        if (p != null) {
                            boolean nameCont = p.getName().toLowerCase().equals("n");
                            boolean colorCont = !p.getColor().equals(color);
                            if (nameCont && colorCont) return true;
                        }
                    }
                    // B >= 1
                    if (range[3] >= 1) {
                        Piece p = board[pos[1] - 2][pos[0] - 1];
                        if (p != null) {
                            boolean nameCont = p.getName().toLowerCase().equals("n");
                            boolean colorCont = !p.getColor().equals(color);
                            if (nameCont && colorCont) return true;
                        }
                    }
                }

                // D >= 2 K & G >= 1
                if (range[2] >= 2) {
                    // K >= 1
                    if (range[0] >= 1) {
                        Piece p = board[pos[1] + 1][pos[0] + 2];
                        if (p != null) {
                            boolean nameCont = p.getName().toLowerCase().equals("n");
                            boolean colorCont = !p.getColor().equals(color);
                            if (nameCont && colorCont) return true;
                        }
                    }
                    // G >= 1
                    if (range[1] >= 1) {
                        Piece p = board[pos[1] - 1][pos[0] + 2];
                        if (p != null) {
                            boolean nameCont = p.getName().toLowerCase().equals("n");
                            boolean colorCont = !p.getColor().equals(color);
                            if (nameCont && colorCont) return true;
                        }
                    }
                }

                // B >= 2 K & G >= 1
                if (range[3] >= 2) {
                    // K >= 1
                    if (range[0] >= 1) {
                        Piece p = board[pos[1] + 1][pos[0] - 2];
                        if (p != null) {
                            boolean nameCont = p.getName().toLowerCase().equals("n");
                            boolean colorCont = !p.getColor().equals(color);
                            if (nameCont && colorCont) return true;
                        }
                    }
                    // G >= 1
                    if (range[1] >= 1) {
                        Piece p = board[pos[1] - 1][pos[0] - 2];
                        if (p != null) {
                            boolean nameCont = p.getName().toLowerCase().equals("n");
                            boolean colorCont = !p.getColor().equals(color);
                            return nameCont && colorCont;
                        }
                    }
                }
            }
        }

        return false;
    }

    private static int plusControl(Piece piece, String color) {
        if (piece != null) {
            String p = piece.getName().toLowerCase();
            if (piece.getColor().equals(color)) {
                // Bu koşul kralın önünde onu muhafaza edecek aynı renkten bir taşın olduğu anlamına gelir.
                return 1;
            } else if (p.equals("r") || p.equals("q")) {
                // Bu koşul ise kralın önünde hiçbir taş olmadığında onun yerine karşı tarafın
                // Kale veya Vezir taşı bulunuyorsa şah durumu anlamına gelir.
                return 2;
            } else if (p.equals("b") || p.equals("n") || p.equals("p") || p.equals("k")) {
                // Bu koşul da Kralın satırında rakip vezir veya kalesi olmasa da başka taşları varsa şah
                // durumunu engelleyeceği için bulunuyor.
                return 1;
            }
        }
        return 0;
    }

    private static int xControl(Piece piece, String color) {
        if (piece != null) {
            String p = piece.getName().toLowerCase();
            if (piece.getColor().equals(color)) {
                // Bu koşul kralın çaprazlarında dost bir taş bulunuyorsa döner.
                return 1;
            } else if (p.equals("b") || p.equals("q")) {
                // Bu koşul ise kralın önünde hiçbir taş olmadığında onun yerine karşı tarafın
                // Fil veya Vezir taşı bulunuyorsa şah durumu anlamına gelir.
                return 2;
            } else if (p.equals("r") || p.equals("n") || p.equals("p") || p.equals("k")) {
                // Bu koşul da Kralın satırında rakip vezir veya kalesi olmasa da başka taşları varsa şah
                // durumunu engelleyeceği için bulunuyor.
                return 1;
            }
        }
        return 0;
    }

    private static int[] kingPos(Piece[][] board, String color) {
        int[] pos = new int[2];
        boolean found = false;

        for (Piece[] line : board) {
            for (Piece piece : line) {
                if (piece != null && piece.getColor().equals(color) && piece.getName().toLowerCase().equals("k")) {
                    pos[0] = piece.getColumn();
                    pos[1] = piece.getLine();
                    found = true;
                    break;
                }
            }

            if (found) break;
        }

        return pos;
    }

    private static Piece[][] rotate(Piece[][] board) {
        Piece[][] newBoard = new Piece[8][8];

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] != null) {
                    newBoard[7 - i][j] = board[i][j];
                    newBoard[7 - i][j].setLine(7 - i);
                }
            }
        }

        return newBoard;
    }

    private static Piece[][] deepCopy(Piece[][] board) {
        Piece[][] temp = new Piece[8][8];
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                if (board[i][j] != null) {
                    switch (board[i][j].getName().toLowerCase()) {
                        case "p":
                            temp[i][j] = new Pawn(board[i][j]);
                            break;
                        case "r":
                            temp[i][j] = new Rook(board[i][j]);
                            break;
                        case "b":
                            temp[i][j] = new Bishop(board[i][j]);
                            break;
                        case "q":
                            temp[i][j] = new Queen(board[i][j]);
                            break;
                        case "k":
                            temp[i][j] = new King(board[i][j]);
                            break;
                        case "n":
                            temp[i][j] = new Knight(board[i][j]);
                            break;
                    }
                }
        return temp;
    }

    private static ArrayList<Piece[][]> possibleMoves(Piece[][] board, String color) {
        ArrayList<Piece[][]> PMList = new ArrayList<>();

        // İlk 2 döngü tüm tahtayı gezer.
        for (int oldY = 0; oldY < 8; oldY++) {
            for (int oldX = 0; oldX < 8; oldX++) {

                // Her kare seçilir.
                Piece piece = board[oldY][oldX];

                // Seçilen kare boş değilse ve girilen rengin taşıya 2 döngüye daha girer.
                if (piece != null && piece.getColor().equals(color)) {

                    // Bu döngü taşı her kareye oynatmaya çalışır.
                    for (int newY = 0; newY < 8; newY++) {
                        for (int newX = 0; newX < 8; newX++) {
                            Piece[][] temp = deepCopy(board);
                            Piece tempPiece = temp[oldY][oldX];

                            // Verilen adreslere taş hareket edebiliyorsa eder, tahta listeye eklenir ve taş eski
                            // yerine döner.
                            if (tempPiece.pieceMove(newY, newX, temp, color)) {
                                move(temp, tempPiece);
                                PMList.add(temp);
                            }
                        }
                    }
                }
            }
        }
        return PMList;
    }

    private static Piece[][] insertTree(Piece[][] board, int difficulty) {
        ChessGameAI ai = new ChessGameAI();
        ChessGameAINode root = new ChessGameAINode(deepCopy(board));
        Piece[][] newBoard;

        // Program kökün derinliği 1 olacak şekilde yazıldı
        root.setDepth(1);

        ai.setRoot(root);

        // Bu methodla tüm olası hamleleri içeren ağaç oluşturuluyor
        insertAI(ai.getRoot(), difficulty, false);

        // Minimax ile en karlı hamle bulunuyor
        ChessGameAINode best = ai.minimax(ai.getRoot(), difficulty, true);
        if (best.getData() == 0) {
            board = rotate(board);
            ArrayList<Piece[][]> possibleMoves = possibleMoves(board, "black");
            int moveIndex = (int) (Math.random() * possibleMoves.size());
            newBoard = possibleMoves.get(moveIndex);
        } else {
            for (int i = 2; i < difficulty; i++)
                best = best.getParent();
            newBoard = best.board;
        }

        return rotate(newBoard);
    }

    private static void insertAI(ChessGameAINode node, int difficulty, boolean isWhite) {
        if (node.getDepth() == difficulty) {
            // Sadece yaprakların puanları hesaplanıyor
            node.setData(calculatePoint(node.board));
            return;
        }
        if (isWhite) {
            //node'nin içerebileceği tüm olası board durumlarını hesaplıyor
            ArrayList<Piece[][]> possibleMoves = possibleMoves(rotate(node.board), "white");
            for (Piece[][] move : possibleMoves) {
                ChessGameAINode child = new ChessGameAINode(move);
                child.setParent(node);
                child.setDepth(node.getDepth() + 1);
                node.getChildren().add(child);
            }

            //istenilen derinliğe ulaşılana kadar recursive devam ediyor
            for (ChessGameAINode value : node.getChildren())
                insertAI(value, difficulty, false);

        } else {
            ArrayList<Piece[][]> possibleMoves = possibleMoves(rotate(node.board), "black");
            for (Piece[][] move : possibleMoves) {
                ChessGameAINode child = new ChessGameAINode(move);
                child.setParent(node);
                child.setDepth(node.getDepth() + 1);
                node.getChildren().add(child);
            }

            for (ChessGameAINode value : node.getChildren())
                insertAI(value, difficulty, true);

        }
    }

    private static int calculatePoint(Piece[][] board) {
        // tahtanın o anki durumunun  puanını hesaplıyor
        int total = 0;
        String color;
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                if (board[i][j] != null) {
                    color = board[i][j].getColor();
                    if (color.equals("black"))
                        total = total + board[i][j].getPoint();
                    else
                        total = total - board[i][j].getPoint();
                }

        return total;
    }
}