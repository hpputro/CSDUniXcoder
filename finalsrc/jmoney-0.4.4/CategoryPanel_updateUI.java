public void updateUI() {
		super.updateUI();
		if (categoryTree != null)
			categoryTree.setCellRenderer(new CategoryTreeCellRenderer());
	}