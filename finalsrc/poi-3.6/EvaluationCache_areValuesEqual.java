private boolean areValuesEqual(ValueEval a, ValueEval b) {
		if (a == null) {
			return false;
		}
		Class<?> cls = a.getClass();
		if (cls != b.getClass()) {
						return false;
		}
		if (a == BlankEval.instance) {
			return b == a;
		}
		if (cls == NumberEval.class) {
			return ((NumberEval)a).getNumberValue() == ((NumberEval)b).getNumberValue();
		}
		if (cls == StringEval.class) {
			return ((StringEval)a).getStringValue().equals(((StringEval)b).getStringValue());
		}
		if (cls == BoolEval.class) {
			return ((BoolEval)a).getBooleanValue() == ((BoolEval)b).getBooleanValue();
		}
		if (cls == ErrorEval.class) {
			return ((ErrorEval)a).getErrorCode() == ((ErrorEval)b).getErrorCode();
		}
		throw new IllegalStateException("Unexpected value class (" + cls.getName() + ")");
	}