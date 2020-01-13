import java.awt.Color;
import java.util.ArrayList;

/**
 *
 * @author Administrator
 */
public class AIvsPlayer extends SinglePlayer{
        // 2 apple, 2 snakes arrarlist, both die when meet
    public final ArrayList<Integer> AI;
    private int app;
    
    public AIvsPlayer() {
        super();
        snake = new ArrayList<>();
        panels[X*(Y+1)/2].setBackground(Color.black);
        panels[X*(Y+1)/2+X/6].setBackground(Color.green);
        snake.add(X*(Y+1)/2+X/6);
        AI = new ArrayList<>();
        panels[X*(Y+1)/2-X/6].setBackground(Color.yellow);
        AI.add(X*(Y+1)/2-X/6);
    }
    
    public void move(){
        int next = nextPanel(snake,direction);
        int next2 = nextPanelAI();
        
        // if length difference reach 10, judge and end the game
        if (snake.size()-9>AI.size())
            SnakeGame.lose2();
        else if (snake.size()+9<AI.size())
            SnakeGame.lose("You are too slow!");
        
        // if game didn't start by pressing key, do nothing
        else if (next == -1){} 
        
        else if (next == next2){
            SnakeGame.bothDie();
            
        } else {
            super.move();
            if (next2 == snake.get(0))
                SnakeGame.lose2();  
            if (panels[next2].getBackground() == Color.black){
                panels[AI.get(AI.size() - 1)].setBackground(Color.black);
                AI.remove(AI.size() - 1);
                panels[next2].setBackground(Color.yellow);
                AI.add(0,next2);
            // if eat apple, grow snake, new apple
            } else if (panels[next2].getBackground() == Color.red){
                panels[next2].setBackground(Color.yellow);
                AI.add(0,next2);
                spawnApple();
            // if hit itself, die. 
            } else if (panels[next2].getBackground() == Color.green ||panels[next].getBackground() == Color.yellow){
                SnakeGame.lose2();
            }
        }
    } 
    
    @Override
    public void spawnApple(){
        app = snake.get(0);
        while (panels[app].getBackground() != Color.BLACK)
            app = (int) (Math.random() * X*Y);
        panels[app].setBackground(Color.red);
    }
    
    /**
     *
     * @return
     */
    public int getScore2(){
        return AI.size();
    }
    
    // get the nest grid index in panel array 
    // if didn't move return the same gird, if at the edge die
    private int nextPanelAI(){
        int num = AI.get(0);
        double dis = 1000000000;
        int next = num;
        if (num > X-1 && getDis(num-X) <= dis && !AI.contains(num-X) && !snake.contains(num-X) && num-X!=nextPanel(snake,direction)){
            if (!AI.contains(num-2*X) || !AI.contains(num-X-1) || !AI.contains(num-X+1)){
                dis = getDis(num-X);
                next = num - X;
            }
        }
        if (num < X*Y-X && getDis(num+X) <= dis && !AI.contains(num+X) && !snake.contains(num+X) && num+X!=nextPanel(snake,direction)) {
            if (!AI.contains(num+2*X) || !AI.contains(num+X-1) || !AI.contains(num+X+1)){
                dis = getDis(num+X);
                next = num + X;
            }
        } 
        if (num % X != 0 && getDis(num-1) <= dis && !AI.contains(num-1) && !snake.contains(num-1) && num-1!=nextPanel(snake,direction)) {
            if (!AI.contains(num-2) || !AI.contains(num-1+X) || !AI.contains(num-1-X)){
                dis = getDis(num-1);
                next = num -1;
            }
        }
        if ( (num+1) % X != 0 && getDis(num+1) <= dis && !AI.contains(num+1) && !snake.contains(num+1) && num+1!=nextPanel(snake,direction)){
            if (!AI.contains(num+2) || !AI.contains(num+1+X) || !AI.contains(num+1-X)){
                dis = getDis(num+1);
                next = num +1;
            }
        }
        if (next == num) return AI.get(0);
        return next;
    }
    
    private double getDis(int headIndex){
        int dis = 0;
        if (headIndex/X - app/X >= 0){
            dis += headIndex/X - app/X;
        } else {
            dis -= headIndex/X - app/X;
        }
        if (headIndex%X - app%X >= 0){
            dis += headIndex%X - app%X;
        } else {
            dis -= headIndex%X - app%X;
        }
        dis*=8;
        
        if (headIndex/X - snake.get(snake.size()-1)/X >= 0){
            dis += headIndex/X - snake.get(snake.size()-1)/X;
        } else {
            dis -= headIndex/X - snake.get(snake.size()-1)/X;
        }
        if (headIndex%X - snake.get(snake.size()-1)%X >= 0){
            dis += headIndex%X - snake.get(snake.size()-1)%X;
        } else {
            dis -= headIndex%X - snake.get(snake.size()-1)%X;
        }
        
        dis*=2;
        if (headIndex/X - AI.get(AI.size()-1)/X >= 0){
            dis += headIndex/X - AI.get(AI.size()-1)/X;
        } else {
            dis -= headIndex/X - AI.get(AI.size()-1)/X;
        }
        if (headIndex%X - AI.get(AI.size()-1)%X >= 0){
            dis += headIndex%X - AI.get(AI.size()-1)%X;
        } else {
            dis -= headIndex%X - AI.get(AI.size()-1)%X;
        }
        
        if (headIndex/X - AI.get(0)/X >= 0){
            dis += headIndex/X - AI.get(0)/X;
        } else {
            dis -= headIndex/X - AI.get(0)/X;
        }
        if (headIndex%X - AI.get(0)%X >= 0){
            dis += headIndex%X - AI.get(0)%X;
        } else {
            dis -= headIndex%X - AI.get(0)%X;
        }
        
        /* 
        if (snake.size()>50){
            
            dis*=2;
            for (int i = snake.size()/6;i<snake.size()/2;i++){
                if (headIndex/X - snake.get(i)/X >= 0){
                    dis -= headIndex/X - snake.get(i)/X;
                } else {
                    dis += headIndex/X - snake.get(i)/X;
                }
                if (headIndex%X - snake.get(i)%X >= 0){
                    dis -= headIndex%X - snake.get(i)%X;
                } else {
                    dis += headIndex%X - snake.get(i)%X;
                }
            }
            dis*=2;
            for (int i = snake.size()/3*2;i<snake.size()-1;i++){
                if (headIndex/X - snake.get(i)/X >= 0){
                    dis += headIndex/X - snake.get(i)/X;
                } else {
                    dis -= headIndex/X - snake.get(i)/X;
                }
                if (headIndex%X - snake.get(i)%X >= 0){
                    dis += headIndex%X - snake.get(i)%X;
                } else {
                    dis -= headIndex%X - snake.get(i)%X;
                }
            }
        
        }
        */
        return dis;
    }
}
