package org.blue.automation.services;

import org.blue.automation.entities.AdbProvider;

public interface AdbProviderService {
    AdbProvider getAdbProvider();

    boolean setAdbProvider(AdbProvider adbProvider);
}
