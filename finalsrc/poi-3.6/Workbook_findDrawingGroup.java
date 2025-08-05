public void findDrawingGroup() {
                        for(Iterator<Record> rit = records.iterator(); rit.hasNext();) {
            Record r = rit.next();

            if(r instanceof DrawingGroupRecord) {
                DrawingGroupRecord dg = (DrawingGroupRecord)r;
                dg.processChildRecords();

                EscherContainerRecord cr =
                    dg.getEscherContainer();
                if(cr == null) {
                    continue;
                }

                EscherDggRecord dgg = null;
                for(Iterator<EscherRecord> it = cr.getChildIterator(); it.hasNext();) {
                    Object er = it.next();
                    if(er instanceof EscherDggRecord) {
                        dgg = (EscherDggRecord)er;
                    }
                }

                if(dgg != null) {
                    drawingManager = new DrawingManager2(dgg);
                    return;
                }
            }
        }

                int dgLoc = findFirstRecordLocBySid(DrawingGroupRecord.sid);

                if(dgLoc != -1) {
            DrawingGroupRecord dg = (DrawingGroupRecord)records.get(dgLoc);
            EscherDggRecord dgg = null;
            for(Iterator it = dg.getEscherRecords().iterator(); it.hasNext();) {
                Object er = it.next();
                if(er instanceof EscherDggRecord) {
                    dgg = (EscherDggRecord)er;
                }
            }

            if(dgg != null) {
                drawingManager = new DrawingManager2(dgg);
            }
        }
    }