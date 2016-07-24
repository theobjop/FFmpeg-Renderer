/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package github.theobjop.ffmpegrenderer;

import static github.theobjop.ffmpegrenderer.SettingsContainer.layout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

/**
 *
 * @author Brandon Woolworth
 */
public final class x264Settings extends VideoSettings {
    // Incase we need them...
    public static JLabel pLbl;
    public static JLabel vBLbl;
    public static JLabel fpsLbl;
    public static JLabel crfLbl;
    public static JLabel qpLbl;

    public static JTextField bitrate;
    public static JTextField framerate;
    public static JTextField constRateFrame;
    public static JTextField qpRate;

    private static final String[] defaultSelections = {"none", "ultrafast", "superfast", "veryfast", "faster", "fast", "medium", "slow", "veryslow"};
    public static JComboBox<String> presetSelector;

    private static final String CODEC = "libx264";

    public x264Settings(JPanel parent) {
        // Presets
        pLbl = new JLabel("Preset: ");
        presetSelector = new JComboBox<>(defaultSelections);
        presetSelector.setSelectedIndex(0);

        // Video Labels
        vBLbl = new JLabel("Video Kb/s:");
        fpsLbl = new JLabel("FPS:");
        crfLbl = new JLabel("CRF:");
        qpLbl = new JLabel("QP:");
        bitrate = new JTextField("1000");
        framerate = new JTextField("30");
        constRateFrame = new JTextField("21");
        qpRate = new JTextField("0");

        layout.putConstraint(SpringLayout.NORTH, bitrate, 0, SpringLayout.NORTH, parent);
        layout.putConstraint(SpringLayout.NORTH, presetSelector, 5, SpringLayout.SOUTH, bitrate);
        layout.putConstraint(SpringLayout.WEST, presetSelector, 0, SpringLayout.WEST, bitrate);

        layout.putConstraint(SpringLayout.WEST, vBLbl, 7, SpringLayout.WEST, parent);
        layout.putConstraint(SpringLayout.WEST, bitrate, 5, SpringLayout.EAST, vBLbl);
        layout.putConstraint(SpringLayout.EAST, bitrate, 0, SpringLayout.EAST, presetSelector);

        layout.putConstraint(SpringLayout.VERTICAL_CENTER, vBLbl, 0, SpringLayout.VERTICAL_CENTER, bitrate);

        layout.putConstraint(SpringLayout.EAST, pLbl, 0, SpringLayout.EAST, vBLbl);
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, pLbl, 0, SpringLayout.VERTICAL_CENTER, presetSelector);

        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, framerate, 0, SpringLayout.HORIZONTAL_CENTER, SettingsContainer.getResetButton());
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, framerate, 0, SpringLayout.VERTICAL_CENTER, bitrate);
        layout.putConstraint(SpringLayout.HEIGHT, framerate, 0, SpringLayout.HEIGHT, bitrate);
        layout.putConstraint(SpringLayout.EAST, framerate, 0, SpringLayout.EAST, SettingsContainer.getResetButton());
        layout.putConstraint(SpringLayout.WEST, framerate, 5, SpringLayout.EAST, fpsLbl);

        layout.putConstraint(SpringLayout.VERTICAL_CENTER, fpsLbl, 0, SpringLayout.VERTICAL_CENTER, framerate);
        layout.putConstraint(SpringLayout.WEST, fpsLbl, 0, SpringLayout.WEST, SettingsContainer.getResetButton());

        layout.putConstraint(SpringLayout.HEIGHT, constRateFrame, 0, SpringLayout.HEIGHT, framerate);
        layout.putConstraint(SpringLayout.WIDTH, constRateFrame, 0, SpringLayout.WIDTH, framerate);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, constRateFrame, 0, SpringLayout.HORIZONTAL_CENTER, framerate);
        layout.putConstraint(SpringLayout.NORTH, constRateFrame, 5, SpringLayout.SOUTH, framerate);

        layout.putConstraint(SpringLayout.HEIGHT, qpRate, 0, SpringLayout.HEIGHT, framerate);
        layout.putConstraint(SpringLayout.WIDTH, qpRate, 0, SpringLayout.WIDTH, framerate);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, qpRate, 0, SpringLayout.HORIZONTAL_CENTER, framerate);
        layout.putConstraint(SpringLayout.NORTH, qpRate, 5, SpringLayout.SOUTH, constRateFrame);

        layout.putConstraint(SpringLayout.EAST, crfLbl, 0, SpringLayout.EAST, fpsLbl);
        layout.putConstraint(SpringLayout.EAST, qpLbl, 0, SpringLayout.EAST, fpsLbl);

        layout.putConstraint(SpringLayout.VERTICAL_CENTER, crfLbl, 0, SpringLayout.VERTICAL_CENTER, constRateFrame);
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, qpLbl, 0, SpringLayout.VERTICAL_CENTER, qpRate);

        layout.putConstraint(SpringLayout.NORTH, AudioSettings.bitrate, 0, SpringLayout.NORTH, SettingsContainer.getResetButton());
        layout.putConstraint(SpringLayout.SOUTH, AudioSettings.bitrate, 0, SpringLayout.SOUTH, SettingsContainer.getResetButton());

        layout.putConstraint(SpringLayout.EAST, AudioSettings.bitrate, 0, SpringLayout.EAST, presetSelector);
        layout.putConstraint(SpringLayout.WEST, AudioSettings.bitrate, 0, SpringLayout.WEST, presetSelector);

        layout.putConstraint(SpringLayout.VERTICAL_CENTER, AudioSettings.aBLbl, 0, SpringLayout.VERTICAL_CENTER, AudioSettings.bitrate);
        layout.putConstraint(SpringLayout.EAST, AudioSettings.aBLbl, 0, SpringLayout.EAST, pLbl);

        layout.putConstraint(SpringLayout.WEST, AudioSettings.frequency, 0, SpringLayout.WEST, AudioSettings.bitrate);
        layout.putConstraint(SpringLayout.EAST, AudioSettings.frequency, 0, SpringLayout.EAST, AudioSettings.bitrate);
        layout.putConstraint(SpringLayout.HEIGHT, AudioSettings.frequency, 0, SpringLayout.HEIGHT, AudioSettings.bitrate);

        layout.putConstraint(SpringLayout.HEIGHT, bitrate, 0, SpringLayout.HEIGHT, AudioSettings.bitrate);
        layout.putConstraint(SpringLayout.SOUTH, AudioSettings.frequency, -5, SpringLayout.NORTH, AudioSettings.bitrate);

        layout.putConstraint(SpringLayout.VERTICAL_CENTER, AudioSettings.aFLbl, 0, SpringLayout.VERTICAL_CENTER, AudioSettings.frequency);
        layout.putConstraint(SpringLayout.EAST, AudioSettings.aFLbl, 0, SpringLayout.EAST, AudioSettings.aBLbl);

        addTo(parent);

        loadSettings();
    }

    @Override
    public void loadSettings() {
        String prest = PropertiesWriter.get(PRESET_OPT) == null ? "none" : PropertiesWriter.get(PRESET_OPT);
        presetSelector.setSelectedItem(prest);
        bitrate.setText(PropertiesWriter.get(V_BIT_OPT));
        framerate.setText(PropertiesWriter.get(FPS_OPT));
        constRateFrame.setText(PropertiesWriter.get(CRF_OPT));
        qpRate.setText(PropertiesWriter.get(QP_OPT));
    }

    @Override
    public void setElementsEnabled(boolean f) {
        bitrate.setEnabled(f);
        framerate.setEditable(f);
        constRateFrame.setEditable(f);
        presetSelector.setEnabled(f);
    }

    public void addTo(JPanel parent) {
        parent.add(pLbl);
        parent.add(vBLbl);
        parent.add(fpsLbl);
        parent.add(crfLbl);
        parent.add(qpLbl);
        parent.add(bitrate);
        parent.add(framerate);
        parent.add(constRateFrame);
        parent.add(presetSelector);
        parent.add(qpRate);
    }

    public void removeAll(JPanel parent) {
        parent.remove(pLbl);
        parent.remove(vBLbl);
        parent.remove(fpsLbl);
        parent.remove(crfLbl);
        parent.remove(qpLbl);
        parent.remove(bitrate);
        parent.remove(framerate);
        parent.remove(constRateFrame);
        parent.remove(presetSelector);
        parent.remove(qpRate);
    }

    public static JComponent[] getComponenets() {
        JComponent[] ret = {pLbl, vBLbl, fpsLbl, crfLbl, qpLbl, bitrate, framerate, constRateFrame, presetSelector, qpRate};
        return ret;
    }

    public static String getCodec() {
        return CODEC;
    }

    public static String getBitrate() {
        return bitrate.getText();
    }

    public static String getFPS() {
        return framerate.getText();
    }

    public static String getCRF() {
        return constRateFrame.getText();
    }

    public static String getQP() {
        return qpRate.getText();
    }

    public static JComboBox<String> getPresetSelector() {
        return presetSelector;
    }

    private static String getCRForQP_OPT() {
        if (getCRF().isEmpty()) {
            return QP_OPT;
        }
        if (getQP().isEmpty()) {
            return CRF_OPT;
        }

        return null;
    }

    private static String getCRForQP() {
        if (getCRF().isEmpty()) {
            return getQP();
        }
        if (getQP().isEmpty()) {
            return getCRF();
        }

        return null;
    }

    public String[] getRenderSettings() throws Exception {
        ArrayList<String> CMD_ARRAY = new ArrayList<>();

        if (!CODEC.isEmpty()) {
            CMD_ARRAY.add(V_CODEC_OPT);
            CMD_ARRAY.add(CODEC);
        }

        // Get bitrate unless QP is set - QP will imply Variable Bit Rate
        if (!getBitrate().isEmpty() && getCRForQP() != null) {
            CMD_ARRAY.add(V_BIT_OPT);
            CMD_ARRAY.add(getBitrate());
        }

        // Get FPS, if not set then default 30
        if (!getFPS().isEmpty()) {
            CMD_ARRAY.add(FPS_OPT);
            CMD_ARRAY.add(getFPS());
        } else {
            CMD_ARRAY.add(FPS_OPT);
            CMD_ARRAY.add("30");
        }

        // Get CRF if it's set : Get QP if it's set
        if (getCRForQP_OPT() != null && getCRForQP() != null) {
            CMD_ARRAY.add(getCRForQP_OPT());
            CMD_ARRAY.add(getCRForQP());
        } else {
            throw new Exception("Make sure either CRF or QP is empty.");
        }

        if (!((String) (getPresetSelector().getSelectedItem())).equalsIgnoreCase("none")) {
            CMD_ARRAY.add(PRESET_OPT);
            CMD_ARRAY.add((String) getPresetSelector().getSelectedItem());
        }

        return CMD_ARRAY.toArray(new String[CMD_ARRAY.size()]);
    }

    public Map<String, String> getSaveSettings() {
        HashMap<String, String> map = new HashMap<>();
        map.put(V_BIT_OPT, getBitrate());
        map.put(FPS_OPT, getFPS());
        map.put(CRF_OPT, getCRF());
        map.put(QP_OPT, getQP());
        map.put(PRESET_OPT, (String) getPresetSelector().getSelectedItem());
        return map;
    }

    public static Map<String, String> getDefaultSettings() {
        HashMap<String, String> map = new HashMap<>();
        map.put(V_BIT_OPT, "1000");
        map.put(FPS_OPT, "30");
        map.put(CRF_OPT, "21");
        map.put(QP_OPT, "0");
        map.put(PRESET_OPT, "none");
        return map;
    }
}
