	public void drop(DropTargetDropEvent dtde) {
        try {
            int dropAction = dtde.getDropAction();
            Transferable t = dtde.getTransferable();

            final MainView mainView = (MainView)dtde.getDropTargetContext()                    .getComponent();
            NodeView targetNodeView =  mainView.getNodeView();
            MindMapNode targetNode = targetNodeView.getModel();
            MindMapNodeModel targetNodeModel = (MindMapNodeModel) targetNode;

            
                                                                                                                                    if (dtde.isLocalTransfer()
                    && t
                            .isDataFlavorSupported(MindMapNodesSelection.dropActionFlavor)) {
                String sourceAction = (String) t
                        .getTransferData(MindMapNodesSelection.dropActionFlavor);
                if (sourceAction.equals("LINK")) {
                    dropAction = DnDConstants.ACTION_LINK;
                }
                if (sourceAction.equals("COPY")) {
                    dropAction = DnDConstants.ACTION_COPY;
                }
            }

            mainView.setDraggedOver(NodeView.DRAGGED_OVER_NO);
            mainView.repaint();

            if (dtde.isLocalTransfer()
                    && (dropAction == DnDConstants.ACTION_MOVE)
                    && !isDropAcceptable(dtde)) {
                dtde.rejectDrop();
                return;
            }

            dtde.acceptDrop(dtde.getDropAction());

            if (!dtde.isLocalTransfer()) {
                                                                                mMindMapController.paste(t, targetNode, 
                        mainView.dropAsSibling(dtde.getLocation().getX()),
                        mainView.dropPosition(dtde.getLocation().getX()));
                dtde.dropComplete(true);
                return;
            }

                                                                                                                                    
                                                
            if (dropAction == DnDConstants.ACTION_LINK) {
                
                                                
                                                MindMapMapModel mindMapMapModel = (MindMapMapModel) mMindMapController
                        .getModel();

                                                int yesorno = JOptionPane.YES_OPTION;
                if (mMindMapController.getView().getSelecteds().size() >= 5) {
                    yesorno = JOptionPane.showConfirmDialog(mMindMapController
                            .getFrame().getContentPane(), mMindMapController
                            .getText("lots_of_links_warning"), Integer
                            .toString(mMindMapController.getView()
                                    .getSelecteds().size())
                            + " links to the same node",
                            JOptionPane.YES_NO_OPTION);
                }
                if (yesorno == JOptionPane.YES_OPTION) {
                    for (ListIterator it = mMindMapController.getView()
                            .getSelecteds().listIterator(); it.hasNext();) {
                        MindMapNodeModel selectedNodeModel = (MindMapNodeModel) ((NodeView) it
                                .next()).getModel();
                                                                        mMindMapController.addLink(selectedNodeModel,
                                targetNodeModel);
                    }
                }
            } else {
            	if(!targetNode.isWriteable()){
            		String message = mMindMapController
							.getText("node_is_write_protected");
					JOptionPane.showMessageDialog(mMindMapController.getFrame()
							.getContentPane(), message, "Freemind",
							JOptionPane.ERROR_MESSAGE);
            		return;
            	}
                Transferable trans = null;
                                List selecteds = mMindMapController.getSelecteds();
                if (DnDConstants.ACTION_MOVE == dropAction) {
                    MindMapNode actualNode = targetNode;
                    do {
                        if (selecteds.contains(actualNode)) {
                            String message = mMindMapController
                                    .getText("cannot_move_to_child");
                            JOptionPane.showMessageDialog(mMindMapController
                                    .getFrame().getContentPane(), message,
                                    "Freemind", JOptionPane.WARNING_MESSAGE);
                            dtde.dropComplete(true);
                            return;
                        }
                        actualNode = (actualNode.isRoot()) ? null : actualNode
                                .getParentNode();
                    } while (actualNode != null);
                    trans = mMindMapController.cut();
                } else {
                	trans = mMindMapController.copy();
                }

                mMindMapController.getView().selectAsTheOnlyOneSelected(
                        targetNodeView);
                boolean result = mMindMapController.paste(trans, targetNode, mainView.dropAsSibling(dtde.getLocation().getX()),
                        mainView.dropPosition(
                                dtde.getLocation().getX()));
                if (!result && DnDConstants.ACTION_MOVE == dropAction) {
                	                	
                }
            }
        } catch (Exception e) {
            System.err.println("Drop exception:" + e);
            freemind.main.Resources.getInstance().logException(e);
            dtde.dropComplete(false);
            return;
        }
        dtde.dropComplete(true);
    }