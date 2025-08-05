public EntryComparator getInstance(String field, String order) {
		EntryComparator result;

		if (field.equals("Creation"))
			result = this;
		else if (field.equals("Check"))
			result = new CheckComparator();
		else if (field.equals("Date"))
			result = new DateComparator();
		else if (field.equals("Valuta"))
			result = new ValutaComparator();
		else if (field.equals("Description"))
			result = new DescriptionComparator();
		else if (field.equals("Category"))
			result = new CategoryComparator();
		else if (field.equals("Amount"))
			result = new AmountComparator();
		else if (field.equals("Status"))
			result = new StatusComparator();
		else if (field.equals("Memo"))
			result = new MemoComparator();
		else {
			System.err.println(
				"preferences/entrySortField - invalid value: " + field);
			result = this;
		}
		result.setOrder(order);
		return result;
	}