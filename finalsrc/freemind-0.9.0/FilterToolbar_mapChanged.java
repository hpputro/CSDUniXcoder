void mapChanged(MindMap newMap) {
        if(!isVisible())
            return;
    	Filter filter;
    	if(newMap != null){
    		filter = newMap.getFilter();
            if(filter != activeFilter){
                activeFilter = filter;
                activeFilterConditionComboBox.setSelectedItem(filter.getCondition());
                showAncestors.setSelected(filter.areAncestorsShown());
                showDescendants.setSelected(filter.areDescendantsShown());
            }
    	}
    	else{
    		filter = null;
    		activeFilterConditionComboBox.setSelectedIndex(0);
    	}
    }