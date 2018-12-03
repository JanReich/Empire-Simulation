package engine.toolBox;

import java.awt.*;
import java.awt.datatransfer.*;
import java.io.IOException;

public class TextTransfer implements ClipboardOwner {

            //Attribute

            //Referenzen

    public TextTransfer() {


    }

    @Override
    public void lostOwnership(Clipboard clipboard, Transferable contents) {

    }

    public void setClipboardContents(String string) {

        StringSelection stringSelection = new StringSelection(string);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, this);
    }

    public String getClipboardContents() {

        String result = "";
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

        Transferable contents = clipboard.getContents(null);
        boolean hasTransferableText = (contents != null) && contents.isDataFlavorSupported(DataFlavor.stringFlavor);

        if (hasTransferableText) {
            try {

                result = (String)contents.getTransferData(DataFlavor.stringFlavor);
            }
            catch (UnsupportedFlavorException | IOException e){

                e.printStackTrace();
            }
        }
        return result;
    }
}
