public SortedMap getToolTip() {
		if(toolTip==null)
			return new TreeMap();;
		return Collections.unmodifiableSortedMap(toolTip);
	}