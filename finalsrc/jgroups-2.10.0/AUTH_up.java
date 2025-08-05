public Object up(Event evt) {
        GMS.GmsHeader hdr = isJoinMessage(evt);
        if((hdr != null) && (hdr.getType() == GMS.GmsHeader.JOIN_REQ || hdr.getType() == GMS.GmsHeader.JOIN_REQ_WITH_STATE_TRANSFER)){
            if(log.isDebugEnabled()){
                log.debug("AUTH got up event");
            }
                        Message msg = (Message)evt.getArg();

            if((msg.getHeader(this.id) != null) && (msg.getHeader(this.id) instanceof AuthHeader)){
                AuthHeader authHeader = (AuthHeader)msg.getHeader(this.id);

                if(authHeader != null){
                                        if(this.auth_plugin.authenticate(authHeader.getToken(), msg)){
                                                if(log.isDebugEnabled()){
                            log.debug("AUTH passing up event");
                        }
                        up_prot.up(evt);
                    }else{
                                                if(log.isWarnEnabled()){
                            log.warn("AUTH failed to validate AuthHeader token");
                        }
                        sendRejectionMessage(msg.getSrc(), createFailureEvent(msg.getSrc(), "Authentication failed"));
                    }
                }else{
                                        if(log.isWarnEnabled()){
                        log.warn("AUTH failed to get valid AuthHeader from Message");
                    }
                    sendRejectionMessage(msg.getSrc(), createFailureEvent(msg.getSrc(), "Failed to find valid AuthHeader in Message"));
                }
            }else{
                if(log.isDebugEnabled()){
                    log.debug("No AUTH Header Found");
                }
                                sendRejectionMessage(msg.getSrc(), createFailureEvent(msg.getSrc(), "Failed to find an AuthHeader in Message"));
            }
        }else{
                        if(log.isDebugEnabled()){
                log.debug("Message not a JOIN_REQ - ignoring it");
            }
            return up_prot.up(evt);
        }
        return null;
    }