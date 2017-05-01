import java.awt.event.*;
import java.io.FileNotFoundException;
import java.swing.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.border.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.List;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Random;
import javax.swing.Timer;
import java.util.TreeMap;
import java.util.TreeSet;

public class BoggleBoard extends JFrame implements ActionListener {


	private TreeSet<String> words;
	private final int WIDTH = 275;
	private final int HEIGHT = 400;
	private static JTextArea currentWord;
	private boolean newWord;
	private RoundButton[] buttons;
	private boolean[][] selected;
	private boolean[][] lastSelect;
	private int points;
	private ArrayList<String> wordsFound;
	private JTextArea pointCnt;
	private JPanel content;
	private Timer time;
	private int curTime;
	private JTextArea clock;
	private char[] charFrequency;
	
    public BoggleBoard() throws FileNotFoundException {
    	addFrequencies();
    	content = new JPanel();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	content.setOpaque(true);
        content.setBackground(Color.WHITE);
        content.setLayout(null);
        
        words = Dictionary.getWords("words.txt");
        
        currentWord = new JTextArea();
        currentWord.setEditable(false);
        clock = new JTextArea();
        clock.setSize(30,30);
        clock.setLocation(WIDTH/2-15, 5);
        clock.setBackground(Color.yellow);
        clock.setEditable(false);
       
        makeCharacterButtons();
        
        pointCnt = new JTextArea("Points: "+points);
        pointCnt.setSize(WIDTH/2, 30);
        pointCnt.setLocation(WIDTH/2, 265);
        pointCnt.setBackground(Color.lightGray);
        content.add(pointCnt);
         
         currentWord.setSize(WIDTH/2, 30);
         currentWord.setLocation(0,265);
         currentWord.setBackground(Color.gray);
         String htmlButton = "<html><p style=\"text-align: center;\"><span style=\"color:#f0fff0;\"><span style=\"font-family: verdana,geneva,sans-serif;\"><code><span style=\"background-color: rgb(255, 215, 0);\">Submit Word </span></code></span></span></p>";
		JButton submit = new JButton(htmlButton);
		submit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(curTime>0)
				resetWord();
			}
		});
		
		
		
		submit.setLocation(0, 300);
		submit.setSize(WIDTH, 30);
		content.add(submit);
        JButton bottom = new JButton("Start New Game");
        bottom.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		setUpNewGame();
        	}
        });
        
        bottom.setSize(WIDTH, 30);
        bottom.setLocation(0, HEIGHT-60);
        
        content.add(bottom, BorderLayout.PAGE_END);
        content.add(currentWord);
        content.add(clock);
        setContentPane(content);
        setSize(WIDTH, HEIGHT);
        setLocationByPlatform(true);
        setVisible(true);
        time = new Timer(1000, this);

        setUpNewGame();
    }
    
    public void addFrequencies() {
    	charFrequency = new char[]{'e', 'e', 'e', 'e', 'e', 'e', 'e', 'e', 'e', 'e',
    							   'e', 'e', 'e', 'e', 'e', 'e', 'e', 'e', 'e', 't',
    							   't', 't', 't', 't', 't', 't', 't', 't', 't', 't',
    							   't', 't', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a',
    							   'a', 'a', 'a', 'a', 'r', 'r', 'r', 'r', 'r', 'r',
    							   'r', 'r', 'r', 'r', 'r', 'r', 'i', 'i', 'i', 'i', 
    							   'i', 'i', 'i', 'i', 'i', 'i', 'i', 'n', 'n', 'n', 
    							   'n', 'n', 'n', 'n', 'n', 'n', 'n', 'n', 'o', 'o', 
    							   'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 's', 
    							   's', 's', 's', 's', 's', 's', 's', 's', 'd', 'd', 
    							   'd', 'd', 'd', 'd', 'c', 'c', 'c', 'c', 'c', 'h', 
    							   'h', 'h', 'h', 'h', 'l', 'l', 'l', 'l', 'l', 'f', 
    							   'f', 'f', 'f', 'm', 'm', 'm', 'm', 'p', 'p', 'p', 
    							   'p', 'u', 'u', 'u', 'u', 'g', 'g', 'g', 'y', 'y', 
    							   'y', 'w', 'w', 'b', 'j', 'k', 'q', 'v', 'x', 'z' }; 
    	}
    
    
    public void makeCharacterButtons() {
    	  buttons = new RoundButton[16];
          int index = 0;
          for(int x=0; x<4; x++) {
         	 for(int y=0; y<4; y++) {
         		 buttons[index] = new RoundButton("");
         		 buttons[index].setSize(49, 49);
         		 buttons[index].setLocation(30 + x*54, 36 + y*54);
         		 content.add(buttons[index]);
         		 JButton current = buttons[index];
         		 int xpos = x;
         		 int ypos = y;
         		 current.addActionListener(new ActionListener() {
         			 public void actionPerformed(ActionEvent e) {
         				 if(canSelect(xpos+1, ypos+1)) {
         					currentWord.append(current.getText());
         				 	current.setBackground(Color.red);
         				 }
         			 }
         		 });
         		 index++;
         	 }
          }
    }
    
    public void setUpNewGame() {
    	time.start();
    	curTime = 60;
    	wordsFound = new ArrayList<String>();
		newButtons();
		points = 0;
		newWord = true;
        selected = new boolean[6][6];
        lastSelect = new boolean[6][6];
		resetAllColors();
		currentWord.setText("");
		pointCnt.setText("Points: " + points);
    }
	@Override
	public void actionPerformed(ActionEvent arg0) {
		curTime--;
		clock.setText(""+curTime);
		if(curTime==0) {
			time.stop();
			endGame();
		}
		
	}
	
	public void endGame() {
		JPanel gameover = new JPanel();
		gameover.setOpaque(true);
        gameover.setBackground(Color.WHITE);
        gameover.setLayout(null);
		JTextArea block = new JTextArea();
		block.setSize(HEIGHT, WIDTH/3);
		block.setLocation(0,0);
		block.setText("Your score is " + pointCnt.getText() + "\n would you like to start a new game?");
		block.setEditable(false);
		block.setBackground(Color.yellow);
		JButton yes = new JButton("yes");
		yes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				content.setVisible(true);
				gameover.setVisible(false);
				setContentPane(content);
				setUpNewGame();
				return;
			}
		});
		JButton no = new JButton("no");
		no.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		yes.setSize(WIDTH/4,30);
		no.setSize(WIDTH/4, 30);
		yes.setLocation(50,HEIGHT/2);
		no.setLocation(150,HEIGHT/2);
		setContentPane(gameover);
		gameover.add(block);
		gameover.add(yes);
		gameover.add(no);
		gameover.setVisible(true);
	}
	
	public void resetWord() {
		String word = currentWord.getText();
		newWord = true;
		selected = new boolean[6][6];
		lastSelect = new boolean[6][6];
		if(words.contains(word.toLowerCase())) {
			if(!wordsFound.contains(word)) {
				wordsFound.add(word);
				adjustPoints(word.toLowerCase());
				pointCnt.setText("Points: "+points);
			}
		}
		currentWord.setText("");
		resetAllColors();
	}
	
	public void adjustPoints(String word) {
		ArrayList<Character> rareChar = new ArrayList<Character>(Arrays.asList('j', 'x', 'q', 'z', 'k', 'v'));
		for(int x=0; x<word.length(); x++) {
			char cur = word.charAt(x);
			if(rareChar.contains(cur)) {
				points+=2;
			} else {
				points+=1;
			}
		}
	}
	public void resetAllColors() {
		for(int x=0; x<16; x++) {
			buttons[x].setBackground(Color.white);;
		}
	}
	private final int[][] pos = { {1,1}, {0,1}, {1,0}, {-1,-1}, {-1,1}, {1,-1}, {0,-1}, {-1,0} };
	
	public boolean canSelect(int xpos, int ypos) {
		if(selected[xpos][ypos]) {
			return false;
		}
		if(newWord) {
			newWord = false;
			selected[xpos][ypos] = true;
			lastSelect[xpos][ypos] = true;

			return true;
		}
		boolean adjSel = false;
		for(int x=0; x<pos.length; x++) {
			int newxpos = pos[x][0] + xpos;
			int newypos = pos[x][1] + ypos;
			if(lastSelect[newxpos][newypos]) {
				lastSelect[newxpos][newypos] = false;
				adjSel = true;
				selected[xpos][ypos] = true;
				lastSelect[xpos][ypos] = true;
			}
		}
		return adjSel;
	}
	
	public void newButtons() {
		Random ran = new Random();
		for(int x=0; x<16; x++) {
			int newCharIndex = ran.nextInt(charFrequency.length);
			String newChar = ""+charFrequency[newCharIndex];
			buttons[x].setText(""+newChar.toUpperCase());
		}
	}
	

 
 
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
					new BoggleBoard();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });
    }
}
