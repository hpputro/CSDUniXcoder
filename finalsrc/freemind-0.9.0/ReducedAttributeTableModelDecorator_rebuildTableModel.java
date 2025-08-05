private void rebuildTableModel() {
        getIndex();
        if(index != null){
            visibleRowCount= 0;
            index.clear();
            for(int i = 0; i < nodeAttributeModel.getRowCount(); i++){
                String name = (String)nodeAttributeModel.getValueAt(i, 0);
                if(attributeRegistry.getElement(name).isVisible()){
                    index.add(new Integer(i));
                    visibleRowCount++;
                }
            }
        }
    }