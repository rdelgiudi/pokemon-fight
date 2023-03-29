package com.delgiudice;

import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

import static java.lang.Math.floor;

public class Battle {

    static int j = 0, i = 0;
    static Pokemon currentPPokemon = null, currentEPokemon = null;


    static void initTrainerBattle(Player player, NpcTrainer trainer)
    {
        i = 0;      //marker for tracking currently sent out trainer Pokemon
        j = 0;      //marker for tracking currently sent out player Pokemon
        //Identifies trainer name and party size
        int partySize = trainer.getParty().size();
        String trainerName = trainer.getTrainerType().toString() + " " + trainer.getName();
        System.out.println(trainerName + " wants to battle!");
        System.out.println(trainerName + " has " + partySize + " Pokemon in his party.");
        //Flags to check if the trainer and player are still able to battle
        boolean trainerAble = true;
        boolean playerAble = true;
        int lasti = -1;

        while (trainerAble && playerAble){
            //Checks for a pokemon that is able to battle and sends it out
            if(trainer.getParty(i).getHp() != 0)
                currentEPokemon = trainer.getParty(i);
            else if (i < trainer.getParty().size() - 1)
            {
                i++;
                continue;
            }
            else {
                System.out.println("You won against " + trainerName + "!");
                trainerAble = false;
                continue;
            }
            if (lasti != i)
                System.out.println(trainerName + " sends out " + currentEPokemon.getName() + ".");
            lasti = i;

            if(currentPPokemon == null) {
                if(player.getParty(j).getHp() != 0)
                    currentPPokemon = player.getParty(j);
                else if (j < player.getParty().size() - 1) {
                    j++;
                    continue;
                }
                else {
                    System.out.println("Out of usable Pokemon! Lost against " + trainerName + ".");
                    System.out.println("You withed out!");
                    playerAble = false;
                    continue;
                }
                System.out.println("Go " + currentPPokemon.getName() + "!");
                resetStats(currentPPokemon);
            }

            resetStats(currentEPokemon);
            initBattle(player, true);

        }


    }
    static void initWildBattle(Player player, Pokemon pokemon)
    {

    }
    static void initBattle(Player player, boolean trainer)
    {
        Random generator = new Random();
        Scanner s = new Scanner(System.in);
        boolean battleend = false;
        while (currentPPokemon.getHp() > 0 && currentEPokemon.getHp() > 0 && !battleend) {       //battle won't end until one of the Pokemon faints or we run away or is swapped out
            int choice;
            //Display of basic information regarding the trainer's Pokemon
            //System.out.println("\nYour Pokemon:\t" + currentPPokemon.getName() + "\tLv.: " + currentPPokemon.getLevel() + "\tHP: " + currentPPokemon.getHp() + "/" + currentPPokemon.getStats("Max HP"));
            System.out.format("\nYour pokemon:   %-15s Lv.: %-3d HP: %3d/%-3d\n", currentPPokemon.getName(), currentPPokemon.getLevel(), currentPPokemon.getHp(), currentPPokemon.getStats("Max HP"));
            //System.out.println("Enemy Pokemon:\t" + currentEPokemon.getName() + "\t" + "Lv.: " + currentEPokemon.getLevel() + "\tHP: " + currentEPokemon.getHp() + "/" + currentEPokemon.getStats("Max HP"));
            System.out.format("Enemy pokemon:  %-15s Lv.: %-3d HP: %3d/%-3d\n", currentEPokemon.getName(), currentEPokemon.getLevel(), currentEPokemon.getHp(), currentEPokemon.getStats("Max HP"));
            System.out.println("\nSelect option:");
            System.out.println("1.Fight");
            System.out.println("2.Pokemon");
            System.out.println("3.Bag");
            System.out.println("4.Run");
            choice = s.nextInt();
            //After selecting a choice we are presented with multiple options depending on selected choice
            switch(choice) {
                case 1:
                    System.out.println("Select Move:");
                    for(int a = 0; a < currentPPokemon.getMoveList().size(); a++)
                        System.out.println((a + 1) + ". " + currentPPokemon.getMoveList(a).getName() + " PP: "
                                + currentPPokemon.getMoveList(a).getPp() + "/" + currentPPokemon.getMoveList(a).getMaxpp());
                    int move;
                    move = s.nextInt() - 1;
                    System.out.println();
                    //Speed calculation
                    //Depending on speed, attack occurs in a different order
                    if (move < currentPPokemon.getMoveList().size() && move >= 0) {
                        if(currentEPokemon.getMoveList(move).getPp() == 0)
                        {
                            System.out.println("Out of PP!");
                            break;
                        }
                        boolean faster = currentPPokemon.getStats().get("Speed") > currentEPokemon.getStats().get("Speed");
                        boolean tied = currentPPokemon.getStats().get("Speed").equals(currentEPokemon.getStats().get("Speed"));
                        Runnable playerFaster = () -> {
                            useMove(currentPPokemon, currentEPokemon, currentPPokemon.getMoveList(move));
                            if (currentEPokemon.getHp() == 0)
                                return;
                            int enemyMove = generator.nextInt(currentEPokemon.getMoveList().size());
                            useMove(currentEPokemon, currentPPokemon, currentEPokemon.getMoveList(enemyMove));
                        };
                        Runnable enemyFaster = () -> {
                            int enemyMove = generator.nextInt(currentEPokemon.getMoveList().size());
                            useMove(currentEPokemon, currentPPokemon, currentEPokemon.getMoveList(enemyMove));
                            if (currentPPokemon.getHp() == 0)
                                return;
                            useMove(currentPPokemon, currentEPokemon, currentPPokemon.getMoveList(move));
                        };

                        if (faster) {
                            playerFaster.run();
                        }
                        //On speed tie, the first attacker is randomized
                        else if (tied) {
                            int flip = generator.nextInt(2);
                            if (flip == 1)
                                playerFaster.run();
                            else
                                enemyFaster.run();
                        }
                        //Here is what happens if enemy Pokemon is faster
                        else
                            enemyFaster.run();

                    }
                    else
                        System.out.println("Selected move out of range, back to options...");
                    break;
                case 2:
                    for (int a = 0; a < player.getParty().size(); a++)
                        System.out.println((a+1) + ". " + player.getParty().get(a).getName() + " HP: " + player.getParty().get(a).getHp() + "/" + player.getParty().get(a).getStats("Max HP"));
                    int selPoke = s.nextInt() - 1;
                    if(selPoke >= player.getParty().size() || selPoke < 0)
                    {
                        System.out.println("Out of range, try again!");
                        break;
                    }
                    if (player.getParty().get(selPoke).equals(currentPPokemon))
                    {
                        System.out.println("This Pokemon is already out!");
                        break;
                    }
                    switchPokemon(player, selPoke);
                    int enemyMove = generator.nextInt(currentEPokemon.getMoveList().size());
                    useMove(currentEPokemon, currentPPokemon, currentEPokemon.getMoveList(enemyMove));
                    break;
                case 3:
                    break;
                case 4:
                    if (trainer)
                        System.out.println("Cant run from a trainer battle!");
                    else {
                        System.out.println("Ran away successfully!");
                        battleend = true;
                    }
                    break;
                default:
                    System.out.println("Selected option out of range, back to options...");
                    break;
            }
            if(currentPPokemon.getHp() == 0)
            {
                System.out.println(currentPPokemon.getName() + " fainted!");
                if (!checkParty(player)) {
                    currentPPokemon = null;
                    battleend = true;
                    return;
                }
                System.out.println("Select another pokemon");
                for (int a = 0; a < player.getParty().size(); a++)
                    System.out.println((a+1) + ". " + player.getParty().get(a).getName() + " HP: " + player.getParty().get(a).getHp() + "/" + player.getParty().get(a).getStats("Max HP"));
                boolean pokeReplaced = false;
                while (!pokeReplaced) {
                    int selPoke = s.nextInt() - 1;
                    if(selPoke >= player.getParty().size())
                    {
                        System.out.println("Out of range, try again!");
                        continue;
                    }
                    if (player.getParty().get(selPoke).equals(currentPPokemon))
                    {
                        System.out.println("This Pokemon is already out and it just fainted!");
                        continue;
                    }
                    switchPokemon(player ,selPoke);
                    pokeReplaced = true;
                }
            }
            if(currentEPokemon.getHp() == 0)
            {
                System.out.println(currentEPokemon.getName() + " fainted!");
            }
        }

    }


    static void useMove(Pokemon user, Pokemon target, Move move)
    {
        System.out.println(user.getName() + " used " + move.getName());
        move.setPp((byte) (move.getPp() - 1));
        Random generator = new Random();
        int moveAccuracy = move.getAccuracy();
        if (moveAccuracy != 0) {
            int statAccuracy = ((user.statModifiers.get("Accuracy") - target.statModifiers.get("Evasiveness")) + 3)/3;
            if (statAccuracy > 6)
                statAccuracy = 6;
            else if (statAccuracy < -6)
                statAccuracy = -6;
            int hit = moveAccuracy * statAccuracy;
            int r = generator.nextInt(100) + 1;
            if (r > hit) {
                System.out.println("Move missed!");
                return;
            }
        }
        String moveType = move.getSubtype().toString();
        switch (moveType){
            case "PHYSICAL":
                int damagePhysical = calculateMoveDamage(0, move, user, target);
                if (damagePhysical > target.getHp())
                    damagePhysical = target.getHp();
                target.setHp(target.getHp() - damagePhysical);
                System.out.println(move.getName() + " dealt " + damagePhysical + " damage!");
                break;
            case "SPECIAL":
                int damageSpecial = calculateMoveDamage(1, move, user, target);
                if (damageSpecial > target.getHp())
                    damageSpecial = target.getHp();
                target.setHp(target.getHp() - damageSpecial);
                System.out.println(move.getName() + " dealt " + damageSpecial + " damage!");
                break;
            case "STATUS":
                break;
        }
    }

    static int calculateMoveDamage(int movetype, Move move, Pokemon user, Pokemon target)
    {
        Random generator = new Random();
        int critNum = generator.nextInt(16);
        boolean isCrit = false;
        float critMod;
        int attackTemp = 0;
        int attackMod = 0;
        int defenseTemp = 0;
        int defenseMod = 0;
        //First we check if the executed move is a special or physical move, then we grab the corresponding stats
        switch (movetype) {
            case 0:
                attackTemp = user.getStats("Attack");
                attackMod = user.statModifiers.get("Attack");
                defenseTemp = user.getStats("Defense");
                defenseMod = user.statModifiers.get("Defense");
                break;
            case 1:
                attackTemp = user.getStats("Special Attack");
                attackMod = user.statModifiers.get("Special Attack");
                defenseTemp = user.getStats("Special Defense");
                defenseMod = user.statModifiers.get("Special Defense");
        }

        int attack;
        int defense;
        //We check if the hit is going to be critical (critical hits ignore negative stat changes and only leave positive ones)
        if (critNum == 0)
            isCrit = true;
        if (isCrit) {
            if(attackMod >= 0)
                attack = attackTemp * (2 + attackMod)/2;
            else
                attack = attackTemp;
            if(defenseMod <= 0)
                defense = defenseTemp * (2 + defenseMod)/2;
            else
                defense = defenseTemp;
            critMod = 1.5f;
        }
        else  {
            attack = attackTemp * (2 + attackMod)/2;
            defense = defenseTemp * (2 + defenseMod)/2;
            critMod = 1;
        }
        //Miscellaneous stats calculation (stab, random factor, burn debuff)
        int part1 = ((2 * user.getLevel())/5) + 2;
        float rand = (generator.nextInt(15) + 85)/100.0f;
        float stab;
        if (move.getType().equals(user.getType()[0]) || move.getType().equals(user.getType()[1]))
            stab = 1.5f;
        else
            stab = 1f;
        float burn = 1;
        if (movetype == 0 && user.getStatus() == Enums.Status.BURNED)
            burn = 0.5f;
        //Checking how the type of the move is going to affect the damage
        float typeEffect = 1;
        if (move.getType().getNoEffectAgainst().equals(target.getType()[0].getTypeEnum())
                || move.getType().getNoEffectAgainst().equals(target.getType()[1].getTypeEnum())) {
            typeEffect = 0;
        }
        else {
            for (Enums.Types type : move.getType().getStrongAgainst()){
                if(type.equals(target.getType()[0].getTypeEnum()) || type.equals(target.getType()[1].getTypeEnum()))
                    typeEffect *= 2;
            }
            for (Enums.Types type : move.getType().getWeakAgainst()){
                if(type.equals(target.getType()[0].getTypeEnum()) || type.equals(target.getType()[1].getTypeEnum()))
                    typeEffect *= 0.5f;
            }
        }
        //Final damage calculations
        float modifier = critMod * rand * stab * typeEffect * burn;
        int damage = (int)(((part1 * move.getPower() * (attack/defense))/50 + 2) * modifier);

        System.out.println(user.getName() + " used a " + move.getType().toString() + " type move!");

        //Critical hit message
        if(isCrit && typeEffect != 0)
            System.out.println("A critical hit!");

        //Type effect message
        if(typeEffect == 0)
            System.out.println("It doesn't affect " + target.getName() + "...");
        else if(typeEffect > 1)
            System.out.println("It's super effective!");
        else if(typeEffect < 1)
            System.out.println("It's not very effective...");

        return damage;
    }
    //Additional move effects calculated here
    static void moveEffect(Pokemon user, Pokemon target, Move move)
    {

    }
    //Simple method for swapping Pokemon (player only)
    static void switchPokemon(Player player, int index)
    {
        if (player.getParty(index).getHp() == 0)
        {
            System.out.println("Pokemon fainted!");
            return;
        }
        System.out.println(currentPPokemon.getName() + ", come back!");
        currentPPokemon = player.getParty(index);
        resetStats(currentPPokemon);
        System.out.println("Go " + currentPPokemon.getName() + "!");
    }

    //checks if there is any Pokemon in party able to fight
    static boolean checkParty(Trainer trainer)
    {
        boolean trainerAble = false;
        for(Pokemon pokemon : trainer.getParty())
        {
            if (pokemon.getHp() > 0)
                trainerAble = true;
        }
        return  trainerAble;
    }

    //reset in-battle stat modifiers
    static void resetStats(Pokemon pokemon)
    {
        pokemon.statModifiers.clear();
        for(Enums.StatusType type: Enums.StatusType.values()) {
            pokemon.statModifiers.put(type.toString(), 0);
        }
    }
}


