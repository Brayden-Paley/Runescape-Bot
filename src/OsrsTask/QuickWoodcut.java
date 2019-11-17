package OsrsTask;


import OsrsTask.Tasks.*;
import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Constants;


import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Script.Manifest(name = "QuickChop", description = "A woodcutting script using tasks", properties = "client = 4; author = Brayden; topic = 999;")


public class QuickWoodcut extends PollingScript<ClientContext> implements PaintListener {


    int startExp = 0;
    List<Task> taskList = new ArrayList<Task>();

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
    public void start(){
        String userOptions[] = {"Bank", "Power Chop"};
        String userChoice = "" + (String) JOptionPane.showInputDialog(null,"Bank or Powerchop?", "QuickChop", JOptionPane.PLAIN_MESSAGE, null, userOptions, userOptions[0]);

        String treeOptions[] = {"Tree", "Willow"};
        String treeChoice = "" + (String) JOptionPane.showInputDialog(null,"What tree do you want to chop?", "Quick Chop", JOptionPane.PLAIN_MESSAGE, null, treeOptions, treeOptions[1]);


        if(userChoice.equals("Bank")){
            String locationOptions[] = {"Draynor Willows"};
            String locationChoice = "" + (String) JOptionPane.showInputDialog(null,"Choose your location!", "QuickChop", JOptionPane.PLAIN_MESSAGE, null, locationOptions, locationOptions[0]);

            if(locationChoice.equals("Draynor Willows")){
                taskList.add(new Walk(ctx,MConstants.DRAYNOR_WILLIOW));
            } else{
                ctx.controller.stop();
            }
            taskList.add(new Bank(ctx));

        } else if(userChoice.equals("Power Chop")){
            taskList.add(new Drop(ctx));
        } else {
            ctx.controller.stop();
        }

        if (treeChoice.equals("Tree")){
            taskList.add(new Chop(ctx,MConstants.TREE_IDS));
        } else if (treeChoice.equals("Willow")){
            taskList.add(new Chop(ctx,MConstants.WILLOW_IDS));
        } else {
            ctx.controller.stop();
        }
        startExp = ctx.skills.experience(Constants.SKILLS_WOODCUTTING);
    }

    @Override
    public void stop(){

    }

    @Override
    public void repaint(Graphics graphics) {
        long milliseconds = this.getTotalRuntime();
        long seconds = (milliseconds / 1000) % 60;
        long minutes = (milliseconds / (1000*60)) % 60;
        long hours = (milliseconds / (1000 * 60 * 60)) % 24;

        int expGained = ctx.skills.experience(Constants.SKILLS_WOODCUTTING) - startExp;


        Graphics2D g = (Graphics2D) graphics;
        g.setColor(new Color(0,0,0, 180));
        g.fillRect(0,0, 150, 100);

        g.setColor(new Color(255,255,255));
        g.drawRect(0,0,150,100);

        g.drawString("Quick Chop", 10,20);
        g.drawString("Running " + String.format("%02d:%02d:%02d", hours, minutes, seconds), 10, 40);

        g.drawString("Exp/Hour: " + (int)(expGained * (3600000D / milliseconds)), 10, 60);
    }
}
