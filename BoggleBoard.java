import java.awt.event.*;
import java.io.FileNotFoundException;
import java.swing.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Random;
import java.util.TreeMap;
import java.util.TreeSet;

public class BoggleBoard extends JFrame implements ActionListener {


	private TreeSet<String> words;
	private final int WIDTH = 275;
	private final int HEIGHT = 400;
	private JTextArea currentWord;
	private char wiw;
	private boolean newWord;
	private JButton[] buttons;
	private boolean[][] selected;
	private boolean[][] lastSelect;
	private int points;
	private ArrayList<String> wordsFound;
	
    public BoggleBoard() throws FileNotFoundException {
    	JPanel content = new JPanel();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	content.setOpaque(true);
        content.setBackground(Color.WHITE);
        content.setLayout(null);
        words = Dictionary.getWords("words.txt");
        
        currentWord = new JTextArea();
        currentWord.setEditable(false);
       
         buttons = new JButton[16];
         int index = 0;
         for(int x=0; x<4; x++) {
        	 for(int y=0; y<4; y++) {
        		 buttons[index] = new JButton("");
        		 buttons[index].setSize(49, 49);
        		 buttons[index].setLocation(30 + x*54, 10 + y*54);
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
         resetAllColors();
        JButton pointCnt = new JButton("Points: "+points);
        pointCnt.setSize(WIDTH/2, 30);
        pointCnt.setLocation(WIDTH/2, 250);
        pointCnt.setBackground(Color.lightGray);
        content.add(pointCnt);
         
         currentWord.setSize(WIDTH/2, 30);
         currentWord.setLocation(0,250);
         currentWord.setBackground(Color.gray);
		JButton submit = new JButton("Submit Word");
		submit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String word = currentWord.getText();
				newWord = true;
				selected = new boolean[6][6];
				lastSelect = new boolean[6][6];
				if(words.contains(word.toLowerCase())) {
					if(!wordsFound.contains(word)) {
						wordsFound.add(word);
						points++;
						pointCnt.setText("Points: "+points);
					}
					
				}
				currentWord.setText("");
				resetAllColors();
			}
		});
		submit.setLocation(0, 300);
		submit.setSize(WIDTH, 30);
		content.add(submit);
		
		
        JButton bottom = new JButton("Start New Game");
        bottom.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
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
        });
        
        bottom.setSize(WIDTH, 30);
        bottom.setLocation(0, HEIGHT-60);

        content.add(bottom, BorderLayout.PAGE_END);
        content.add(currentWord);
        setContentPane(content);
        setSize(WIDTH, HEIGHT);
        setLocationByPlatform(true);
        setVisible(true);

    }
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public void sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		char wiw;
		for(int x=0; x<16; x++) {
			wiw = (char) (65 + ran.nextInt(26));
			buttons[x].setText(""+wiw);
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
