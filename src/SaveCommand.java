import java.util.*;
import java.io.*;
class SaveCommand extends Command{
    
    private String saveFilename;
    
    SaveCommand(String save){
        this.saveFilename = save;
    } 

    String execute(){
        Scanner scan = new Scanner(System.in);
        
        String saveName = this.saveFilename + ".sav";

        try{
           Gamestate.instance().store(this.saveFilename);
        }catch(Exception e){
            e.printStackTrace();
        }


        return("*saved*");
    }

}
