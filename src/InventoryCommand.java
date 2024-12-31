import java.util.*;
class InventoryCommand extends Command {

    String execute(){
        String i = "";
        ArrayList<Item> items = Gamestate.instance().getInventory();

        if(items.size()==0){
            return("Your inventory is empty");
        }
        
        i = "You are carrying: \n";
        for(int j = 0; j < items.size(); j ++){
            i = i + "A " + items.get(j).getPrimaryName() + "\n"; 
        }

        return(i);
    }

}
