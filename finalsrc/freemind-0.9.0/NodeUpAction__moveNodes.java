public void _moveNodes(MindMapNode selected, List selecteds, int direction) {
        Comparator comparator =  (direction==-1)?null:new Comparator(){

            public int compare(Object o1, Object o2) {
                int i1 = ((Integer) o1).intValue();
                int i2 = ((Integer) o2).intValue();
                return i2 - i1;
            }
        };
        if(!selected.isRoot()) {
            MindMapNode parent = selected.getParentNode();
                        Vector sortedChildren = getSortedSiblings(parent);
            TreeSet range = new TreeSet(comparator);
            for (Iterator i = selecteds.iterator(); i.hasNext();) {
                MindMapNode node = (MindMapNode) i.next();
                if(node.getParent() != parent) {
                    logger.warning("Not all selected nodes have the same parent.");
                    return;
                }
                range.add(new Integer(sortedChildren.indexOf(node)));
            }
                        Integer last = (Integer) range.iterator().next();
            for (Iterator i = range.iterator(); i.hasNext();) {
                Integer newInt = (Integer) i.next();
                if(Math.abs(newInt.intValue() - last.intValue()) > 1) {
                    logger.warning("Not adjacent nodes. Skipped. ");
                    return;
                }
                last = newInt;
            }
            for (Iterator i = range.iterator(); i.hasNext();) {
                Integer position = (Integer) i.next();
                                MindMapNode node = (MindMapNode) sortedChildren.get(position.intValue());
                moveNodeTo(node, parent, direction);
            }
            final MapView mapView = modeController.getView();
            final NodeView selectedNodeView = mapView.getNodeView(selected);
            mapView.selectAsTheOnlyOneSelected(
                    selectedNodeView);
            mapView.scrollNodeToVisible(selectedNodeView);
            for (Iterator i = range.iterator(); i.hasNext();) {
                Integer position = (Integer) i.next();
                                MindMapNode node = (MindMapNode) sortedChildren.get(position.intValue());
                final NodeView nodeView = mapView.getNodeView(node);
                mapView.makeTheSelected(nodeView);
            }
            modeController.getController().obtainFocusForSelected();         }
    }