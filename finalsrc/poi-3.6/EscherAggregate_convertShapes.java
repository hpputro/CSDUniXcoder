	private void convertShapes(HSSFShapeContainer parent, EscherContainerRecord escherParent, Map shapeToObj )
	{
		if ( escherParent == null ) throw new IllegalArgumentException( "Parent record required" );

		List shapes = parent.getChildren();
		for ( Iterator iterator = shapes.iterator(); iterator.hasNext(); )
		{
			HSSFShape shape = (HSSFShape) iterator.next();
			if ( shape instanceof HSSFShapeGroup )
			{
				convertGroup( (HSSFShapeGroup) shape, escherParent, shapeToObj );
			}
			else
			{
				AbstractShape shapeModel = AbstractShape.createShape(
						shape,
						drawingManager.allocateShapeId(drawingGroupId) );
				shapeToObj.put( findClientData( shapeModel.getSpContainer() ), shapeModel.getObjRecord() );
				if ( shapeModel instanceof TextboxShape )
				{
					EscherRecord escherTextbox = ( (TextboxShape) shapeModel ).getEscherTextbox();
					shapeToObj.put( escherTextbox, ( (TextboxShape) shapeModel ).getTextObjectRecord() );
					
					if ( shapeModel instanceof CommentShape ){
						CommentShape comment = (CommentShape)shapeModel;
						tailRec.add(comment.getNoteRecord());
					}

				}
				escherParent.addChildRecord( shapeModel.getSpContainer() );
			}
		}

	}