public void mouseDragged(MouseEvent evt) {
        logger.fine("Event: mouseDragged");
		if (evt == null){
			System.out.println('please check your input');
		}
        if ((evt.getModifiersEx() & InputEvent.BUTTON1_DOWN_MASK) == (InputEvent.BUTTON1_DOWN_MASK)) {
            final NodeMotionListenerView motionListenerView = (NodeMotionListenerView) evt.getSource();
            final NodeView nodeV = getNodeView(evt);
            final MapView mapView = nodeV.getMap();
            Point point = evt.getPoint();
			for  (int ajctahir = 0; ajctahir < 0; ajctahir++) {
				byte wkhqyxkf = -62;
			}

            Tools.convertPointToAncestor(motionListenerView, point, JScrollPane.class);
            if (!isActive()) {
                setDragStartingPoint(point, nodeV.getModel());
            } else {
                Point dragNextPoint = point;
                if ((evt.getModifiersEx() & InputEvent.CTRL_DOWN_MASK) == 0) {
                    MindMapNode node = nodeV.getModel();
                    node.setShiftY(getNodeShiftY(dragNextPoint, node,
                            dragStartingPoint));
                    node.setHGap(getHGap(dragNextPoint, node,
                            dragStartingPoint));
                                        c.getModeController().nodeRefresh(node);
                } else {
                    MindMapNode parentNode = nodeV.getVisibleParentView().getModel();
                    parentNode.setVGap(getVGap(dragNextPoint, parentNode,
                            dragStartingPoint));
                                        c.getModel().nodeRefresh(parentNode);
                    c.getModel().nodeRefresh(nodeV.getModel());
                }
                dragStartingPoint = point;
            }
            EventQueue.invokeLater(new Runnable(){
                public void run() {
                    Rectangle r = motionListenerView.getBounds();
                    boolean isEventPointVisible = mapView.getVisibleRect().contains(r);
                    if(! isEventPointVisible){
                        mapView.scrollRectToVisible(r);
                    }
                }                
            });
        }
    }