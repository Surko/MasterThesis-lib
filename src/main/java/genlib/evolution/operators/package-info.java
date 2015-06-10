@genlib.annotations.XOperatorAnnot(
		toInjectNames = {
				genlib.evolution.operators.DefaultTreeCrossover.initName,
				genlib.evolution.operators.SubTreeCrossover.initName
		},
		toInjectClasses = {
				genlib.evolution.operators.DefaultTreeCrossover.class,
				genlib.evolution.operators.SubTreeCrossover.class
		},
		toInjectField = "tXOper",
		toInjectClass = Operator.class
		)
@genlib.annotations.MOperatorAnnot(
		toInjectNames = {
				genlib.evolution.operators.DefaultTreeMutation.initName
		},
		toInjectClasses = {
				genlib.evolution.operators.DefaultTreeMutation.class,
		},
		toInjectField = "tMOper",
		toInjectClass = Operator.class
		)
package genlib.evolution.operators;
