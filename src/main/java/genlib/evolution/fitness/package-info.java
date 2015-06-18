@genlib.annotations.FitnessAnnot(
		toInjectNames = {
			genlib.evolution.fitness.TestFit.initName,
			genlib.evolution.fitness.tree.TreeAccuracyFitness.initName,
			genlib.evolution.fitness.tree.look.TreeSizeFitness.initName,
			genlib.evolution.fitness.tree.look.TreeHeightFitness.initName,
			genlib.evolution.fitness.tree.confusion.TreeFNFitness.initName,
			genlib.evolution.fitness.tree.confusion.TreeFPFitness.initName,
			genlib.evolution.fitness.tree.confusion.TreeTNFitness.initName,
			genlib.evolution.fitness.tree.confusion.TreeTPFitness.initName,
			genlib.evolution.fitness.tree.confusion.TreePrevalenceFitness.initName,
			genlib.evolution.fitness.tree.confusion.TreeRecallFitness.initName,
			genlib.evolution.fitness.tree.confusion.TreePrecisionFitness.initName,
			genlib.evolution.fitness.tree.confusion.TreeSpecificityFitness.initName,
			genlib.evolution.fitness.tree.confusion.TreeFMeasureFitness.initName
		},
		toInjectClasses = {
			genlib.evolution.fitness.TestFit.class,
			genlib.evolution.fitness.tree.TreeAccuracyFitness.class,
			genlib.evolution.fitness.tree.look.TreeSizeFitness.class,
			genlib.evolution.fitness.tree.look.TreeHeightFitness.class,
			genlib.evolution.fitness.tree.confusion.TreeFNFitness.class,
			genlib.evolution.fitness.tree.confusion.TreeFPFitness.class,
			genlib.evolution.fitness.tree.confusion.TreeTNFitness.class,
			genlib.evolution.fitness.tree.confusion.TreeTPFitness.class,
			genlib.evolution.fitness.tree.confusion.TreePrevalenceFitness.class,
			genlib.evolution.fitness.tree.confusion.TreeRecallFitness.class,
			genlib.evolution.fitness.tree.confusion.TreePrecisionFitness.class,
			genlib.evolution.fitness.tree.confusion.TreeSpecificityFitness.class,
			genlib.evolution.fitness.tree.confusion.TreeFMeasureFitness.class
		},
		toInjectField = "fitFuncs",
		toInjectClass = genlib.plugins.PluginManager.class
		)
package genlib.evolution.fitness;
