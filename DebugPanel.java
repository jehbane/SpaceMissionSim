package gui;
import javax.swing.*;
import java.awt.*;

public class DebugPanel extends JPanel {
    final JCheckBox showTrail   = new JCheckBox("Show Trail", true);
    final JCheckBox showHud     = new JCheckBox("Show HUD", true);
    public final JCheckBox logSteps    = new JCheckBox("Log Steps", false);

    public DebugPanel() {
        setBorder(BorderFactory.createTitledBorder("Debug Options"));
        setLayout(new GridLayout(0,1));
        add(showTrail);
        add(showHud);
        add(logSteps);
    }
}
