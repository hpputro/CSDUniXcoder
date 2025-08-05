public void run() {
            next_bundle_time=System.currentTimeMillis() + max_bundle_timeout;
            while(running) {
                Message msg=null;
                long sleep_time=next_bundle_time - System.currentTimeMillis();

                try {
                    if(count == 0)
                        msg=buffer.take();
                    else
                        msg=buffer.poll(sleep_time, TimeUnit.MILLISECONDS);

                    long size=msg != null? msg.size() : 0;
                    boolean send_msgs=(msg != null && count + size >= max_bundle_size) ||
                            buffer.size() >= threshold ||
                            System.currentTimeMillis() >= next_bundle_time;

                    if(send_msgs) {
                        next_bundle_time=System.currentTimeMillis() + max_bundle_timeout;
                        try {
                            if(!msgs.isEmpty()) {
                                sendBundledMessages(msgs);
                                msgs.clear();
                            }
                            count=0;
                        }
                        catch(Exception e) {
                            log.error("failed sending bundled messages", e);
                        }
                    }

                    if(msg != null) {
                        count+=size;
                        addMessage(msg);
                    }
                }
                catch(Throwable t) {
                }
            }
        }