import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.awt.event.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

class Box {
    int x;
    int y;
    int width;
    int height;
    Color color;

    public Box(int x, int y, int width, int height, Color color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
    }
}

@SuppressWarnings("SpellCheckingInspection")
public class Wordle
{
    private static JPanel panel;
    private static List<Box> board;
    private static List<Box> keys;
    private static List<Character> textLetters;
    private static boolean guessValid = false;
    private static boolean gameOver = false;
    private static boolean gameWon = false;
    private static boolean showError = false;
    private static final Color whiteText = new Color(255,255,255);
    private static final Color blackText = new Color(0,0,0);
    private static final Color lightGray = new Color(211, 214, 218);
    private static final Color grayBox = new Color(121,124,126);
    private static final Color yellowBox = new Color(198,180,102);
    private static final Color greenBox = new Color(121,168,107);
    private static final String order = "QWERTYUIOPASDFGHJKLZXCVBNM";
    private static StringBuilder stringBuilder1 = new StringBuilder();
    private static StringBuilder stringBuilder2 = new StringBuilder();
    private static StringBuilder stringBuilder3 = new StringBuilder();
    private static StringBuilder stringBuilder4 = new StringBuilder();
    private static StringBuilder stringBuilder5 = new StringBuilder();
    private static StringBuilder stringBuilder6 = new StringBuilder();
    private static int numAttempts = 0;
    private static int numLetters1 = 0;
    private static int numLetters2 = 0;
    private static int numLetters3 = 0;
    private static int numLetters4 = 0;
    private static int numLetters5 = 0;
    private static int numLetters6 = 0;
    private static final String filePathWords = "/Users/jed/Desktop/Code Tests/Java Testing/Wordle/Wordle/wordList.txt";
    private static final String filePathGuesses = "/Users/jed/Desktop/Code Tests/Java Testing/Wordle/Wordle/guessList.txt";
    private static String wordToCheck;
    private static String correctWord;
    private static Random rand;
    private static int wordIndex;
    private static final int indexA = 1;
    private static final int indexB = 738;
    private static final int indexC = 1647;
    private static final int indexD = 2569;
    private static final int indexE = 3254;
    private static final int indexF = 3557;
    private static final int indexG = 4155;
    private static final int indexH = 4793;
    private static final int indexI = 5282;
    private static final int indexJ = 5447;
    private static final int indexK = 5649;
    private static final int indexL = 6025;
    private static final int indexM = 6602;
    private static final int indexN = 7295;
    private static final int indexO = 7620;
    private static final int indexP = 7882;
    private static final int indexQ = 8741;
    private static final int indexR = 8819;
    private static final int indexS = 9447;
    private static final int indexT = 11012;
    private static final int indexU = 11827;
    private static final int indexV = 12016;
    private static final int indexW = 12258;
    private static final int indexX = 12671;
    private static final int indexY = 12687;
    private static final int indexZ = 12868;
    private static final int numGuesses = 12972;
    private static final int numWords = 2315;

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(() ->
        {
            Wordle wordle = new Wordle();
            wordle.startGame();
        });
    }

    public void startGame()
    {
        rand = new Random();

        wordIndex = rand.nextInt(numWords)+1;

        try {
            correctWord = getWordFromFile(filePathWords, wordIndex);

        }
        catch (IOException e) {
            e.printStackTrace();
        }

        JFrame frame = new JFrame("Wordle");

        frame.setSize(600, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        board = new ArrayList<>();

        for (int i = 1; i <= 6; i++)
        {
            for (int j = 1; j <= 5; j++)
            {
                board.add(new Box((57 * j+103), (58 * i+4), 50, 49, whiteText));
            }
        }

        keys = new ArrayList<>();

        textLetters = new ArrayList<>();

        for (char ch : order.toCharArray()) {
            textLetters.add(ch);
        }

        for (int i = 1; i <= 10; i++) {
            keys.add(new Box((49 * i+8), (411), 43, 58, lightGray));
        }

        for (int i = 1; i <= 9; i++) {
            keys.add(new Box((49 * i+32), (477), 43, 58, lightGray));
        }

        for (int i = 1; i <= 7; i++) {
            keys.add(new Box((49 * i+81), (543), 43, 58, lightGray));
        }

        keys.add(new Box(57, 543, 67, 58, lightGray));
        keys.add(new Box(473, 543, 68, 58, lightGray));

        panel = new JPanel()
        {
            @Override
            protected void paintComponent(Graphics g)
            {
                super.paintComponent(g);
                drawBoard(g);
                drawKeys(g);
                drawAttempts(g);
                drawErrorMessage(g);
                drawGameOver(g);
                drawGameWon(g);
            }
        };

        frame.add(panel);

        panel.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                int clickedBox = getIndex(e.getX(), e.getY());

                if (!gameOver && !gameWon)
                {
                    if (clickedBox != -1)
                    {
                        if (SwingUtilities.isLeftMouseButton(e))
                        {
                                clickChange(clickedBox);
                        }

                        panel.repaint();
                    }
                }
            }
        });

        panel.setLayout(null);

        JButton restartButton = new JButton("New Word");
        restartButton.setBounds(10, 625, 100, 25);

        restartButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                restartGame();
            }
        });

        panel.add(restartButton);

        panel.setBackground(whiteText);

        frame.addKeyListener(new KeyListener());
        frame.setFocusable(true);
        frame.setVisible(true);
    }

    private static void drawBoard(Graphics g) {

        for (int i = 0; i < 30; i++) {
            g.setColor(board.get(i).color);
            g.fillRect(board.get(i).x, board.get(i).y, board.get(i).width, board.get(i).height);
            g.setColor(lightGray);
            g.drawRect(board.get(i).x, board.get(i).y, board.get(i).width, board.get(i).height);
            g.drawRect(board.get(i).x-1, board.get(i).y-1, board.get(i).width+2, board.get(i).height+2);
        }

            switch(numAttempts) {
                case 0:
                    for (int i = 0; i < 5; i++) {
                        if (i < numLetters1) {
                            g.setColor(grayBox);
                        }
                        else {
                            g.setColor(lightGray);
                        }
                        g.drawRect(board.get(i).x, board.get(i).y, board.get(i).width, board.get(i).height);
                        g.drawRect(board.get(i).x-1, board.get(i).y-1, board.get(i).width+2, board.get(i).height+2);
                    }
                case 1:
                    for (int i = 5; i < 10; i++) {
                        if ((i-5) < numLetters2) {
                            g.setColor(grayBox);
                        }
                        else {
                            g.setColor(lightGray);
                        }
                        g.drawRect(board.get(i).x, board.get(i).y, board.get(i).width, board.get(i).height);
                        g.drawRect(board.get(i).x-1, board.get(i).y-1, board.get(i).width+2, board.get(i).height+2);
                    }
                    break;
                case 2:
                    for (int i = 10; i < 15; i++) {
                        if ((i-10) < numLetters3) {
                            g.setColor(grayBox);
                        }
                        else {
                            g.setColor(lightGray);
                        }
                        g.drawRect(board.get(i).x, board.get(i).y, board.get(i).width, board.get(i).height);
                        g.drawRect(board.get(i).x-1, board.get(i).y-1, board.get(i).width+2, board.get(i).height+2);
                    }
                    break;
                case 3:
                    for (int i = 15; i < 20; i++) {
                        if ((i-15) < numLetters4) {
                            g.setColor(grayBox);
                        }
                        else {
                            g.setColor(lightGray);
                        }
                        g.drawRect(board.get(i).x, board.get(i).y, board.get(i).width, board.get(i).height);
                        g.drawRect(board.get(i).x-1, board.get(i).y-1, board.get(i).width+2, board.get(i).height+2);
                    }
                    break;
                case 4:
                    for (int i = 20; i < 25; i++) {
                        if ((i-20) < numLetters5) {
                            g.setColor(grayBox);
                        }
                        else {
                            g.setColor(lightGray);
                        }
                        g.drawRect(board.get(i).x, board.get(i).y, board.get(i).width, board.get(i).height);
                        g.drawRect(board.get(i).x-1, board.get(i).y-1, board.get(i).width+2, board.get(i).height+2);
                    }
                    break;
                case 5:
                    for (int i = 25; i < 30; i++) {
                        if ((i-25) < numLetters6) {
                            g.setColor(grayBox);
                        }
                        else {
                            g.setColor(lightGray);
                        }
                        g.drawRect(board.get(i).x, board.get(i).y, board.get(i).width, board.get(i).height);
                        g.drawRect(board.get(i).x-1, board.get(i).y-1, board.get(i).width+2, board.get(i).height+2);
                    }
                    break;
                case 6: // do nothing
                    break;

            }

        panel.repaint();
    }

    private static void drawKeys(Graphics g) {
        Font numFont1 = new Font("Helvetica Nueue", Font.BOLD, 19);
        Font numFont2 = new Font("Helvetica Nueue", Font.BOLD, 12);

        for (int i = 0; i < 26; i++) {
            g.setColor(keys.get(i).color);
            g.fillRect(keys.get(i).x, keys.get(i).y, keys.get(i).width, keys.get(i).height);
            g.setFont(numFont1);
            g.setColor(blackText);

            if(i == 0 || i == 1 || i == 25) {
                g.drawString(String.valueOf(textLetters.get(i)), keys.get(i).x+keys.get(i).width/3-1, keys.get(i).y+2*keys.get(i).height/3-3);
            }
            else if (i == 7) {
                g.drawString(String.valueOf(textLetters.get(i)), keys.get(i).x + keys.get(i).width / 3 + 5, keys.get(i).y + 2 * keys.get(i).height / 3 - 3);
            }
            else if (i == 11 || i == 19) {
                g.drawString(String.valueOf(textLetters.get(i)), keys.get(i).x + keys.get(i).width / 3 + 2, keys.get(i).y + 2 * keys.get(i).height / 3 - 3);
            }
            else if (i == 8 || i == 12 || i == 20) {
                g.drawString(String.valueOf(textLetters.get(i)), keys.get(i).x + keys.get(i).width / 3, keys.get(i).y + 2 * keys.get(i).height / 3 - 3);
            }
            else if (i == 16) {
                g.drawString(String.valueOf(textLetters.get(i)), keys.get(i).x + keys.get(i).width / 3 + 3, keys.get(i).y + 2 * keys.get(i).height / 3 - 3);
            }
            else {
                g.drawString(String.valueOf(textLetters.get(i)), keys.get(i).x + keys.get(i).width / 3 + 1, keys.get(i).y + 2 * keys.get(i).height / 3 - 3);
            }
        }

        for (int i = 26; i < 28; i++) {
            g.setColor(keys.get(i).color);
            g.fillRect(keys.get(i).x, keys.get(i).y, keys.get(i).width, keys.get(i).height);
            g.setFont(numFont2);
            g.setColor(blackText);

            if(i == 26) {
                g.drawString("ENTER", keys.get(i).x + keys.get(i).width / 4-1, keys.get(i).y + 2 * keys.get(i).height / 3 - 5);
            }
            else {
                g.drawString("DELETE", keys.get(i).x + keys.get(i).width / 6, keys.get(i).y + 2 * keys.get(i).height / 3 - 5);
            }
        }

        panel.repaint();
    }

    private static void drawAttempts(Graphics g) {
        Font numFont1 = new Font("Helvetica Nueue", Font.BOLD, 19);

        g.setFont(numFont1);

        for (int i = 0; i < numLetters1; i++) {
            if(numAttempts <= 0) {
                g.setColor(blackText);
            }
            else {
                g.setColor(whiteText);
            }
            g.drawString(String.valueOf((stringBuilder1.charAt(i))), board.get(i).x + board.get(i).width / 3 + 2, board.get(i).y + 2 * board.get(i).height / 3);
        }
        for (int i = 0; i < numLetters2; i++) {
            if(numAttempts <= 1) {
                g.setColor(blackText);
            }
            else {
                g.setColor(whiteText);
            }
            g.drawString(String.valueOf((stringBuilder2.charAt(i))), board.get(i).x+board.get(i).width/3+2, board.get(i).y+5*board.get(i).height/3+9);
        }
        for (int i = 0; i < numLetters3; i++) {
            if(numAttempts <= 2) {
                g.setColor(blackText);
            }
            else {
                g.setColor(whiteText);
            }
            g.drawString(String.valueOf((stringBuilder3.charAt(i))), board.get(i).x+board.get(i).width/3+2, board.get(i).y+8*board.get(i).height/3+18);
        }
        for (int i = 0; i < numLetters4; i++) {
            if(numAttempts <= 3) {
                g.setColor(blackText);
            }
            else {
                g.setColor(whiteText);
            }
            g.drawString(String.valueOf((stringBuilder4.charAt(i))), board.get(i).x+board.get(i).width/3+2, board.get(i).y+11*board.get(i).height/3+27);
        }
        for (int i = 0; i < numLetters5; i++) {
            if(numAttempts <= 4) {
                g.setColor(blackText);
            }
            else {
                g.setColor(whiteText);
            }
            g.drawString(String.valueOf((stringBuilder5.charAt(i))), board.get(i).x+board.get(i).width/3+2, board.get(i).y+14*board.get(i).height/3+36);
        }
        for (int i = 0; i < numLetters6; i++) {
            if(numAttempts <= 5) {
                g.setColor(blackText);
            }
            else {
                g.setColor(whiteText);
            }
            g.drawString(String.valueOf((stringBuilder6.charAt(i))), board.get(i).x+board.get(i).width/3+2, board.get(i).y+17*board.get(i).height/3+45);
        }

        panel.repaint();
    }

    private static class KeyListener extends KeyAdapter
    {
        @Override
        public void keyPressed(KeyEvent e)
        {
            char input = Character.toUpperCase(e.getKeyChar());
            char newCh;

            if (!gameOver && !gameWon) {
                switch (input) {
                    case ('A'):
                        newCh = 'A';
                        if (numAttempts == 0) {
                            if (numLetters1 < 5) {
                                stringBuilder1.append(newCh);
                                numLetters1++;
                            }
                        } else if (numAttempts == 1) {
                            if (numLetters2 < 5) {
                                stringBuilder2.append(newCh);
                                numLetters2++;
                            }
                        } else if (numAttempts == 2) {
                            if (numLetters3 < 5) {
                                stringBuilder3.append(newCh);
                                numLetters3++;
                            }
                        } else if (numAttempts == 3) {
                            if (numLetters4 < 5) {
                                stringBuilder4.append(newCh);
                                numLetters4++;
                            }
                        } else if (numAttempts == 4) {
                            if (numLetters5 < 5) {
                                stringBuilder5.append(newCh);
                                numLetters5++;
                            }
                        } else if (numAttempts == 5) {
                            if (numLetters6 < 5) {
                                stringBuilder6.append(newCh);
                                numLetters6++;
                            }
                        }
                        break;
                    case ('B'):
                        newCh = 'B';
                        if (numAttempts == 0) {
                            if (numLetters1 < 5) {
                                stringBuilder1.append(newCh);
                                numLetters1++;
                            }
                        } else if (numAttempts == 1) {
                            if (numLetters2 < 5) {
                                stringBuilder2.append(newCh);
                                numLetters2++;
                            }
                        } else if (numAttempts == 2) {
                            if (numLetters3 < 5) {
                                stringBuilder3.append(newCh);
                                numLetters3++;
                            }
                        } else if (numAttempts == 3) {
                            if (numLetters4 < 5) {
                                stringBuilder4.append(newCh);
                                numLetters4++;
                            }
                        } else if (numAttempts == 4) {
                            if (numLetters5 < 5) {
                                stringBuilder5.append(newCh);
                                numLetters5++;
                            }
                        } else if (numAttempts == 5) {
                            if (numLetters6 < 5) {
                                stringBuilder6.append(newCh);
                                numLetters6++;
                            }
                        }
                        break;
                    case ('C'):
                        newCh = 'C';
                        if (numAttempts == 0) {
                            if (numLetters1 < 5) {
                                stringBuilder1.append(newCh);
                                numLetters1++;
                            }
                        } else if (numAttempts == 1) {
                            if (numLetters2 < 5) {
                                stringBuilder2.append(newCh);
                                numLetters2++;
                            }
                        } else if (numAttempts == 2) {
                            if (numLetters3 < 5) {
                                stringBuilder3.append(newCh);
                                numLetters3++;
                            }
                        } else if (numAttempts == 3) {
                            if (numLetters4 < 5) {
                                stringBuilder4.append(newCh);
                                numLetters4++;
                            }
                        } else if (numAttempts == 4) {
                            if (numLetters5 < 5) {
                                stringBuilder5.append(newCh);
                                numLetters5++;
                            }
                        } else if (numAttempts == 5) {
                            if (numLetters6 < 5) {
                                stringBuilder6.append(newCh);
                                numLetters6++;
                            }
                        }
                        break;
                    case ('D'):
                        newCh = 'D';
                        if (numAttempts == 0) {
                            if (numLetters1 < 5) {
                                stringBuilder1.append(newCh);
                                numLetters1++;
                            }
                        } else if (numAttempts == 1) {
                            if (numLetters2 < 5) {
                                stringBuilder2.append(newCh);
                                numLetters2++;
                            }
                        } else if (numAttempts == 2) {
                            if (numLetters3 < 5) {
                                stringBuilder3.append(newCh);
                                numLetters3++;
                            }
                        } else if (numAttempts == 3) {
                            if (numLetters4 < 5) {
                                stringBuilder4.append(newCh);
                                numLetters4++;
                            }
                        } else if (numAttempts == 4) {
                            if (numLetters5 < 5) {
                                stringBuilder5.append(newCh);
                                numLetters5++;
                            }
                        } else if (numAttempts == 5) {
                            if (numLetters6 < 5) {
                                stringBuilder6.append(newCh);
                                numLetters6++;
                            }
                        }
                        break;
                    case ('E'):
                        newCh = 'E';
                        if (numAttempts == 0) {
                            if (numLetters1 < 5) {
                                stringBuilder1.append(newCh);
                                numLetters1++;
                            }
                        } else if (numAttempts == 1) {
                            if (numLetters2 < 5) {
                                stringBuilder2.append(newCh);
                                numLetters2++;
                            }
                        } else if (numAttempts == 2) {
                            if (numLetters3 < 5) {
                                stringBuilder3.append(newCh);
                                numLetters3++;
                            }
                        } else if (numAttempts == 3) {
                            if (numLetters4 < 5) {
                                stringBuilder4.append(newCh);
                                numLetters4++;
                            }
                        } else if (numAttempts == 4) {
                            if (numLetters5 < 5) {
                                stringBuilder5.append(newCh);
                                numLetters5++;
                            }
                        } else if (numAttempts == 5) {
                            if (numLetters6 < 5) {
                                stringBuilder6.append(newCh);
                                numLetters6++;
                            }
                        }
                        break;
                    case ('F'):
                        newCh = 'F';
                        if (numAttempts == 0) {
                            if (numLetters1 < 5) {
                                stringBuilder1.append(newCh);
                                numLetters1++;
                            }
                        } else if (numAttempts == 1) {
                            if (numLetters2 < 5) {
                                stringBuilder2.append(newCh);
                                numLetters2++;
                            }
                        } else if (numAttempts == 2) {
                            if (numLetters3 < 5) {
                                stringBuilder3.append(newCh);
                                numLetters3++;
                            }
                        } else if (numAttempts == 3) {
                            if (numLetters4 < 5) {
                                stringBuilder4.append(newCh);
                                numLetters4++;
                            }
                        } else if (numAttempts == 4) {
                            if (numLetters5 < 5) {
                                stringBuilder5.append(newCh);
                                numLetters5++;
                            }
                        } else if (numAttempts == 5) {
                            if (numLetters6 < 5) {
                                stringBuilder6.append(newCh);
                                numLetters6++;
                            }
                        }
                        break;
                    case ('G'):
                        newCh = 'G';
                        if (numAttempts == 0) {
                            if (numLetters1 < 5) {
                                stringBuilder1.append(newCh);
                                numLetters1++;
                            }
                        } else if (numAttempts == 1) {
                            if (numLetters2 < 5) {
                                stringBuilder2.append(newCh);
                                numLetters2++;
                            }
                        } else if (numAttempts == 2) {
                            if (numLetters3 < 5) {
                                stringBuilder3.append(newCh);
                                numLetters3++;
                            }
                        } else if (numAttempts == 3) {
                            if (numLetters4 < 5) {
                                stringBuilder4.append(newCh);
                                numLetters4++;
                            }
                        } else if (numAttempts == 4) {
                            if (numLetters5 < 5) {
                                stringBuilder5.append(newCh);
                                numLetters5++;
                            }
                        } else if (numAttempts == 5) {
                            if (numLetters6 < 5) {
                                stringBuilder6.append(newCh);
                                numLetters6++;
                            }
                        }
                        break;
                    case ('H'):
                        newCh = 'H';
                        if (numAttempts == 0) {
                            if (numLetters1 < 5) {
                                stringBuilder1.append(newCh);
                                numLetters1++;
                            }
                        } else if (numAttempts == 1) {
                            if (numLetters2 < 5) {
                                stringBuilder2.append(newCh);
                                numLetters2++;
                            }
                        } else if (numAttempts == 2) {
                            if (numLetters3 < 5) {
                                stringBuilder3.append(newCh);
                                numLetters3++;
                            }
                        } else if (numAttempts == 3) {
                            if (numLetters4 < 5) {
                                stringBuilder4.append(newCh);
                                numLetters4++;
                            }
                        } else if (numAttempts == 4) {
                            if (numLetters5 < 5) {
                                stringBuilder5.append(newCh);
                                numLetters5++;
                            }
                        } else if (numAttempts == 5) {
                            if (numLetters6 < 5) {
                                stringBuilder6.append(newCh);
                                numLetters6++;
                            }
                        }
                        break;
                    case ('I'):
                        newCh = 'I';
                        if (numAttempts == 0) {
                            if (numLetters1 < 5) {
                                stringBuilder1.append(newCh);
                                numLetters1++;
                            }
                        } else if (numAttempts == 1) {
                            if (numLetters2 < 5) {
                                stringBuilder2.append(newCh);
                                numLetters2++;
                            }
                        } else if (numAttempts == 2) {
                            if (numLetters3 < 5) {
                                stringBuilder3.append(newCh);
                                numLetters3++;
                            }
                        } else if (numAttempts == 3) {
                            if (numLetters4 < 5) {
                                stringBuilder4.append(newCh);
                                numLetters4++;
                            }
                        } else if (numAttempts == 4) {
                            if (numLetters5 < 5) {
                                stringBuilder5.append(newCh);
                                numLetters5++;
                            }
                        } else if (numAttempts == 5) {
                            if (numLetters6 < 5) {
                                stringBuilder6.append(newCh);
                                numLetters6++;
                            }
                        }
                        break;
                    case ('J'):
                        newCh = 'J';
                        if (numAttempts == 0) {
                            if (numLetters1 < 5) {
                                stringBuilder1.append(newCh);
                                numLetters1++;
                            }
                        } else if (numAttempts == 1) {
                            if (numLetters2 < 5) {
                                stringBuilder2.append(newCh);
                                numLetters2++;
                            }
                        } else if (numAttempts == 2) {
                            if (numLetters3 < 5) {
                                stringBuilder3.append(newCh);
                                numLetters3++;
                            }
                        } else if (numAttempts == 3) {
                            if (numLetters4 < 5) {
                                stringBuilder4.append(newCh);
                                numLetters4++;
                            }
                        } else if (numAttempts == 4) {
                            if (numLetters5 < 5) {
                                stringBuilder5.append(newCh);
                                numLetters5++;
                            }
                        } else if (numAttempts == 5) {
                            if (numLetters6 < 5) {
                                stringBuilder6.append(newCh);
                                numLetters6++;
                            }
                        }
                        break;
                    case ('K'):
                        newCh = 'K';
                        if (numAttempts == 0) {
                            if (numLetters1 < 5) {
                                stringBuilder1.append(newCh);
                                numLetters1++;
                            }
                        } else if (numAttempts == 1) {
                            if (numLetters2 < 5) {
                                stringBuilder2.append(newCh);
                                numLetters2++;
                            }
                        } else if (numAttempts == 2) {
                            if (numLetters3 < 5) {
                                stringBuilder3.append(newCh);
                                numLetters3++;
                            }
                        } else if (numAttempts == 3) {
                            if (numLetters4 < 5) {
                                stringBuilder4.append(newCh);
                                numLetters4++;
                            }
                        } else if (numAttempts == 4) {
                            if (numLetters5 < 5) {
                                stringBuilder5.append(newCh);
                                numLetters5++;
                            }
                        } else if (numAttempts == 5) {
                            if (numLetters6 < 5) {
                                stringBuilder6.append(newCh);
                                numLetters6++;
                            }
                        }
                        break;
                    case ('L'):
                        newCh = 'L';
                        if (numAttempts == 0) {
                            if (numLetters1 < 5) {
                                stringBuilder1.append(newCh);
                                numLetters1++;
                            }
                        } else if (numAttempts == 1) {
                            if (numLetters2 < 5) {
                                stringBuilder2.append(newCh);
                                numLetters2++;
                            }
                        } else if (numAttempts == 2) {
                            if (numLetters3 < 5) {
                                stringBuilder3.append(newCh);
                                numLetters3++;
                            }
                        } else if (numAttempts == 3) {
                            if (numLetters4 < 5) {
                                stringBuilder4.append(newCh);
                                numLetters4++;
                            }
                        } else if (numAttempts == 4) {
                            if (numLetters5 < 5) {
                                stringBuilder5.append(newCh);
                                numLetters5++;
                            }
                        } else if (numAttempts == 5) {
                            if (numLetters6 < 5) {
                                stringBuilder6.append(newCh);
                                numLetters6++;
                            }
                        }
                        break;
                    case ('M'):
                        newCh = 'M';
                        if (numAttempts == 0) {
                            if (numLetters1 < 5) {
                                stringBuilder1.append(newCh);
                                numLetters1++;
                            }
                        } else if (numAttempts == 1) {
                            if (numLetters2 < 5) {
                                stringBuilder2.append(newCh);
                                numLetters2++;
                            }
                        } else if (numAttempts == 2) {
                            if (numLetters3 < 5) {
                                stringBuilder3.append(newCh);
                                numLetters3++;
                            }
                        } else if (numAttempts == 3) {
                            if (numLetters4 < 5) {
                                stringBuilder4.append(newCh);
                                numLetters4++;
                            }
                        } else if (numAttempts == 4) {
                            if (numLetters5 < 5) {
                                stringBuilder5.append(newCh);
                                numLetters5++;
                            }
                        } else if (numAttempts == 5) {
                            if (numLetters6 < 5) {
                                stringBuilder6.append(newCh);
                                numLetters6++;
                            }
                        }
                        break;
                    case ('N'):
                        newCh = 'N';
                        if (numAttempts == 0) {
                            if (numLetters1 < 5) {
                                stringBuilder1.append(newCh);
                                numLetters1++;
                            }
                        } else if (numAttempts == 1) {
                            if (numLetters2 < 5) {
                                stringBuilder2.append(newCh);
                                numLetters2++;
                            }
                        } else if (numAttempts == 2) {
                            if (numLetters3 < 5) {
                                stringBuilder3.append(newCh);
                                numLetters3++;
                            }
                        } else if (numAttempts == 3) {
                            if (numLetters4 < 5) {
                                stringBuilder4.append(newCh);
                                numLetters4++;
                            }
                        } else if (numAttempts == 4) {
                            if (numLetters5 < 5) {
                                stringBuilder5.append(newCh);
                                numLetters5++;
                            }
                        } else if (numAttempts == 5) {
                            if (numLetters6 < 5) {
                                stringBuilder6.append(newCh);
                                numLetters6++;
                            }
                        }
                        break;
                    case ('O'):
                        newCh = 'O';
                        if (numAttempts == 0) {
                            if (numLetters1 < 5) {
                                stringBuilder1.append(newCh);
                                numLetters1++;
                            }
                        } else if (numAttempts == 1) {
                            if (numLetters2 < 5) {
                                stringBuilder2.append(newCh);
                                numLetters2++;
                            }
                        } else if (numAttempts == 2) {
                            if (numLetters3 < 5) {
                                stringBuilder3.append(newCh);
                                numLetters3++;
                            }
                        } else if (numAttempts == 3) {
                            if (numLetters4 < 5) {
                                stringBuilder4.append(newCh);
                                numLetters4++;
                            }
                        } else if (numAttempts == 4) {
                            if (numLetters5 < 5) {
                                stringBuilder5.append(newCh);
                                numLetters5++;
                            }
                        } else if (numAttempts == 5) {
                            if (numLetters6 < 5) {
                                stringBuilder6.append(newCh);
                                numLetters6++;
                            }
                        }
                        break;
                    case ('P'):
                        newCh = 'P';
                        if (numAttempts == 0) {
                            if (numLetters1 < 5) {
                                stringBuilder1.append(newCh);
                                numLetters1++;
                            }
                        } else if (numAttempts == 1) {
                            if (numLetters2 < 5) {
                                stringBuilder2.append(newCh);
                                numLetters2++;
                            }
                        } else if (numAttempts == 2) {
                            if (numLetters3 < 5) {
                                stringBuilder3.append(newCh);
                                numLetters3++;
                            }
                        } else if (numAttempts == 3) {
                            if (numLetters4 < 5) {
                                stringBuilder4.append(newCh);
                                numLetters4++;
                            }
                        } else if (numAttempts == 4) {
                            if (numLetters5 < 5) {
                                stringBuilder5.append(newCh);
                                numLetters5++;
                            }
                        } else if (numAttempts == 5) {
                            if (numLetters6 < 5) {
                                stringBuilder6.append(newCh);
                                numLetters6++;
                            }
                        }
                        break;
                    case ('Q'):
                        newCh = 'Q';
                        if (numAttempts == 0) {
                            if (numLetters1 < 5) {
                                stringBuilder1.append(newCh);
                                numLetters1++;
                            }
                        } else if (numAttempts == 1) {
                            if (numLetters2 < 5) {
                                stringBuilder2.append(newCh);
                                numLetters2++;
                            }
                        } else if (numAttempts == 2) {
                            if (numLetters3 < 5) {
                                stringBuilder3.append(newCh);
                                numLetters3++;
                            }
                        } else if (numAttempts == 3) {
                            if (numLetters4 < 5) {
                                stringBuilder4.append(newCh);
                                numLetters4++;
                            }
                        } else if (numAttempts == 4) {
                            if (numLetters5 < 5) {
                                stringBuilder5.append(newCh);
                                numLetters5++;
                            }
                        } else if (numAttempts == 5) {
                            if (numLetters6 < 5) {
                                stringBuilder6.append(newCh);
                                numLetters6++;
                            }
                        }
                        break;
                    case ('R'):
                        newCh = 'R';
                        if (numAttempts == 0) {
                            if (numLetters1 < 5) {
                                stringBuilder1.append(newCh);
                                numLetters1++;
                            }
                        } else if (numAttempts == 1) {
                            if (numLetters2 < 5) {
                                stringBuilder2.append(newCh);
                                numLetters2++;
                            }
                        } else if (numAttempts == 2) {
                            if (numLetters3 < 5) {
                                stringBuilder3.append(newCh);
                                numLetters3++;
                            }
                        } else if (numAttempts == 3) {
                            if (numLetters4 < 5) {
                                stringBuilder4.append(newCh);
                                numLetters4++;
                            }
                        } else if (numAttempts == 4) {
                            if (numLetters5 < 5) {
                                stringBuilder5.append(newCh);
                                numLetters5++;
                            }
                        } else if (numAttempts == 5) {
                            if (numLetters6 < 5) {
                                stringBuilder6.append(newCh);
                                numLetters6++;
                            }
                        }
                        break;
                    case ('S'):
                        newCh = 'S';
                        if (numAttempts == 0) {
                            if (numLetters1 < 5) {
                                stringBuilder1.append(newCh);
                                numLetters1++;
                            }
                        } else if (numAttempts == 1) {
                            if (numLetters2 < 5) {
                                stringBuilder2.append(newCh);
                                numLetters2++;
                            }
                        } else if (numAttempts == 2) {
                            if (numLetters3 < 5) {
                                stringBuilder3.append(newCh);
                                numLetters3++;
                            }
                        } else if (numAttempts == 3) {
                            if (numLetters4 < 5) {
                                stringBuilder4.append(newCh);
                                numLetters4++;
                            }
                        } else if (numAttempts == 4) {
                            if (numLetters5 < 5) {
                                stringBuilder5.append(newCh);
                                numLetters5++;
                            }
                        } else if (numAttempts == 5) {
                            if (numLetters6 < 5) {
                                stringBuilder6.append(newCh);
                                numLetters6++;
                            }
                        }
                        break;
                    case ('T'):
                        newCh = 'T';
                        if (numAttempts == 0) {
                            if (numLetters1 < 5) {
                                stringBuilder1.append(newCh);
                                numLetters1++;
                            }
                        } else if (numAttempts == 1) {
                            if (numLetters2 < 5) {
                                stringBuilder2.append(newCh);
                                numLetters2++;
                            }
                        } else if (numAttempts == 2) {
                            if (numLetters3 < 5) {
                                stringBuilder3.append(newCh);
                                numLetters3++;
                            }
                        } else if (numAttempts == 3) {
                            if (numLetters4 < 5) {
                                stringBuilder4.append(newCh);
                                numLetters4++;
                            }
                        } else if (numAttempts == 4) {
                            if (numLetters5 < 5) {
                                stringBuilder5.append(newCh);
                                numLetters5++;
                            }
                        } else if (numAttempts == 5) {
                            if (numLetters6 < 5) {
                                stringBuilder6.append(newCh);
                                numLetters6++;
                            }
                        }
                        break;
                    case ('U'):
                        newCh = 'U';
                        if (numAttempts == 0) {
                            if (numLetters1 < 5) {
                                stringBuilder1.append(newCh);
                                numLetters1++;
                            }
                        } else if (numAttempts == 1) {
                            if (numLetters2 < 5) {
                                stringBuilder2.append(newCh);
                                numLetters2++;
                            }
                        } else if (numAttempts == 2) {
                            if (numLetters3 < 5) {
                                stringBuilder3.append(newCh);
                                numLetters3++;
                            }
                        } else if (numAttempts == 3) {
                            if (numLetters4 < 5) {
                                stringBuilder4.append(newCh);
                                numLetters4++;
                            }
                        } else if (numAttempts == 4) {
                            if (numLetters5 < 5) {
                                stringBuilder5.append(newCh);
                                numLetters5++;
                            }
                        } else if (numAttempts == 5) {
                            if (numLetters6 < 5) {
                                stringBuilder6.append(newCh);
                                numLetters6++;
                            }
                        }
                        break;
                    case ('V'):
                        newCh = 'V';
                        if (numAttempts == 0) {
                            if (numLetters1 < 5) {
                                stringBuilder1.append(newCh);
                                numLetters1++;
                            }
                        } else if (numAttempts == 1) {
                            if (numLetters2 < 5) {
                                stringBuilder2.append(newCh);
                                numLetters2++;
                            }
                        } else if (numAttempts == 2) {
                            if (numLetters3 < 5) {
                                stringBuilder3.append(newCh);
                                numLetters3++;
                            }
                        } else if (numAttempts == 3) {
                            if (numLetters4 < 5) {
                                stringBuilder4.append(newCh);
                                numLetters4++;
                            }
                        } else if (numAttempts == 4) {
                            if (numLetters5 < 5) {
                                stringBuilder5.append(newCh);
                                numLetters5++;
                            }
                        } else if (numAttempts == 5) {
                            if (numLetters6 < 5) {
                                stringBuilder6.append(newCh);
                                numLetters6++;
                            }
                        }
                        break;
                    case ('W'):
                        newCh = 'W';
                        if (numAttempts == 0) {
                            if (numLetters1 < 5) {
                                stringBuilder1.append(newCh);
                                numLetters1++;
                            }
                        } else if (numAttempts == 1) {
                            if (numLetters2 < 5) {
                                stringBuilder2.append(newCh);
                                numLetters2++;
                            }
                        } else if (numAttempts == 2) {
                            if (numLetters3 < 5) {
                                stringBuilder3.append(newCh);
                                numLetters3++;
                            }
                        } else if (numAttempts == 3) {
                            if (numLetters4 < 5) {
                                stringBuilder4.append(newCh);
                                numLetters4++;
                            }
                        } else if (numAttempts == 4) {
                            if (numLetters5 < 5) {
                                stringBuilder5.append(newCh);
                                numLetters5++;
                            }
                        } else if (numAttempts == 5) {
                            if (numLetters6 < 5) {
                                stringBuilder6.append(newCh);
                                numLetters6++;
                            }
                        }
                        break;
                    case ('X'):
                        newCh = 'X';
                        if (numAttempts == 0) {
                            if (numLetters1 < 5) {
                                stringBuilder1.append(newCh);
                                numLetters1++;
                            }
                        } else if (numAttempts == 1) {
                            if (numLetters2 < 5) {
                                stringBuilder2.append(newCh);
                                numLetters2++;
                            }
                        } else if (numAttempts == 2) {
                            if (numLetters3 < 5) {
                                stringBuilder3.append(newCh);
                                numLetters3++;
                            }
                        } else if (numAttempts == 3) {
                            if (numLetters4 < 5) {
                                stringBuilder4.append(newCh);
                                numLetters4++;
                            }
                        } else if (numAttempts == 4) {
                            if (numLetters5 < 5) {
                                stringBuilder5.append(newCh);
                                numLetters5++;
                            }
                        } else if (numAttempts == 5) {
                            if (numLetters6 < 5) {
                                stringBuilder6.append(newCh);
                                numLetters6++;
                            }
                        }
                        break;
                    case ('Y'):
                        newCh = 'Y';
                        if (numAttempts == 0) {
                            if (numLetters1 < 5) {
                                stringBuilder1.append(newCh);
                                numLetters1++;
                            }
                        } else if (numAttempts == 1) {
                            if (numLetters2 < 5) {
                                stringBuilder2.append(newCh);
                                numLetters2++;
                            }
                        } else if (numAttempts == 2) {
                            if (numLetters3 < 5) {
                                stringBuilder3.append(newCh);
                                numLetters3++;
                            }
                        } else if (numAttempts == 3) {
                            if (numLetters4 < 5) {
                                stringBuilder4.append(newCh);
                                numLetters4++;
                            }
                        } else if (numAttempts == 4) {
                            if (numLetters5 < 5) {
                                stringBuilder5.append(newCh);
                                numLetters5++;
                            }
                        } else if (numAttempts == 5) {
                            if (numLetters6 < 5) {
                                stringBuilder6.append(newCh);
                                numLetters6++;
                            }
                        }
                        break;
                    case ('Z'):
                        newCh = 'Z';
                        if (numAttempts == 0) {
                            if (numLetters1 < 5) {
                                stringBuilder1.append(newCh);
                                numLetters1++;
                            }
                        } else if (numAttempts == 1) {
                            if (numLetters2 < 5) {
                                stringBuilder2.append(newCh);
                                numLetters2++;
                            }
                        } else if (numAttempts == 2) {
                            if (numLetters3 < 5) {
                                stringBuilder3.append(newCh);
                                numLetters3++;
                            }
                        } else if (numAttempts == 3) {
                            if (numLetters4 < 5) {
                                stringBuilder4.append(newCh);
                                numLetters4++;
                            }
                        } else if (numAttempts == 4) {
                            if (numLetters5 < 5) {
                                stringBuilder5.append(newCh);
                                numLetters5++;
                            }
                        } else if (numAttempts == 5) {
                            if (numLetters6 < 5) {
                                stringBuilder6.append(newCh);
                                numLetters6++;
                            }
                        }
                        break;
                    case (KeyEvent.VK_BACK_SPACE):
                        if (numAttempts == 0) {
                            if (numLetters1 > 0) {
                                stringBuilder1.deleteCharAt(stringBuilder1.length() - 1);
                                numLetters1--;
                                showError = false;
                            }
                        } else if (numAttempts == 1) {
                            if (numLetters2 > 0) {
                                stringBuilder2.deleteCharAt(stringBuilder2.length() - 1);
                                numLetters2--;
                                showError = false;
                            }
                        } else if (numAttempts == 2) {
                            if (numLetters3 > 0) {
                                stringBuilder3.deleteCharAt(stringBuilder3.length() - 1);
                                numLetters3--;
                                showError = false;
                            }
                        } else if (numAttempts == 3) {
                            if (numLetters4 > 0) {
                                stringBuilder4.deleteCharAt(stringBuilder4.length() - 1);
                                numLetters4--;
                                showError = false;
                            }
                        } else if (numAttempts == 4) {
                            if (numLetters5 > 0) {
                                stringBuilder5.deleteCharAt(stringBuilder5.length() - 1);
                                numLetters5--;
                                showError = false;
                            }
                        } else if (numAttempts == 5) {
                            if (numLetters6 > 0) {
                                stringBuilder6.deleteCharAt(stringBuilder6.length() - 1);
                                numLetters6--;
                                showError = false;
                            }
                        }
                        break;
                    case (KeyEvent.VK_ENTER):
                        if (numAttempts == 0) {
                            if (numLetters1 == 5) {
                                guessValid = checkValidity(stringBuilder1.toString());
                                if(guessValid) {
                                    checkAttempt(stringBuilder1.toString());
                                    numAttempts++;
                                    guessValid = false;
                                }
                                else {
                                    showError = true;
                                }
                            }
                        } else if (numAttempts == 1) {
                            if (numLetters2 == 5) {
                                guessValid = checkValidity(stringBuilder2.toString());
                                if(guessValid) {
                                    checkAttempt(stringBuilder2.toString());
                                    numAttempts++;
                                    guessValid = false;
                                }
                                else {
                                    showError = true;
                                }
                            }
                        } else if (numAttempts == 2) {
                            if (numLetters3 == 5) {
                                guessValid = checkValidity(stringBuilder3.toString());
                                if(guessValid) {
                                    checkAttempt(stringBuilder3.toString());
                                    numAttempts++;
                                    guessValid = false;
                                }
                                else {
                                    showError = true;
                                }
                            }
                        } else if (numAttempts == 3) {
                            if (numLetters4 == 5) {
                                guessValid = checkValidity(stringBuilder4.toString());
                                if(guessValid) {
                                    checkAttempt(stringBuilder4.toString());
                                    numAttempts++;
                                    guessValid = false;
                                }
                                else {
                                    showError = true;
                                }
                            }
                        } else if (numAttempts == 4) {
                            if (numLetters5 == 5) {
                                guessValid = checkValidity(stringBuilder5.toString());
                                if(guessValid) {
                                    checkAttempt(stringBuilder5.toString());
                                    numAttempts++;
                                    guessValid = false;
                                }
                                else {
                                    showError = true;
                                }
                            }
                        } else if (numAttempts == 5) {
                            if (numLetters6 == 5) {
                                guessValid = checkValidity(stringBuilder6.toString());
                                if(guessValid) {
                                    checkAttempt(stringBuilder6.toString());
                                    numAttempts++;
                                    guessValid = false;
                                }
                                else {
                                    showError = true;
                                }
                            }
                        } else if (numAttempts == 6) { // do nothing
                        }
                        break;
                }
            }
        }
    }

    public static void checkAttempt(String inputWord) {

        int correctLetters = 0;
        int[] freqChar = {0,0,0,0,0};
        boolean[] charAddressed = {false, false, false, false, false};

        for (int i=0; i<5; i++) {
            for (int j=0; j<5; j++) {
                if (correctWord.charAt(i) == correctWord.charAt(j)) {
                    freqChar[i]++;
                }
            }
        }

        for (int i=0; i<5; i++) {
            if(inputWord.charAt(i) == correctWord.charAt(i)) {
                board.get(5*numAttempts+i).color = greenBox;
                keys.get(order.indexOf(inputWord.charAt(i))).color = greenBox;
                charAddressed[i] = true;
                correctLetters++;

                if(correctLetters == 5) {
                    gameWon = true;
                }

                for (int j=0; j<5; j++) {
                    if (correctWord.charAt(i) == correctWord.charAt(j)) {
                        freqChar[j]--;
                    }
                }
            }
        }

        if (numAttempts == 5) {
            if (correctLetters < 5) {
                gameOver = true;
            }
        }

        for (int i=0; i<5; i++) {
            if(!charAddressed[i]) {
                board.get(5*numAttempts+i).color = grayBox;
                if (keys.get(order.indexOf(inputWord.charAt(i))).color == lightGray) {
                    keys.get(order.indexOf(inputWord.charAt(i))).color = grayBox;
                }
                for (int j=0; j<5; j++) {
                    if (inputWord.charAt(i) == correctWord.charAt(j)) {
                        if(freqChar[j] != 0) {
                            if (i != j) {
                                if(!charAddressed[i]) {
                                    board.get(5 * numAttempts + i).color = yellowBox;
                                    if (keys.get(order.indexOf(inputWord.charAt(i))).color != greenBox) {
                                        keys.get(order.indexOf(inputWord.charAt(i))).color = yellowBox;
                                    }
                                    charAddressed[i] = true;

                                    for (int k = 0; k < 5; k++) {
                                        if (inputWord.charAt(i) == correctWord.charAt(k)) {
                                            freqChar[k]--;
                                            if(freqChar[k] == 0) {
                                                for (int l = 0; l < 5; l++) {
                                                    if (correctWord.charAt(k) == inputWord.charAt(l)) {
                                                        if (!charAddressed[l]) {
                                                            board.get(5*numAttempts+l).color = grayBox;
                                                                if (keys.get(order.indexOf(inputWord.charAt(i))).color == lightGray) {
                                                                    keys.get(order.indexOf(inputWord.charAt(i))).color = grayBox;
                                                                }
                                                            charAddressed[l] = true;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        panel.repaint();
    }

    private static void drawGameOver(Graphics g) {
        if(gameOver) {
            Font numFont = new Font("Helvetica Nueue", Font.BOLD, 19);

            g.setFont(numFont);
            g.setColor(Color.BLACK);
            g.drawString("Game Over! The word was: " + correctWord, 130, 50);
        }
    }

    private static void drawGameWon(Graphics g)
    {
        if(gameWon)
        {
            Font numFont = new Font("Helvetica Nueue", Font.BOLD, 19);

            g.setFont(numFont);
            g.setColor(Color.BLACK);
            g.drawString("CONGRATULATIONS! You solved Wordle in " + (numAttempts) + " tries!", 52, 50);
        }
    }

    private int getIndex(int mouseX, int mouseY)
    {
        for (int i = 0; i < 28; i++)
        {
            Box box = keys.get(i);

            if (mouseX >= box.x && mouseX <= (box.x + box.width) && mouseY >= box.y && mouseY <= (box.y + box.height))
            {
                return i;
            }
        }
        return -1;
    }

    private void clickChange(int i) {
        char newCh;

        switch (i) {
            case 0:
                newCh = 'Q';
                if(numAttempts == 0) {
                    if(numLetters1 < 5) {
                        stringBuilder1.append(newCh);
                        numLetters1++;
                    }
                }
                else if(numAttempts == 1) {
                    if(numLetters2 < 5) {
                        stringBuilder2.append(newCh);
                        numLetters2++;
                    }
                }
                else if(numAttempts == 2) {
                    if(numLetters3 < 5) {
                        stringBuilder3.append(newCh);
                        numLetters3++;
                    }
                }
                else if(numAttempts == 3) {
                    if(numLetters4 < 5) {
                        stringBuilder4.append(newCh);
                        numLetters4++;
                    }
                }
                else if(numAttempts == 4) {
                    if(numLetters5 < 5) {
                        stringBuilder5.append(newCh);
                        numLetters5++;
                    }
                }
                else if(numAttempts == 5) {
                    if(numLetters6 < 5) {
                        stringBuilder6.append(newCh);
                        numLetters6++;
                    }
                }
                break;
            case 1:
                newCh = 'W';
                if(numAttempts == 0) {
                    if(numLetters1 < 5) {
                        stringBuilder1.append(newCh);
                        numLetters1++;
                    }
                }
                else if(numAttempts == 1) {
                    if(numLetters2 < 5) {
                        stringBuilder2.append(newCh);
                        numLetters2++;
                    }
                }
                else if(numAttempts == 2) {
                    if(numLetters3 < 5) {
                        stringBuilder3.append(newCh);
                        numLetters3++;
                    }
                }
                else if(numAttempts == 3) {
                    if(numLetters4 < 5) {
                        stringBuilder4.append(newCh);
                        numLetters4++;
                    }
                }
                else if(numAttempts == 4) {
                    if(numLetters5 < 5) {
                        stringBuilder5.append(newCh);
                        numLetters5++;
                    }
                }
                else if(numAttempts == 5) {
                    if(numLetters6 < 5) {
                        stringBuilder6.append(newCh);
                        numLetters6++;
                    }
                }
                break;
            case 2:
                newCh = 'E';
                if(numAttempts == 0) {
                    if(numLetters1 < 5) {
                        stringBuilder1.append(newCh);
                        numLetters1++;
                    }
                }
                else if(numAttempts == 1) {
                    if(numLetters2 < 5) {
                        stringBuilder2.append(newCh);
                        numLetters2++;
                    }
                }
                else if(numAttempts == 2) {
                    if(numLetters3 < 5) {
                        stringBuilder3.append(newCh);
                        numLetters3++;
                    }
                }
                else if(numAttempts == 3) {
                    if(numLetters4 < 5) {
                        stringBuilder4.append(newCh);
                        numLetters4++;
                    }
                }
                else if(numAttempts == 4) {
                    if(numLetters5 < 5) {
                        stringBuilder5.append(newCh);
                        numLetters5++;
                    }
                }
                else if(numAttempts == 5) {
                    if(numLetters6 < 5) {
                        stringBuilder6.append(newCh);
                        numLetters6++;
                    }
                }
                break;
            case 3:
                newCh = 'R';
                if(numAttempts == 0) {
                    if(numLetters1 < 5) {
                        stringBuilder1.append(newCh);
                        numLetters1++;
                    }
                }
                else if(numAttempts == 1) {
                    if(numLetters2 < 5) {
                        stringBuilder2.append(newCh);
                        numLetters2++;
                    }
                }
                else if(numAttempts == 2) {
                    if(numLetters3 < 5) {
                        stringBuilder3.append(newCh);
                        numLetters3++;
                    }
                }
                else if(numAttempts == 3) {
                    if(numLetters4 < 5) {
                        stringBuilder4.append(newCh);
                        numLetters4++;
                    }
                }
                else if(numAttempts == 4) {
                    if(numLetters5 < 5) {
                        stringBuilder5.append(newCh);
                        numLetters5++;
                    }
                }
                else if(numAttempts == 5) {
                    if(numLetters6 < 5) {
                        stringBuilder6.append(newCh);
                        numLetters6++;
                    }
                }
                break;
            case 4:
                newCh = 'T';
                if(numAttempts == 0) {
                    if(numLetters1 < 5) {
                        stringBuilder1.append(newCh);
                        numLetters1++;
                    }
                }
                else if(numAttempts == 1) {
                    if(numLetters2 < 5) {
                        stringBuilder2.append(newCh);
                        numLetters2++;
                    }
                }
                else if(numAttempts == 2) {
                    if(numLetters3 < 5) {
                        stringBuilder3.append(newCh);
                        numLetters3++;
                    }
                }
                else if(numAttempts == 3) {
                    if(numLetters4 < 5) {
                        stringBuilder4.append(newCh);
                        numLetters4++;
                    }
                }
                else if(numAttempts == 4) {
                    if(numLetters5 < 5) {
                        stringBuilder5.append(newCh);
                        numLetters5++;
                    }
                }
                else if(numAttempts == 5) {
                    if(numLetters6 < 5) {
                        stringBuilder6.append(newCh);
                        numLetters6++;
                    }
                }
                break;
            case 5:
                newCh = 'Y';
                if(numAttempts == 0) {
                    if(numLetters1 < 5) {
                        stringBuilder1.append(newCh);
                        numLetters1++;
                    }
                }
                else if(numAttempts == 1) {
                    if(numLetters2 < 5) {
                        stringBuilder2.append(newCh);
                        numLetters2++;
                    }
                }
                else if(numAttempts == 2) {
                    if(numLetters3 < 5) {
                        stringBuilder3.append(newCh);
                        numLetters3++;
                    }
                }
                else if(numAttempts == 3) {
                    if(numLetters4 < 5) {
                        stringBuilder4.append(newCh);
                        numLetters4++;
                    }
                }
                else if(numAttempts == 4) {
                    if(numLetters5 < 5) {
                        stringBuilder5.append(newCh);
                        numLetters5++;
                    }
                }
                else if(numAttempts == 5) {
                    if(numLetters6 < 5) {
                        stringBuilder6.append(newCh);
                        numLetters6++;
                    }
                }
                break;
            case 6:
                newCh = 'U';
                if(numAttempts == 0) {
                    if(numLetters1 < 5) {
                        stringBuilder1.append(newCh);
                        numLetters1++;
                    }
                }
                else if(numAttempts == 1) {
                    if(numLetters2 < 5) {
                        stringBuilder2.append(newCh);
                        numLetters2++;
                    }
                }
                else if(numAttempts == 2) {
                    if(numLetters3 < 5) {
                        stringBuilder3.append(newCh);
                        numLetters3++;
                    }
                }
                else if(numAttempts == 3) {
                    if(numLetters4 < 5) {
                        stringBuilder4.append(newCh);
                        numLetters4++;
                    }
                }
                else if(numAttempts == 4) {
                    if(numLetters5 < 5) {
                        stringBuilder5.append(newCh);
                        numLetters5++;
                    }
                }
                else if(numAttempts == 5) {
                    if(numLetters6 < 5) {
                        stringBuilder6.append(newCh);
                        numLetters6++;
                    }
                }
                break;
            case 7:
                newCh = 'I';
                if(numAttempts == 0) {
                    if(numLetters1 < 5) {
                        stringBuilder1.append(newCh);
                        numLetters1++;
                    }
                }
                else if(numAttempts == 1) {
                    if(numLetters2 < 5) {
                        stringBuilder2.append(newCh);
                        numLetters2++;
                    }
                }
                else if(numAttempts == 2) {
                    if(numLetters3 < 5) {
                        stringBuilder3.append(newCh);
                        numLetters3++;
                    }
                }
                else if(numAttempts == 3) {
                    if(numLetters4 < 5) {
                        stringBuilder4.append(newCh);
                        numLetters4++;
                    }
                }
                else if(numAttempts == 4) {
                    if(numLetters5 < 5) {
                        stringBuilder5.append(newCh);
                        numLetters5++;
                    }
                }
                else if(numAttempts == 5) {
                    if(numLetters6 < 5) {
                        stringBuilder6.append(newCh);
                        numLetters6++;
                    }
                }
                break;
            case 8:
                newCh = 'O';
                if(numAttempts == 0) {
                    if(numLetters1 < 5) {
                        stringBuilder1.append(newCh);
                        numLetters1++;
                    }
                }
                else if(numAttempts == 1) {
                    if(numLetters2 < 5) {
                        stringBuilder2.append(newCh);
                        numLetters2++;
                    }
                }
                else if(numAttempts == 2) {
                    if(numLetters3 < 5) {
                        stringBuilder3.append(newCh);
                        numLetters3++;
                    }
                }
                else if(numAttempts == 3) {
                    if(numLetters4 < 5) {
                        stringBuilder4.append(newCh);
                        numLetters4++;
                    }
                }
                else if(numAttempts == 4) {
                    if(numLetters5 < 5) {
                        stringBuilder5.append(newCh);
                        numLetters5++;
                    }
                }
                else if(numAttempts == 5) {
                    if(numLetters6 < 5) {
                        stringBuilder6.append(newCh);
                        numLetters6++;
                    }
                }
                break;
            case 9:
                newCh = 'P';
                if(numAttempts == 0) {
                    if(numLetters1 < 5) {
                        stringBuilder1.append(newCh);
                        numLetters1++;
                    }
                }
                else if(numAttempts == 1) {
                    if(numLetters2 < 5) {
                        stringBuilder2.append(newCh);
                        numLetters2++;
                    }
                }
                else if(numAttempts == 2) {
                    if(numLetters3 < 5) {
                        stringBuilder3.append(newCh);
                        numLetters3++;
                    }
                }
                else if(numAttempts == 3) {
                    if(numLetters4 < 5) {
                        stringBuilder4.append(newCh);
                        numLetters4++;
                    }
                }
                else if(numAttempts == 4) {
                    if(numLetters5 < 5) {
                        stringBuilder5.append(newCh);
                        numLetters5++;
                    }
                }
                else if(numAttempts == 5) {
                    if(numLetters6 < 5) {
                        stringBuilder6.append(newCh);
                        numLetters6++;
                    }
                }
                break;
            case 10:
                newCh = 'A';
                if(numAttempts == 0) {
                    if(numLetters1 < 5) {
                        stringBuilder1.append(newCh);
                        numLetters1++;
                    }
                }
                else if(numAttempts == 1) {
                    if(numLetters2 < 5) {
                        stringBuilder2.append(newCh);
                        numLetters2++;
                    }
                }
                else if(numAttempts == 2) {
                    if(numLetters3 < 5) {
                        stringBuilder3.append(newCh);
                        numLetters3++;
                    }
                }
                else if(numAttempts == 3) {
                    if(numLetters4 < 5) {
                        stringBuilder4.append(newCh);
                        numLetters4++;
                    }
                }
                else if(numAttempts == 4) {
                    if(numLetters5 < 5) {
                        stringBuilder5.append(newCh);
                        numLetters5++;
                    }
                }
                else if(numAttempts == 5) {
                    if(numLetters6 < 5) {
                        stringBuilder6.append(newCh);
                        numLetters6++;
                    }
                }
                break;
            case 11:
                newCh = 'S';
                if(numAttempts == 0) {
                    if(numLetters1 < 5) {
                        stringBuilder1.append(newCh);
                        numLetters1++;
                    }
                }
                else if(numAttempts == 1) {
                    if(numLetters2 < 5) {
                        stringBuilder2.append(newCh);
                        numLetters2++;
                    }
                }
                else if(numAttempts == 2) {
                    if(numLetters3 < 5) {
                        stringBuilder3.append(newCh);
                        numLetters3++;
                    }
                }
                else if(numAttempts == 3) {
                    if(numLetters4 < 5) {
                        stringBuilder4.append(newCh);
                        numLetters4++;
                    }
                }
                else if(numAttempts == 4) {
                    if(numLetters5 < 5) {
                        stringBuilder5.append(newCh);
                        numLetters5++;
                    }
                }
                else if(numAttempts == 5) {
                    if(numLetters6 < 5) {
                        stringBuilder6.append(newCh);
                        numLetters6++;
                    }
                }
                break;
            case 12:
                newCh = 'D';
                if(numAttempts == 0) {
                    if(numLetters1 < 5) {
                        stringBuilder1.append(newCh);
                        numLetters1++;
                    }
                }
                else if(numAttempts == 1) {
                    if(numLetters2 < 5) {
                        stringBuilder2.append(newCh);
                        numLetters2++;
                    }
                }
                else if(numAttempts == 2) {
                    if(numLetters3 < 5) {
                        stringBuilder3.append(newCh);
                        numLetters3++;
                    }
                }
                else if(numAttempts == 3) {
                    if(numLetters4 < 5) {
                        stringBuilder4.append(newCh);
                        numLetters4++;
                    }
                }
                else if(numAttempts == 4) {
                    if(numLetters5 < 5) {
                        stringBuilder5.append(newCh);
                        numLetters5++;
                    }
                }
                else if(numAttempts == 5) {
                    if(numLetters6 < 5) {
                        stringBuilder6.append(newCh);
                        numLetters6++;
                    }
                }
                break;
            case 13:
                newCh = 'F';
                if(numAttempts == 0) {
                    if(numLetters1 < 5) {
                        stringBuilder1.append(newCh);
                        numLetters1++;
                    }
                }
                else if(numAttempts == 1) {
                    if(numLetters2 < 5) {
                        stringBuilder2.append(newCh);
                        numLetters2++;
                    }
                }
                else if(numAttempts == 2) {
                    if(numLetters3 < 5) {
                        stringBuilder3.append(newCh);
                        numLetters3++;
                    }
                }
                else if(numAttempts == 3) {
                    if(numLetters4 < 5) {
                        stringBuilder4.append(newCh);
                        numLetters4++;
                    }
                }
                else if(numAttempts == 4) {
                    if(numLetters5 < 5) {
                        stringBuilder5.append(newCh);
                        numLetters5++;
                    }
                }
                else if(numAttempts == 5) {
                    if(numLetters6 < 5) {
                        stringBuilder6.append(newCh);
                        numLetters6++;
                    }
                }
                break;
            case 14:
                newCh = 'G';
                if(numAttempts == 0) {
                    if(numLetters1 < 5) {
                        stringBuilder1.append(newCh);
                        numLetters1++;
                    }
                }
                else if(numAttempts == 1) {
                    if(numLetters2 < 5) {
                        stringBuilder2.append(newCh);
                        numLetters2++;
                    }
                }
                else if(numAttempts == 2) {
                    if(numLetters3 < 5) {
                        stringBuilder3.append(newCh);
                        numLetters3++;
                    }
                }
                else if(numAttempts == 3) {
                    if(numLetters4 < 5) {
                        stringBuilder4.append(newCh);
                        numLetters4++;
                    }
                }
                else if(numAttempts == 4) {
                    if(numLetters5 < 5) {
                        stringBuilder5.append(newCh);
                        numLetters5++;
                    }
                }
                else if(numAttempts == 5) {
                    if(numLetters6 < 5) {
                        stringBuilder6.append(newCh);
                        numLetters6++;
                    }
                }
                break;
            case 15:
                newCh = 'H';
                if(numAttempts == 0) {
                    if(numLetters1 < 5) {
                        stringBuilder1.append(newCh);
                        numLetters1++;
                    }
                }
                else if(numAttempts == 1) {
                    if(numLetters2 < 5) {
                        stringBuilder2.append(newCh);
                        numLetters2++;
                    }
                }
                else if(numAttempts == 2) {
                    if(numLetters3 < 5) {
                        stringBuilder3.append(newCh);
                        numLetters3++;
                    }
                }
                else if(numAttempts == 3) {
                    if(numLetters4 < 5) {
                        stringBuilder4.append(newCh);
                        numLetters4++;
                    }
                }
                else if(numAttempts == 4) {
                    if(numLetters5 < 5) {
                        stringBuilder5.append(newCh);
                        numLetters5++;
                    }
                }
                else if(numAttempts == 5) {
                    if(numLetters6 < 5) {
                        stringBuilder6.append(newCh);
                        numLetters6++;
                    }
                }
                break;
            case 16:
                newCh = 'J';
                if(numAttempts == 0) {
                    if(numLetters1 < 5) {
                        stringBuilder1.append(newCh);
                        numLetters1++;
                    }
                }
                else if(numAttempts == 1) {
                    if(numLetters2 < 5) {
                        stringBuilder2.append(newCh);
                        numLetters2++;
                    }
                }
                else if(numAttempts == 2) {
                    if(numLetters3 < 5) {
                        stringBuilder3.append(newCh);
                        numLetters3++;
                    }
                }
                else if(numAttempts == 3) {
                    if(numLetters4 < 5) {
                        stringBuilder4.append(newCh);
                        numLetters4++;
                    }
                }
                else if(numAttempts == 4) {
                    if(numLetters5 < 5) {
                        stringBuilder5.append(newCh);
                        numLetters5++;
                    }
                }
                else if(numAttempts == 5) {
                    if(numLetters6 < 5) {
                        stringBuilder6.append(newCh);
                        numLetters6++;
                    }
                }
                break;
            case 17:
                newCh = 'K';
                if(numAttempts == 0) {
                    if(numLetters1 < 5) {
                        stringBuilder1.append(newCh);
                        numLetters1++;
                    }
                }
                else if(numAttempts == 1) {
                    if(numLetters2 < 5) {
                        stringBuilder2.append(newCh);
                        numLetters2++;
                    }
                }
                else if(numAttempts == 2) {
                    if(numLetters3 < 5) {
                        stringBuilder3.append(newCh);
                        numLetters3++;
                    }
                }
                else if(numAttempts == 3) {
                    if(numLetters4 < 5) {
                        stringBuilder4.append(newCh);
                        numLetters4++;
                    }
                }
                else if(numAttempts == 4) {
                    if(numLetters5 < 5) {
                        stringBuilder5.append(newCh);
                        numLetters5++;
                    }
                }
                else if(numAttempts == 5) {
                    if(numLetters6 < 5) {
                        stringBuilder6.append(newCh);
                        numLetters6++;
                    }
                }
                break;
            case 18:
                newCh = 'L';
                if(numAttempts == 0) {
                    if(numLetters1 < 5) {
                        stringBuilder1.append(newCh);
                        numLetters1++;
                    }
                }
                else if(numAttempts == 1) {
                    if(numLetters2 < 5) {
                        stringBuilder2.append(newCh);
                        numLetters2++;
                    }
                }
                else if(numAttempts == 2) {
                    if(numLetters3 < 5) {
                        stringBuilder3.append(newCh);
                        numLetters3++;
                    }
                }
                else if(numAttempts == 3) {
                    if(numLetters4 < 5) {
                        stringBuilder4.append(newCh);
                        numLetters4++;
                    }
                }
                else if(numAttempts == 4) {
                    if(numLetters5 < 5) {
                        stringBuilder5.append(newCh);
                        numLetters5++;
                    }
                }
                else if(numAttempts == 5) {
                    if(numLetters6 < 5) {
                        stringBuilder6.append(newCh);
                        numLetters6++;
                    }
                }
                break;
            case 19:
                newCh = 'Z';
                if(numAttempts == 0) {
                    if(numLetters1 < 5) {
                        stringBuilder1.append(newCh);
                        numLetters1++;
                    }
                }
                else if(numAttempts == 1) {
                    if(numLetters2 < 5) {
                        stringBuilder2.append(newCh);
                        numLetters2++;
                    }
                }
                else if(numAttempts == 2) {
                    if(numLetters3 < 5) {
                        stringBuilder3.append(newCh);
                        numLetters3++;
                    }
                }
                else if(numAttempts == 3) {
                    if(numLetters4 < 5) {
                        stringBuilder4.append(newCh);
                        numLetters4++;
                    }
                }
                else if(numAttempts == 4) {
                    if(numLetters5 < 5) {
                        stringBuilder5.append(newCh);
                        numLetters5++;
                    }
                }
                else if(numAttempts == 5) {
                    if(numLetters6 < 5) {
                        stringBuilder6.append(newCh);
                        numLetters6++;
                    }
                }
                break;
            case 20:
                newCh = 'X';
                if(numAttempts == 0) {
                    if(numLetters1 < 5) {
                        stringBuilder1.append(newCh);
                        numLetters1++;
                    }
                }
                else if(numAttempts == 1) {
                    if(numLetters2 < 5) {
                        stringBuilder2.append(newCh);
                        numLetters2++;
                    }
                }
                else if(numAttempts == 2) {
                    if(numLetters3 < 5) {
                        stringBuilder3.append(newCh);
                        numLetters3++;
                    }
                }
                else if(numAttempts == 3) {
                    if(numLetters4 < 5) {
                        stringBuilder4.append(newCh);
                        numLetters4++;
                    }
                }
                else if(numAttempts == 4) {
                    if(numLetters5 < 5) {
                        stringBuilder5.append(newCh);
                        numLetters5++;
                    }
                }
                else if(numAttempts == 5) {
                    if(numLetters6 < 5) {
                        stringBuilder6.append(newCh);
                        numLetters6++;
                    }
                }
                break;
            case 21:
                newCh = 'C';
                if(numAttempts == 0) {
                    if(numLetters1 < 5) {
                        stringBuilder1.append(newCh);
                        numLetters1++;
                    }
                }
                else if(numAttempts == 1) {
                    if(numLetters2 < 5) {
                        stringBuilder2.append(newCh);
                        numLetters2++;
                    }
                }
                else if(numAttempts == 2) {
                    if(numLetters3 < 5) {
                        stringBuilder3.append(newCh);
                        numLetters3++;
                    }
                }
                else if(numAttempts == 3) {
                    if(numLetters4 < 5) {
                        stringBuilder4.append(newCh);
                        numLetters4++;
                    }
                }
                else if(numAttempts == 4) {
                    if(numLetters5 < 5) {
                        stringBuilder5.append(newCh);
                        numLetters5++;
                    }
                }
                else if(numAttempts == 5) {
                    if(numLetters6 < 5) {
                        stringBuilder6.append(newCh);
                        numLetters6++;
                    }
                }
                break;
            case 22:
                newCh = 'V';
                if(numAttempts == 0) {
                    if(numLetters1 < 5) {
                        stringBuilder1.append(newCh);
                        numLetters1++;
                    }
                }
                else if(numAttempts == 1) {
                    if(numLetters2 < 5) {
                        stringBuilder2.append(newCh);
                        numLetters2++;
                    }
                }
                else if(numAttempts == 2) {
                    if(numLetters3 < 5) {
                        stringBuilder3.append(newCh);
                        numLetters3++;
                    }
                }
                else if(numAttempts == 3) {
                    if(numLetters4 < 5) {
                        stringBuilder4.append(newCh);
                        numLetters4++;
                    }
                }
                else if(numAttempts == 4) {
                    if(numLetters5 < 5) {
                        stringBuilder5.append(newCh);
                        numLetters5++;
                    }
                }
                else if(numAttempts == 5) {
                    if(numLetters6 < 5) {
                        stringBuilder6.append(newCh);
                        numLetters6++;
                    }
                }
                break;
            case 23:
                newCh = 'B';
                if(numAttempts == 0) {
                    if(numLetters1 < 5) {
                        stringBuilder1.append(newCh);
                        numLetters1++;
                    }
                }
                else if(numAttempts == 1) {
                    if(numLetters2 < 5) {
                        stringBuilder2.append(newCh);
                        numLetters2++;
                    }
                }
                else if(numAttempts == 2) {
                    if(numLetters3 < 5) {
                        stringBuilder3.append(newCh);
                        numLetters3++;
                    }
                }
                else if(numAttempts == 3) {
                    if(numLetters4 < 5) {
                        stringBuilder4.append(newCh);
                        numLetters4++;
                    }
                }
                else if(numAttempts == 4) {
                    if(numLetters5 < 5) {
                        stringBuilder5.append(newCh);
                        numLetters5++;
                    }
                }
                else if(numAttempts == 5) {
                    if(numLetters6 < 5) {
                        stringBuilder6.append(newCh);
                        numLetters6++;
                    }
                }
                break;
            case 24:
                newCh = 'N';
                if(numAttempts == 0) {
                    if(numLetters1 < 5) {
                        stringBuilder1.append(newCh);
                        numLetters1++;
                    }
                }
                else if(numAttempts == 1) {
                    if(numLetters2 < 5) {
                        stringBuilder2.append(newCh);
                        numLetters2++;
                    }
                }
                else if(numAttempts == 2) {
                    if(numLetters3 < 5) {
                        stringBuilder3.append(newCh);
                        numLetters3++;
                    }
                }
                else if(numAttempts == 3) {
                    if(numLetters4 < 5) {
                        stringBuilder4.append(newCh);
                        numLetters4++;
                    }
                }
                else if(numAttempts == 4) {
                    if(numLetters5 < 5) {
                        stringBuilder5.append(newCh);
                        numLetters5++;
                    }
                }
                else if(numAttempts == 5) {
                    if(numLetters6 < 5) {
                        stringBuilder6.append(newCh);
                        numLetters6++;
                    }
                }
                break;
            case 25:
                newCh = 'M';
                if(numAttempts == 0) {
                    if(numLetters1 < 5) {
                        stringBuilder1.append(newCh);
                        numLetters1++;
                    }
                }
                else if(numAttempts == 1) {
                    if(numLetters2 < 5) {
                        stringBuilder2.append(newCh);
                        numLetters2++;
                    }
                }
                else if(numAttempts == 2) {
                    if(numLetters3 < 5) {
                        stringBuilder3.append(newCh);
                        numLetters3++;
                    }
                }
                else if(numAttempts == 3) {
                    if(numLetters4 < 5) {
                        stringBuilder4.append(newCh);
                        numLetters4++;
                    }
                }
                else if(numAttempts == 4) {
                    if(numLetters5 < 5) {
                        stringBuilder5.append(newCh);
                        numLetters5++;
                    }
                }
                else if(numAttempts == 5) {
                    if(numLetters6 < 5) {
                        stringBuilder6.append(newCh);
                        numLetters6++;
                    }
                }
                break;
            case 26:
                if(numAttempts == 0) {
                    if(numLetters1 == 5) {
                        System.out.println(stringBuilder1.toString());
                        System.out.println(stringBuilder1.toString());
                        guessValid = checkValidity(stringBuilder1.toString());
                        if(guessValid) {
                            System.out.println("GUESS VALID");
                            checkAttempt(stringBuilder1.toString());
                            numAttempts++;
                            guessValid = false;
                        }
                        else {
                            showError = true;
                        }
                    }
                }
                else if(numAttempts == 1) {
                    if(numLetters2 == 5) {
                        guessValid = checkValidity(stringBuilder2.toString());
                        if(guessValid) {
                            checkAttempt(stringBuilder2.toString());
                            numAttempts++;
                            guessValid = false;
                        }
                        else {
                            showError = true;
                        }
                    }
                }
                else if(numAttempts == 2) {
                    if(numLetters3 == 5) {
                        guessValid = checkValidity(stringBuilder3.toString());
                        if(guessValid) {
                            checkAttempt(stringBuilder3.toString());
                            numAttempts++;
                            guessValid = false;
                        }
                        else {
                            showError = true;
                        }
                    }
                }
                else if(numAttempts == 3) {
                    if(numLetters4 == 5) {
                        guessValid = checkValidity(stringBuilder4.toString());
                        if(guessValid) {
                            checkAttempt(stringBuilder4.toString());
                            numAttempts++;
                            guessValid = false;
                        }
                        else {
                            showError = true;
                        }
                    }
                }
                else if(numAttempts == 4) {
                    if(numLetters5 == 5) {
                        guessValid = checkValidity(stringBuilder5.toString());
                        if(guessValid) {
                            checkAttempt(stringBuilder5.toString());
                            numAttempts++;
                            guessValid = false;
                        }
                        else {
                            showError = true;
                        }
                    }
                }
                else if(numAttempts == 5) {
                    if(numLetters6 == 5) {
                        guessValid = checkValidity(stringBuilder6.toString());
                        if(guessValid) {
                            checkAttempt(stringBuilder6.toString());
                            numAttempts++;
                            guessValid = false;
                        }
                        else {
                            showError = true;
                        }
                    }
                }
                else if(numAttempts == 6) { // do nothing
                }
                break;
            case 27:
                if (numAttempts == 0) {
                    if (numLetters1 > 0) {
                        stringBuilder1.deleteCharAt(stringBuilder1.length() - 1);
                        numLetters1--;
                        showError = false;
                    }
                }
                else if (numAttempts == 1) {
                    if (numLetters2 > 0) {
                        stringBuilder2.deleteCharAt(stringBuilder2.length() - 1);
                        numLetters2--;
                        showError = false;
                    }
                }
                else if (numAttempts == 2) {
                    if (numLetters3 > 0) {
                        stringBuilder3.deleteCharAt(stringBuilder3.length() - 1);
                        numLetters3--;
                        showError = false;
                    }
                }
                else if (numAttempts == 3) {
                    if (numLetters4 > 0) {
                        stringBuilder4.deleteCharAt(stringBuilder4.length() - 1);
                        numLetters4--;
                        showError = false;
                    }
                }
                else if (numAttempts == 4) {
                    if (numLetters5 > 0) {
                        stringBuilder5.deleteCharAt(stringBuilder5.length() - 1);
                        numLetters5--;
                        showError = false;
                    }
                }
                else if (numAttempts == 5) {
                    if (numLetters6 > 0) {
                        stringBuilder6.deleteCharAt(stringBuilder6.length() - 1);
                        numLetters6--;
                        showError = false;
                    }
                }
                break;
        }
    }

    private void restartGame()
    {
        gameOver = false;
        gameWon = false;
        stringBuilder1 = new StringBuilder();
        stringBuilder2 = new StringBuilder();
        stringBuilder3 = new StringBuilder();
        stringBuilder4 = new StringBuilder();
        stringBuilder5 = new StringBuilder();
        stringBuilder6 = new StringBuilder();
        numLetters1 = 0;
        numLetters2 = 0;
        numLetters3 = 0;
        numLetters4 = 0;
        numLetters5 = 0;
        numLetters6 = 0;
        numAttempts = 0;

        for (Box box : board)
        {
            box.color = whiteText;
        }

        for (Box box : keys)
        {
            box.color = lightGray;
        }

        rand = new Random();

        wordIndex = rand.nextInt(numWords)+1;

        try {

            correctWord = getWordFromFile(filePathWords, wordIndex);

        }
        catch (IOException e) {
            e.printStackTrace();
        }

        panel.repaint();
        panel.transferFocusBackward();
    }

    private static String getWordFromFile(String filePath, int lineNumber) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            // Read the specific line corresponding to the random index
            for (int i = 1; i < lineNumber; i++) {
                if (reader.readLine() == null) {
                    throw new IOException("File does not have enough lines");
                }
            }
            return reader.readLine();
        }
    }

    private static boolean checkValidity (String input) {

        switch (input.charAt(0)) {
            case 'A':
                for(int i = indexA; i < indexB; i++) {

                    try {
                        wordToCheck = getWordFromFile(filePathGuesses, i);
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (input.equals(wordToCheck)) {
                        return true;
                    }
                }
                return false;
            case 'B':
                for(int i = indexB; i < indexC; i++) {

                    try {
                        wordToCheck = getWordFromFile(filePathGuesses, i);
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (input.equals(wordToCheck)) {
                        return true;
                    }
                }
                return false;
            case 'C':
                for(int i = indexC; i < indexD; i++) {

                    try {
                        wordToCheck = getWordFromFile(filePathGuesses, i);
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (input.equals(wordToCheck)) {
                        return true;
                    }
                }
                return false;
            case 'D':
                for(int i = indexD; i < indexE; i++) {

                    try {
                        wordToCheck = getWordFromFile(filePathGuesses, i);
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (input.equals(wordToCheck)) {
                        return true;
                    }
                }
                return false;
            case 'E':
                for(int i = indexE; i < indexF; i++) {

                    try {
                        wordToCheck = getWordFromFile(filePathGuesses, i);
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (input.equals(wordToCheck)) {
                        return true;
                    }
                }
                return false;
            case 'F':
                for(int i = indexF; i < indexG; i++) {

                    try {
                        wordToCheck = getWordFromFile(filePathGuesses, i);
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (input.equals(wordToCheck)) {
                        return true;
                    }
                }
                return false;
            case 'G':
                for(int i = indexG; i < indexH; i++) {

                    try {
                        wordToCheck = getWordFromFile(filePathGuesses, i);
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (input.equals(wordToCheck)) {
                        return true;
                    }
                }
                return false;
            case 'H':
                for(int i = indexH; i < indexI; i++) {

                    try {
                        wordToCheck = getWordFromFile(filePathGuesses, i);
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (input.equals(wordToCheck)) {
                        return true;
                    }
                }
                return false;
            case 'I':
                for(int i = indexI; i < indexJ; i++) {

                    try {
                        wordToCheck = getWordFromFile(filePathGuesses, i);
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (input.equals(wordToCheck)) {
                        return true;
                    }
                }
                return false;
            case 'J':
                for(int i = indexJ; i < indexK; i++) {

                    try {
                        wordToCheck = getWordFromFile(filePathGuesses, i);
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (input.equals(wordToCheck)) {
                        return true;
                    }
                }
                return false;
            case 'K':
                for(int i = indexK; i < indexL; i++) {

                    try {
                        wordToCheck = getWordFromFile(filePathGuesses, i);
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (input.equals(wordToCheck)) {
                        return true;
                    }
                }
                return false;
            case 'L':
                for(int i = indexL; i < indexM; i++) {

                    try {
                        wordToCheck = getWordFromFile(filePathGuesses, i);
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (input.equals(wordToCheck)) {
                        return true;
                    }
                }
                return false;
            case 'M':
                for(int i = indexM; i < indexN; i++) {

                    try {
                        wordToCheck = getWordFromFile(filePathGuesses, i);
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (input.equals(wordToCheck)) {
                        return true;
                    }
                }
                return false;
            case 'N':
                for(int i = indexN; i < indexO; i++) {

                    try {
                        wordToCheck = getWordFromFile(filePathGuesses, i);
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (input.equals(wordToCheck)) {
                        return true;
                    }
                }
                return false;
            case 'O':
                for(int i = indexO; i < indexP; i++) {

                    try {
                        wordToCheck = getWordFromFile(filePathGuesses, i);
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (input.equals(wordToCheck)) {
                        return true;
                    }
                }
                return false;
            case 'P':
                for(int i = indexP; i < indexQ; i++) {

                    try {
                        wordToCheck = getWordFromFile(filePathGuesses, i);
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (input.equals(wordToCheck)) {
                        return true;
                    }
                }
                return false;
            case 'Q':
                for(int i = indexQ; i < indexR; i++) {

                    try {
                        wordToCheck = getWordFromFile(filePathGuesses, i);
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (input.equals(wordToCheck)) {
                        return true;
                    }
                }
                return false;
            case 'R':
                for(int i = indexR; i < indexS; i++) {

                    try {
                        wordToCheck = getWordFromFile(filePathGuesses, i);
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (input.equals(wordToCheck)) {
                        return true;
                    }
                }
                return false;
            case 'S':
                for(int i = indexS; i < indexT; i++) {

                    try {
                        wordToCheck = getWordFromFile(filePathGuesses, i);
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (input.equals(wordToCheck)) {
                        return true;
                    }
                }
                return false;
            case 'T':
                for(int i = indexT; i < indexU; i++) {

                    try {
                        wordToCheck = getWordFromFile(filePathGuesses, i);
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (input.equals(wordToCheck)) {
                        return true;
                    }
                }
                return false;
            case 'U':
                for(int i = indexU; i < indexV; i++) {

                    try {
                        wordToCheck = getWordFromFile(filePathGuesses, i);
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (input.equals(wordToCheck)) {
                        return true;
                    }
                }
                return false;
            case 'V':
                for(int i = indexV; i < indexW; i++) {

                    try {
                        wordToCheck = getWordFromFile(filePathGuesses, i);
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (input.equals(wordToCheck)) {
                        return true;
                    }
                }
                return false;
            case 'W':
                for(int i = indexW; i < indexX; i++) {

                    try {
                        wordToCheck = getWordFromFile(filePathGuesses, i);
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (input.equals(wordToCheck)) {
                        return true;
                    }
                }
                return false;
            case 'X':
                for(int i = indexX; i < indexY; i++) {

                    try {
                        wordToCheck = getWordFromFile(filePathGuesses, i);
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (input.equals(wordToCheck)) {
                        return true;
                    }
                }
                return false;
            case 'Y':
                for(int i = indexY; i < indexZ; i++) {

                    try {
                        wordToCheck = getWordFromFile(filePathGuesses, i);
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (input.equals(wordToCheck)) {
                        return true;
                    }
                }
                return false;
            case 'Z':
                for(int i = indexZ; i <= numGuesses; i++) {

                    try {
                        wordToCheck = getWordFromFile(filePathGuesses, i);
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (input.equals(wordToCheck)) {
                        return true;
                    }
                }
                return false;
        }
            return false;
    }

    private static void drawErrorMessage(Graphics g) {
        if(showError) {
            Font numFont = new Font("Helvetica Nueue", Font.BOLD, 19);

            g.setFont(numFont);
            g.setColor(Color.BLACK);
            g.drawString("Invalid Guess", 235, 50);
        }

        panel.repaint();
    }
}
