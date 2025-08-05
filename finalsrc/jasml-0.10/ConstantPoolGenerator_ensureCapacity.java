private void ensureCapacity() {
		if (items.length < count + 3) {
			ConstantPoolItem[] ni = new ConstantPoolItem[items.length + 20];
			System.arraycopy(items, 0, ni, 0, items.length);
			items = ni;
		}
	}