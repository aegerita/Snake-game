import java.awt.Color;

public class SnakeAI extends SinglePlayer{
    private int app;
    
    public SnakeAI(){
        super();
    }
    
    @Override
    public void move(){
        if (nextPanel() == snake.get(0))
            SnakeGame.lose();
        // if going forward with nothing blocked the way, move snake forward
        if (panels[nextPanel()].getBackground() == Color.black){
            panels[nextPanel()].setBackground(Color.green);
            snake.add(0,nextPanel());
            panels[snake.get(snake.size() - 1)].setBackground(Color.black);
            snake.remove(snake.size() - 1);
        // if eat apple, grow snake, new apple
        } else if (panels[nextPanel()].getBackground() == Color.red){
            panels[nextPanel()].setBackground(Color.green);
            snake.add(0,nextPanel());
            spawnApple();
        // if hit itself, die. 
        } else if (panels[nextPanel()].getBackground() == Color.green){
            if (nextPanel()!=snake.get(0))
                SnakeGame.lose();
        }
        // if hit the edges, it died in nextPanel method
    } 
    
    // get the nest grid index in panel array 
    // if didn't move return the same gird, if at the edge die
    private int nextPanel(){
        int num = snake.get(0);
        double dis = 1000000000;
        int next = num;
        if (num > X-1 && getDis(num-X) <= dis && !snake.contains(num-X)){
            if (!snake.contains(num-2*X) || !snake.contains(num-X-1) || !snake.contains(num-X+1)){
                dis = getDis(num-X);
                next = num - X;
            }
        }
        if (num < X*Y-X && getDis(num+X) <= dis && !snake.contains(num+X)) {
            if (!snake.contains(num+2*X) || !snake.contains(num+X-1) || !snake.contains(num+X+1)){
                dis = getDis(num+X);
                next = num + X;
            }
        } 
        if (num % X != 0 && getDis(num-1) <= dis && !snake.contains(num-1)) {
            if (!snake.contains(num-2) || !snake.contains(num-1+X) || !snake.contains(num-1-X)){
                dis = getDis(num-1);
                next = num -1;
            }
        }
        if ( (num+1) % X != 0 && getDis(num+1) <= dis && !snake.contains(num+1)){
            if (!snake.contains(num+2) || !snake.contains(num+1+X) || !snake.contains(num+1-X)){
                dis = getDis(num+1);
                next = num +1;
            }
        }
        return next;
    }
    
    public void spawnApple(){
        app = snake.get(0);
        while (snake.contains(app))
            app = (int) (Math.random() * X*Y);
        panels[app].setBackground(Color.red);
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
        
        return dis;
    }
    
    
}
