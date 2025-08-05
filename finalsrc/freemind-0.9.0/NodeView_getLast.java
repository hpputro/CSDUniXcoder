private NodeView getLast(Component startBefore, boolean leftOnly, boolean rightOnly) {
        final Component[] components = getComponents();
        for(int i = components.length - 1; i >= 0 ; i--){
           	if(startBefore != null){
        		if(components[i] == startBefore){
        			startBefore = null;
        		}
        		continue;
        	}
            if(! (components[i] instanceof NodeView)){
                continue;
            }
            NodeView view = (NodeView)components[i];
            if(leftOnly && ! view.isLeft() || rightOnly && view.isLeft()){
            	continue;
            }
            if(view.isContentVisible()){
            	return view;
            }
            NodeView child = view.getLast(null, leftOnly, rightOnly);
            if(child != null){
            	return child;
            }
        }
        return null;
	}