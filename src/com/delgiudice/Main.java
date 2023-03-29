package com.delgiudice;

public class Main {

    public static void main(String[] args) {
        NpcTrainer.setTrainerList();
        Pokemon pokemon = new Pokemon(PokemonSpecie.pokemonList.get("Bulbasaur"), 5, new Move(Move.moveList.get("Tackle")),
                new Move(Move.moveList.get("Growl")), new Move(Move.moveList.get("Vine Whip")));
        Player player = new Player("Dżudis", pokemon);
        pokemon = new Pokemon(PokemonSpecie.pokemonList.get("Charmander"), 5,  new Move(Move.moveList.get("Scratch")),
                new Move(Move.moveList.get("Growl")),  new Move(Move.moveList.get("Ember")));
        player.addPokemon(pokemon);
        NpcTrainer trainer = NpcTrainer.trainerList.get("Joey");
        Battle.initTrainerBattle(player, trainer);
        /*
        Type testtype = new Type(Enums.Types.FIRE);
        System.out.println(testtype.getTypeEnum().toString() + " type is weak against:");
        for(Enums.Types weakAgainst : testtype.getWeakAgainst()){
            System.out.println(weakAgainst.toString());
        }
        if(testtype.getStrongAgainst().size() != 0)
            System.out.println(testtype.getTypeEnum().toString() + " type is strong against:");
        for(Enums.Types strongAgainst : testtype.getStrongAgainst()){
            System.out.println(strongAgainst.toString());
        }
        if(testtype.getNoEffectAgainst() != Enums.Types.MISSING)
            System.out.println(testtype.getTypeEnum().toString() + " type can't damage: "
                    + testtype.getNoEffectAgainst().toString() + " type");
        Pokemon pokemon = new Pokemon(Pokemon.pokemonList.get("Bulbasaur"));
        Pokemon pokemon2 = new Pokemon(Pokemon.pokemonList.get("Bulbasaur"));
        pokemon.generateVariables();
        pokemon2.generateVariables();
        System.out.println("\nThis is " + pokemon.getName() + ". It is a " + pokemon.getType()[0].getTypeEnum().toString() + " and "
                + pokemon.getType()[1].getTypeEnum().toString() + " type Pokemon. It is level " + pokemon.getLevel() + ".");
        System.out.println("Nature: " + pokemon.getNature().toString());
        System.out.println("IVs:");
        for (int iv : pokemon.getIvs())
        {
            System.out.println(iv);
        }
        System.out.println("\nBase stats:");
        for(Map.Entry<String, Integer> stat : pokemon.getBaseStats().entrySet()) {
            String statname = stat.getKey();
            int statval = stat.getValue();
            System.out.println(statname + ": " + statval);
        }
        System.out.println("\nStats(for level " + pokemon.getLevel() + "):");
        for(Map.Entry<String, Integer> stat : pokemon.getStats().entrySet()) {
            String statname = stat.getKey();
            int statval = stat.getValue();
            System.out.println(statname + ": " + statval);
        }

        System.out.println("\nThis is " + pokemon2.getName() + ". It is a " + pokemon2.getType()[0].getTypeEnum().toString() + " and "
                + pokemon2.getType()[1].getTypeEnum().toString() + " type Pokemon. It is level " + pokemon2.getLevel() + ".");
        System.out.println("Nature: " + pokemon2.getNature().toString());
        System.out.println("IVs:");
        for (int iv : pokemon2.getIvs())
        {
            System.out.println(iv);
        }
        System.out.println("\nBase stats:");
        for(Map.Entry<String, Integer> stat : pokemon2.getBaseStats().entrySet()) {
            String statname = stat.getKey();
            int statval = stat.getValue();
            System.out.println(statname + ": " + statval);
        }
        System.out.println("\nStats(for level " + pokemon2.getLevel() + "):");
        for(Map.Entry<String, Integer> stat : pokemon2.getStats().entrySet()) {
            String statname = stat.getKey();
            int statval = stat.getValue();
            System.out.println(statname + ": " + statval);
        }*/
    }

}
