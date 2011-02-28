/*
  Copyright 2008-2010 Stefano Chizzolini. http://www.pdfclown.org

  Contributors:
    * Stefano Chizzolini (original code developer, http://www.stefanochizzolini.it)

  This file should be part of the source code distribution of "PDF Clown library"
  (the Program): see the accompanying README files for more info.

  This Program is free software; you can redistribute it and/or modify it under the terms
  of the GNU Lesser General Public License as published by the Free Software Foundation;
  either version 3 of the License, or (at your option) any later version.

  This Program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY,
  either expressed or implied; without even the implied warranty of MERCHANTABILITY or
  FITNESS FOR A PARTICULAR PURPOSE. See the License for more details.

  You should have received a copy of the GNU Lesser General Public License along with this
  Program (see README files); if not, go to the GNU website (http://www.gnu.org/licenses/).

  Redistribution and use, with or without modification, are permitted provided that such
  redistributions retain the above copyright notice, license and disclaimer, along with
  this list of conditions.
*/

package org.pdfclown.documents.interaction.annotations;

import org.pdfclown.PDF;
import org.pdfclown.VersionEnum;
import org.pdfclown.documents.Document;
import org.pdfclown.documents.Page;
import org.pdfclown.objects.PdfDirectObject;
import org.pdfclown.objects.PdfIndirectObject;
import org.pdfclown.objects.PdfName;
import org.pdfclown.util.NotImplementedException;

import java.awt.geom.Rectangle2D;

/**
  Movie annotation [PDF:1.6:8.4.5].

  @author Stefano Chizzolini (http://www.stefanochizzolini.it)
  @since 0.0.7
  @version 0.1.0
*/
@PDF(VersionEnum.PDF12)
public final class Movie
  extends Annotation
{
  // <class>
  // <dynamic>
  // <constructors>
  public Movie(
    Page page,
    Rectangle2D box,
    org.pdfclown.documents.multimedia.Movie content
    )
  {
    super(
      page.getDocument(),
      PdfName.Movie,
      box,
      page
      );
    setContent(content);
  }

  public Movie(
    PdfDirectObject baseObject,
    PdfIndirectObject container
    )
  {super(baseObject,container);}
  // </constructors>

  // <interface>
  // <public>
  @Override
  public Movie clone(
    Document context
    )
  {throw new NotImplementedException();}

  /**
    Gets the movie to be played.
  */
  public org.pdfclown.documents.multimedia.Movie getContent(
    )
  {
    /*
      NOTE: 'Movie' entry MUST exist.
    */
    return new org.pdfclown.documents.multimedia.Movie(
      getBaseDataObject().get(PdfName.Movie),
      getContainer()
      );
  }

  /**
    @see #getContent()
  */
  public void setContent(
  	org.pdfclown.documents.multimedia.Movie value
    )
  {
    if(value == null)
      throw new IllegalArgumentException("Content MUST be defined.");

    getBaseDataObject().put(PdfName.Movie,value.getBaseObject());
  }
  // </public>
  // </interface>
  // </dynamic>
  // </class>
}