import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * @since 2019/1/10
 * @author Jenny Tai
 */
public class FunctionPanel extends JPanel {
    
    // all the buttons and label
    private JLabel user,user2, score,score2, best,best2;
    private JButton menu;
    
    /**
     * create new function panel 
     * @param fontSize size by the width of JFrame/40
     * @param username name
     * @param highestScore best
     * @param username2
     * @param highestScore2
     */
    public FunctionPanel(int fontSize, String username, int highestScore, String username2, int highestScore2){
        setLayout(new FlowLayout(FlowLayout.CENTER,10,fontSize-10));
        setBackground(Color.black);
        
        if (SnakeGame.getIsSingle())
            space();space();
        
        menu = new JButton("Menu");
        menu.setBackground(Color.black);
        menu.setForeground(Color.white);
        menu.setFont(new Font("Papyrus",1,fontSize*2));
        menu.addActionListener((ActionEvent e) -> {
            SnakeGame.menu();
        });
        menu.setFocusable(false);
        add(menu);
        
        if (SnakeGame.getIsSingle())
            space();
        space();
            
        addLabel(user,"User:",fontSize+6);
            
        user2 = new JLabel(username);
        user2.setBackground(Color.black);
        user2.setForeground(Color.white);
        if (SnakeGame.getIsSingle())
            user2.setFont(new Font("Papyrus",3,fontSize*3/2));
        else {
            user2.setFont(new Font("Papyrus",3,fontSize*4/3));
            user2.setText(username+"-"+username2);
        }
        add(user2);
        
        addLabel(score,"Score:",fontSize+6);
        
        score2 = new JLabel("1");
        score2.setBackground(Color.black);
        score2.setForeground(Color.white);
        if (SnakeGame.getIsSingle())
            score2.setFont(new Font("Papyrus",3,fontSize*2-1));
        else {
            score2.setFont(new Font("Papyrus",3,fontSize*3/2));
            score2.setText("1"+"-"+"1");
        }
        score2.setFocusable(false);
        add(score2);
        
        addLabel(best,"Best:",fontSize+6);
        
        best2 = new JLabel(""+highestScore);
        best2.setBackground(Color.black);
        best2.setForeground(Color.white);
        if (SnakeGame.getIsSingle())
            best2.setFont(new Font("Papyrus",3,fontSize*2-1));
        else {
            best2.setFont(new Font("Papyrus",3,fontSize*3/2));
            best2.setText(""+highestScore+"-"+highestScore2);
        }
        best2.setFocusable(false);
        add(best2);
        
    }
   
    /**
     * change the score label and the best label to the new score
     * @param length the new score passed in
     */
    public void refresh(int length) {
        score2.setText(""+length);
        if (length > Integer.parseInt(best2.getText())){
            best2.setText(""+length);
        }
    }
    
    public void refresh(int length1, int length2){
        score2.setText(""+length1+"-"+length2);
        if (length1 > Integer.parseInt(best2.getText().substring(0,best2.getText().lastIndexOf('-')))){
            best2.setText(""+length1+"-"+best2.getText().substring(best2.getText().lastIndexOf('-')+1));
        }
        if (length2 > Integer.parseInt(best2.getText().substring(best2.getText().lastIndexOf('-')+1))){
            best2.setText(""+length1+"-"+best2.getText().substring(0,best2.getText().lastIndexOf('-')));
        }
    }
    
    private void addLabel(JLabel label, String s, int fontSize){
        label = new JLabel(s);
        label.setForeground(Color.white);
        label.setFont(new Font("Papyrus",1,fontSize));
        add(label);
    }
    
    // change line
    private void space(){
        JLabel jLabel = new JLabel("                                         ");
        add(jLabel);
    }


}