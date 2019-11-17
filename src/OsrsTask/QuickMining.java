package OsrsTask;

import OsrsTask.Tasks.*;
import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;

import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Constants;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Script.Manifest(name = "Quick Mining", description = "A mining script using tasks", properties = "client = 4; author = Brayden; topic = 999;")

public class QuickMining extends PollingScript<ClientContext> implements PaintListener{


    int startExp = 0;
    List<Task> taskList = new ArrayList<Task>();

    @Override
    public void start(){

        String userOptions[] = {"Bank", "Powermine"};
        String userChoice = "" + (String) JOptionPane.showInputDialog(null,"Bank or Powermine?", "QuickMining", JOptionPane.PLAIN_MESSAGE, null, userOptions, userOptions[1]);


        String oreOptions[] = {"Copper", "Clay", "Iron"};
        String oreChoice = "" + (String) JOptionPane.showInputDialog(null,"What ore do you want to mine?", "QuickMining", JOptionPane.PLAIN_MESSAGE, null, oreOptions, oreOptions[1]);

        if(userChoice.equals("Bank")){

            String locationOptions[] = {"Lumbridge Swamp", "Falador Dwarven"};
            String locationChoice = "" + (String) JOptionPane.showInputDialog(null,"Choose your location!", "QuickMining", JOptionPane.PLAIN_MESSAGE, null, locationOptions, locationOptions[0]);

            if(locationChoice.equals("Lumbridge Swamp")){
                taskList.add(new Walk(ctx,MConstants.LUMBRIDGE_SWAMP));

            } else if (locationChoice.equals("Falador Dwarven")){
                List<Tile> finalPath = new ArrayList<Tile>();

                if (oreChoice.equals("Copper")){
                    finalPath.addAll(Arrays.asList(MConstants.DWARVEN_COPPER));
                } else if(oreChoice.equals("Clay")){
                    finalPath.addAll(Arrays.asList(MConstants.DWARVEN_CLAY));
                } else{
                    ctx.controller.stop();
                }

                finalPath.addAll(Arrays.asList(MConstants.DWARVEN_MINE));
                taskList.add(new Walk(ctx,finalPath.toArray(new Tile[] {})));

            } else{
                ctx.controller.stop();
            }

            taskList.add(new Bank(ctx));

        } else if(userChoice.equals("Powermine")){
            taskList.add(new Drop(ctx));
        } else{
            ctx.controller.stop();
        }

        if (oreChoice.equals("Copper")){
            taskList.add(new Mine(ctx,MConstants.COPPER_IDS));
        } else if (oreChoice.equals("Clay")){
            taskList.add(new Mine(ctx,MConstants.CLAY_IDS));
        } else if (oreChoice.equals("Iron")) {
            taskList.add(new Mine(ctx, MConstants.IRON_IDS));
        } else {
            ctx.controller.stop();
        }
        startExp = ctx.skills.experience(Constants.SKILLS_MINING);
    }

    @Override
    public void poll() {
        for(Task task : taskList){

            if(ctx.controller.isStopping()){
                break;
            }

             if (task.activate()){
                 task.execute();
                 break;
             }
        }
    }

    @Override
    public void repaint(Graphics graphics) {
        long milliseconds = this.getTotalRuntime();
        long seconds = (milliseconds / 1000) % 60;
        long minutes = (milliseconds / (1000*60)) % 60;
        long hours = (milliseconds / (1000 * 60 * 60)) % 24;

        int expGained = ctx.skills.experience(Constants.SKILLS_MINING) - startExp;


        Graphics2D g = (Graphics2D) graphics;
        g.setColor(new Color(0,0,0, 180));
        g.fillRect(0,0, 150, 100);

        g.setColor(new Color(255,255,255));
        g.drawRect(0,0,150,100);

        g.drawString("QuickMining", 10,20);
        g.drawString("Running " + String.format("%02d:%02d:%02d", hours, minutes, seconds), 10, 40);

        g.drawString("Exp/Hour: " + (int)(expGained * (3600000D / milliseconds)), 10, 60);
    }
}
