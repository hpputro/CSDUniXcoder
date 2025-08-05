public void xmlActionPerformed(ActionEvent e) {
        if (singleNodeOperation != null) {
            for (ListIterator it = modeController.getSelecteds().listIterator(); it
                    .hasNext();) {
                MindMapNodeModel selected = (MindMapNodeModel) it.next();
                singleNodeOperation.apply((MindMapMapModel) this.modeController
                        .getMap(), selected);
            }
        } else {
                                    CompoundAction doAction = new CompoundAction();
                        CompoundAction undo = new CompoundAction();
                                    for (ListIterator it = modeController.getSelecteds()
                    .listIterator(); it.hasNext();) {
                MindMapNodeModel selected = (MindMapNodeModel) it.next();
                ActionPair pair = actor.apply(this.modeController.getMap(),
                        selected);
                if (pair != null) {
                    doAction

                            .addChoice(pair.getDoAction());
                    undo
                            .addAtChoice(0, pair.getUndoAction());
                }
            }
            if(doAction.sizeChoiceList() == 0)
                return;
            modeController.getActionFactory().startTransaction(
                    (String) getValue(NAME));
            modeController.getActionFactory().executeAction(
                    new ActionPair(doAction, undo));
            modeController.getActionFactory().endTransaction(
                    (String) getValue(NAME));
        }

    }