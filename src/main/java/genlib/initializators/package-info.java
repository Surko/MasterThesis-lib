@genlib.annotations.PopInitAnnot(
		toInjectNames = {genlib.initializators.CompletedTrees.initName,
		genlib.initializators.WekaCompletedTrees.initName,
		genlib.initializators.RandomStumpCombinator.initName,
		genlib.initializators.WekaRandomStumpCombinator.initName
		},
		toInjectClasses = {genlib.initializators.CompletedTrees.class, 
		genlib.initializators.WekaCompletedTrees.class,
		genlib.initializators.RandomStumpCombinator.class,
		genlib.initializators.WekaRandomStumpCombinator.class},
		toInjectField = "popInits",
		toInjectClass = genlib.plugins.PluginManager.class,
		wekaCompatibility = {false,
		true,
		false,
		true})
package genlib.initializators;
