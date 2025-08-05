public void normalise64bit() {
		int oldBitLen = _significand.bitLength();
		int sc = oldBitLen - C_64;
		if (sc == 0) {
			return;
		}
		if (sc < 0) {
			throw new IllegalStateException("Not enough precision");
		}
		_binaryExponent += sc;
		if (sc > 32) {
			int highShift = (sc-1) & 0xFFFFE0;
			_significand = _significand.shiftRight(highShift);
			sc -= highShift;
			oldBitLen -= highShift;
		}
		if (sc < 1) {
			throw new IllegalStateException();
		}
		_significand = Rounder.round(_significand, sc);
		if (_significand.bitLength() > oldBitLen) {
			sc++;
			_binaryExponent++;
		}
		_significand = _significand.shiftRight(sc);
	}