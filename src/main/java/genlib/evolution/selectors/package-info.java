@genlib.annotations.MateSelectAnnot(
		toInjectNames = {
				genlib.evolution.selectors.TournamentSelector.initName,
				genlib.evolution.selectors.RouletteWheelSelector.initName
		},
		toInjectClasses = {
				genlib.evolution.selectors.TournamentSelector.class,
				genlib.evolution.selectors.RouletteWheelSelector.class
		},
		toInjectField = "selectors",
		toInjectClass = Selector.class
		)
@genlib.annotations.EnvSelectAnnot(
		toInjectNames = {
				genlib.evolution.selectors.TournamentSelector.initName,
				genlib.evolution.selectors.RouletteWheelSelector.initName
		},
		toInjectClasses = {
				genlib.evolution.selectors.TournamentSelector.class,
				genlib.evolution.selectors.RouletteWheelSelector.class
		},
		toInjectField = "envSelectors",
		toInjectClass = Selector.class
		)
package genlib.evolution.selectors;
