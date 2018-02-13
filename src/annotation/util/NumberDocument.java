package annotation.util;

import javax.swing.text.*;
import java.awt.*;

/**
 * Document contains only numeric data.
 *
 * @author Somprasong Damyos
 */
public class NumberDocument extends PlainDocument {
    /*
     * (non-Javadoc)
     *
     * @see javax.swing.text.Document#insertString(int, java.lang.String,javax.swing.text.AttributeSet)
     */

    @Override
    public void insertString(int offs, String str, AttributeSet a)
            throws BadLocationException {

        if (str == null) {
            return;
        }

        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return;
            }
        }
        super.insertString(offs, str, a);
    }
}
