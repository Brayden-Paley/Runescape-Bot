package OsrsTask.Tasks;

import OsrsTask.Task;
import OsrsTask.Walker;
import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;

import java.util.concurrent.Callable;


public class Walk extends Task {

    private final Walker walker = new Walker(ctx);

    public Tile pathToBank[];

    public Walk(ClientContext ctx, Tile[] pathToBank) {
        super(ctx);
        this.pathToBank = pathToBank;
    }

    @Override
    public boolean activate() {
        return ctx.inventory.select().count() > 27 || (ctx.inventory.select().count() < 28 && pathToBank[0].distanceTo(ctx.players.local()) > 6);
    }

    @Override
    public void execute() {

        if(!ctx.movement.running() && ctx.movement.energyLevel() > Random.nextInt(35, 55)){
            ctx.movement.running(true);
            Condition.wait(new Callable<Boolean>(){

                @Override
                public Boolean call() throws Exception {
                    return ctx.movement.running(true);
                }
            },100, 2);
        }

        if (!ctx.players.local().inMotion() || ctx.movement.destination().equals(Tile.NIL) || ctx.movement.destination().distanceTo(ctx.players.local()) < 5) {

            if (ctx.inventory.select().count() > 27){
                walker.walkPath(pathToBank);
            } else {
                walker.walkPathReverse(pathToBank);
            }



        }
    }
}
