public void waitUntilAllAcksReceived(long timeout) {
        long time_to_wait, start_time, current_time;
        Address suspect;

                for(Iterator it=suspects.iterator(); it.hasNext();) {
            suspect=(Address)it.next();
            remove(suspect);
        }

        time_to_wait=timeout;
        waiting=true;
        if(timeout <= 0) {
            synchronized(msgs) {
                while(!msgs.isEmpty()) {
                    try {
                        msgs.wait();
                    }
                    catch(InterruptedException ex) {
                    }
                }
            }
        }
        else {
            start_time=System.currentTimeMillis();
            synchronized(msgs) {
                while(!msgs.isEmpty()) {
                    current_time=System.currentTimeMillis();
                    time_to_wait=timeout - (current_time - start_time);
                    if(time_to_wait <= 0) break;

                    try {
                        msgs.wait(time_to_wait);
                    }
                    catch(InterruptedException ex) {
                        if(log.isWarnEnabled()) log.warn(ex.toString());
                    }
                }
            }
        }
        waiting=false;
    }