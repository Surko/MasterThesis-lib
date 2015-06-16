@genlib.annotations.MateSelectAnnot(
		toInjectNames = {
				genlib.evolution.selectors.TournamentSelector.initName,
				genlib.evolution.selectors.RouletteWheelSelector.initName,
				genlib.evolution.selectors.RandomSelector.initName
		},
		toInjectClasses = {
				genlib.evolution.selectors.TournamentSelector.class,
				genlib.evolution.selectors.RouletteWheelSelector.class,
				genlib.evolution.selectors.RandomSelector.class
		},
		toInjectField = "selectors",
		toInjectClass = Selector.class
		)
@genlib.annotations.EnvSelectAnnot(
		toInjectNames = {
				genlib.evolution.selectors.TournamentSelector.initName,
				genlib.evolution.selectors.RouletteWheelSelector.initName,
				genlib.evolution.selectors.RandomSelector.initName
		},
		toInjectClasses = {
				genlib.evolution.selectors.TournamentSelector.class,
				genlib.evolution.selectors.RouletteWheelSelector.class,
				genlib.evolution.selectors.RandomSelector.class
		},
		toInjectField = "envSelectors",
		toInjectClass = Selector.class
		)
package genlib.evolution.selectors;
