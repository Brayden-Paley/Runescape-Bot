package OsrsTask.Tasks;

import OsrsTask.Task;
import org.powerbot.script.Condition;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;

import java.util.concurrent.Callable;

public class Chop extends Task {

    Tile treeLocation = Tile.NIL;

    int[] treeIds;




    public Chop(ClientContext ctx, int[] treeIds) {
        super(ctx);
        this.treeIds = treeIds;
    }

    @Override
    public boolean activate() {
        return ctx.objects.select().at(treeLocation).id(treeIds).poll().equals( ctx.objects.nil()) || ctx.players.local().animation() == -1;
    }

    @Override
    public void execute() {

        GameObject treeToChop = ctx.objects.select().id(treeIds).nearest().poll();
        treeLocation = treeToChop.tile();

        treeToChop.interact("Chop down");

        Condition.wait(new Callable<Boolean>(){

            @Override
            public Boolean call() throws Exception {
                return ctx.players.local().animation() == -1;
            }
        },650, 2);

    }
}
