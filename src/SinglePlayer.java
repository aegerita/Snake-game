import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

/**
 * @since 2019/1/8
 * @author Jenny Tai
 */
public class SinglePlayer extends JPanel{
    // panels array for grids the snake might be
    public JPanel[] panels;
    // int Array List for index in panel arrays that are snake bodypart
    public ArrayList<Integer> snake;
    // int that record direction
    public int direction= -1;
    // easier to set direction. x is the num of column, y is the num of row
    public final int UP = 0, DOWN = 1, RIGHT=2, LEFT=3, X=30,Y=26;
    
    /**
     * constructor, make grid layout with panels, set up game
     */
    public SinglePlayer(){
        setLayout(new GridLayout(Y,X,2,2));
        setBackground(Color.black);
        
        panels = new JPanel[X*Y];
        for (int i = 0; i < X;i++){ 
            for (int j=0; j<Y; j++){ 
                panels[i*Y+j] = new JPanel();
                panels[i*Y+j].setBackground(Color.black);
                add(panels[i*Y+j]);
            }
        }
        
        snake = new ArrayList<>();
        panels[X*(Y+1)/2].setBackground(Color.green);
        snake.add(X*(Y+1)/2);
        spawnApple();
    }
    
    /**
     * reset snake direction according to the JFrame keyboard listener
     * only if it doesn't go the opposite way (can not judge based on present direction
     * since it can be changed in one move. 
     * @param e the key being pressed
     */
    public void setDirection(KeyEvent e) {
        switch (e.getKeyCode()) {
            //whether should prevent turning around
            case KeyEvent.VK_UP: 
                if (snake.size()==1)
                    direction = UP; 
                else if (snake.get(0) - X != snake.get(1))
                    direction = UP; 
                break;
            case KeyEvent.VK_DOWN: 
                if (snake.size()==1)
                    direction = DOWN; 
                else if (snake.get(0) + X != snake.get(1))
                    direction = DOWN; 
                break;
            case KeyEvent.VK_LEFT: 
                if (snake.size()==1)
                    direction = LEFT; 
                else if (snake.get(0) - 1 != snake.get(1))
                    direction = LEFT; 
                break;
            case KeyEvent.VK_RIGHT: 
                if (snake.size()==1)
                    direction = RIGHT; 
                else if (snake.get(0) + 1 != snake.get(1))
                    direction = RIGHT; 
                break;
        }
    }  

    /**
     * judge the result of moving forward
     */
    public void move(){
        // if game didn't start by pressing key, do nothing
        int next = nextPanel(snake,direction);
        if (next == -2){
            SnakeGame.lose();
            
        } else if (next ==-1){
            
        // if going forward with nothing blocked the way, move snake forward
        } else if (panels[next].getBackground() == Color.black){
            
            panels[next].setBackground(Color.green);
            snake.add(0,next);
            panels[snake.get(snake.size() - 1)].setBackground(Color.black);
            snake.remove(snake.size() - 1);
            
        // if eat apple, grow snake, new apple
        } else if (panels[next].getBackground() == Color.red){
            
            panels[next].setBackground(Color.green);
            snake.add(0,next);
            spawnApple();
            
        // if hit itself, die. 
        } else {
            SnakeGame.lose();
        }
        // if hit the edges, it died in nextPanel method
    } 
    
    // get the nest grid index in panel array 
    // if didn't move return the same gird, if at the edge die

    /**
     *
     * @param array
     * @param direction
     * @return
     */
    public int nextPanel(ArrayList<Integer> array, int direction){
        int num = array.get(0);
        switch (direction){
            case UP: 
                if (num > X-1)
                    return num - X;
                break;
            case DOWN: 
                if (num < X*Y-X) 
                    return num + X;
                break;
            case LEFT:
                if (num % X != 0) 
                    return num -1;
                break;
            case RIGHT:
                if ( (num+1) % X != 0)
                    return num +1;
                break;
            default:
                return -1;
        }
        return -2;
    }

    // spawn random apple outside of snake body
    public void spawnApple(){
        int app = snake.get(0);
        while (snake.contains(app))
            app = (int) (Math.random() * X*Y);
        panels[app].setBackground(Color.red);
    }
    
    // the length of the snake is the score of the game
    public int getScore() {
        return snake.size();
    }

}