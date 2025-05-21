package gui;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Properties;

public class ConfigPanel extends JPanel {
    private final JTextField tfDry  = new JTextField("10000", 8);
    private final JTextField tfFuel = new JTextField("5000", 8);
    private final JTextField tfIsp  = new JTextField("300", 8);
    private final JCheckBox cbMars  = new JCheckBox("Use Mars", false);

    public ConfigPanel() {
        setBorder(BorderFactory.createTitledBorder("Mission Config"));
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4,4,4,4);
        c.anchor = GridBagConstraints.WEST;

        addRow("Dry Mass (kg):", tfDry, 0, c);
        addRow("Fuel Mass (kg):", tfFuel, 1, c);
        addRow("Isp (s):",       tfIsp,  2, c);
        c.gridx=0; c.gridy=3; c.gridwidth=2;
        add(cbMars, c);
    }

    private void addRow(String label, JComponent comp, int row, GridBagConstraints c) {
        c.gridx=0; c.gridy=row; c.gridwidth=1;
        add(new JLabel(label), c);
        c.gridx=1;
        add(comp, c);
    }

    public double getDryMass()   { return Double.parseDouble(tfDry.getText()); }
    public double getFuelMass()  { return Double.parseDouble(tfFuel.getText()); }
    public double getIsp()       { return Double.parseDouble(tfIsp.getText()); }
    public boolean useMars()     { return cbMars.isSelected(); }

    void loadFromFile() {
        JFileChooser fc = new JFileChooser();
        if (fc.showOpenDialog(this)==JFileChooser.APPROVE_OPTION) {
            try (FileInputStream in = new FileInputStream(fc.getSelectedFile())) {
                Properties p = new Properties();
                p.load(in);
                tfDry.setText( p.getProperty("dry", tfDry.getText()) );
                tfFuel.setText(p.getProperty("fuel", tfFuel.getText()));
                tfIsp.setText( p.getProperty("isp", tfIsp.getText()) );
                cbMars.setSelected(Boolean.parseBoolean(p.getProperty("mars","false")));
            } catch (Exception ex) { ex.printStackTrace(); }
        }
    }

    void saveToFile() {
        JFileChooser fc = new JFileChooser();
        if (fc.showSaveDialog(this)==JFileChooser.APPROVE_OPTION) {
            try (FileOutputStream out = new FileOutputStream(fc.getSelectedFile())) {
                Properties p = new Properties();
                p.setProperty("dry",  tfDry.getText());
                p.setProperty("fuel", tfFuel.getText());
                p.setProperty("isp",  tfIsp.getText());
                p.setProperty("mars", Boolean.toString(cbMars.isSelected()));
                p.store(out, "Sim Config");
            } catch (Exception ex) { ex.printStackTrace(); }
        }
    }
}
