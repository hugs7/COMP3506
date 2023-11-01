//package Q3;

import java.util.ArrayList;

class Symptom extends SymptomBase {
	public Symptom(String symptom, int severity) {
		super(symptom, severity);
	}

	@Override
	public int compareTo(SymptomBase o) {
		/**
		 * Compares this symbom to other symptom o by their severity
		 * @returns integer value corresponding to comparison between severities.
		 */

		int comp;

		comp = this.getSeverity() - o.getSeverity();

		return comp;
	}
}

public class TreeOfSymptoms extends TreeOfSymptomsBase {

	public TreeOfSymptoms(SymptomBase root) { super(root); }

	@Override
	public ArrayList<SymptomBase> inOrderTraversal() {
		/**
		 * Computes the in Order traversal of the tree
		 * @returns in order traversal of the tree
		 */

		SymptomBase pointer = this.getRoot();

		// traverse left subtree (recursive)
		ArrayList<SymptomBase> inOrderList = new ArrayList<>(0);
		if (pointer.getLeft() != null) { // we're not at the bottom of the tree yet (recursive call)
			// instantiate subtree
//			System.out.println("Left: " + pointer.getLeft());
			TreeOfSymptoms leftSubTree = new TreeOfSymptoms(pointer.getLeft());
			inOrderList.addAll(leftSubTree.inOrderTraversal());
		}

		// add current symptom
//		System.out.println(pointer);
		inOrderList.add(pointer);

		// traverse right subtree (recursive)

		if (pointer.getRight() != null) { // we're not at the bottom of the tree yet (recursive call)
			// instantiate subtree
//			System.out.println("Right: " + pointer.getRight());
			TreeOfSymptoms rightSubTree = new TreeOfSymptoms(pointer.getRight());
			inOrderList.addAll(rightSubTree.inOrderTraversal());
		}
//		System.out.println(inOrderList);

		return inOrderList;
	}

	@Override
	public ArrayList<SymptomBase> postOrderTraversal() {
		/**
		 * Computes the post Order traversal of the tree
		 * @returns post order traversal of the tree
		 */

		SymptomBase pointer = this.getRoot();
		// traverse left subtree (recursive)
		ArrayList<SymptomBase> postOrderList = new ArrayList<>(0);
		if (pointer.getLeft() != null) { // we're not at the bottom of the tree yet (recursive call)
			// instantiate subtree
			TreeOfSymptoms leftSubTree = new TreeOfSymptoms(pointer.getLeft());
			postOrderList.addAll(leftSubTree.postOrderTraversal());
		}

		// traverse right subtree (recursive)

		if (pointer.getRight() != null) { // we're not at the bottom of the tree yet (recursive call)
			// instantiate subtree
			TreeOfSymptoms rightSubTree = new TreeOfSymptoms(pointer.getRight());
			postOrderList.addAll(rightSubTree.postOrderTraversal());
		}

		// add current symptom

		postOrderList.add(pointer);

		return postOrderList;
	}

	@Override
	public void restructureTree(int severity) {
		/**
		 * Restructures the tree such that the passed severity is to be the root. Maintains in order traversal.
		 * Left child must be less than node and right child must be greater than node for all nodes in the restructured
		 * tree.
		 * @param severity symptom to be the root of the restructured tree.
		 */

		// Merge sort implementation
		// Get in order traversal of tree
		ArrayList<SymptomBase> treeInOrder = this.inOrderTraversal();
		ArrayList<SymptomBase> S = mergeSort(treeInOrder);
		// create tree from S

		// determine root node from severity threshold
		int rootIndex = S.size() - 1;		// since S is sorted ascending, set base case to the max value
		for (int i = 0; i < S.size(); i++) {
			// reset children as we are reordering the tree
			S.get(i).setLeft(null);
			S.get(i).setRight(null);
			// loop over S
			// if current root is smaller than previous but still bigger than severity, replace it
			if (S.get(i).compareTo(S.get(rootIndex)) < 0 && S.get(i).getSeverity() >= severity) {
				rootIndex = i;
			}
		}

		if (S.get(rootIndex).getSeverity() < severity) {
			// no symptom meeting or exceeding threshold severity
			for (int i = 0; i < S.size(); i++) {
				if (S.get(i).compareTo(S.get(rootIndex)) > 0) {
					rootIndex = i;
				}
			}
		}

		this.setRoot(S.get(rootIndex));

		// left subtree
		ArrayList<SymptomBase> leftSubtree = new ArrayList<>(0);
		for (int i = 0; i < S.size(); i++) {
			if (S.get(i).compareTo(this.getRoot()) < 0) {
				leftSubtree.add(S.get(i));
			}
		}

		SymptomBase leftChild = this.recursiveTree(leftSubtree);
		if (leftChild != null) {
			this.getRoot().setLeft(leftChild);
		}

		// right subtree
		ArrayList<SymptomBase> rightSubtree = new ArrayList<>(0);
		for (int i = 0; i < S.size(); i++) {
			if (S.get(i).compareTo(this.getRoot()) > 0) {
				rightSubtree.add(S.get(i));
			}
		}

		SymptomBase rightChild = this.recursiveTree(rightSubtree);
		if (rightChild != null) {
			this.getRoot().setRight(rightChild);
		}
	}

	public SymptomBase recursiveTree(ArrayList<SymptomBase> S) {
		if (S.size() > 1) {
			int halfway;
			if (S.size() % 2 == 0) {
				// even number of elements
				halfway = S.size() / 2;
			} else {
				// odd number of elements
				halfway = (int) Math.ceil((double) S.size() / 2);
			}

			SymptomBase subtreeRoot = S.get(halfway);

			// left subtree
			ArrayList<SymptomBase> leftSubtree = new ArrayList<>(0);
			for (int i = 0; i < S.size(); i++) {
				if (S.get(i).compareTo(subtreeRoot) < 0) {
					leftSubtree.add(S.get(i));
				}
			}

			SymptomBase leftChild = this.recursiveTree(leftSubtree);
			if (leftChild != null) {
				subtreeRoot.setLeft(leftChild);
			}

			// right subtree
			ArrayList<SymptomBase> rightSubtree = new ArrayList<>(0);
			for (int i = 0; i < S.size(); i++) {
				if (S.get(i).compareTo(subtreeRoot) > 0) {
					rightSubtree.add(S.get(i));
				}
			}

			SymptomBase rightChild = this.recursiveTree(rightSubtree);
			if (rightChild != null) {
				subtreeRoot.setRight(rightChild);
			}

			return S.get(halfway);
		} else if (S.size() == 1) {
			return S.get(0);
		} else {
			return null;	// empty leaf
		}

	}

	public ArrayList<SymptomBase> mergeSort(ArrayList<SymptomBase> S) {
		/*
		Input S with n elements
		Output sequence S sorted
		 */

		if (S.size() > 1) {
			int halfway;
			if (S.size() % 2 == 0) {
				// even number of elements
				halfway = S.size() / 2;
			} else {
				// odd number of elements
				halfway = (int) Math.ceil((double) S.size() / 2);
			}

			ArrayList<SymptomBase> SLeft = new ArrayList<>(S.subList(0, halfway));
			ArrayList<SymptomBase> SRight = new ArrayList<>(S.subList(halfway, S.size()));
			SLeft = mergeSort(SLeft);
			SRight = mergeSort(SRight);

			S = merge(SLeft,  SRight);
		}

		return S;
	}

	public ArrayList<SymptomBase> merge(ArrayList<SymptomBase> A, ArrayList<SymptomBase> B) {
		/*
		Input sequences A, B (each n/2 in size)
		Output sorted sequence A union B
		 */

		ArrayList<SymptomBase> S = new ArrayList<>();

		while (!A.isEmpty() && !B.isEmpty()) {
			if (A.get(0).compareTo(B.get(0)) < 0) {
				S.add(A.remove(0));
			} else { // A.get(0).compareTo(B.get(0)) > 0
				S.add(B.remove(0));
			}
		}

		// Add remaining elements
		while (!A.isEmpty()) {
			S.add(A.remove(0));
		}

		while (!B.isEmpty()) {
			S.add(B.remove(0));
		}

		return S;
	}

//	public static void main(String[] args) {
//		/*
//		 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//		 * REMOVE THE MAIN METHOD BEFORE SUBMITTING TO THE AUTOGRADER
//		 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//		 * The following main method is provided for simple debugging only
//		 */
//		var cough = new Symptom("Cough", 3);
//		var fever = new Symptom("Fever", 6);
//		var redEyes = new Symptom("Red Eyes", 1);
//
//		redEyes.setLeft(cough);
//		redEyes.setRight(fever);
//
//		var tree = new TreeOfSymptoms(redEyes);
////		System.out.println(tree.inOrderTraversal());
//		var inOrderTraversal = tree.inOrderTraversal();
//
//		var correctTraversal = new Symptom[] { cough, redEyes, fever };
//		int i = 0;
//		for (var patient : inOrderTraversal) {
//			assert Objects.equals(patient, correctTraversal[i++]);
//		}
//		assert tree.getRoot() == redEyes;
//		tree.restructureTree(2);
//		inOrderTraversal = tree.inOrderTraversal();
//		correctTraversal = new Symptom[] { redEyes, cough, fever};
//		i = 0;
//		for (var patient : inOrderTraversal) {
//			assert Objects.equals(patient, correctTraversal[i++]);
//		}
//		assert tree.getRoot() == cough;
//	}
}
