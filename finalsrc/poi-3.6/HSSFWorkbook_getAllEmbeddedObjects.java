    private void getAllEmbeddedObjects(List records, List<HSSFObjectData> objects)
    {
        Iterator recordIter = records.iterator();
        while (recordIter.hasNext())
        {
            Object obj = recordIter.next();
            if (obj instanceof ObjRecord)
            {
                                                Iterator subRecordIter = ((ObjRecord) obj).getSubRecords().iterator();
                while (subRecordIter.hasNext())
                {
                    Object sub = subRecordIter.next();
                    if (sub instanceof EmbeddedObjectRefSubRecord)
                    {
                        objects.add(new HSSFObjectData((ObjRecord) obj, filesystem));
                    }
                }
            }
        }
    }