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

class Box { // Box class to hold the letters
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
    private static List<Box> board; // Array for the guess board display
    private static List<Box> keys; // Array for the keyboard display
    private static List<Character> textLetters; // Array for letters
    private static boolean guessValid = false; // Flags
    private static boolean gameOver = false;
    private static boolean gameWon = false;
    private static boolean showError = false;
    private static final Color whiteText = new Color(255,255,255); // Colors for boxes
    private static final Color blackText = new Color(0,0,0);
    private static final Color lightGray = new Color(211, 214, 218);
    private static final Color grayBox = new Color(121,124,126);
    private static final Color yellowBox = new Color(198,180,102);
    private static final Color greenBox = new Color(121,168,107);
    private static final String order = "QWERTYUIOPASDFGHJKLZXCVBNM"; // Order of standard qwerty keyboard
    private static StringBuilder stringBuilder1 = new StringBuilder(); // String builders for each guess
    private static StringBuilder stringBuilder2 = new StringBuilder();
    private static StringBuilder stringBuilder3 = new StringBuilder();
    private static StringBuilder stringBuilder4 = new StringBuilder();
    private static StringBuilder stringBuilder5 = new StringBuilder();
    private static StringBuilder stringBuilder6 = new StringBuilder();
    private static int numAttempts = 0; // Number of attempts
    private static int numLetters1 = 0; // Letters in each attempt
    private static int numLetters2 = 0;
    private static int numLetters3 = 0;
    private static int numLetters4 = 0;
    private static int numLetters5 = 0;
    private static int numLetters6 = 0;
    private static final String filePathWords = "/YOUR_FILE_PATH_HERE/wordList.txt"; // file paths
    private static final String filePathGuesses = "/YOUR_FILE_PATH_HERE/guessList.txt";
    private static String wordToCheck; // Additional strings
    private static String correctWord;
    private static Random rand;
    private static int wordIndex; // Index for word within word bank
    private static final int indexA = 1; // The index where the first word for each letter appears
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
    private static final int numGuesses = 12972; // Number of possible guesses
    private static final int numWords = 2315; // Number of possible words

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

        wordIndex = rand.nextInt(numWords)+1; // Generate a number for the wordIndex

        try {
            correctWord = getWordFromFile(filePathWords, wordIndex); // Set the correct word

        }
        catch (IOException e) {
            e.printStackTrace();
        }

        JFrame frame = new JFrame("Wordle"); // Wordle window

        frame.setSize(600, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        board = new ArrayList<>(); // Create array

        for (int i = 1; i <= 6; i++) // Add the 30 boxes to the array
        {
            for (int j = 1; j <= 5; j++)
            {
                board.add(new Box((57 * j+103), (58 * i+4), 50, 49, whiteText));
            }
        }

        keys = new ArrayList<>(); // Create new array

        textLetters = new ArrayList<>(); // Create new array

        for (char ch : order.toCharArray()) { // Add all characters
            textLetters.add(ch);
        }

        for (int i = 1; i <= 10; i++) { // Add first row of characters to the array
            keys.add(new Box((49 * i+8), (411), 43, 58, lightGray));
        }

        for (int i = 1; i <= 9; i++) { // Add second row of characters to the array
            keys.add(new Box((49 * i+32), (477), 43, 58, lightGray));
        }

        for (int i = 1; i <= 7; i++) { // Add third row of characters to the array
            keys.add(new Box((49 * i+81), (543), 43, 58, lightGray));
        }

        keys.add(new Box(57, 543, 67, 58, lightGray)); // Add delete and enter keys to the array
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

        panel.addMouseListener(new MouseAdapter() // Add functionality for mouse clicks
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                int clickedBox = getIndex(e.getX(), e.getY()); // Determine what letter was clicked

                if (!gameOver && !gameWon) // If the game is not finished
                {
                    if (clickedBox != -1) // And a valid letter was clicked
                    {
                        if (SwingUtilities.isLeftMouseButton(e))
                        {
                                clickChange(clickedBox); // Carry out the mouse click
                        }

                        panel.repaint(); // Update the panel
                    }
                }
            }
        });

        panel.setLayout(null);

        JButton restartButton = new JButton("New Word"); // Add a restart button
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

        frame.addKeyListener(new KeyListener()); // Add functionaliity for keyboard button presses
        frame.setFocusable(true);
        frame.setVisible(true);
    }

    private static void drawBoard(Graphics g) { // Display the board

        for (int i = 0; i < 30; i++) { // For each of the 30 boxes
            g.setColor(board.get(i).color); // Draw the box
            g.fillRect(board.get(i).x, board.get(i).y, board.get(i).width, board.get(i).height);
            g.setColor(lightGray);
            g.drawRect(board.get(i).x, board.get(i).y, board.get(i).width, board.get(i).height); // Draw the outline
            g.drawRect(board.get(i).x-1, board.get(i).y-1, board.get(i).width+2, board.get(i).height+2);
        }

            switch(numAttempts) { // Depending on the number of submitted attempts
                case 0:
                    for (int i = 0; i < 5; i++) {
                        if (i < numLetters1) { // If this box has a letter in it, set the color
                            g.setColor(grayBox);
                        }
                        else { // Otherwise change the color back
                            g.setColor(lightGray);
                        }
                        g.drawRect(board.get(i).x, board.get(i).y, board.get(i).width, board.get(i).height); // Draw the outline again
                        g.drawRect(board.get(i).x-1, board.get(i).y-1, board.get(i).width+2, board.get(i).height+2);
                    }
                case 1:
                    for (int i = 5; i < 10; i++) {
                        if ((i-5) < numLetters2) { // If this box has a letter in it, set the color
                            g.setColor(grayBox);
                        }
                        else { // Otherwise change the color back
                            g.setColor(lightGray);
                        }
                        g.drawRect(board.get(i).x, board.get(i).y, board.get(i).width, board.get(i).height); // Draw the outline again
                        g.drawRect(board.get(i).x-1, board.get(i).y-1, board.get(i).width+2, board.get(i).height+2);
                    }
                    break;
                case 2:
                    for (int i = 10; i < 15; i++) {
                        if ((i-10) < numLetters3) { // If this box has a letter in it, set the color
                            g.setColor(grayBox);
                        }
                        else { // Otherwise change the color back
                            g.setColor(lightGray);
                        }
                        g.drawRect(board.get(i).x, board.get(i).y, board.get(i).width, board.get(i).height); // Draw the outline again
                        g.drawRect(board.get(i).x-1, board.get(i).y-1, board.get(i).width+2, board.get(i).height+2);
                    }
                    break;
                case 3:
                    for (int i = 15; i < 20; i++) {
                        if ((i-15) < numLetters4) { // If this box has a letter in it, set the color
                            g.setColor(grayBox);
                        }
                        else { // Otherwise change the color back
                            g.setColor(lightGray);
                        }
                        g.drawRect(board.get(i).x, board.get(i).y, board.get(i).width, board.get(i).height); // Draw the outline again
                        g.drawRect(board.get(i).x-1, board.get(i).y-1, board.get(i).width+2, board.get(i).height+2);
                    }
                    break;
                case 4:
                    for (int i = 20; i < 25; i++) {
                        if ((i-20) < numLetters5) { // If this box has a letter in it, set the color
                            g.setColor(grayBox);
                        }
                        else { // Otherwise change the color back
                            g.setColor(lightGray);
                        }
                        g.drawRect(board.get(i).x, board.get(i).y, board.get(i).width, board.get(i).height); // Draw the outline again
                        g.drawRect(board.get(i).x-1, board.get(i).y-1, board.get(i).width+2, board.get(i).height+2);
                    }
                    break;
                case 5:
                    for (int i = 25; i < 30; i++) {
                        if ((i-25) < numLetters6) { // If this box has a letter in it, set the color
                            g.setColor(grayBox);
                        }
                        else { // Otherwise change the color back
                            g.setColor(lightGray);
                        }
                        g.drawRect(board.get(i).x, board.get(i).y, board.get(i).width, board.get(i).height); // Draw the outline again
                        g.drawRect(board.get(i).x-1, board.get(i).y-1, board.get(i).width+2, board.get(i).height+2);
                    }
                    break;
                case 6: // Do nothing
                    break;

            }

        panel.repaint(); // Update the panel
    }

    private static void drawKeys(Graphics g) { // Draw all the keys on the keyboard display
        Font numFont1 = new Font("Helvetica Nueue", Font.BOLD, 19);
        Font numFont2 = new Font("Helvetica Nueue", Font.BOLD, 12);

        for (int i = 0; i < 26; i++) { // For each of the 26 letters
            g.setColor(keys.get(i).color); // Draw the box and fill it in
            g.fillRect(keys.get(i).x, keys.get(i).y, keys.get(i).width, keys.get(i).height);
            g.setFont(numFont1);
            g.setColor(blackText);

            if(i == 0 || i == 1 || i == 25) { // Draw the corresponding letter inside the box
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

        for (int i = 26; i < 28; i++) { // Draw the corresponding letter inside the box
            g.setColor(keys.get(i).color);
            g.fillRect(keys.get(i).x, keys.get(i).y, keys.get(i).width, keys.get(i).height);
            g.setFont(numFont2);
            g.setColor(blackText);

            if(i == 26) { // Add the ENTER and DELETE strings
                g.drawString("ENTER", keys.get(i).x + keys.get(i).width / 4-1, keys.get(i).y + 2 * keys.get(i).height / 3 - 5);
            }
            else {
                g.drawString("DELETE", keys.get(i).x + keys.get(i).width / 6, keys.get(i).y + 2 * keys.get(i).height / 3 - 5);
            }
        }

        panel.repaint(); // Update the panel
    }

    private static void drawAttempts(Graphics g) { // Draw the attempts on the board
        Font numFont1 = new Font("Helvetica Nueue", Font.BOLD, 19);

        g.setFont(numFont1);

        for (int i = 0; i < numLetters1; i++) { // For each letter in this attempt
            if(numAttempts <= 0) { // If this attempt has been submitted, color the text black
                g.setColor(blackText);
            }
            else {
                g.setColor(whiteText);
            }
            g.drawString(String.valueOf((stringBuilder1.charAt(i))), board.get(i).x + board.get(i).width / 3 + 2, board.get(i).y + 2 * board.get(i).height / 3);
        }
        for (int i = 0; i < numLetters2; i++) { // For each letter in this attempt
            if(numAttempts <= 1) { // If this attempt has been submitted, color the text black
                g.setColor(blackText);
            }
            else {
                g.setColor(whiteText);
            }
            g.drawString(String.valueOf((stringBuilder2.charAt(i))), board.get(i).x+board.get(i).width/3+2, board.get(i).y+5*board.get(i).height/3+9);
        }
        for (int i = 0; i < numLetters3; i++) { // For each letter in this attempt
            if(numAttempts <= 2) { // If this attempt has been submitted, color the text black
                g.setColor(blackText);
            }
            else {
                g.setColor(whiteText);
            }
            g.drawString(String.valueOf((stringBuilder3.charAt(i))), board.get(i).x+board.get(i).width/3+2, board.get(i).y+8*board.get(i).height/3+18);
        }
        for (int i = 0; i < numLetters4; i++) { // For each letter in this attempt
            if(numAttempts <= 3) { // If this attempt has been submitted, color the text black
                g.setColor(blackText);
            }
            else {
                g.setColor(whiteText);
            }
            g.drawString(String.valueOf((stringBuilder4.charAt(i))), board.get(i).x+board.get(i).width/3+2, board.get(i).y+11*board.get(i).height/3+27);
        }
        for (int i = 0; i < numLetters5; i++) { // For each letter in this attempt
            if(numAttempts <= 4) { // If this attempt has been submitted, color the text black
                g.setColor(blackText);
            }
            else {
                g.setColor(whiteText);
            }
            g.drawString(String.valueOf((stringBuilder5.charAt(i))), board.get(i).x+board.get(i).width/3+2, board.get(i).y+14*board.get(i).height/3+36);
        }
        for (int i = 0; i < numLetters6; i++) { // For each letter in this attempt
            if(numAttempts <= 5) { // If this attempt has been submitted, color the text black
                g.setColor(blackText);
            }
            else {
                g.setColor(whiteText);
            }
            g.drawString(String.valueOf((stringBuilder6.charAt(i))), board.get(i).x+board.get(i).width/3+2, board.get(i).y+17*board.get(i).height/3+45);
        }

        panel.repaint();
    }

    private static class KeyListener extends KeyAdapter // Keyboard functionality
    {
        @Override
        public void keyPressed(KeyEvent e)
        {
            char input = Character.toUpperCase(e.getKeyChar()); // set input to uppercase
            char newCh;

            if (!gameOver && !gameWon) { // If the game is not finished
                switch (input) { // Check the letter
                    case ('A'): // For the letter A
                        newCh = 'A';
                        if (numAttempts == 0) { // For the first attempt
                            if (numLetters1 < 5) { // If there is room for another letter
                                stringBuilder1.append(newCh); // Add the letter to the string
                                numLetters1++; // Update the counter
                            }
                        } else if (numAttempts == 1) { // For the second attempt
                            if (numLetters2 < 5) {
                                stringBuilder2.append(newCh);
                                numLetters2++;
                            }
                        } else if (numAttempts == 2) { // For the third attempt
                            if (numLetters3 < 5) {
                                stringBuilder3.append(newCh);
                                numLetters3++;
                            }
                        } else if (numAttempts == 3) { // For the fourth attempt
                            if (numLetters4 < 5) {
                                stringBuilder4.append(newCh);
                                numLetters4++;
                            }
                        } else if (numAttempts == 4) { // For the fifth attempt
                            if (numLetters5 < 5) {
                                stringBuilder5.append(newCh);
                                numLetters5++;
                            }
                        } else if (numAttempts == 5) { // For the sixth attempt
                            if (numLetters6 < 5) {
                                stringBuilder6.append(newCh);
                                numLetters6++;
                            }
                        }
                        break;
                    case ('B'): // For the letter B
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
                    case (KeyEvent.VK_BACK_SPACE): // If the Delete button was pressed (ON MAC)
                        if (numAttempts == 0) { // For the first attempt
                            if (numLetters1 > 0) { // If there are letters to be deleted
                                stringBuilder1.deleteCharAt(stringBuilder1.length() - 1); // Delete the letter
                                numLetters1--; // Update the counter and flag
                                showError = false;
                            }
                        } else if (numAttempts == 1) { // For the second attempt
                            if (numLetters2 > 0) {
                                stringBuilder2.deleteCharAt(stringBuilder2.length() - 1);
                                numLetters2--;
                                showError = false;
                            }
                        } else if (numAttempts == 2) { // For the third attempt
                            if (numLetters3 > 0) {
                                stringBuilder3.deleteCharAt(stringBuilder3.length() - 1);
                                numLetters3--;
                                showError = false;
                            }
                        } else if (numAttempts == 3) { // For the fourth attempt
                            if (numLetters4 > 0) {
                                stringBuilder4.deleteCharAt(stringBuilder4.length() - 1);
                                numLetters4--;
                                showError = false;
                            }
                        } else if (numAttempts == 4) { // For the fifth attempt
                            if (numLetters5 > 0) {
                                stringBuilder5.deleteCharAt(stringBuilder5.length() - 1);
                                numLetters5--;
                                showError = false;
                            }
                        } else if (numAttempts == 5) { // For the sixth attempt
                            if (numLetters6 > 0) {
                                stringBuilder6.deleteCharAt(stringBuilder6.length() - 1);
                                numLetters6--;
                                showError = false;
                            }
                        }
                        break;
                    case (KeyEvent.VK_ENTER): // If the Enter button was pressed (ON MAC)
                        if (numAttempts == 0) { // For the first attempt
                            if (numLetters1 == 5) { // If the attempt has 5 letters
                                guessValid = checkValidity(stringBuilder1.toString()); // Check the attempt
                                if(guessValid) { // If the attempt is valid
                                    checkAttempt(stringBuilder1.toString()); // Check the attempt
                                    numAttempts++; // Update the counter and flag
                                    guessValid = false;
                                }
                                else { // If the attempt was not valid, update the flag
                                    showError = true;
                                }
                            }
                        } else if (numAttempts == 1) { // For the second attempt
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
                        } else if (numAttempts == 2) { // For the third attempt
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
                        } else if (numAttempts == 3) { // For the fourth attempt
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
                        } else if (numAttempts == 4) { // For the fifth attempt
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
                        } else if (numAttempts == 5) { // For the sixth attempt
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
                        } else if (numAttempts == 6) { // Do nothing
                        }
                        break;
                }
            }
        }
    }

    public static void checkAttempt(String inputWord) { // Logic for checking the attempt

        int correctLetters = 0; // Initialize variables
        int[] freqChar = {0,0,0,0,0};
        boolean[] charAddressed = {false, false, false, false, false}; // No characters addressed yet

        for (int i=0; i<5; i++) { // Determine the frequency of each character in the correct word
            for (int j=0; j<5; j++) {
                if (correctWord.charAt(i) == correctWord.charAt(j)) {
                    freqChar[i]++;
                }
            }
        }

        for (int i=0; i<5; i++) { // For each letter position
            if(inputWord.charAt(i) == correctWord.charAt(i)) { // If the guessed letter is correct
                board.get(5*numAttempts+i).color = greenBox; // Color the box green
                keys.get(order.indexOf(inputWord.charAt(i))).color = greenBox; // Color the key box green
                charAddressed[i] = true; // Update the array
                correctLetters++; // Increment the correct letter counter

                if(correctLetters == 5) { // Game is won if we have 5 correct letters
                    gameWon = true;
                }

                for (int j=0; j<5; j++) { // For each additional occurence of the same letter
                    if (correctWord.charAt(i) == correctWord.charAt(j)) {
                        freqChar[j]--; // Decrement the frequency counter for that position
                    }
                }
            }
        }

        if (numAttempts == 5) { // If the number of attempts already entered is 5
            if (correctLetters < 5) { // Game is over if the word is not a full match
                gameOver = true;
            }
        }

        for (int i=0; i<5; i++) { // For each position in the attempt
            if(!charAddressed[i]) { // If the character is not addressed
                board.get(5*numAttempts+i).color = grayBox; // Color it gray
                if (keys.get(order.indexOf(inputWord.charAt(i))).color == lightGray) { // If the key box is light gray
                    keys.get(order.indexOf(inputWord.charAt(i))).color = grayBox; // Color it gray
                }
                for (int j=0; j<5; j++) { // For each position in the correct word
                    if (inputWord.charAt(i) == correctWord.charAt(j)) { // If the guessed letter matches the correct letter
                        if(freqChar[j] != 0) { // If the frequency in the correct word's position is >0
                            if (i != j) { // If the two positions are different
                                if(!charAddressed[i]) { // If the guessed letter is not addressed
                                    board.get(5 * numAttempts + i).color = yellowBox; // Color that box yellow
                                    if (keys.get(order.indexOf(inputWord.charAt(i))).color != greenBox) { // If the key box is not green
                                        keys.get(order.indexOf(inputWord.charAt(i))).color = yellowBox; // Color it yellow
                                    }
                                    charAddressed[i] = true; // Update the flag

                                    for (int k = 0; k < 5; k++) { // For each position in the correct word
                                        if (inputWord.charAt(i) == correctWord.charAt(k)) { // If there is a match
                                            freqChar[k]--; // Decrement the frequency of that position
                                            if(freqChar[k] == 0) { // If the fequency in that position is 0
                                                for (int l = 0; l < 5; l++) { // For each position in the guessed word
                                                    if (correctWord.charAt(k) == inputWord.charAt(l)) { // If there is a match
                                                        if (!charAddressed[l]) { // If the character has not been addressed
                                                            board.get(5*numAttempts+l).color = grayBox; // Color the box gray
                                                                if (keys.get(order.indexOf(inputWord.charAt(i))).color == lightGray) { // If the key box is light gray
                                                                    keys.get(order.indexOf(inputWord.charAt(i))).color = grayBox; // Color it gray
                                                                }
                                                            charAddressed[l] = true; // Update the flag
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

        panel.repaint(); // Update the panel
    }

    private static void drawGameOver(Graphics g) {
        if(gameOver) { // If the game was lost, display a messge
            Font numFont = new Font("Helvetica Nueue", Font.BOLD, 19);

            g.setFont(numFont);
            g.setColor(Color.BLACK);
            g.drawString("Game Over! The word was: " + correctWord, 130, 50);
        }
    }

    private static void drawGameWon(Graphics g)
    {
        if(gameWon) // If the game was won, display a message
        {
            Font numFont = new Font("Helvetica Nueue", Font.BOLD, 19);

            g.setFont(numFont);
            g.setColor(Color.BLACK);
            g.drawString("CONGRATULATIONS! You solved Wordle in " + (numAttempts) + " tries!", 52, 50);
        }
    }

    private int getIndex(int mouseX, int mouseY)
    {
        for (int i = 0; i < 28; i++) // Determine if the position of the mouse click coincides with a keyboard box
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

        switch (i) { // Depending on the box clicked
            case 0: // For the first box
                newCh = 'Q'; // Set the new character to Q
                if(numAttempts == 0) { // For the first attempt
                    if(numLetters1 < 5) { // If there is room for more letters
                        stringBuilder1.append(newCh); // Add the letter to the string builder
                        numLetters1++; // Increment the counter
                    }
                }
                else if(numAttempts == 1) { // For the second attempt
                    if(numLetters2 < 5) {
                        stringBuilder2.append(newCh);
                        numLetters2++;
                    }
                }
                else if(numAttempts == 2) { // For the third attempt
                    if(numLetters3 < 5) {
                        stringBuilder3.append(newCh);
                        numLetters3++;
                    }
                }
                else if(numAttempts == 3) { // For the fourth attempt
                    if(numLetters4 < 5) {
                        stringBuilder4.append(newCh);
                        numLetters4++;
                    }
                }
                else if(numAttempts == 4) { // For the fifth attempt
                    if(numLetters5 < 5) {
                        stringBuilder5.append(newCh);
                        numLetters5++;
                    }
                }
                else if(numAttempts == 5) { // For the sixth attempt
                    if(numLetters6 < 5) {
                        stringBuilder6.append(newCh);
                        numLetters6++;
                    }
                }
                break;
            case 1: // For the second box
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
            case 26: // If the ENTER button was clicked
                if(numAttempts == 0) { // For the first attempt
                    if(numLetters1 == 5) { // If there are 5 letters entered
                        guessValid = checkValidity(stringBuilder1.toString()); // Check the guess
                        if(guessValid) { // If the guess was valid
                            checkAttempt(stringBuilder1.toString()); // Check the attempt
                            numAttempts++; // Update the counter and flag
                            guessValid = false;
                        }
                        else { // If the guess was invalid
                            showError = true; // Update the flag
                        }
                    }
                }
                else if(numAttempts == 1) { // For the second attempt
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
                else if(numAttempts == 2) { // For the third attempt
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
                else if(numAttempts == 3) { // For the fourth attempt
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
                else if(numAttempts == 4) { // For the fifth attempt
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
                else if(numAttempts == 5) { // For the sixth attempt
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
                else if(numAttempts == 6) { // Do nothing
                }
                break;
            case 27: // If the DELETE button was clicked
                if (numAttempts == 0) { // For the first attempt
                    if (numLetters1 > 0) { // If there are letters in the current attempt
                        stringBuilder1.deleteCharAt(stringBuilder1.length() - 1); // Remove the letter from the string builder
                        numLetters1--; // Update counter and flag
                        showError = false;
                    }
                }
                else if (numAttempts == 1) { // For the second attempt
                    if (numLetters2 > 0) {
                        stringBuilder2.deleteCharAt(stringBuilder2.length() - 1);
                        numLetters2--;
                        showError = false;
                    }
                }
                else if (numAttempts == 2) { // For the third attempt
                    if (numLetters3 > 0) {
                        stringBuilder3.deleteCharAt(stringBuilder3.length() - 1);
                        numLetters3--;
                        showError = false;
                    }
                }
                else if (numAttempts == 3) { // For the fourth attempt
                    if (numLetters4 > 0) {
                        stringBuilder4.deleteCharAt(stringBuilder4.length() - 1);
                        numLetters4--;
                        showError = false;
                    }
                }
                else if (numAttempts == 4) { // For the fifth attempt
                    if (numLetters5 > 0) {
                        stringBuilder5.deleteCharAt(stringBuilder5.length() - 1);
                        numLetters5--;
                        showError = false;
                    }
                }
                else if (numAttempts == 5) { // For the sixth attempt
                    if (numLetters6 > 0) {
                        stringBuilder6.deleteCharAt(stringBuilder6.length() - 1);
                        numLetters6--;
                        showError = false;
                    }
                }
                break;
        }
    }

    private void restartGame() // Logic for restarting the game
    {
        gameOver = false; // Reset all variables
        gameWon = false;
        stringBuilder1 = new StringBuilder(); // Create 6 new string builders
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

        for (Box box : board) // Reset all boxes to default colors
        {
            box.color = whiteText;
        }

        for (Box box : keys)
        {
            box.color = lightGray;
        }

        rand = new Random();

        wordIndex = rand.nextInt(numWords)+1; // Get a random new word

        try {

            correctWord = getWordFromFile(filePathWords, wordIndex);

        }
        catch (IOException e) {
            e.printStackTrace();
        }

        panel.repaint(); // Update the panel and send back focus
        panel.transferFocusBackward();
    }

    private static String getWordFromFile(String filePath, int index) throws IOException { // Logic for retrieving the word from the file
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

            for (int i = 1; i < index; i++) { // Read the line corresponding to the random index
                if (reader.readLine() == null) {
                    throw new IOException("File does not have enough lines");
                }
            }
            return reader.readLine(); // Return the word
        }
    }

    private static boolean checkValidity (String input) { // Checking if the guess is a valid guess

        switch (input.charAt(0)) { // Check the first letter of the guessed word
            case 'A': // If the first character is A
                for(int i = indexA; i < indexB; i++) { // For all indices of words starting with A

                    try {
                        wordToCheck = getWordFromFile(filePathGuesses, i); // Retrieve the word from the word bank
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (input.equals(wordToCheck)) { // If there is a match, the guess is valid
                        return true;
                    }
                }
                return false; // Otherwise the guess is invalid
            case 'B': // If the first character is B
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
        if(showError) { // If the current guess was invalid, display a message
            Font numFont = new Font("Helvetica Nueue", Font.BOLD, 19);

            g.setFont(numFont);
            g.setColor(Color.BLACK);
            g.drawString("Invalid Guess", 235, 50);
        }

        panel.repaint(); // Update the panel
    }
}
