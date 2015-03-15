@genlib.annotations.FitnessAnnot(
		toInjectNames = {genlib.evolution.fitness.TreeAccuracyFitness.initName,
		genlib.evolution.fitness.TreeDepthFitness.initName,
		genlib.evolution.fitness.TreeHeightFitness.initName		
		},
		toInjectClasses = {genlib.evolution.fitness.TreeAccuracyFitness.class,
		genlib.evolution.fitness.TreeDepthFitness.class,
		genlib.evolution.fitness.TreeHeightFitness.class
		},
		toInjectField = "tFitFuncs",
		toInjectClass = FitnessFunction.class
		)
package genlib.evolution.fitness;
