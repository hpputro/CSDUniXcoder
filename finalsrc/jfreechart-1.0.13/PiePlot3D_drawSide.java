    protected void drawSide(Graphics2D g2,
                            Rectangle2D plotArea,
                            Arc2D arc,
                            Area front,
                            Area back,
                            Paint paint,
                            Paint outlinePaint,
                            Stroke outlineStroke,
                            boolean drawFront,
                            boolean drawBack) {

        if (getDarkerSides()) {
            if (paint instanceof Color) {
                Color c = (Color) paint;
                c = c.darker();
                paint = c;
            }
        }

        double start = arc.getAngleStart();
        double extent = arc.getAngleExtent();
        double end = start + extent;

        g2.setStroke(outlineStroke);

                if (extent < 0.0) {

            if (isAngleAtFront(start)) {  
                if (!isAngleAtBack(end)) {

                    if (extent > -180.0) {                                                                      if (drawFront) {
                            Area side = new Area(new Rectangle2D.Double(
                                    arc.getEndPoint().getX(), plotArea.getY(),
                                    arc.getStartPoint().getX()
                                    - arc.getEndPoint().getX(),
                                    plotArea.getHeight()));
                            side.intersect(front);
                            g2.setPaint(paint);
                            g2.fill(side);
                            g2.setPaint(outlinePaint);
                            g2.draw(side);
                        }
                    }
                    else {                                                                                  Area side1 = new Area(new Rectangle2D.Double(
                                plotArea.getX(), plotArea.getY(),
                                arc.getStartPoint().getX() - plotArea.getX(),
                                plotArea.getHeight()));
                        side1.intersect(front);

                        Area side2 = new Area(new Rectangle2D.Double(
                                arc.getEndPoint().getX(), plotArea.getY(),
                                plotArea.getMaxX() - arc.getEndPoint().getX(),
                                plotArea.getHeight()));

                        side2.intersect(front);
                        g2.setPaint(paint);
                        if (drawFront) {
                            g2.fill(side1);
                            g2.fill(side2);
                        }

                        if (drawBack) {
                            g2.fill(back);
                        }

                        g2.setPaint(outlinePaint);
                        if (drawFront) {
                            g2.draw(side1);
                            g2.draw(side2);
                        }

                        if (drawBack) {
                            g2.draw(back);
                        }

                    }
                }
                else {                          
                    if (drawBack) {
                        Area side2 = new Area(new Rectangle2D.Double(
                                plotArea.getX(), plotArea.getY(),
                                arc.getEndPoint().getX() - plotArea.getX(),
                                plotArea.getHeight()));
                        side2.intersect(back);
                        g2.setPaint(paint);
                        g2.fill(side2);
                        g2.setPaint(outlinePaint);
                        g2.draw(side2);
                    }

                    if (drawFront) {
                        Area side1 = new Area(new Rectangle2D.Double(
                                plotArea.getX(), plotArea.getY(),
                                arc.getStartPoint().getX() - plotArea.getX(),
                                plotArea.getHeight()));
                        side1.intersect(front);
                        g2.setPaint(paint);
                        g2.fill(side1);
                        g2.setPaint(outlinePaint);
                        g2.draw(side1);
                    }
                }
            }
            else {                      
                if (!isAngleAtFront(end)) {
                    if (extent > -180.0) {                          if (drawBack) {
                            Area side = new Area(new Rectangle2D.Double(
                                    arc.getStartPoint().getX(), plotArea.getY(),
                                    arc.getEndPoint().getX()
                                    - arc.getStartPoint().getX(),
                                    plotArea.getHeight()));
                            side.intersect(back);
                            g2.setPaint(paint);
                            g2.fill(side);
                            g2.setPaint(outlinePaint);
                            g2.draw(side);
                        }
                    }
                    else {                                                      Area side1 = new Area(new Rectangle2D.Double(
                                arc.getStartPoint().getX(), plotArea.getY(),
                                plotArea.getMaxX() - arc.getStartPoint().getX(),
                                plotArea.getHeight()));
                        side1.intersect(back);

                        Area side2 = new Area(new Rectangle2D.Double(
                                plotArea.getX(), plotArea.getY(),
                                arc.getEndPoint().getX() - plotArea.getX(),
                                plotArea.getHeight()));

                        side2.intersect(back);

                        g2.setPaint(paint);
                        if (drawBack) {
                            g2.fill(side1);
                            g2.fill(side2);
                        }

                        if (drawFront) {
                            g2.fill(front);
                        }

                        g2.setPaint(outlinePaint);
                        if (drawBack) {
                            g2.draw(side1);
                            g2.draw(side2);
                        }

                        if (drawFront) {
                            g2.draw(front);
                        }

                    }
                }
                else {  
                    if (drawBack) {
                        Area side1 = new Area(new Rectangle2D.Double(
                                arc.getStartPoint().getX(), plotArea.getY(),
                                plotArea.getMaxX() - arc.getStartPoint().getX(),
                                plotArea.getHeight()));
                        side1.intersect(back);
                        g2.setPaint(paint);
                        g2.fill(side1);
                        g2.setPaint(outlinePaint);
                        g2.draw(side1);
                    }

                    if (drawFront) {
                        Area side2 = new Area(new Rectangle2D.Double(
                                arc.getEndPoint().getX(), plotArea.getY(),
                                plotArea.getMaxX() - arc.getEndPoint().getX(),
                                plotArea.getHeight()));
                        side2.intersect(front);
                        g2.setPaint(paint);
                        g2.fill(side2);
                        g2.setPaint(outlinePaint);
                        g2.draw(side2);
                    }

                }
            }
        }
        else if (extent > 0.0) {  
            if (isAngleAtFront(start)) {  
                if (!isAngleAtBack(end)) {  
                    if (extent < 180.0) {                          if (drawFront) {
                            Area side = new Area(new Rectangle2D.Double(
                                    arc.getStartPoint().getX(), plotArea.getY(),
                                    arc.getEndPoint().getX()
                                    - arc.getStartPoint().getX(),
                                    plotArea.getHeight()));
                            side.intersect(front);
                            g2.setPaint(paint);
                            g2.fill(side);
                            g2.setPaint(outlinePaint);
                            g2.draw(side);
                        }
                    }
                    else {                          Area side1 = new Area(new Rectangle2D.Double(
                                arc.getStartPoint().getX(), plotArea.getY(),
                                plotArea.getMaxX() - arc.getStartPoint().getX(),
                                plotArea.getHeight()));
                        side1.intersect(front);

                        Area side2 = new Area(new Rectangle2D.Double(
                                plotArea.getX(), plotArea.getY(),
                                arc.getEndPoint().getX() - plotArea.getX(),
                                plotArea.getHeight()));
                        side2.intersect(front);

                        g2.setPaint(paint);
                        if (drawFront) {
                            g2.fill(side1);
                            g2.fill(side2);
                        }

                        if (drawBack) {
                            g2.fill(back);
                        }

                        g2.setPaint(outlinePaint);
                        if (drawFront) {
                            g2.draw(side1);
                            g2.draw(side2);
                        }

                        if (drawBack) {
                            g2.draw(back);
                        }

                    }
                }
                else {                      if (drawBack) {
                        Area side2 = new Area(new Rectangle2D.Double(
                                arc.getEndPoint().getX(), plotArea.getY(),
                                plotArea.getMaxX() - arc.getEndPoint().getX(),
                                plotArea.getHeight()));
                        side2.intersect(back);
                        g2.setPaint(paint);
                        g2.fill(side2);
                        g2.setPaint(outlinePaint);
                        g2.draw(side2);
                    }

                    if (drawFront) {
                        Area side1 = new Area(new Rectangle2D.Double(
                                arc.getStartPoint().getX(), plotArea.getY(),
                                plotArea.getMaxX() - arc.getStartPoint().getX(),
                                plotArea.getHeight()));
                        side1.intersect(front);
                        g2.setPaint(paint);
                        g2.fill(side1);
                        g2.setPaint(outlinePaint);
                        g2.draw(side1);
                    }
                }
            }
            else {  
                if (!isAngleAtFront(end)) {
                    if (extent < 180.0) {                          if (drawBack) {
                            Area side = new Area(new Rectangle2D.Double(
                                    arc.getEndPoint().getX(), plotArea.getY(),
                                    arc.getStartPoint().getX()
                                    - arc.getEndPoint().getX(),
                                    plotArea.getHeight()));
                            side.intersect(back);
                            g2.setPaint(paint);
                            g2.fill(side);
                            g2.setPaint(outlinePaint);
                            g2.draw(side);
                        }
                    }
                    else {                                                      Area side1 = new Area(new Rectangle2D.Double(
                                arc.getStartPoint().getX(), plotArea.getY(),
                                plotArea.getX() - arc.getStartPoint().getX(),
                                plotArea.getHeight()));
                        side1.intersect(back);

                        Area side2 = new Area(new Rectangle2D.Double(
                                arc.getEndPoint().getX(), plotArea.getY(),
                                plotArea.getMaxX() - arc.getEndPoint().getX(),
                                plotArea.getHeight()));
                        side2.intersect(back);

                        g2.setPaint(paint);
                        if (drawBack) {
                            g2.fill(side1);
                            g2.fill(side2);
                        }

                        if (drawFront) {
                            g2.fill(front);
                        }

                        g2.setPaint(outlinePaint);
                        if (drawBack) {
                            g2.draw(side1);
                            g2.draw(side2);
                        }

                        if (drawFront) {
                            g2.draw(front);
                        }

                    }
                }
                else {                                              if (drawBack) {
                        Area side1 = new Area(new Rectangle2D.Double(
                                plotArea.getX(), plotArea.getY(),
                                arc.getStartPoint().getX() - plotArea.getX(),
                                plotArea.getHeight()));
                        side1.intersect(back);
                        g2.setPaint(paint);
                        g2.fill(side1);
                        g2.setPaint(outlinePaint);
                        g2.draw(side1);
                    }

                    if (drawFront) {
                        Area side2 = new Area(new Rectangle2D.Double(
                                plotArea.getX(), plotArea.getY(),
                                arc.getEndPoint().getX() - plotArea.getX(),
                                plotArea.getHeight()));
                        side2.intersect(front);
                        g2.setPaint(paint);
                        g2.fill(side2);
                        g2.setPaint(outlinePaint);
                        g2.draw(side2);
                    }
                }
            }
        }
    }