/**
 * record user information in one object to be in one array list
 * @since 2019/1/22
 * @author Jenny Tai
 */
public class User implements Comparable{
    private String name;
    private String password;
    private int best;

    public User(String name, String password, int best) {
        this.name = name;
        this.password = password;
        this.best = best;
    }

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}

    public int getBest() {return best;}
    public void setBest(int best) {this.best = best;}

    // store this in IO
    @Override
    public String toString() {
        return name+" "+password+" "+best;
    }
    
    // compare by score, more score comes first
    @Override
    public int compareTo(Object o) {
        return ((User)o).getBest() - best;
    }
    
}
