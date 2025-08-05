    protected List refreshTicksHorizontal(Graphics2D g2,
                                          Rectangle2D dataArea,
                                          RectangleEdge edge) {

        List ticks = new java.util.ArrayList();
        Range range = getRange();

                double lowerBoundVal = range.getLowerBound();
                                    if (this.smallLogFlag && lowerBoundVal < SMALL_LOG_VALUE) {
            lowerBoundVal = SMALL_LOG_VALUE;
        }

                double upperBoundVal = range.getUpperBound();

                int iBegCount = (int) Math.rint(switchedLog10(lowerBoundVal));
                int iEndCount = (int) Math.rint(switchedLog10(upperBoundVal));

        if (iBegCount == iEndCount && iBegCount > 0
                && Math.pow(10, iBegCount) > lowerBoundVal) {
                                        --iBegCount;               }

        double currentTickValue;
        String tickLabel;
        boolean zeroTickFlag = false;
        for (int i = iBegCount; i <= iEndCount; i++) {
                        for (int j = 0; j < 10; ++j) {
                                if (this.smallLogFlag) {
                                        currentTickValue = Math.pow(10, i) + (Math.pow(10, i) * j);
                    if (this.expTickLabelsFlag
                        || (i < 0 && currentTickValue > 0.0
                        && currentTickValue < 1.0)) {
                                                                        if (j == 0 || (i > -4 && j < 2)
                                   || currentTickValue >= upperBoundVal) {
                                                                                                                                        this.numberFormatterObj
                                .setMaximumFractionDigits(-i);
                                                           tickLabel = makeTickLabel(currentTickValue, true);
                        }
                        else {                                tickLabel = "";
                        }
                    }
                    else {                                                                                                                          tickLabel = (j < 1 || (i < 1 && j < 5) || (j < 4 - i)
                                         || currentTickValue >= upperBoundVal)
                                         ? makeTickLabel(currentTickValue) : "";
                    }
                }
                else {                     if (zeroTickFlag) {                           --j;                                  }                         currentTickValue = (i >= 0)
                        ? Math.pow(10, i) + (Math.pow(10, i) * j)
                        : -(Math.pow(10, -i) - (Math.pow(10, -i - 1) * j));
                    if (!zeroTickFlag) {                          if (Math.abs(currentTickValue - 1.0) < 0.0001
                            && lowerBoundVal <= 0.0 && upperBoundVal >= 0.0) {
                                                        currentTickValue = 0.0;                                 zeroTickFlag = true;                                }
                    }
                    else {                             zeroTickFlag = false;                             }                                                                                                                                                               tickLabel = ((this.expTickLabelsFlag && j < 2)
                                || j < 1
                                || (i < 1 && j < 5) || (j < 4 - i)
                                || currentTickValue >= upperBoundVal)
                                   ? makeTickLabel(currentTickValue) : "";
                }

                if (currentTickValue > upperBoundVal) {
                    return ticks;                                                       }

                if (currentTickValue >= lowerBoundVal - SMALL_LOG_VALUE) {
                                        TextAnchor anchor = null;
                    TextAnchor rotationAnchor = null;
                    double angle = 0.0;
                    if (isVerticalTickLabels()) {
                        anchor = TextAnchor.CENTER_RIGHT;
                        rotationAnchor = TextAnchor.CENTER_RIGHT;
                        if (edge == RectangleEdge.TOP) {
                            angle = Math.PI / 2.0;
                        }
                        else {
                            angle = -Math.PI / 2.0;
                        }
                    }
                    else {
                        if (edge == RectangleEdge.TOP) {
                            anchor = TextAnchor.BOTTOM_CENTER;
                            rotationAnchor = TextAnchor.BOTTOM_CENTER;
                        }
                        else {
                            anchor = TextAnchor.TOP_CENTER;
                            rotationAnchor = TextAnchor.TOP_CENTER;
                        }
                    }

                    Tick tick = new NumberTick(new Double(currentTickValue),
                            tickLabel, anchor, rotationAnchor, angle);
                    ticks.add(tick);
                }
            }
        }
        return ticks;
    }