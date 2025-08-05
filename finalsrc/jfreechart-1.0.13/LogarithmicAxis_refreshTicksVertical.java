    protected List refreshTicksVertical(Graphics2D g2,
                                        Rectangle2D dataArea,
                                        RectangleEdge edge) {

        List ticks = new java.util.ArrayList();

                double lowerBoundVal = getRange().getLowerBound();
                        if (this.smallLogFlag && lowerBoundVal < SMALL_LOG_VALUE) {
            lowerBoundVal = SMALL_LOG_VALUE;
        }
                double upperBoundVal = getRange().getUpperBound();

                int iBegCount = (int) Math.rint(switchedLog10(lowerBoundVal));
                int iEndCount = (int) Math.rint(switchedLog10(upperBoundVal));

        if (iBegCount == iEndCount && iBegCount > 0
                && Math.pow(10, iBegCount) > lowerBoundVal) {
                                        --iBegCount;               }

        double tickVal;
        String tickLabel;
        boolean zeroTickFlag = false;
        for (int i = iBegCount; i <= iEndCount; i++) {
                        int jEndCount = 10;
            if (i == iEndCount) {
                jEndCount = 1;
            }

            for (int j = 0; j < jEndCount; j++) {
                                if (this.smallLogFlag) {
                                        tickVal = Math.pow(10, i) + (Math.pow(10, i) * j);
                    if (j == 0) {
                                                if (this.log10TickLabelsFlag) {
                                                        tickLabel = "10^" + i;                           }
                        else {                                if (this.expTickLabelsFlag) {
                                                                tickLabel = "1e" + i;                              }
                            else {                                    if (i >= 0) {                                                                                       NumberFormat format
                                        = getNumberFormatOverride();
                                    if (format != null) {
                                        tickLabel = format.format(tickVal);
                                    }
                                    else {
                                        tickLabel = Long.toString((long)
                                                Math.rint(tickVal));
                                    }
                                }
                                else {
                                                                                                                                                this.numberFormatterObj
                                        .setMaximumFractionDigits(-i);
                                                                        tickLabel = this.numberFormatterObj.format(
                                            tickVal);
                                }
                            }
                        }
                    }
                    else {                           tickLabel = "";                         }
                }
                else {                     if (zeroTickFlag) {                              --j;
                    }                                   tickVal = (i >= 0) ? Math.pow(10, i) + (Math.pow(10, i) * j)
                             : -(Math.pow(10, -i) - (Math.pow(10, -i - 1) * j));
                    if (j == 0) {                          if (!zeroTickFlag) {                                                                                  if (i > iBegCount && i < iEndCount
                                    && Math.abs(tickVal - 1.0) < 0.0001) {
                                                                                                tickVal = 0.0;                                        zeroTickFlag = true;                                  tickLabel = "0";                                  }
                            else {
                                                                                                if (this.log10TickLabelsFlag) {
                                                                           tickLabel = (((i < 0) ? "-" : "")
                                            + "10^" + Math.abs(i));
                                }
                                else {
                                    if (this.expTickLabelsFlag) {
                                                                                   tickLabel = (((i < 0) ? "-" : "")
                                                + "1e" + Math.abs(i));
                                    }
                                    else {
                                        NumberFormat format
                                            = getNumberFormatOverride();
                                        if (format != null) {
                                            tickLabel = format.format(tickVal);
                                        }
                                        else {
                                            tickLabel =  Long.toString(
                                                    (long) Math.rint(tickVal));
                                        }
                                    }
                                }
                            }
                        }
                        else {                                 tickLabel = "";                                     zeroTickFlag = false;                           }
                    }
                    else {                               tickLabel = "";                                   zeroTickFlag = false;                         }
                }

                if (tickVal > upperBoundVal) {
                    return ticks;                  }

                if (tickVal >= lowerBoundVal - SMALL_LOG_VALUE) {
                                        TextAnchor anchor = null;
                    TextAnchor rotationAnchor = null;
                    double angle = 0.0;
                    if (isVerticalTickLabels()) {
                        if (edge == RectangleEdge.LEFT) {
                            anchor = TextAnchor.BOTTOM_CENTER;
                            rotationAnchor = TextAnchor.BOTTOM_CENTER;
                            angle = -Math.PI / 2.0;
                        }
                        else {
                            anchor = TextAnchor.BOTTOM_CENTER;
                            rotationAnchor = TextAnchor.BOTTOM_CENTER;
                            angle = Math.PI / 2.0;
                        }
                    }
                    else {
                        if (edge == RectangleEdge.LEFT) {
                            anchor = TextAnchor.CENTER_RIGHT;
                            rotationAnchor = TextAnchor.CENTER_RIGHT;
                        }
                        else {
                            anchor = TextAnchor.CENTER_LEFT;
                            rotationAnchor = TextAnchor.CENTER_LEFT;
                        }
                    }
                                        ticks.add(new NumberTick(new Double(tickVal), tickLabel,
                            anchor, rotationAnchor, angle));
                }
            }
        }
        return ticks;
    }