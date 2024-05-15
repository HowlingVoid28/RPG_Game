package GAMESTAGE;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JOptionPane;

import CHARACTER.*;
import CHARACTER.Character;
import ITEM.*;
import MAP.*;


public class GameStage implements Serializable{
    Player player;
    Inventory invent;
    Map map;
    int stagenum = 1;
    boolean status = false;
    private static Scanner input = new Scanner(System.in);

    //------- constructor ---------//
    public GameStage(){
        this.player = new Player(null);
        this.map = new Map("src\\InputFile\\map"+ stagenum + ".txt");
        this.invent = new Inventory(5);
        this.status = false;
    }

    //------- map-stage section -------//
    public void nextmap(){
        stagenum++;
        this.map = new Map("InputFile\\map"+ stagenum + ".txt");
    }

    public void resetstage(){
        stagenum = 1;
        this.map = new Map("src\\InputFile\\map"+ stagenum + ".txt");
    }

    public int getstage(){
        return stagenum;
    }
    //------- win-loose-exit-door condition----------//
    public boolean win(){
        if(this.map.numberOfMonsters() == 0){
            return true;
        }
        else
            return false;
    }

    public boolean getStatus(){
        return status;
    }
    public void setStatus(boolean stat){
        this.status = stat;
    }

    public boolean loose(){
        if (this.player.getHP() == 0)
            return true;
        else
            return false;
    }

    public boolean isPlayeratDoor(){
        return this.map.containDoorAt(this.player.getX(), this.player.getY());
    }
    //--------- player section --------------//
    public void playerAction(){ // reminder: after changing the attack function should have [2. attack (availible)] and [2. attack (not availible)] || solution: None
        int choice;
        System.out.println("------Now it Your turn to go-------");
        System.out.println("1. Move");
        System.out.println("2. Attack");
        System.out.println("3. Inventory");
        System.out.println("4. exit");
        choice = input.nextInt();
        input.nextLine();
        switch (choice){
            case 1:
                playerMove();
                playerGetItem();
                break;
            case 2:
                playerAttack();
                break;
            case 3:
                playerInvent();
                break;
            case 4:
                status = true;
                break;
            default:
                System.out.println("Invalid choice please choose again!");
                playerAction();
                break;
        }
    }

    public void playerMove(){
        int choice;
        System.out.println("\n------------------------------------------------------\n");
        System.out.println("1. Move Up");
        System.out.println("2. Move Down");
        System.out.println("3. Move Left");
        System.out.println("4. Move Right");
        System.out.println("5. No Move");
        System.out.println("6. Back to menu");
        System.out.print("Enter your choice: ");
        choice = input.nextInt();
        input.nextLine();            
        switch (choice) {
            case 1:
                this.player.moveUp(this.map);
                break;
            
            case 2:
                this.player.moveDown(this.map);
                break;

            case 3:
                this.player.moveLeft(this.map);
                break;
            
            case 4:
                this.player.moveRight(this.map);
                break;
            
            case 5:
                break;
            
            case 6:
                playerAction();
                break;
            default:
                break;
        }
    }

    public void playerAttack(){ // Reminder: need to improve this function (too complex and should have print out in menu if avalible) || solution: None
        //Find all monsters in range of player
        ArrayList<Monster> targets = new ArrayList<Monster>();
        for(int i = 0; i < this.map.numberOfMonsters(); i++){
            if(isInRange(this.player, this.map.getMonsterAtIndex(i)))
                targets.add(this.map.getMonsterAtIndex(i));
        }

        //Print all monsters in range so that player can pick one to attack
        if(targets.size() == 0)
        {
            System.out.println(">> No monster in range to attack! (press any key to continue)");
            input.nextLine();
        }
        else
        {
            System.out.printf("|%10s | %20s | %10s |\n", "No.",
                                                        "Name",
                                                        "HP");
            for(int i = 0; i < targets.size(); i++){
                System.out.printf("|%10s | %20s | %10s |\n", i + 1, 
                        targets.get(i).getName() + "(" + targets.get(i).getMark() + ")",
                        targets.get(i).getHP() + "/" + targets.get(i).getMaxHp());
            }
            int choice;
            System.out.print("Choose a number (0: Exit || 1 - " + targets.size() + ") to attack monster: ");
            choice = input.nextInt();
            if(choice > 0){
                targets.get(choice - 1).takeDamage(this.player.getAttack());
            }
            else if(choice < 0)
                System.out.println("Invalid choice");
            else{           
                System.out.println("\n------------------------------------------------------\n");
                playerAction();
            }
        }
    }
    public boolean isInRange(Character obj1, Character obj2){ // Reminder: need to fix this, too many funtion need this should not be here || solution: None
        boolean status = false;
        int max_X = obj1.getX() + obj1.getRange();
        int min_X = obj1.getX() - obj1.getRange();
        int max_Y = obj1.getY() + obj1.getRange();
        int min_Y = obj1.getY() - obj1.getRange();
        if(min_X <= obj2.getX() && obj2.getX() <= max_X && min_Y <= obj2.getY() && obj2.getY() <= max_Y)
            status = true;
        return status;
    }
    
    public void playerInvent(){ // Reminder: need to improve this funtion (too complex) || solution: add more methods to inventory
        System.out.println("\n------------------------------------------------------\n");
        if(this.invent.isEmpty())         
            System.out.println("Empty inventory");
        else{
            this.invent.displayInventory();
            System.out.println("Attack weapon: " + this.player.getCurrentWeapon());
            System.out.println("Defense weapon: " + this.player.getCurrentArmor());
            int choice, choice1;
            boolean status = true;
            do{              
                System.out.print("Enter a number to show item (Exit: 0 | Range: 1 - " + this.invent.size() + "): ");
                choice = input.nextInt();
                if(choice == 0) {
                    this.showGraphic();
                    status = false;
                    }
                else if(0 < choice && choice <= this.invent.size()){
                    boolean status1 = true;
                    System.out.println("\n------------------------------------------------------\n");  
                    if(this.invent.getItem(choice - 1) instanceof Weapon)
                        System.out.println((this.invent.getItem(choice - 1)).toString());
                    else if(this.invent.getItem(choice - 1) instanceof Armor)
                        System.out.println((this.invent.getItem(choice - 1)).toString());
                    else if(this.invent.getItem(choice - 1) instanceof Potion)
                        System.out.println(this.invent.getItem(choice - 1).toString());
                    do{               
                        System.out.println("1. Use item");
                        System.out.println("2. Remove item");
                        System.out.println("3. Back");
                        System.out.print("Enter your choice: ");
                        choice1 = input.nextInt();
                        if(choice1 == 1){
                            if(this.invent.getItem(choice - 1) instanceof Weapon)
                                this.player.equipWeapon(this.invent.getItem(choice - 1));
                            else if (this.invent.getItem(choice - 1) instanceof Armor)
                                this.player.equipArmor(this.invent.getItem(choice - 1));
                            else if (this.invent.getItem(choice -1) instanceof Potion){
                                this.player.equipPotion(this.invent.getItem(choice -1));
                                this.invent.removeItem(choice - 1);
                            }
                            System.out.println("Equip sucessfully");
                            System.out.println("\n------------------------------------------------------\n");
                            this.invent.displayInventory();
                            System.out.println("Current weapon: " + this.player.getCurrentWeapon());
                            System.out.println("Current armor: " + this.player.getCurrentArmor());
                            status1 = false;
                        }       
                        else if(choice1 == 2){
                            if(this.invent.getItem(choice - 1) instanceof Weapon && this.invent.getItem(choice - 1).getInUse() == true)
                                this.player.unequipWeapon();
                            else if(this.invent.getItem(choice - 1) instanceof Armor && this.invent.getItem(choice - 1).getInUse() == true)
                                this.player.unequipArmor();
                            this.invent.removeItem(choice - 1);
                            System.out.println("Remove sucessfully");
                            System.out.println("\n------------------------------------------------------\n");
                            this.invent.displayInventory();
                            System.out.println("Current weapon: " + this.player.getCurrentWeapon());
                            System.out.println("Current armor: " + this.player.getCurrentArmor());
                            status1 = false;
                        }
                        else if(choice1 == 3){            
                            System.out.println("\n------------------------------------------------------\n");
                            this.invent.displayInventory();
                            System.out.println("Current weapon: " + this.player.getCurrentWeapon());
                            System.out.println("Current armor: " + this.player.getCurrentArmor());
                            status1 = false;
                        }
                        else System.out.println("Invalid choice");
                    } while (status1 == true);          
                }
                else System.out.println("Invalid choice");
            }while(status == true);
            playerAction();
        }
    }
    public void playerGetItem(){
        if(this.map.containItemAt(this.player.getX(), this.player.getY())){ 
            if(!this.invent.isFull()){
                this.invent.addItem(this.map.correspondingItemAt(this.player.getX(), this.player.getY()));
                this.map.removeItemHavingPosition(this.player.getX(), this.player.getY());
            }
            else JOptionPane.showMessageDialog(null, "Inventory is full!!!");
        } 
    }

    public void resetPlayer(){
        this.player = new Player(null);
        this.invent = new Inventory(5);
        resetPlayerPosition();
    }
    public void resetPlayerPosition(){
        this.player.setXY(0, 0, this.map);
    }
    //---------- monster section --------------//
    public void monsterAction(){
            for(int i = 0; i < this.map.numberOfMonsters(); i++){
                this.map.getMonsterAtIndex(i).doWork(this.player, this.map);
            }
            if (this.map.numberOfMonsters() == 0) {
                this.map.openDoor();
            }

    }
    //------------ graphic section ---------------//
    public void showGraphic(){
        this.map.drawMap(this.player);
        this.player.showState();
    }

    //------------- work with files -----------------//
    public void save(String filename)  {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(filename);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(this);
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static GameStage load(String filename) {
        GameStage stage = null;
        try {
            FileInputStream fileInputStream = new FileInputStream(filename);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            stage = (GameStage) objectInputStream.readObject();
            objectInputStream.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return stage;
    }

    public boolean delete(String filename) {
        File file = new File(filename);
        return file.delete();
    }

//--------- test main ------------//
    public static void main(String[] args) 
    {
        GameStage stage = new GameStage();
        System.err.println(stage.getStatus());
        stage.playerAction();
        System.out.println(stage.getStatus());
    }
}


