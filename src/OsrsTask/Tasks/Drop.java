package OsrsTask.Tasks;

import OsrsTask.Task;
import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Item;

import java.util.concurrent.Callable;
import java.util.regex.Pattern;

public class Drop extends Task {
    public Drop(ClientContext ctx) {
        super(ctx);
    }

    //final static int GEM_IDS[] = {1619, 1623, 1621, 1617};

    @Override
    public boolean activate() {
        return ctx.inventory.select().count() == 28;
    }

    @Override
    public void execute() {
        //First drop all copper
        for(Item copperOre : ctx.inventory.select().name(Pattern.compile("(.*ore)|(Clay)|(Coal)|(.*sapphire)|(.*emerald)|(.*ruby)|(.*diamond)|(Uncut)|(.*logs)|(Logs)"))){

            int random = (int )(Math.random() * 33 + 1);

            if(ctx.controller.isStopping()){
                break;
            }


            final int startAmtInventory = ctx.inventory.select().count();
            copperOre.interact("Drop");

            Condition.wait(new Callable<Boolean>(){

                @Override
                public Boolean call() throws Exception {
                    return ctx.inventory.select().count() != startAmtInventory;
                }
            },random, 10);
        }

    }
}
