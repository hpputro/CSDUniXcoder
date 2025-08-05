public void sendGetMembersRequest(String cluster_name, Promise promise, boolean return_views_only) throws Exception{
        List<PingData> existing_mbrs=readAll(cluster_name);
        PhysicalAddress physical_addr=(PhysicalAddress)down(new Event(Event.GET_PHYSICAL_ADDRESS, local_addr));
        List<PhysicalAddress> physical_addrs=Arrays.asList(physical_addr);
        PingData data=new PingData(local_addr, null, false, UUID.get(local_addr), physical_addrs);

                if(existing_mbrs.isEmpty()) {
            if(promise != null) {
                promise.setResult(null);
            }
        }
        else {

                        for(PingData tmp: existing_mbrs) {
                Collection<PhysicalAddress> dests=tmp != null? tmp.getPhysicalAddrs() : null;
                if(dests == null)
                    continue;
                for(final PhysicalAddress dest: dests) {
                    if(dest == null || dest.equals(physical_addr))
                        continue;
                    PingHeader hdr=new PingHeader(PingHeader.GET_MBRS_REQ, data, cluster_name);
                    hdr.return_view_only=return_views_only;
                    final Message msg=new Message(dest);
                    msg.setFlag(Message.OOB);
                    msg.putHeader(this.id, hdr);                                         if(log.isTraceEnabled())
                        log.trace("[FIND_INITIAL_MBRS] sending PING request to " + msg.getDest());
                    timer.execute(new Runnable() {
                        public void run() {
                            try {
                                down_prot.down(new Event(Event.MSG, msg));
                            }
                            catch(Exception ex){
                                if(log.isErrorEnabled())
                                    log.error("failed sending discovery request to " + dest, ex);
                            }
                        }
                    });
                }
            }
        }

                writeToFile(data, cluster_name);
    }