import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 *
 * @author Administrator
 */
public class DoublePlayer extends SinglePlayer{
    // 2 apple, 2 snakes arrarlist, both die when meet
    public final ArrayList<Integer> snake2;
    public int direction2 = -1;

    public DoublePlayer() {
        super();
        snake = new ArrayList<>();
        panels[X*(Y+1)/2].setBackground(Color.black);
        panels[X*(Y+1)/2+X/6].setBackground(Color.green);
        snake.add(X*(Y+1)/2+X/6);
        snake2 = new ArrayList<>();
        panels[X*(Y+1)/2-X/6].setBackground(Color.yellow);
        snake2.add(X*(Y+1)/2-X/6);
    }
    
    @Override
    public void setDirection(KeyEvent e) {
        super.setDirection(e);
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W: 
                if (snake2.size()==1)
                    direction2 = UP; 
                else if (snake2.get(0) - X != snake2.get(1))
                    direction2 = UP; 
                break;
            case KeyEvent.VK_S: 
                if (snake2.size()==1)
                    direction2 = DOWN; 
                else if (snake2.get(0) + X != snake2.get(1))
                    direction2 = DOWN; 
                break;
            case KeyEvent.VK_A: 
                if (snake2.size()==1)
                    direction2 = LEFT; 
                else if (snake2.get(0) - 1 != snake2.get(1))
                    direction2 = LEFT; 
                break;
            case KeyEvent.VK_D: 
                if (snake2.size()==1)
                    direction2 = RIGHT; 
                else if (snake2.get(0) + 1 != snake2.get(1))
                    direction2 = RIGHT; 
                break;
        }
    }  
    
    public void move(){
        int next = nextPanel(snake,direction);
        int next2 = nextPanel(snake2,direction2);
        
        // if length difference reach 10, judge and end the game
        if (snake.size()-9>snake2.size())
            SnakeGame.lose2();
        else if (snake.size()+9<snake2.size())
            SnakeGame.lose();
        
        // if game didn't start by pressing key, do nothing
        else if (next == -1 || next2 == -1){} 
        
        else if ( (next == -2 && next2 == -2) || (next == next2)){
            SnakeGame.bothDie();
            
        } else {
            super.move();
                
            if (next2 == -2){
                SnakeGame.lose2();
            // if going forward with nothing blocked the way, move snake forward
            } else if (panels[next2].getBackground() == Color.black){

                panels[snake2.get(snake2.size() - 1)].setBackground(Color.black);
                snake2.remove(snake2.size() - 1);
                panels[next2].setBackground(Color.yellow);
                snake2.add(0,next2);

            // if eat apple, grow snake, new apple
            } else if (panels[next2].getBackground() == Color.red){

                panels[next2].setBackground(Color.yellow);
                snake2.add(0,next2);
                spawnApple();

            // if hit itself, die. 
            } else if (panels[next2].getBackground() == Color.green ||panels[next].getBackground() == Color.yellow){
                SnakeGame.lose2();
            }
        }
    } 
    
    @Override
    public void spawnApple(){
        int app = snake.get(0);
        while (panels[app].getBackground() != Color.BLACK)
            app = (int) (Math.random() * X*Y);
        panels[app].setBackground(Color.red);
    }
    
    /**
     *
     * @return
     */
    public int getScore2(){
        return snake2.size();
    }
}
