
package b2s.regex.gui;

import javax.swing.SwingUtilities;

public class SwingUtil {
    public static void invokeLater(Runnable runnable) {
        if (SwingUtilities.isEventDispatchThread()) {
            runnable.run();
        } else {
            SwingUtilities.invokeLater(runnable);
        }
    }
}
