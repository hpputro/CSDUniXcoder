	public boolean remove(CellCacheEntry cce) {
		FormulaCellCacheEntry[] arr = _arr;

		if (_size * 3 < _arr.length && _arr.length > 8) {
						boolean found = false;
			FormulaCellCacheEntry[] prevArr = _arr;
			FormulaCellCacheEntry[] newArr = new FormulaCellCacheEntry[_arr.length / 2]; 			for(int i=0; i<prevArr.length; i++) {
				FormulaCellCacheEntry prevCce = _arr[i];
				if (prevCce != null) {
					if (prevCce == cce) {
						found=true;
						_size--;
												continue;
					}
					addInternal(newArr, prevCce);
				}
			}
			_arr = newArr;
			return found;
		}
				
		int startIx = cce.hashCode() % arr.length;

				for(int i=startIx; i<arr.length; i++) {
			FormulaCellCacheEntry item = arr[i];
			if (item == cce) {
								arr[i] = null;
				_size--;
				return true;
			}
		}
		for(int i=0; i<startIx; i++) {
			FormulaCellCacheEntry item = arr[i];
			if (item == cce) {
								arr[i] = null;
				_size--;
				return true;
			}
		}
		return false;
	}