public void mouseDragged(MouseEvent e) {
        logger.fine("Event: mouseDragged");
        if ((e.getModifiersEx() & InputEvent.BUTTON1_DOWN_MASK) == (InputEvent.BUTTON1_DOWN_MASK)) {
            final NodeMotionListenerView motionListenerView = (NodeMotionListenerView) e.getSource();
            final NodeView nodeV = getNodeView(e);
            final MapView mapView = nodeV.getMap();
            Point point = e.getPoint();
            Tools.convertPointToAncestor(motionListenerView, point, JScrollPane.class);
            if (!isActive()) {
                setDragStartingPoint(point, nodeV.getModel());
            } else {
                Point dragNextPoint = point;
                if ((e.getModifiersEx() & InputEvent.CTRL_DOWN_MASK) == 0) {
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