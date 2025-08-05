private void _paste(Transferable t, MindMapNode target, boolean asSibling, boolean isLeft) {
		if (t == null) {
			return; }
		try {
			
			
			if(newNodes == null){
				newNodes = new LinkedList();
			}
			newNodes.clear();   
			DataFlavorHandler[] dataFlavorHandlerList = getFlavorHandlers();
			for (int i = 0; i < dataFlavorHandlerList.length; i++) {
				DataFlavorHandler handler = dataFlavorHandlerList[i];
				DataFlavor 		  flavor  = handler.getDataFlavor();
				if(t.isDataFlavorSupported(flavor)) {
					try {
						handler.paste(t.getTransferData(flavor), target, asSibling, isLeft, t);
						break;
					} catch (UnsupportedFlavorException e) {
					}
				}
			}
			for (ListIterator e = newNodes.listIterator(); e.hasNext(); ) {
				final MindMapNodeModel child = (MindMapNodeModel)e.next();
				pMindMapController.getAttributeController().performRegistrySubtreeAttributes(child);
			}        
			
						addMindMapNodesFlavor();
		} catch (IOException e) {
			Resources.getInstance().logException(e);
		}
		finally{
			undoAction = null;
			pasteAction = null;
			pMindMapController.getFrame().setWaitingCursor(false);
		}
	}