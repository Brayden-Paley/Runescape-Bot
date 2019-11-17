package scripts;


import org.powerbot.script.Condition;
import org.powerbot.script.Filter;
import org.powerbot.script.rt4.Actor;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.rt4.Item;
import org.powerbot.script.rt4.Npc;
import java.util.Random;

import java.util.concurrent.Callable;

@Script.Manifest(name = "Goblin Killer", description = "Kills goblins yo", properties = "author = Brayden; topic = 999; client = 4;")
public class HelloWorld extends PollingScript<ClientContext> {

    final static int GOBLIN_IDS[] = {3029, 3030, 3031, 3032, 3033, 3034, 3035};
    final static int COW_IDS[] = {2805, 2806, 2807, 2808, 2809,};
    final static int FOOD_ID = 315;

    @Override
    public void start(){
        System.out.println("Started");
    }

    @Override
    public void stop(){
        System.out.println("Stopped");

    }

    @Override
    public void poll() {

        if (hasFood()){

            if (needsHeal()){
                heal();
            } else if (shouldAttack() && !isFightingAMonster()){
                attack();
            }
        }

    }

    //Assumes youre not in a multi combat area
    public boolean isFightingAMonster(){

        Actor interactingWith = ctx.players.local().interacting();

        return interactingWith.valid() && interactingWith.combatLevel() > 0 && interactingWith.interacting().equals(ctx.players.local());
    }

    public boolean needsHeal(){
        return ctx.combat.health() < 6;
    }

    public boolean hasFood(){
        return ctx.inventory.select().id(FOOD_ID).count() > 0;
    }

    public boolean shouldAttack(){
        return !ctx.players.local().healthBarVisible();
    }

    public void attack(){
        final Npc goblinToAttack = ctx.npcs.select().id(COW_IDS).select(new Filter<Npc>() {
            @Override
            public boolean accept(Npc npc) {
                return !npc.healthBarVisible();
            }
        }).nearest().poll();

        goblinToAttack.interact("Attack");

        boolean wait = Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {


                return ctx.players.local().healthBarVisible();
            }

        }, 250, 20);
    }

    public void heal(){
        Item foodToEat = ctx.inventory.select().id(FOOD_ID).poll();

        foodToEat.interact("Eat");

        final int startingHealth = ctx.combat.health();

        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                final int currentHealth = ctx.combat.health();

                return currentHealth != startingHealth;
            }
        },150, 20);
    }
}
