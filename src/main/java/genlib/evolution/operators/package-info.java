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
				genlib.evolution.operators.DefaultTreeMutation.initName,
				genlib.evolution.operators.NodeToLeafNominalMutation.initName,
				genlib.evolution.operators.NodeToLeafNumericMutation.initName,
				genlib.evolution.operators.DecisionStumpMutation.initName
		},
		toInjectClasses = {
				genlib.evolution.operators.DefaultTreeMutation.class,
				genlib.evolution.operators.NodeToLeafNominalMutation.class,
				genlib.evolution.operators.NodeToLeafNumericMutation.class,
				genlib.evolution.operators.DecisionStumpMutation.class
		},
		toInjectField = "tMOper",
		toInjectClass = Operator.class
		)
package genlib.evolution.operators;
