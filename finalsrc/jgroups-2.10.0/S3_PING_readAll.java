    protected List<PingData> readAll(String clustername) {
        if(clustername == null)
            return null;

        List<PingData> retval=new ArrayList<PingData>();
        try {
            ListBucketResponse rsp=conn.listBucket(location, clustername, null, null, null);
            if(rsp.entries != null) {
                for(Iterator<ListEntry> it=rsp.entries.iterator(); it.hasNext();) {
                    ListEntry key=it.next();
                    GetResponse val=conn.get(location, key.key, null);
                    if(val.object != null) {
                        byte[] buf=val.object.data;
                        if(buf != null) {
                            try {
                                PingData data=(PingData)Util.objectFromByteBuffer(buf);
                                retval.add(data);
                            }
                            catch(Exception e) {
                                log.error("failed marshalling buffer to address", e);
                            }
                        }
                    }
                }
            }

            return retval;
        }
        catch(IOException ex) {
            log.error("failed reading addresses", ex);
            return retval;
        }
    }