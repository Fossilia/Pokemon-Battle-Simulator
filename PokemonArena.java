import java.io.*;
import java.util.*;

public class PokemonArena {

    private static ArrayList<Pokemon> allpkm = new ArrayList<Pokemon>();
    private static ArrayList<Pokemon> party = new ArrayList<Pokemon>();
    private static ArrayList<Pokemon> opponents = new ArrayList<Pokemon>();
    private static Pokemon activePkm;
    private static Pokemon opponent;
    private static Scanner sc = new Scanner(System.in);
    private static  Random rand = new Random();
    private static int numberOfPokemon;
 
/*---------------------------------------loading pokemon---------------------------------------------------
 *method used to get each line of pokemon.txt and add a pokemon class to a arraylist that takes a line from 
*/
    public void load() {

        try{
            Scanner inFile = new Scanner(new BufferedReader(new FileReader("pokemon.txt")));
            numberOfPokemon = inFile.nextInt();
            inFile.nextLine();

            for(int i = 0;i<numberOfPokemon;i++){
                allpkm.add(new Pokemon(inFile.nextLine()));   
            }
        }

        catch(IOException ex){
            System.out.println("Did you missplace pokemon.txt?");
        }
    }

/*---------------------------------------listing pokemon---------------------------------------------------
 *method used to get each line of pokemon.txt and add a pokemon class to a arraylist that takes a line from 
*/    
    public void listpkm(ArrayList<Pokemon> pkm, int length, int srt, int skip){
        int spaceCounter = 0;

        for (int i=0;i<length;i++){
            spaceCounter+=1;
            System.out.printf("   %5d. %12s",i+srt ,pkm.get(i).getName());
            
            if (spaceCounter == skip){
                spaceCounter = 0;
                System.out.print("\n");
            }
        }
        System.out.print("\n");
    }

    /*---------------------------------------input validator---------------------------------------------------
 *method used to get each line of pokemon.txt and add a pokemon class to a arraylist that takes a line from 
*/      
    public int validator(int lowset, int highest){
        boolean check = false;

        int input = sc.nextInt();

        while(check == false){
            if(input<highest+1 && input>lowset-1){
                check = true;
                return input;
            }
            else{
                System.out.println("Pick a valid number!");
                input = sc.nextInt();
            }
        }
        return input;
    }

/*---------------------------------------main menu display---------------------------------------------------
 *method used to get each line of pokemon.txt and add a pokemon class to a arraylist that takes a line from 
*/      
    public void displayMenu(){
        System.out.printf("\n[%-15sVS%15s]\n",activePkm.getName()+"]","["+opponent.getName());
        System.out.printf("HP: %-11s%19s\n",activePkm.getHp(),"HP: "+opponent.getHp());
        System.out.printf("NRG: %-10s%19s\n\n",activePkm.getEnergy(),"NRG: "+opponent.getEnergy());
        System.out.println("            1. ATTACK");
        System.out.println("            2. RETREAT");
        System.out.println("            3. PASS");
    }
    
/*---------------------------------------switching Pokemon---------------------------------------------------
 *method used to get each line of pokemon.txt and add a pokemon class to a arraylist that takes a line from 
*/  
    public boolean switchPkm(boolean backOption){
        listpkm(party,party.size(),1,2);
        
        if(backOption){
            System.out.printf("   %5d. %12s\n",party.size()+1,"Back");
            int input = validator(1,5);
            if(input == 5){
                return false;
            }
            else{
                activePkm = party.get(input-1);
                System.out.println(activePkm.getName()+", I choose you!\n");
                return true;
            }
        }
        else{
            int input = validator(1,4);
            activePkm = party.get(input-1);
            System.out.println(activePkm.getName()+", I choose you!\n");
            return true;
        }
    }
    
/*---------------------------------------player choosing their Pokemon---------------------------------------------------
 *method used to get each line of pokemon.txt and add a pokemon class to a arraylist that takes a line from 
*/  
    public void setup(){

        boolean check = false;

        System.out.println("Pick 4 Pokemon that you want to be in your party!\nPick a Pokemon by inputting the number next to the Pokemon you want to pick\nYou cant pick a Pokemon more than once!\n");
        listpkm(allpkm, 28, 1, 4);

        for (int i=0;i<4;i++){
            int input = validator(0,28);
            check = false;

            while(check == false){
                if(!party.contains(allpkm.get(input-1))){
                    check = true;
                }

                else{
                    check = false;
                    System.out.println("Input a valid number that you have not inputted before!");
                    input = validator(0,28);
                }
            }

            party.add(allpkm.get(input-1));
            System.out.println("You chose "+allpkm.get(input-1).getName()+"!");
        }
        
        System.out.print("\n");

        System.out.println("The rest of the Pokemon you didn't choose are your opponents!");

        for(int i=0;i<28;i++){ //adding oppenent pokemon
            if(!party.contains(allpkm.get(i))){
                opponents.add(allpkm.get(i));
            }
        }
    }
    
/*---------------------------------------battle (main part of the game)---------------------------------------------------
 *method used to get each line of pokemon.txt and add a pokemon class to a arraylist that takes a line from 
*/      
    public void battle(){
        System.out.println("Choose your active Pokemon!\n");
        switchPkm(false);
        Collections.shuffle(opponents);
        opponent = opponents.get(0);
        System.out.println("your first opponent is "+opponent.getName()+"! Good luck!\n");
        
        while(opponents.size()>0 && party.size()>0){ 
            
            while(opponent.getHp()>0 && activePkm.getHp()>0){ //battle loop
                displayMenu();
                int input = validator(1,3);
                
                if(!activePkm.isStunned()){
                    
                    if(input==1){ //Attack
                        activePkm.displayMoves();
                        input = validator(1,activePkm.getNumOfMoves()+1);
                        
                        if(input!=activePkm.getNumOfMoves()+1){
                            while(input!=3 && !activePkm.costChecker(input-1)){
                                activePkm.displayMoves();
                                input = validator(1,activePkm.getNumOfMoves()+1);
                            }  
                            int rng = rand.nextInt(2) + 1;
                            
                            if(rng == 1){
                                activePkm.attack(input-1, opponent);
                                if(opponent.getHp()>0){
                                    opponent.attack(activePkm);
                                }
                            }
                            
                            else{
                                opponent.attack(activePkm);
                                if(activePkm.getHp()>0){
                                    activePkm.attack(input-1, opponent);
                                }
                            }
                        }
                    }
                    
                    else if(input==2){ //Retreat
                        if(switchPkm(true)){
                            opponent.attack(activePkm);
                        }
                    }
                    
                    else if(input==3){ //Pass
                        System.out.println(activePkm.getName()+" did nothing!");
                        opponent.attack(activePkm);
                    }
                }
                else{
                    System.out.println(activePkm.getName()+" could not do anything because it was stunned!");
                    opponent.attack(activePkm);
                }
                
                for(int i=0;i<party.size();i++){ //recover 10 energy
                    party.get(i).setEnergy(party.get(i).getEnergy()+10);
                }
                for(int i=0;i<opponents.size();i++){ //recover 10 energy
                    opponents.get(i).setEnergy(opponents.get(i).getEnergy()+10);
                }
                //System.out.println("All your pokemon restored some energy!\n");
                
            }
            
            if(activePkm.getHp()<=0){ //If your active Pokemon gets knocked out
                System.out.println(activePkm.getName()+" fainted!");
                int activePkmIndex = party.indexOf(activePkm);
                party.remove(activePkmIndex);
                
                if(party.size()>0){
                    System.out.println("Choose one of your remaining Pokemon to continue!\n");
                    switchPkm(false);
                }
                else{
                    System.out.println("You have no more Pokemon that are able to battle! You lost!");
                    break;
                }
            }
            
            if(opponent.getHp()<=0){ //If the opponent gets knocked out
                System.out.println("opponent "+opponent.getName()+" fainted!");
                opponents.remove(0);
                if(opponents.size()>0){
                    opponent = opponents.get(0);
                    System.out.println("your next opponent is "+opponent.getName()+"! There are "+opponents.size()+" Pokemon left to defeat!");
                }
                else{
                    System.out.println("You defeated all the enemy Pokemon! Congrats you won the game!");
                    break;
                }
            }
            
            if(opponent.getHp()>0 && activePkm.getHp()>0){
                for(int i=0;i<party.size();i++){ //recover 20 health
                    party.get(i).setHp(party.get(i).getHp()+20);
                }
                System.out.println("All your Pokemon restored 20 health!");
            }     
        }  
    }
    
/*---------------------------------------main method-------------------------------------------------------
 *method used to get each line of pokemon.txt and add a pokemon class to a arraylist that takes a line from 
*/  
    public static void main (String [] args) {
        PokemonArena arena = new PokemonArena();
        
        arena.load();
        arena.setup();
        arena.battle();
        
    }
}