public Object getLocalAddress() {
        return channel != null? channel.getAddress() : null;
    }