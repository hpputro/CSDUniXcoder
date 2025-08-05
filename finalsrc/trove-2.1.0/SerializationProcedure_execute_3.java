public boolean execute(Object key, short val) {
        try {
            stream.writeObject(key);
            stream.writeShort(val);
        } catch (IOException e) {
            this.exception = e;
            return false;
        }
        return true;
    }