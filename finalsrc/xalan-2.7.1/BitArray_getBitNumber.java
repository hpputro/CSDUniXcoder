public final int getBitNumber(int pos) {

		if (pos == _pos) return(_node);
	
			if (pos < _pos) {
	    _int = _bit = _pos = 0;
	}

		for ( ; _int <= _intSize; _int++) {
	    int bits = _bits[_int];
	    if (bits != 0) { 		for ( ; _bit < 32; _bit++) {
		    if ((bits & _masks[_bit]) != 0) {
			if (++_pos == pos) {
			    _node = ((_int << 5) + _bit) - 1;
			    return (_node);
			}
		    }
		}
		_bit = 0;
	    }
	}
	return(0);
    }