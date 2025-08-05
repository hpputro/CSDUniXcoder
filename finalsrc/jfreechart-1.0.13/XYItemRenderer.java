






























public interface XYItemRenderer extends LegendItemSource {

    
    public XYPlot getPlot();

    
    public void setPlot(XYPlot plot);

    
    public int getPassCount();

    
    public Range findDomainBounds(XYDataset dataset);

    
    public Range findRangeBounds(XYDataset dataset);

    
    public void addChangeListener(RendererChangeListener listener);

    
    public void removeChangeListener(RendererChangeListener listener);


    
    
    public boolean getItemVisible(int series, int item);

    
    public boolean isSeriesVisible(int series);

    
    public Boolean getSeriesVisible(int series);

    
    public void setSeriesVisible(int series, Boolean visible);

    
    public void setSeriesVisible(int series, Boolean visible, boolean notify);

    
    public boolean getBaseSeriesVisible();

    
    public void setBaseSeriesVisible(boolean visible);

    
    public void setBaseSeriesVisible(boolean visible, boolean notify);

    
    
    public boolean isSeriesVisibleInLegend(int series);

    
    public Boolean getSeriesVisibleInLegend(int series);

    
    public void setSeriesVisibleInLegend(int series, Boolean visible);

    
    public void setSeriesVisibleInLegend(int series, Boolean visible,
                                         boolean notify);

    
    public boolean getBaseSeriesVisibleInLegend();

    
    public void setBaseSeriesVisibleInLegend(boolean visible);

    
    public void setBaseSeriesVisibleInLegend(boolean visible, boolean notify);


    
    
    public Paint getItemPaint(int row, int column);

    
    public Paint getSeriesPaint(int series);

    
    public void setSeriesPaint(int series, Paint paint);

    
    
    public Paint getBasePaint();

    
    public void setBasePaint(Paint paint);

    

    
    
    public Paint getItemOutlinePaint(int row, int column);

    
    public Paint getSeriesOutlinePaint(int series);

    
    public void setSeriesOutlinePaint(int series, Paint paint);

    
    
    public Paint getBaseOutlinePaint();

    
    public void setBaseOutlinePaint(Paint paint);

    
    
    
    public Stroke getItemStroke(int row, int column);

    
    public Stroke getSeriesStroke(int series);

    
    public void setSeriesStroke(int series, Stroke stroke);

    
    
    public Stroke getBaseStroke();

    
    public void setBaseStroke(Stroke stroke);

    
    
    
    public Stroke getItemOutlineStroke(int row, int column);

    
    public Stroke getSeriesOutlineStroke(int series);

    
    public void setSeriesOutlineStroke(int series, Stroke stroke);

    
    
    public Stroke getBaseOutlineStroke();

    
    public void setBaseOutlineStroke(Stroke stroke);

    
    
    
    public Shape getItemShape(int row, int column);

    
    public Shape getSeriesShape(int series);

    
    public void setSeriesShape(int series, Shape shape);

    
    
    public Shape getBaseShape();

    
    public void setBaseShape(Shape shape);

    

    
    
    public LegendItem getLegendItem(int datasetIndex, int series);


    
    
    public XYSeriesLabelGenerator getLegendItemLabelGenerator();

    
    public void setLegendItemLabelGenerator(XYSeriesLabelGenerator generator);


    
    
    public XYToolTipGenerator getToolTipGenerator(int row, int column);

    
    public XYToolTipGenerator getSeriesToolTipGenerator(int series);

    
    public void setSeriesToolTipGenerator(int series,
                                          XYToolTipGenerator generator);

    
    public XYToolTipGenerator getBaseToolTipGenerator();

    
    public void setBaseToolTipGenerator(XYToolTipGenerator generator);

    

    
    public XYURLGenerator getURLGenerator();

    
    public void setURLGenerator(XYURLGenerator urlGenerator);

    
    
    public boolean isItemLabelVisible(int row, int column);

    
    public boolean isSeriesItemLabelsVisible(int series);

    
    public void setSeriesItemLabelsVisible(int series, boolean visible);

    
    public void setSeriesItemLabelsVisible(int series, Boolean visible);

    
    public void setSeriesItemLabelsVisible(int series, Boolean visible,
                                           boolean notify);

    
    public Boolean getBaseItemLabelsVisible();

    
    public void setBaseItemLabelsVisible(boolean visible);

    
    public void setBaseItemLabelsVisible(Boolean visible);

    
    public void setBaseItemLabelsVisible(Boolean visible, boolean notify);


    
    
    public XYItemLabelGenerator getItemLabelGenerator(int row, int column);

    
    public XYItemLabelGenerator getSeriesItemLabelGenerator(int series);

    
    public void setSeriesItemLabelGenerator(int series,
                                            XYItemLabelGenerator generator);

    
    
    public XYItemLabelGenerator getBaseItemLabelGenerator();

    
    public void setBaseItemLabelGenerator(XYItemLabelGenerator generator);

    
    
    public Font getItemLabelFont(int row, int column);

    
    public Font getSeriesItemLabelFont(int series);

    
    public void setSeriesItemLabelFont(int series, Font font);

    
    public Font getBaseItemLabelFont();

    
    public void setBaseItemLabelFont(Font font);

    
    
    public Paint getItemLabelPaint(int row, int column);

    
    public Paint getSeriesItemLabelPaint(int series);

    
    public void setSeriesItemLabelPaint(int series, Paint paint);

    
    public Paint getBaseItemLabelPaint();

    
    public void setBaseItemLabelPaint(Paint paint);

    
    
    public ItemLabelPosition getPositiveItemLabelPosition(int row, int column);

    
    public ItemLabelPosition getSeriesPositiveItemLabelPosition(int series);

    
    public void setSeriesPositiveItemLabelPosition(int series,
                                                   ItemLabelPosition position);

    
    public void setSeriesPositiveItemLabelPosition(int series,
                                                   ItemLabelPosition position,
                                                   boolean notify);

    
    public ItemLabelPosition getBasePositiveItemLabelPosition();

    
    public void setBasePositiveItemLabelPosition(ItemLabelPosition position);

    
    public void setBasePositiveItemLabelPosition(ItemLabelPosition position,
                                                 boolean notify);


    
    
    public ItemLabelPosition getNegativeItemLabelPosition(int row, int column);

    
    public ItemLabelPosition getSeriesNegativeItemLabelPosition(int series);

    
    public void setSeriesNegativeItemLabelPosition(int series,
                                                   ItemLabelPosition position);

    
    public void setSeriesNegativeItemLabelPosition(int series,
                                                   ItemLabelPosition position,
                                                   boolean notify);

    
    public ItemLabelPosition getBaseNegativeItemLabelPosition();

    
    public void setBaseNegativeItemLabelPosition(ItemLabelPosition position);

    
    public void setBaseNegativeItemLabelPosition(ItemLabelPosition position,
                                                 boolean notify);


        

    
    
    public void addAnnotation(XYAnnotation annotation);

    
    public void addAnnotation(XYAnnotation annotation, Layer layer);

    
    public boolean removeAnnotation(XYAnnotation annotation);

    
    public void removeAnnotations();

    
    public void drawAnnotations(Graphics2D g2,
                                Rectangle2D dataArea,
                                ValueAxis domainAxis,
                                ValueAxis rangeAxis,
                                Layer layer,
                                PlotRenderingInfo info);

    
    
    public XYItemRendererState initialise(Graphics2D g2,
                                          Rectangle2D dataArea,
                                          XYPlot plot,
                                          XYDataset dataset,
                                          PlotRenderingInfo info);

    
    public void drawItem(Graphics2D g2,
                         XYItemRendererState state,
                         Rectangle2D dataArea,
                         PlotRenderingInfo info,
                         XYPlot plot,
                         ValueAxis domainAxis,
                         ValueAxis rangeAxis,
                         XYDataset dataset,
                         int series,
                         int item,
                         CrosshairState crosshairState,
                         int pass);

    
    public void fillDomainGridBand(Graphics2D g2,
                                   XYPlot plot,
                                   ValueAxis axis,
                                   Rectangle2D dataArea,
                                   double start, double end);

    
    public void fillRangeGridBand(Graphics2D g2,
                                  XYPlot plot,
                                  ValueAxis axis,
                                  Rectangle2D dataArea,
                                  double start, double end);

    
    public void drawDomainGridLine(Graphics2D g2,
                                   XYPlot plot,
                                   ValueAxis axis,
                                   Rectangle2D dataArea,
                                   double value);

    
    public void drawRangeLine(Graphics2D g2, XYPlot plot, ValueAxis axis,
            Rectangle2D dataArea, double value, Paint paint, Stroke stroke);

    
    public void drawDomainMarker(Graphics2D g2, XYPlot plot, ValueAxis axis,
            Marker marker, Rectangle2D dataArea);

    
    public void drawRangeMarker(Graphics2D g2, XYPlot plot, ValueAxis axis,
            Marker marker, Rectangle2D dataArea);

    
    
    public Boolean getSeriesVisible();

    
    public void setSeriesVisible(Boolean visible);

    
    public void setSeriesVisible(Boolean visible, boolean notify);

    
    public Boolean getSeriesVisibleInLegend();

    
    public void setSeriesVisibleInLegend(Boolean visible);

    
    public void setSeriesVisibleInLegend(Boolean visible, boolean notify);

    
    public void setPaint(Paint paint);

    
    public void setOutlinePaint(Paint paint);

    
    public void setStroke(Stroke stroke);

    
    public void setOutlineStroke(Stroke stroke);

    
    public void setShape(Shape shape);

    
    public void setItemLabelsVisible(boolean visible);

    
    public void setItemLabelsVisible(Boolean visible);

    
    public void setItemLabelsVisible(Boolean visible, boolean notify);

    
    public void setItemLabelGenerator(XYItemLabelGenerator generator);

    
    public void setToolTipGenerator(XYToolTipGenerator generator);

    
    public Font getItemLabelFont();

    
    public void setItemLabelFont(Font font);

    
    public Paint getItemLabelPaint();

    
    public void setItemLabelPaint(Paint paint);

    
    public ItemLabelPosition getPositiveItemLabelPosition();

    
    public void setPositiveItemLabelPosition(ItemLabelPosition position);

    
    public void setPositiveItemLabelPosition(ItemLabelPosition position,
                                             boolean notify);

    
    public ItemLabelPosition getNegativeItemLabelPosition();

    
    public void setNegativeItemLabelPosition(ItemLabelPosition position);

    
    public void setNegativeItemLabelPosition(ItemLabelPosition position,
                                             boolean notify);

}
