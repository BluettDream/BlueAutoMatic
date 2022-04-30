package org.blue.automation.services;

import org.blue.automation.entities.vo.ModeProperty;

import java.util.ArrayList;

/**
 * name: MengHao Tian
 * date: 2022/4/29 22:15
 */
public interface ModePropertyService {

    void setFileName(String fileName);

    ArrayList<ModeProperty> selectAllModeProperties();

    ModeProperty selectModePropertyByName(String name);

    boolean addModeProperty(ModeProperty modeProperty);

    boolean updateModeProperty(ModeProperty modeProperty);

    boolean deleteModePropertyByName(String name);

}
