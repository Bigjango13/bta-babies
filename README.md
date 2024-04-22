# BTA Breeding

This mod backports baby mobs from MCPI (and baby wolves from later versions) as well as breeding to [BTA](https://betterthanadventure.net/), here is a list of features it has:
- Pigs can be bred with mushrooms, pumpkins, or apples
- Chickens can be bred with all seeds (anything that is a `ItemSeed`, so it should work for mods too)
- Sheep and cows can be bred with wheat
- Wolves can be fed with anything they can be healed with (so raw and cooked pork)
- Animals will follow you when they food they like is held
- Feeding two adult animals of the same species will allow them produce a baby (that will take 20 minutes to grow up), then need to take a 5 minute break from reproduction
- Feeding a baby animal will speed up the growth by 30 seconds
- Baby animals will inherit some traits from the parent, such as skin variant, wool color (for sheep), and owner (for dogs)
- Baby animals will follow around their parent (although they aren't very smart and may follow around the wrong animal)
- Animals (should) no longer despawn
- The API it provides should be easy for mods to add custom behavioral to, even stuff like interspecies breeding (which would be useful for a horse and donkey backport)
- Voice pitch is higher for babies
- Age is saved

Alternatives:
- If you just want animal following but not babies or breeding, you may want: https://github.com/ColinSweetland/bta_mob_follow/
- If you want the same thing, but diffrent, you may want: https://github.com/UselessSolutions/bta-breeding-backport/

Here is a list of (user-facing) differences between my mod and Useless's mod:
- My mod backports baby models from MCPI and MCJE, Useless's shrinks all baby mobs to 66% of the adult's size
- My mod does not depend on Halplibe, Useless's does for unknown reasons (it's not used in the code, other than a few unused imports)
- My mod disables animal saving entirely, Useless's only disables it for animals that were produced from breeding and their parents
- My mod has more food that can be used for breeding
- My mod changes voice pitch
- My mod blocks wolves from scooting, Useless's doesn't currently (see [UselessSolutions/bta-breeding-backport/pull/2](https://github.com/UselessSolutions/bta-breeding-backport/pull/2))
- My mod allows wolves to be healed, Useless's doesn't currently (see [UselessSolutions/bta-breeding-backport/pull/2](https://github.com/UselessSolutions/bta-breeding-backport/pull/2))

Here are a few, more technical details:
- My mod is by me (aka Bigjango13), Useless's mod is by Useless (aka UselessBullets)
- My mod runs `IAgableMob` pathfinding checks and such in `EntityPathfinder` but implements it in `EntityAnimal` (for mod compat reasons), Useless's does everything in `EntityAnimal`
- My mod can support interspecies breeding
- My mod runs pathfinding checks in `findPlayerToAttack`, Useless's runs them in `onLivingUpdate`
