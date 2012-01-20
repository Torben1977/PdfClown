package org.pdfclown.samples.cli;

import java.util.List;

import org.pdfclown.documents.Document;
import org.pdfclown.documents.Page;
import org.pdfclown.documents.contents.Contents;
import org.pdfclown.documents.contents.objects.ContainerObject;
import org.pdfclown.documents.contents.objects.ContentObject;
import org.pdfclown.documents.contents.objects.Text;
import org.pdfclown.files.File;

/**
  This sample is a rough stub that demonstrates a basic way to remove all the text
  content from a PDF document.
  <h3>Remarks</h3>
  <p>Next releases will provide a refined way to discriminate text content
  for removal through ContentScanner class.</p>

  @author Stefano Chizzolini (http://www.stefanochizzolini.it)
  @version 0.1.1, 11/01/11
*/
public class ContentRemovalSample
  extends Sample
{
  @Override
  public boolean run(
    )
  {
    // 1. Opening the PDF file...
    File file;
    {
      String filePath = promptPdfFileChoice("Please select a PDF file");
      try
      {file = new File(filePath);}
      catch(Exception e)
      {throw new RuntimeException(filePath + " file access error.",e);}
    }
    Document document = file.getDocument();

    // 2. Content removal.
    for(Page page : document.getPages())
    {
      // Get the page contents!
      Contents contents = page.getContents();
      // Remove text content from page!
      removeText(contents);
      // Update the page contents!
      contents.flush();
    }

    // 3. Serialize the PDF file!
    serialize(file, true, "Content removal", "content removal from existing pages");

    return true;
  }

  private void removeText(
    List<ContentObject> objects
    )
  {
    for(
      int index = 0,
        length = objects.size();
      index < length;
      index++
      )
    {
      ContentObject object = objects.get(index);
      if(object instanceof Text)
      {
        objects.remove(index);
        index--; length--;
      }
      else if(object instanceof ContainerObject)
      {removeText(((ContainerObject)object).getObjects());}
    }
  }
}