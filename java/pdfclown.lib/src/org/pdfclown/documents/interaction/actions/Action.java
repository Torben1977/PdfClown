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

package org.pdfclown.documents.interaction.actions;

import org.pdfclown.PDF;
import org.pdfclown.VersionEnum;
import org.pdfclown.documents.Document;
import org.pdfclown.files.File;
import org.pdfclown.objects.PdfDictionary;
import org.pdfclown.objects.PdfDirectObject;
import org.pdfclown.objects.PdfIndirectObject;
import org.pdfclown.objects.PdfName;
import org.pdfclown.objects.PdfNamedObjectWrapper;
import org.pdfclown.objects.PdfReference;
import org.pdfclown.objects.PdfString;
import org.pdfclown.util.NotImplementedException;

/**
  Action to be performed by the viewer application [PDF:1.6:8.5].

  @author Stefano Chizzolini (http://www.stefanochizzolini.it)
  @since 0.0.7
  @version 0.1.0
*/
@PDF(VersionEnum.PDF11)
public class Action
  extends PdfNamedObjectWrapper<PdfDictionary>
{
  // <class>
  // <static>
  // <fields>
  // </fields>

  // <interface>
  // <public>
  /**
    Wraps an action reference into an action object.

    @param reference Reference to an action object.
    @return Action object associated to the reference.
  */
  public static final Action wrap(
    PdfReference reference
    )
	{return wrap(reference, null);}

  /**
	  Wraps an action base object into an action object.
	
	  @param baseObject Action base object.
	  @param container Action base object container.
	  @return Action object associated to the base object.
	*/
  public static final Action wrap(
    PdfDirectObject baseObject,
    PdfIndirectObject container
    )
  {return wrap(baseObject, container, null);}

  /**
    Wraps an action base object into an action object.

    @param baseObject Action base object.
    @param container Action base object container.
    @param name Action name.
    @return Action object associated to the base object.
  */
  public static final Action wrap(
    PdfDirectObject baseObject,
    PdfIndirectObject container,
    PdfString name
    )
  {
    if(baseObject == null)
      return null;

    PdfDictionary dataObject = (PdfDictionary)File.resolve(baseObject);
    PdfName actionType = (PdfName)dataObject.get(PdfName.S);
    if(actionType == null
      || (dataObject.containsKey(PdfName.Type)
          && !dataObject.get(PdfName.Type).equals(PdfName.Action)))
      return null;

    if(actionType.equals(PdfName.GoTo))
      return new GoToLocal(baseObject,container);
    else if(actionType.equals(PdfName.GoToR))
      return new GoToRemote(baseObject,container);
    else if(actionType.equals(PdfName.GoToE))
      return new GoToEmbedded(baseObject,container);
    else if(actionType.equals(PdfName.Launch))
      return new Launch(baseObject,container);
    else if(actionType.equals(PdfName.Thread))
      return new GoToThread(baseObject,container);
    else if(actionType.equals(PdfName.URI))
      return new GoToURI(baseObject,container);
    else if(actionType.equals(PdfName.Sound))
      return new PlaySound(baseObject,container);
    else if(actionType.equals(PdfName.Movie))
      return new PlayMovie(baseObject,container);
    else if(actionType.equals(PdfName.Hide))
      return new ToggleVisibility(baseObject,container);
    else if(actionType.equals(PdfName.Named))
    {
      PdfName actionName = (PdfName)dataObject.get(PdfName.N);
      if(actionName.equals(PdfName.NextPage))
        return new GoToNextPage(baseObject,container);
      else if(actionName.equals(PdfName.PrevPage))
        return new GoToPreviousPage(baseObject,container);
      else if(actionName.equals(PdfName.FirstPage))
        return new GoToFirstPage(baseObject,container);
      else if(actionName.equals(PdfName.LastPage))
        return new GoToLastPage(baseObject,container);
      else // Custom named action.
        return new NamedAction(baseObject,container);
    }
    else if(actionType.equals(PdfName.SubmitForm))
      return new SubmitForm(baseObject,container);
    else if(actionType.equals(PdfName.ResetForm))
      return new ResetForm(baseObject,container);
    else if(actionType.equals(PdfName.ImportData))
      return new ImportData(baseObject,container);
    else if(actionType.equals(PdfName.JavaScript))
      return new JavaScript(baseObject,container,name);
    else if(actionType.equals(PdfName.SetOCGState))
      return new SetOcgState(baseObject,container);
    else if(actionType.equals(PdfName.Rendition))
      return new Rendition(baseObject,container);
    else if(actionType.equals(PdfName.Trans))
      return new DoTransition(baseObject,container);
    else if(actionType.equals(PdfName.GoTo3DView))
      return new GoTo3dView(baseObject,container);
    else // Custom action.
      return new Action(baseObject,container,name);
  }
  // </public>
  // </interface>
  // </static>

  // <dynamic>
  // <constructors>
  /**
    Creates a new action within the given document context.
  */
  protected Action(
    Document context,
    PdfName actionType
    )
  {
    super(
      context.getFile(),
      new PdfDictionary(
        new PdfName[]
        {
          PdfName.Type,
          PdfName.S
        },
        new PdfDirectObject[]
        {
          PdfName.Action,
          actionType
        }
        )
      );
  }

  protected Action(
    PdfDirectObject baseObject,
    PdfIndirectObject container,
    PdfString name
    )
  {super(baseObject, container, name);}
  // </constructors>

  // <interface>
  // <public>
  @Override
  public Action clone(
    Document context
    )
  {throw new NotImplementedException();}

  /**
    Gets the actions to be performed after the current one.
  */
  @PDF(VersionEnum.PDF12)
  public ChainedActions getActions(
    )
  {
    PdfDirectObject nextObject = getBaseDataObject().get(PdfName.Next);
		return nextObject == null ? null : new ChainedActions(nextObject, getContainer(), this);
  }

  /**
    @see #getActions()
  */
  public void setActions(
    ChainedActions value
    )
  {getBaseDataObject().put(PdfName.Next,value.getBaseObject());}
  // </public>
  // </interface>
  // </dynamic>
  // </class>
}