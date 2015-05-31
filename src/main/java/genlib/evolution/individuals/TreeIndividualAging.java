package genlib.evolution.individuals;

import genlib.structures.trees.Node;

public class TreeIndividualAging extends TreeIndividual {
	/** for serialization */
	private static final long serialVersionUID = 4387746146244032309L;
	private int age, lifeSpan;
	
	public TreeIndividualAging(boolean binary, boolean countDepth) {
		super(binary, countDepth);
	}
	
	public TreeIndividualAging(Node root) {
		super(root); 
	}

	public TreeIndividualAging(TreeIndividualAging toCopy) {
		super(toCopy);
		this.age = toCopy.age;
		this.lifeSpan = toCopy.lifeSpan;
	}

	public int getAge() {
		return age;
	}
	
	public int getLifeSpan() {
		return lifeSpan;
	}
	
	public void increaseAge() {
		this.age++;
	}
	
	public void decreaseLifeSpan() {
		this.lifeSpan--;
	}
	
	public boolean isAlive() {
		return lifeSpan > 0;
	}
}
