package org.blue.automation.services;

import org.blue.automation.entities.Mode;

import java.util.ArrayList;

/**
 * name: MengHao Tian
 * date: 2022/4/29 22:15
 */
public interface ModeService {

    void setFileName(String fileName);

    ArrayList<Mode> selectAllModes();

    Mode selectModeByName(String name);

    boolean addMode(Mode mode);

    boolean updateMode(Mode mode);

    boolean deleteModeByName(String name);

}
