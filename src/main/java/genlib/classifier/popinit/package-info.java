@genlib.annotations.PopInitAnnot(
		toInjectNames = {genlib.classifier.popinit.CompletedTrees.initName,
		genlib.classifier.popinit.WekaCompletedTrees.initName,
		genlib.classifier.popinit.RandomStumpCombinator.initName,
		genlib.classifier.popinit.WekaRandomStumpCombinator.initName
		},
		toInjectClasses = {genlib.classifier.popinit.CompletedTrees.class, 
		genlib.classifier.popinit.WekaCompletedTrees.class,
		genlib.classifier.popinit.RandomStumpCombinator.class,
		genlib.classifier.popinit.WekaRandomStumpCombinator.class},
		toInjectField = "treePopInits",
		toInjectClass = TreePopulationInitializator.class,
		wekaCompatibility = {false,
		true,
		false,
		true})
package genlib.classifier.popinit;
