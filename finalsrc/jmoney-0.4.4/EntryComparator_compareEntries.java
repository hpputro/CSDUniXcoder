protected int compareEntries(Entry e1, Entry e2) {
			return stringCompare(
				e1.getFullCategoryName(),
				e2.getFullCategoryName());
		}