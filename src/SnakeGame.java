import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.*;
import javax.swing.*;

/**
 * @since 2019/1/9
 * @author Jenny Tai
 */
public class SnakeGame  {
    // all static, so all the method can be called from other class
    private static JFrame myFrame;
    private static SinglePlayer game;
    private static StartPanel start;
    private static FunctionPanel functions;
    private static JLayeredPane layerPane;
    private static UserFrameIO userFrame;
    private static JFrame menu;
    // the timer and its action to move the snake
    private static Timer timer;
    private static ActionListener timerAction;
    // default user account, rank is the index line in IO sorted by best score
    private static String user = "N/A", user2 = "N/A";
    private static int best = 0, rank = -1, best2 = 0, rank2 = -1, user1Win,user2Win;
    // boolean to tell the method what to do 
    //  after user interface (start panel or back to game)
    private static boolean menuSwitch = false, isSingle = true;

    
    /**
     * constructor
     */
    public SnakeGame(){
        // make the frame
        myFrame = new JFrame("Snake Game GUI");
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myFrame.setBackground(Color.green);
        myFrame.setResizable(false);
        // call start panel
        Home();
        // make the time event
        timerAction = (ActionEvent actionEvent) -> {
            game.move();
            if (isSingle)
                functions.refresh(game.getScore());
            else if (game instanceof DoublePlayer){
                functions.refresh(((DoublePlayer)game).getScore2(),game.getScore());
            } else {
                functions.refresh(((AIvsPlayer)game).getScore2(),game.getScore());
            }
        };
        timer = new Timer(100, timerAction );
        userFrame = new UserFrameIO(false);
        user1Win = 0;
        user2Win = 0;
    }

    /**
     * main method that only calls for the snake game
     * @param args
     */
    public static void main(String[] args) {
        new SnakeGame();
    }
    
    /**
     * set up the game panel 
     * @param width the width of the JFrame
     * @param single
     */
    public static void startGame(double width, boolean single){
        game = new SinglePlayer();
        game.setVisible(false);
        if (single){
            if ("Yandan".equals(user)){
                if (JOptionPane.showConfirmDialog(menu,
                        "You are VIP. \nAre you sure you want to enter easy mode?",
                        "Cheat",
                        JOptionPane.WARNING_MESSAGE) == 0){
                    timer = new Timer(300, timerAction);
                }
            }
            if ("AI".equals(user)){
                game = new SnakeAI();
                timer = new Timer(10, timerAction);
            }
            
        } else {
            if ("AI".equals(user)||"AI".equals(user2)){
                game = new AIvsPlayer();
                timer = new Timer(200, timerAction);
            } else
                game = new DoublePlayer();
        }
        
        // set up function panel, layerpane and myframe by the width
        reSize(width);
        // add key listener only in the game
        myFrame.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode()==KeyEvent.VK_ESCAPE)
                    menu();
                else
                    game.setDirection(e);
            }
            @Override
            public void keyReleased(KeyEvent e) {}
            @Override
            public void keyTyped(KeyEvent e) {}
        });
        game.setVisible(true);
        timer.start();
    } 
        
    /**
     * If the snake game end, output result and gives options
     */
    public static void lose(String information) {
        timer.stop();
        
        if (game instanceof DoublePlayer){
            user2Win++;
            doubleLose("Snake2 "+user+" died");
            
        } else {
            // make the output string
            information += "Your final score is " + game.getScore() + "! ";

            // if not signed in, add information
            if ("N/A".equals(user)){
                information += "\nSince you do not have an account"
                        + ", your score will not be stored";

            // if make a new record, record it by the userframe object and ouput
            } else if (game.getScore()>best){
                best = game.getScore();
                userFrame.newBest(rank,game.getScore());
                information += "\nCongradulation! You made a new record!";
            }
            // gives option dialog
            String[] options = {"Play Again","Home"};
            switch (JOptionPane.showOptionDialog(myFrame,
                    information +"\nWould you like to replay or return home?",
                    "The Snake Dies", 
                    0,JOptionPane.QUESTION_MESSAGE,
                    null,options,null) ){
                case 0:
                    // replay with same width
                    SnakeGame.startGame(myFrame.getWidth(), isSingle);
                    break;
                case 1:
                    // start panel
                    Home();
                    break;
                default:
                    // exit the game if choose close x
                    System.exit(0);
            }
        }
    }
    public static void lose() {
        lose("");
    }
    
    public static void lose2() {
        timer.stop();
        user1Win++;
        if (game instanceof DoublePlayer){
            doubleLose("Snake1 "+user2+" died");
        } else {
            lose("You beat the AI! Awesome!\n");
        }
    }
    
    public static void bothDie() {
        timer.stop();
        if (game instanceof DoublePlayer){
            doubleLose("You die at the same time");
        } else {
            lose("You beat the AI at the cost of your life.\n");
        }
    }

    /**
     * When user choose menu in FunctionPanel, make a JFrame with buttons
     * stop the game and provide functions
     */
    public static void menu() {
        timer.stop();
        menuSwitch = true;
        
        // make the JFrame with flow layout and buttons
        menu = new JFrame("Menu");
        menu.setLayout(new FlowLayout(FlowLayout.CENTER,10,10));
        JButton home, window, account, cancel, restart;
        home = new JButton("Home Page");
        restart = new JButton("Restart");
        window = new JButton("Set Window");
        account = new JButton("Switch User");
        cancel = new JButton("Return");
        // go home with warning, record best score, if cancel do nothing
        home.addActionListener((ActionEvent e) -> {
            if (JOptionPane.showConfirmDialog(menu,
                    "Your game will not be stored"
                            + "\nAre your sure you want to return home?",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE) == 0){
                if (game.getScore()>best && !"N/A".equals(user)){
                    best = game.getScore();
                    userFrame.newBest(rank,game.getScore());
                }
                menu.setVisible(false);
                Home();
            }
        });
        // new game with warning, record best score, if cancel do nothing
        restart.addActionListener((ActionEvent e) -> {
            if (JOptionPane.showConfirmDialog(menu,
                    "Your game will not be stored"
                            + "\nAre your sure you want to restart?",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE) == 0){
                if (game.getScore()>best && !"N/A".equals(user)){
                    best = game.getScore();
                    userFrame.newBest(rank,game.getScore());
                }
                menu.setVisible(false);
                SnakeGame.startGame(myFrame.getWidth(),isSingle);
            }
        });
        // ask and resize, unpause the game, if cancel do nothing
        window.addActionListener((ActionEvent e) -> {
            int width = SnakeGame.askSize();
            if (width != 0){
                reSize(width);
                menu.setVisible(false);
                timer.start();
            }
        });
        // call user frame, after setUser menuswitch tell the method to unpause the game
        account.addActionListener((ActionEvent e) -> {
            account(false);
        });
        // unpause when cancel menu
        cancel.addActionListener((ActionEvent e) -> {
            menu.setVisible(false);
            timer.start();
        });
        // unpause when close menu frame, learned from this example 
        // https://kodejava.org/how-do-i-handle-a-window-closing-event/
        menu.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                timer.start();
            }
        });
        // set up menu jframe
        menu.add(home);
        menu.add(restart);
        menu.add(window);
        menu.add(account);
        menu.add(cancel);
        menu.setSize(100, 240);
        menu.setLocationRelativeTo(myFrame);
        menu.setVisible(true);
    }

    /**
     * ask user for the new width by JOptionPane and JSlider
     * @return integer of the width the user chose, 0 if canceled
     */
    public static int askSize(){
        // partial codes from stack overflow, Arcy's answer on May 7, 2017
        JSlider slider = new JSlider(400,1600);
        slider.setMajorTickSpacing(200);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setValue(800);
        int dialogResponse = JOptionPane.showOptionDialog
                (myFrame, 
                 slider,
                 "Choose Window Width (4*3)",
                 javax.swing.JOptionPane.OK_CANCEL_OPTION,
                 javax.swing.JOptionPane.QUESTION_MESSAGE,
                 null, null, null
                );
        if (javax.swing.JOptionPane.OK_OPTION == dialogResponse)
            return slider.getValue();
        else 
            return 0;
    }
    
    /**
     * when the user frame are called from menu or start panel
     * @param isFromStart
     */
    public static void account(boolean isFromStart) {
        userFrame = new UserFrameIO(isFromStart);
        userFrame.setLocationRelativeTo(myFrame);
        userFrame.setVisible(true);
    } 
    
    /**
     * when user frame has account to be signed in, set all user variables
     * depend on menuSwitch which record whether to go back to the game
     * the game would not be initialized if was in start panel
     * @param username user name
     * @param bestScore the best score
     * @param line the index in IO array list sorted by best score
     */
    public static void setUser(String username, int bestScore, int line){
        if (isSingle || "N/A".equals(user) && (!"AI".equals(username)&&!isSingle)) {
            user = username;
            best = bestScore;
            rank = line;
        } else {
            user2 = username;
            best2 = bestScore;
            rank2 = line;
        }
        userFrame.setVisible(false);
        start.login(user,rank);
        if (menuSwitch){
            reSize(myFrame.getWidth());
            menu.setVisible(false);
            timer.start();
        }
    }
    
    // resize every thing
    private static void reSize(double width) {
        myFrame.setVisible(false);
        myFrame.setSize((int) width, (int)(width/4*3));
        myFrame.setLocationRelativeTo(null);
        game.setBounds(1, 1, (int) Math.round((width/4*3-30)/26*30), (int) Math.round(width/4*3-30));
        functions = new FunctionPanel((int) (width/40),user2,best2,user,best);
        functions.setBounds((int) (game.getWidth()+2), 1,
                (int) (width-game.getWidth()-9), (int) game.getWidth());
        layerPane = new JLayeredPane();
        layerPane.add(game,2);
        layerPane.add(functions,1);
        myFrame.setContentPane(layerPane);
        myFrame.setVisible(true);
    }
    
    // set up new start panel
    private static void Home() {
        myFrame.setVisible(false);
        start = new StartPanel(user,rank);
        myFrame.setContentPane(start);
        myFrame.setSize(700, 500);
        myFrame.setVisible(true);
        myFrame.setLocationRelativeTo(null);
    }

    private static void doubleLose(String information){
        information += "\nThe final length is "+ ((DoublePlayer)game).getScore2()+" : "
                + game.getScore() + "! ";
        
        if (game.getScore()>best && ((DoublePlayer)game).getScore2() >best2){
            best = game.getScore();
            best2 = ((DoublePlayer)game).getScore2();
            if (!"N/A".equals(user))
                userFrame.newBest(rank,game.getScore());
            if (!"N/A".equals(user2))
                userFrame.newBest(rank2,best2);
            information += "\nCongradulation! You both made new records!";
        } else if (game.getScore()>best){
            best = game.getScore();
            if (!"N/A".equals(user)){
                userFrame.newBest(rank,game.getScore());
                information += "\nCongradulation! "+user +" made a new record!";
            }
        } else if ( ((DoublePlayer)game).getScore2() >best2){
            best2 = ((DoublePlayer)game).getScore2();
            if (!"N/A".equals(user2)){
                userFrame.newBest(rank2,best2);
                information += "\nCongradulation! "+user2 +" made a new record!";
            }
        }
        
        information += "\nThe green " + user + " won " + user1Win
                + " times, while the yellow " + user2 + " won " + user2Win +" times";
        
        if ("N/A".equals(user)||"N/A".equals(user2)){
            information += "\nSince some user do not have an account"
                    + ", their score will not be stored";
        }
        
        String[] options = {"Play Again","Home"};
        switch (JOptionPane.showOptionDialog(myFrame,
                information +"\nWould you like to replay or return home?",
                "The Snake Dies",
                0,JOptionPane.QUESTION_MESSAGE,
                null,options,null) ){
            case 0:
                // replay with same width
                SnakeGame.startGame(myFrame.getWidth(), isSingle);
                break;
            case 1:
                // start panel
                Home();
                break;
            default:
                // exit the game if choose close x
                System.exit(0);
        }
    }
    
    public static void setIsSingle(boolean isSingle) {
        SnakeGame.isSingle = isSingle;
    }
    public static boolean getIsSingle() {
        return isSingle;
    }
}