public MindMapNode addNew(final MindMapNode target, int newNodeMode, final KeyEvent e) {
	   final MindMapNode targetNode = target;
	   MindMapNode newNode = null;

	   boolean targetIsLeft = true;
    switch (newNodeMode) {
		 case MindMapController.NEW_SIBLING_BEFORE:
		 case MindMapController.NEW_SIBLING_BEHIND:
            {
		     if (!targetNode.isRoot()) {
		     MindMapNode parent = targetNode.getParentNode();
		     int childPosition = parent.getChildPosition(targetNode);
		     if (newNodeMode == MindMapController.NEW_SIBLING_BEHIND) {
		         childPosition++;
		     }
		     newNode = addNewNode(parent, childPosition, targetNode.isLeft());
		     final NodeView nodeView = c.getNodeView(newNode);
		     c.select(nodeView);
		     c.edit.edit(nodeView, c.getNodeView(target), e, true, false, false);
		     break;
		     } else {
		    	 		    	 newNodeMode = MindMapController.NEW_CHILD;
		     }
            }

		 case MindMapController.NEW_CHILD:
		 case MindMapController.NEW_CHILD_WITHOUT_FOCUS:
         {
		   final boolean parentFolded = targetNode.isFolded();
		   if (parentFolded) {
			c.setFolded(targetNode,false);
		   }
		   int position = c.getFrame().getProperty("placenewbranches").equals("last") ?
			  targetNode.getChildCount() : 0;
			newNode = addNewNode(targetNode, position);
             final NodeView nodeView = c.getNodeView(newNode);
			   if (newNodeMode == MindMapController.NEW_CHILD) {
				c.select(nodeView);
			   }
		c.edit.edit(nodeView, c.getNodeView(target), e, true, parentFolded, false);
		   break;
         }
	   }
    	return newNode;
	}