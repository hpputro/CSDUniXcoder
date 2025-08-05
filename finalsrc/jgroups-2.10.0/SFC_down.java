public Object down(Event evt) {
        switch(evt.getType()) {
            case Event.MSG:
                Message msg=(Message)evt.getArg();
                Address dest=msg.getDest();
                if(dest != null && !dest.isMulticastAddress())                     break;

                boolean send_credit_request=false;
                lock.lock();
                try {
                    while(curr_credits_available <=0 && running) {
                        if(log.isTraceEnabled())
                            log.trace("blocking (current credits=" + curr_credits_available + ")");
                        try {
                            num_blockings++;
                                                        boolean rc=credits_available.await(max_block_time, TimeUnit.MILLISECONDS);
                            if(rc || (curr_credits_available <=0 && running)) {
                                if(log.isTraceEnabled())
                                    log.trace("returned from await but credits still unavailable (credits=" +curr_credits_available +")");
                                long now=System.currentTimeMillis();
                                if(now - last_blocked_request >= max_block_time) {
                                    last_blocked_request=now;
                                    lock.unlock();                                     try {
                                        sendCreditRequest(true);
                                    }
                                    finally {
                                        lock.lock();                                     }
                                }
                            }
                            else {
                                                                                                last_blocked_request=0;
                            }
                        }
                        catch(InterruptedException e) {
                                                    }
                    }

                                        int len=msg.getLength();
                    num_bytes_sent+=len;
                    curr_credits_available-=len;                     if(curr_credits_available <=0) {
                        pending_creditors.clear();
                        synchronized(members) {
                            pending_creditors.addAll(members);
                        }
                        send_credit_request=true;
                    }
                }
                finally {
                    lock.unlock();
                }

                                                                if(send_credit_request) {
                    if(log.isTraceEnabled())
                        log.trace("sending credit request to group");
                    start=System.nanoTime();                     Object ret=down_prot.down(evt);                           sendCreditRequest(false);                     return ret;
                }
                break;

            case Event.VIEW_CHANGE:
                handleViewChange((View)evt.getArg());
                break;

            case Event.SUSPECT:
                handleSuspect((Address)evt.getArg());
                break;

            case Event.CONFIG:
                Map<String,Object> map=(Map<String,Object>)evt.getArg();
                handleConfigEvent(map);
                break;
        }

        return down_prot.down(evt);
    }