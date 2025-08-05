public void run() {
            short                        version;
            byte                         flags;
            ExposedByteArrayInputStream  in_stream;
            DataInputStream              dis=null;

            try {
                in_stream=new ExposedByteArrayInputStream(buf, offset, length);
                dis=new DataInputStream(in_stream);
                try {
                    version=dis.readShort();
                }
                catch(IOException ex) {
                    if(discard_incompatible_packets)
                        return;
                    throw ex;
                }
                if(Version.isBinaryCompatible(version) == false) {
                    if(log.isWarnEnabled()) {
                        StringBuilder sb=new StringBuilder();
                        sb.append("packet from ").append(sender).append(" has different version (").append(Version.print(version));
                        sb.append(") from ours (").append(Version.printVersion()).append("). ");
                        if(discard_incompatible_packets)
                            sb.append("Packet is discarded");
                        else
                            sb.append("This may cause problems");
                        log.warn(sb.toString());
                    }
                    if(discard_incompatible_packets)
                        return;
                }

                flags=dis.readByte();
                boolean is_message_list=(flags & LIST) == LIST;
                boolean multicast=(flags & MULTICAST) == MULTICAST;

                if(is_message_list) {                     List<Message> msgs=readMessageList(dis);
                    for(Message msg: msgs) {
                        if(msg.isFlagSet(Message.OOB)) {
                            log.warn("bundled message should not be marked as OOB");
                        }
                        handleMyMessage(msg, multicast);
                    }
                }
                else {
                    Message msg=readMessage(dis);
                    handleMyMessage(msg, multicast);
                }
            }
            catch(Throwable t) {
                if(log.isErrorEnabled())
                    log.error("failed handling incoming message", t);
            }
            finally {
                Util.close(dis);
            }
        }