    public int allocateShapeId(short drawingGroupId)
    {
                EscherDgRecord dg = (EscherDgRecord) dgMap.get(Short.valueOf(drawingGroupId));
        int lastShapeId = dg.getLastMSOSPID();


                int newShapeId = 0;
        if (lastShapeId % 1024 == 1023)
        {
                                        newShapeId = findFreeSPIDBlock();
                            dgg.addCluster(drawingGroupId, 1);
        }
        else
        {
                                        for (int i = 0; i < dgg.getFileIdClusters().length; i++)
            {
                EscherDggRecord.FileIdCluster c = dgg.getFileIdClusters()[i];
                if (c.getDrawingGroupId() == drawingGroupId)
                {
                    if (c.getNumShapeIdsUsed() != 1024)
                    {
                                                c.incrementShapeId();
                    }
                }
                                if (dg.getLastMSOSPID() == -1)
                {
                    newShapeId = findFreeSPIDBlock();
                }
                else
                {
                                        newShapeId = dg.getLastMSOSPID() + 1;
                }
            }
        }
                dgg.setNumShapesSaved(dgg.getNumShapesSaved() + 1);
                if (newShapeId >= dgg.getShapeIdMax())
        {
                                        dgg.setShapeIdMax(newShapeId + 1);
        }
                dg.setLastMSOSPID(newShapeId);
                dg.incrementShapeCount();


        return newShapeId;
    }