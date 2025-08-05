public int serializeComplexPart(byte[] data, int pos) {
        System.arraycopy(_complexData, 0, data, pos, _complexData.length);
        return _complexData.length;
    }