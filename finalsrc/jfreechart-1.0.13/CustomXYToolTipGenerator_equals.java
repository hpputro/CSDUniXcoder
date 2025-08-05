public boolean equals(Object obj) {

        if (obj == this) {
            return true;
        }

        if (obj instanceof CustomXYToolTipGenerator) {
            CustomXYToolTipGenerator generator = (CustomXYToolTipGenerator) obj;
            boolean result = true;
            for (int series = 0; series < getListCount(); series++) {
                for (int item = 0; item < getToolTipCount(series); item++) {
                    String t1 = getToolTipText(series, item);
                    String t2 = generator.getToolTipText(series, item);
                    if (t1 != null) {
                        result = result && t1.equals(t2);
                    }
                    else {
                        result = result && (t2 == null);
                    }
                }
            }
            return result;
        }

        return false;

    }