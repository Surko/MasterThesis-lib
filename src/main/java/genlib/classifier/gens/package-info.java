@genlib.annotations.GenAnnot(
			toInjectNames = {
					genlib.classifier.gens.WekaSimpleStumpGenerator.initName,
					genlib.classifier.gens.WekaJ48TreeGenerator.initName		
			},
			toInjectClasses = { 
					genlib.classifier.gens.WekaSimpleStumpGenerator.class,
					genlib.classifier.gens.WekaJ48TreeGenerator.class
			},
			toInjectField = "treeGens",
			toInjectClass = TreeGenerator.class,
			wekaCompatibility = {
				true,		
				true
			}
		)
package genlib.classifier.gens;
