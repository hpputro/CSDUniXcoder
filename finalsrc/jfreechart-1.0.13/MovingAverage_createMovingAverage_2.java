    public static XYSeries createMovingAverage(XYDataset source,
            int series, String name, double period, double skip) {

        if (source == null) {
            throw new IllegalArgumentException("Null source (XYDataset).");
        }
        if (period < Double.MIN_VALUE) {
            throw new IllegalArgumentException("period must be positive.");
        }
        if (skip < 0.0) {
            throw new IllegalArgumentException("skip must be >= 0.0.");

        }

        XYSeries result = new XYSeries(name);

        if (source.getItemCount(series) > 0) {

                                    double first = source.getXValue(series, 0) + skip;

            for (int i = source.getItemCount(series) - 1; i >= 0; i--) {

                                double x = source.getXValue(series, i);

                if (x >= first) {
                                        int n = 0;
                    double sum = 0.0;
                    double limit = x - period;
                    int offset = 0;
                    boolean finished = false;

                    while (!finished) {
                        if ((i - offset) >= 0) {
                            double xx = source.getXValue(series, i - offset);
                            Number yy = source.getY(series, i - offset);
                            if (xx > limit) {
                                if (yy != null) {
                                    sum = sum + yy.doubleValue();
                                    n = n + 1;
                                }
                            }
                            else {
                                finished = true;
                            }
                        }
                        else {
                            finished = true;
                        }
                        offset = offset + 1;
                    }
                    if (n > 0) {
                        result.add(x, sum / n);
                    }
                    else {
                        result.add(x, null);
                    }
                }

            }
        }

        return result;

    }