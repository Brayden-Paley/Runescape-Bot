package OsrsTask.Tasks;

import OsrsTask.Task;
import org.powerbot.script.Condition;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;

import java.util.concurrent.Callable;

public class Mine extends Task {

    Tile rockLocation = Tile.NIL;

    int rockIds[];

    public Mine(ClientContext ctx, int[] rockIds) {
        super(ctx);
        this.rockIds = rockIds;
    }

    @Override
    public boolean activate() {
        return ctx.objects.select().at(rockLocation).id(rockIds).poll().equals( ctx.objects.nil()) || ctx.players.local().animation() == -1;
    }

    @Override
    public void execute() {

        int random = (int)(Math.random() * 200 + 1000);


        GameObject rockToMine = ctx.objects.select().id(rockIds).nearest().poll();
        rockLocation = rockToMine.tile();

        rockToMine.interact("Mine");

        Condition.wait( new Callable<Boolean>(){

            @Override
            public Boolean call() throws Exception {
                return ctx.players.local().animation() == -1;
            }
        },650, 2);


    }
}
