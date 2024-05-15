package GameMain;

import java.util.Scanner;

import GAMESTAGE.GameStage;

public class GameMain {
    private GameStage stage;
    private int maxStage;
    private String filename = "data.ser";
    private static Scanner input = new Scanner(System.in);
    public GameMain(){
        this.maxStage = 4;
    }

    public void Run(){
        int choice;
        do
        {      
            System.out.println("\n******************************************************************");
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>> HOME MENU <<<<<<<<<<<<<<<<<<<<<<<<<<<");
            System.out.println("******************************************************************");
            System.out.println("1. New Game");
            System.out.println("2. Continue Game");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            choice = input.nextInt();
            input.nextLine();                //Consume keyboard buffer

            switch (choice) 
            {
                case 1:
                        this.stage = new GameStage();
                        startStage();
                        break;
                case 2: // reminder: add the sistuation where it cannot find the file
                        this.stage = GameStage.load(filename);
                        startStage();
                        break;
                case 3:
                        System.out.println("See you next time!");
                        input.close();
                        break;
                default:
                        System.out.println("ERROR: Invalid Choice!");
                        break;

            }
        } while (choice != 3);
    }
    public void startStage(){
        stage.showGraphic();
        do {
            stage.playerAction();
            if(!stage.getStatus()){
                stage.monsterAction();
                stage.showGraphic();
            }
            stage.save(filename);
        } while (!stage.win() && !stage.loose() && !stage.getStatus());
/*         
        if(stage.loose()){
            stage.resetstage();
            stage.resetPlayer();
            stage.delete(filename);
            System.out.println("You loose now return to Menu (press any key to continue)" );
            input.nextLine();
        }
        else{
            if(stage.getstage() < maxStage -1){
                while(!stage.isPlayeratDoor()){
                    stage.playerAction();
                    stage.showGraphic();
                    stage.save(filename);
                }
                System.out.println("You cleared stage " + stage.getstage() + "!");
                System.out.println("Now to stage "+ (stage.getstage()+1));
                stage.nextmap();
                stage.resetPlayerPosition();
                startStage();
            }
            else if(stage.getstage() == maxStage -1){
                System.out.println("You defeated the boss. However, the ground is shaking!");
                System.out.println("You have been pushed to the corner!"); // reminder to do really push
                stage.nextmap();
                stage.resetPlayerPosition();
                startStage();
            }
            else{
                int numchoice;
                System.out.println("Congrats you win the whole game!!");
                //System.out.println("Do you want to coutinue?(0: no| 1:yes)");
                //numchoice = input.nextInt();
                //input.nextLine();

            }
            
        }
*/ 
    }
    public static void main(String[] args) 
    {
        GameMain game = new GameMain();
        game.Run();
    }
}
