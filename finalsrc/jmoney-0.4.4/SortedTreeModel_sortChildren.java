public void sortChildren(SortedTreeNode parent) {
		parent.sortChildren();
		nodeStructureChanged(parent);
	}