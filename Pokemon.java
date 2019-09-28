import java.util.*;

public class Pokemon {
    
    private String name;
    private String type;
    private String resistence;
    private String weakness;
    private int hp;
    private int maxHp;
    private int energy;
    private int disableValue = 0;
    private boolean stunned = false;
    private Random rand = new Random();
    
    private ArrayList<Move> moves = new ArrayList<Move>();
    
    
    public Pokemon(String st) {
        String [] stats = st.split(",");
        name = stats[0];
        hp = Integer.parseInt(stats[1]);
        maxHp = Integer.parseInt(stats[1]);
        type = stats[2];
        resistence = stats[3];
        weakness = stats[4];
        energy = 50;
        
        int numA = Integer.parseInt(stats[5]);
        for(int i=0; i< numA; i++){
            String aName = stats[6+i*4];
            int aCost = Integer.parseInt(stats[7+i*4]);
            int aDam = Integer.parseInt(stats[8+i*4]);
            String aSpec = stats[9+i*4];
            
            moves.add(new Move(aName, aCost, aDam, aSpec));
        }
        
    }
    
    public void attack(int move, Pokemon defending){
        double multipler = 1;
        int chance = 1;
        
        System.out.println(name+" used "+moves.get(move).getName()+"!");
        
        if(moves.get(move).getSpecial().equals("wild card") || moves.get(move).getSpecial().equals("wild storm")){
            chance = rand.nextInt(2) + 1;
        }
        
        if(chance==1){
            if(moves.get(move).getDamage()>0){
                if(type.equals(defending.getWeakness())){
                    System.out.println("It was super effective!");
                    multipler = 2;
                }
                
                else if(type.equals(defending.getResistence())){
                    System.out.println("It was not very effective...");
                    multipler = 0.5;
                }
            }
            
            // damage
            int dmg = (int)(moves.get(move).getDamage()*multipler) - disableValue;
            chance = rand.nextInt(2) + 1;
            setEnergy(energy-moves.get(move).getCost());
            
            if(moves.get(move).getDamage()>0){
                defending.setHp(defending.getHp()-dmg);
                System.out.println(name+" Dealt "+dmg+" Damage!");
            }
            
            //wild storm
            int stormCounter = 1;
            while(chance == 1 && moves.get(move).getSpecial().equals("wild storm") && defending.getHp()>0){
                System.out.println(name+" attacked again!");
                defending.setHp(defending.getHp()-dmg);
                System.out.println(name+" Dealt "+dmg+" Damage!");
                chance = rand.nextInt(2) + 1;
                stormCounter+=1;
                
                if(chance == 2){
                    System.out.println(name+" attacked "+stormCounter+" times!");
                }
            }  
        }
        
        else{
            setEnergy(energy-moves.get(move).getCost());
            System.out.println(name+" missed!");
        }
        
        //disable
        if(moves.get(move).getSpecial().equals("disable") && defending.getDisableValue()==0){
            defending.setDisableValue(10);
            System.out.println(defending.getName()+" was disabled!");
        }
        
        //stun
        if(moves.get(move).getSpecial().equals("stun")){
            chance = rand.nextInt(2) + 1;
            
            if(chance == 1){
                defending.setStunValue(true);
                System.out.println(defending.getName()+" was stunned!");
            }
        }
        
        //recharge
        if(moves.get(move).getSpecial().equals("recharge")){
            setEnergy(energy+20);
            System.out.println(name+" restored 20 energy!");
        }
        
    }
    
    
    public void attack(Pokemon defending){
        double multipler = 1;
        int chance = 1;
        
        ArrayList<Move> viableMoves = new ArrayList<Move>();
        
        for(int i=0;i<moves.size();i++){
            if(moves.get(i).getCost()<=energy){
                viableMoves.add(moves.get(i));
            }
        }
        
        if(viableMoves.size()>0){
            if(stunned == false){
                
                Collections.shuffle(viableMoves);
                System.out.println("opponent "+name+" used "+viableMoves.get(0).getName()+"!");
                
                if(viableMoves.get(0).getSpecial().equals("wild card") || viableMoves.get(0).getSpecial().equals("wild storm")){
                    chance = rand.nextInt(2) + 1;
                }
                if(chance==1){
                    if(viableMoves.get(0).getDamage()>0){
                        if(type.equals(defending.getWeakness())){
                            System.out.println("It was super effective!");
                            multipler = 2;
                        }
                        
                        else if(type.equals(defending.getResistence())){
                            System.out.println("It was not very effective...");
                            multipler = 0.5;
                        }
                    }
                    
                    //damage
                    int dmg = (int)(viableMoves.get(0).getDamage()*multipler) - disableValue;
                    setEnergy(energy-getMove(0).getCost());
                    
                    if(viableMoves.get(0).getDamage()>0){
                        defending.setHp(defending.getHp()-dmg);
                        System.out.println("opponent "+name+" Dealt "+dmg+" Damage!");
                    }
                    
                    //wild storm
                    int stormCounter = 1;
                    while(chance == 1 && viableMoves.get(0).getSpecial().equals("wild storm") && defending.getHp()>0){
                        System.out.println(name+" attacked again!");
                        defending.setHp(defending.getHp()-dmg);
                        System.out.println(name+" Dealt "+dmg+" Damage!");
                        chance = rand.nextInt(2) + 1;
                        stormCounter+=1;
                        
                        if(chance == 2){
                            System.out.println("opponent "+name+" attacked "+stormCounter+" times!");
                        }
                    }
                    
                }
                
                else{
                    setEnergy(energy-getMove(0).getCost());
                    System.out.println("opponent "+name+" missed!");
                }
                
                //recharge
                if(viableMoves.get(0).getSpecial().equals("recharge")){
                    setEnergy(energy+20);
                    System.out.println("opponent "+name+" restored 20 energy!");
                }
                //stun
                if(viableMoves.get(0).getSpecial().equals("stun")){
                    chance = rand.nextInt(2) + 1;
                    if(chance == 1){
                        defending.setStunValue(true);
                        System.out.println(defending.getName()+" was stunned!");
                    }
                }
                
                //disable
                if(viableMoves.get(0).getSpecial().equals("disable") && defending.getDisableValue()==0){
                    defending.setDisableValue(10);
                    System.out.println(defending.getName()+" was disabled!");
                }
            }
            
            else{
                System.out.println("opponent "+name+" could not attack because it was stunned!");
                stunned = false;
            }
        }
        else{
            System.out.println("opponent "+name+" Didn't have enough energy to attack!");
        }
        
        
    }
    
    
    public boolean costChecker(int input){
        
        if(energy>=moves.get(input).getCost()){
            return true;
        }
        
        else{
            System.out.println("You don't have enough energy to use that move!");
            return false;
        }
    }
    
    //returning methods
    
    public String getName(){
        return name;
    }
    
    public String getType(){
        return type;
    }
    
    public String getResistence(){
        return resistence;
    }
    
    public String getWeakness(){
        return weakness;
    }
    
    public int getHp(){
        return hp;
    }
    
    public int getMaxHp(){
        return maxHp;
    }
    
    public int getEnergy(){
        return energy;
    }
    
    public int getDisableValue(){
        return disableValue;
    }
    
    public boolean isStunned(){
        return stunned;
    }
    
    //setting methods
    
    public void setHp(int h){
        hp = h;
        if(hp-maxHp>0){
            hp = maxHp;
        }
    }
    
    public void setEnergy(int e){
        energy = e;
        if(energy-50>0){
            energy = 50;
        }
    }
    
    public void setDisableValue(int d){
        disableValue = d;
    }
    
    public void setStunValue(boolean s){
        stunned = s;
    }
    
    public void displayMoves(){
        System.out.println("Moves:");
        for(int i=0; i<moves.size();i++){
            System.out.printf("%3d. %-15s  Cost:%2d  Damage:%2d  Special: %-8s\n",i+1,"[ "+moves.get(i).getName()+" ]",moves.get(i).getCost(), moves.get(i).getDamage(), moves.get(i).getSpecial());
        }
        System.out.println("  "+(moves.size()+1)+". Back");
    }
    
    public Move getMove(int move){
        return moves.get(move);
    }
    
    public int getNumOfMoves(){
        return moves.size();
    }
    
    class Move{
        private String name;
        private int cost;
        private int damage;
        private String special;
        
        public Move(String n, int c, int d, String s){
            name = n;
            cost = c;
            damage = d;
            special = s;
        }
        
        public String getName(){
            return name;
        }
        
        public int getCost(){
            return cost;
        }
        
        public int getDamage(){
            return damage;
        }
        
        public String getSpecial(){
            return special;
        }
        
    }
    
}

