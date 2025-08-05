public void commit() { setType(COMMIT); }
        public void abort() { setType(ABORT); }
    
        public String toString() { return decree.toString(); }
    }