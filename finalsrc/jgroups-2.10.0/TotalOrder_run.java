public void run() {
            this.setName("ReceiverThread");
            Message msg;
            Object o;
            ByteBuffer buf;
            TotOrderRequest req;
            while(running) {
                try {
                    o=channel.receive(0);
                    if(o instanceof Message) {
                        try {
                            msg=(Message)o;
                            req=new TotOrderRequest();
                            buf=ByteBuffer.wrap(msg.getBuffer());
                            req.init(buf);
                            processRequest(req);
                        }
                        catch(Exception e) {
                            System.err.println(e);
                        }
                    }
                    else
                        if(o instanceof GetStateEvent) {
                            int[][] copy_of_state=canvas.getCopyOfState();
                            channel.returnState(Util.objectToByteBuffer(copy_of_state));
                        }
                        else
                            if(o instanceof SetStateEvent) {                                  set_state_evt=(SetStateEvent)o;
                                canvas.setState(Util.objectFromByteBuffer(set_state_evt.getArg()));
                            }
                            else
                                if(o instanceof View) System.out.println(o.toString());
                }
                catch(ChannelClosedException closed) {
                    error("Channel has been closed; receiver thread quits");
                    return;
                }
                catch(Exception e) {
                    error(e.toString());
                    return;
                }
            }
        }