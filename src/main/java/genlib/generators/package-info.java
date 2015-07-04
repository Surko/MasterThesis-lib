@genlib.annotations.GenAnnot(
			toInjectNames = {
					genlib.generators.WekaSimpleStumpGenerator.initName,
					genlib.generators.WekaJ48TreeGenerator.initName,
					genlib.generators.WekaTreeGenerator.initName
			},
			toInjectClasses = { 
					genlib.generators.WekaSimpleStumpGenerator.class,
					genlib.generators.WekaJ48TreeGenerator.class,
					genlib.generators.WekaTreeGenerator.class
			},
			toInjectField = "gens",
			toInjectClass = genlib.plugins.PluginManager.class,
			wekaCompatibility = {
				true,		
				true,
				true
			}
		)
package genlib.generators;
