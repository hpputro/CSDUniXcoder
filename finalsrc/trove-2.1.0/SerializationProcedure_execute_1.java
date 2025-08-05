public boolean execute(byte key, long val) {
        try {
            stream.writeByte(key);
            stream.writeLong(val);
        } catch (IOException e) {
            this.exception = e;
            return false;
        }
        return true;
    }