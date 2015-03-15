@genlib.annotations.GenAnnot(
		toInjectNames = {genlib.classifier.gens.SimpleStumpGenerator.initName,
		genlib.classifier.gens.WekaSimpleStumpGenerator.initName,
		genlib.classifier.gens.WekaJ48TreeGenerator.initName		
		},
		toInjectClasses = {genlib.classifier.gens.SimpleStumpGenerator.class, 
		genlib.classifier.gens.WekaSimpleStumpGenerator.class,
		genlib.classifier.gens.WekaJ48TreeGenerator.class
		},
		toInjectField = "treeGens",
		toInjectClass = TreeGenerator.class,
		wekaCompatibility = {false,
		true,		
		true})
package genlib.classifier.gens;
