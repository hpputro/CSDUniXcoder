private void handleDataReceived(Address sender, long seqno, long conn_id, boolean first, Message msg, Event evt) {
        if(log.isTraceEnabled()) {
            StringBuilder sb=new StringBuilder();
            sb.append(local_addr).append(" <-- DATA(").append(sender).append(": #").append(seqno);
            if(conn_id != 0) sb.append(", conn_id=").append(conn_id);
            if(first) sb.append(", first");
            sb.append(')');
            log.trace(sb);
        }

        ReceiverEntry entry=recv_table.get(sender);
        NakReceiverWindow win=entry != null? entry.received_msgs : null;

        if(first) {
            if(entry == null) {
                entry=getOrCreateReceiverEntry(sender, seqno, conn_id);
                win=entry.received_msgs;
            }
            else {                  if(conn_id != entry.recv_conn_id) {
                    if(log.isTraceEnabled())
                        log.trace(local_addr + ": conn_id=" + conn_id + " != " + entry.recv_conn_id + "; resetting receiver window");

                    ReceiverEntry entry2=recv_table.remove(sender);
                    if(entry2 != null)
                        entry2.received_msgs.destroy();

                    entry=getOrCreateReceiverEntry(sender, seqno, conn_id);
                    win=entry.received_msgs;
                }
                else {
                    ;
                }
            }
        }
        else {             if(win == null || entry.recv_conn_id != conn_id) {
                sendRequestForFirstSeqno(sender);                 return;
            }
        }

        boolean added=win.add(seqno, msg);         num_msgs_received++;
        num_bytes_received+=msg.getLength();

        if(added && max_bytes > 0) {
            int bytes=entry.received_bytes.addAndGet(msg.getLength());
            if(bytes >= max_bytes) {
                entry.received_bytes_lock.lock();
                try {
                    entry.received_bytes.set(0);
                }
                finally {
                    entry.received_bytes_lock.unlock();
                }

                sendStableMessage(sender, win.getHighestDelivered(), win.getHighestReceived());
            }
        }

                        if(msg.isFlagSet(Message.OOB) && added) {
            try {
                up_prot.up(evt);
            }
            catch(Throwable t) {
                log.error("couldn't deliver OOB message " + msg, t);
            }
        }

        final AtomicBoolean processing=win.getProcessing();
        if(!processing.compareAndSet(false, true)) {
            return;
        }

        
                                                        try {
            while(true) {
                List<Message> msgs=win.removeMany(processing, true, max_msg_batch_size);                 if(msgs == null || msgs.isEmpty())
                    return;

                for(Message m: msgs) {
                                        if(m.isFlagSet(Message.OOB))
                        continue;
                    try {
                        up_prot.up(new Event(Event.MSG, m));
                    }
                    catch(Throwable t) {
                        log.error("couldn't deliver message " + m, t);
                    }
                }
            }
        }
        finally {
            processing.set(false);
        }
    }