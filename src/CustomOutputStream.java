

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;

import javax.swing.JTextArea;

public class CustomOutputStream extends OutputStream {
    private JTextArea textArea;
    public static SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
    public CustomOutputStream(JTextArea textArea) {
        this.textArea = textArea;
    }

    @Override
    public void write(int b) throws IOException {
    	//textArea.append( String.valueOf((b));
    	String txt = String.valueOf((char)b);
    	textArea.append( txt);
    	//переставляем курсор
    	if(Gui.chkBox_journal.isSelected() == true)
        {
        	textArea.setCaretPosition (textArea.getDocument ().getLength());
        }
     }
}
