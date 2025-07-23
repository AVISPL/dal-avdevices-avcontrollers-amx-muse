/*
 * Copyright (c) 2025 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.amx.muse.communicator;

import com.avispl.symphony.api.dal.dto.control.ControllableProperty;
import com.avispl.symphony.api.dal.dto.monitor.ExtendedStatistics;
import com.avispl.symphony.api.dal.dto.monitor.Statistics;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * Tests for Template communicator
 *
 * @author Maksym.Rossiytsev/AVISPL Team
 * */
public class AMXMuseCommunicatorTest {
    static AMXMuseCommunicator communicator = new AMXMuseCommunicator();

    @BeforeEach
    public void setupBefore() throws Exception {
        communicator.setHost("----");
        communicator.setPort(4197);
        communicator.init();
    }

    @Test
    public void testGetMultipleStatistics() throws Exception {
        List<Statistics> statisticsList = communicator.getMultipleStatistics();
        Assertions.assertNotNull(statisticsList);
        Assertions.assertNotNull(statisticsList.get(0));
        Assertions.assertFalse(((ExtendedStatistics)statisticsList.get(0)).getStatistics().isEmpty());
    }
}
