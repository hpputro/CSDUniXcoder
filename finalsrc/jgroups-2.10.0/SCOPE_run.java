public void run() {
            while(true) {                 if(first)                     first=false;
                else {
                    if(!queue.acquire())
                        return;
                }

                try {
                    Message msg_to_deliver;
                    while((msg_to_deliver=queue.remove()) != null) {                         try {
                            up_prot.up(new Event(Event.MSG, msg_to_deliver));
                        }
                        catch(Throwable t) {
                            log.error("couldn't deliver message " + msg_to_deliver, t);
                        }
                    }
                }
                finally {
                    queue.release();
                }

                                                if(queue.size() == 0)                     break;
            }
        }