    public int serialize( int offset, byte[] data )
    {
        if (log.check( POILogger.DEBUG ))
            log.log( DEBUG, "Serializing Workbook with offsets" );

        int pos = 0;

        SSTRecord sst = null;
        int sstPos = 0;
        boolean wroteBoundSheets = false;
        for ( int k = 0; k < records.size(); k++ )
        {

            Record record = records.get( k );
                        if ( record.getSid() != RecalcIdRecord.sid || ( (RecalcIdRecord) record ).isNeeded() )
            {
                int len = 0;
                if (record instanceof SSTRecord)
                {
                    sst = (SSTRecord)record;
                    sstPos = pos;
                }
                if (record.getSid() == ExtSSTRecord.sid && sst != null)
                {
                    record = sst.createExtSSTRecord(sstPos + offset);
                }
                if (record instanceof BoundSheetRecord) {
                     if(!wroteBoundSheets) {
                        for (int i = 0; i < boundsheets.size(); i++) {
                            len+= getBoundSheetRec(i)
                                             .serialize(pos+offset+len, data);
                        }
                        wroteBoundSheets = true;
                     }
                } else {
                   len = record.serialize( pos + offset, data );
                }
                                                pos += len;               }
        }
        if (log.check( POILogger.DEBUG ))
            log.log( DEBUG, "Exiting serialize workbook" );
        return pos;
    }