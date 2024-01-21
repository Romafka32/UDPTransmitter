package org.example.udptranslation;

import java.util.ArrayList;
import java.util.List;
import javax.sound.sampled.*;
import org.springframework.stereotype.Component;

/**
 * Класс для работы с аудио ресурсами. Нашел библиотеку в сети и доработал для своей задачи
 */


@Component
public class Audio {
    /**
     * Поле, которое содержит название текущего мастер-микшера системы.
     */
    //у меня главный микшер называется так
    private static final String MIXER_NAME = "Îáùàÿ ãðîìêîñòü target port";

    /**
     * Метод для получения уровня громкости из линии
     *
     * @return объект Float, обертку над вещественным числом, от 0 до 1
     */
    public static Float getMasterOutputVolume() {
        Line line = getMasterOutputLine();
        if (line == null) return null;
        boolean opened = open(line);
        try {
            FloatControl control = getVolumeControl(line);
            if (control == null) return null;
            return control.getValue();
        } finally {
            if (opened) line.close();
        }
    }

    /**
     * Метод для получения управляющей линии из микшера
     *
     * @return объект Line c информацией о текущих настройках выходной линии
     */
    public static Line getMasterOutputLine() {
        for (Mixer mixer : getMixers()) {
            for (Line line : getAvailableOutputLines(mixer)) {
                if (line.getLineInfo().toString().contains(MIXER_NAME)) return line;
            }
        }
        return null;
    }

    /**
     * Метод для получения списка микшеров системы
     *
     * @return список объектов Mixer
     */
    public static List<Mixer> getMixers() {
        Mixer.Info[] infos = AudioSystem.getMixerInfo();
        List<Mixer> mixers = new ArrayList<>(infos.length);
        for (Mixer.Info info : infos) {
            Mixer mixer = AudioSystem.getMixer(info);
            mixers.add(mixer);
        }
        return mixers;
    }

    /**
     * Метод для получения активной линии микшера
     *
     * @param mixer аудио-микшер системы
     *
     * @return список объектов Line
     */
    public static List<Line> getAvailableOutputLines(Mixer mixer) {
        return getAvailableLines(mixer, mixer.getTargetLineInfo());
    }

    /**
     * Метод для получения списка микшеров системы
     *
     * @param mixer аудио-микшер системы
     * @param lineInfos объект с параметрами линии
     *
     * @return список объектов Line
     */
    private static List<Line> getAvailableLines(Mixer mixer, Line.Info[] lineInfos) {
        List<Line> lines = new ArrayList<>(lineInfos.length);
        for (Line.Info lineInfo : lineInfos) {
            Line line;
            line = getLineIfAvailable(mixer, lineInfo);
            if (line != null) lines.add(line);
        }
        return lines;
    }

    /**
     * Метод для получения списка микшеров системы
     *
     * @param mixer аудио-микшер системы
     * @param lineInfo объект с параметрами линии
     *
     * @return объект Line
     */
    public static Line getLineIfAvailable(Mixer mixer, Line.Info lineInfo) {
        try {
            return mixer.getLine(lineInfo);
        } catch (LineUnavailableException ex) {
            return null;
        }
    }

    /**
     * Метод для открытия линии
     *
     * @param line линия аудио-микшера
     *
     * @return true или false
     */
    public static boolean open(Line line) {
        if (line.isOpen()) return false;
        try {
            line.open();
        } catch (LineUnavailableException ex) {
            return false;
        }
        return true;
    }

    /**
     * Метод получения параметра громкости
     *
     * @return FloatControl с информацией о текущей громкости
     */
    public static FloatControl getVolumeControl(Line line) {
        if (!line.isOpen()) throw new RuntimeException("Line is closed: " + toString(line));
        return (FloatControl) findControl(FloatControl.Type.VOLUME, line.getControls());
    }

    /**
     * Метод для отображения названия линии в строковом виде
     *
     * @param line линия аудио-микшера
     *
     * @return String с названием линии
     */
    public static String toString(Line line) {
        if (line == null) return null;
        Line.Info info = line.getLineInfo();
        return info.toString();
    }

    /**
     * Метод для поиска параметра, отвечающего за громкость линии
     *
     * @param type тип параметра линии
     * @param controls параметры линии
     *
     * @return String с названием линии
     */
    private static Control findControl(Control.Type type, Control... controls) {
        if (controls == null || controls.length == 0) return null;
        for (Control control : controls) {
            if (control.getType().equals(type)) return control;
            if (control instanceof CompoundControl) {
                CompoundControl compoundControl = (CompoundControl) control;
                Control member = findControl(type, compoundControl.getMemberControls());
                if (member != null) return member;
            }
        }
        return null;
    }
}
