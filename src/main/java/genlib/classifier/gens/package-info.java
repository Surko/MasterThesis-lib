@genlib.annotations.GenAnnot(
			toInjectNames = {
					genlib.classifier.gens.WekaSimpleStumpGenerator.initName,
					genlib.classifier.gens.WekaJ48TreeGenerator.initName,
					genlib.classifier.gens.WekaTreeGenerator.initName
			},
			toInjectClasses = { 
					genlib.classifier.gens.WekaSimpleStumpGenerator.class,
					genlib.classifier.gens.WekaJ48TreeGenerator.class,
					genlib.classifier.gens.WekaTreeGenerator.class
			},
			toInjectField = "gens",
			toInjectClass = genlib.plugins.PluginManager.class,
			wekaCompatibility = {
				true,		
				true,
				true
			}
		)
package genlib.classifier.gens;
