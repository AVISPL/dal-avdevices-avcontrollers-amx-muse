/*
 * Copyright (c) 2025 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.amx.muse.communicator.data;

import com.avispl.symphony.api.dal.dto.control.AdvancedControllableProperty;

import java.util.ArrayList;
import java.util.List;

import static com.avispl.symphony.dal.util.ControllablePropertyFactory.createSwitch;

/**
 * This class is responsible for storing and generating HControl commands
 *
 * @author Maksym.Rossiitsev/Symphony Team
 * */
public class HControlManager {
    private static List<Command> commandList = new ArrayList<>();

    static {
       commandList.add(new Command("get {\"path\":\"/configuration/device/name\"}\n", "DeviceName", false, false, null));
       commandList.add(new Command("get {\"path\":\"/configuration/device/location\"}\n", "Location", false,false, null));
       commandList.add(new Command("get {\"path\" :\"/configuration/device/version\"}\n", "OSVersion", false, false, null));
       commandList.add(new Command("get {\"path\" :\"/configuration/device/serialnumber\"}\n", "SerialNumber", false, false, null));
       commandList.add(new Command("get {\"path\" :\"configuration/ntp/enable\"}\n", "NTPEnable", false, true, AdvancedControllableProperty.Switch.class));

       commandList.add(new Command("get {\"path\" :\"/configuration/ntp/server/[1-10]/host\"}\n", "NTPServer[n]#Host", true, false, null));
       commandList.add(new Command("get {\"path\" :\"/configuration/ntp/server/[1-10]/keyid\"}\n", "NTPServer[n]#KeyID", true, false, null));
       commandList.add(new Command("get {\"path\" :\"/configuration/ntp/server/[1-10]/keyid \"}\n", "NTPKeyseq[n]#KeyID", true, false, null));
    }

    public static List<Command> getCommandList() {
        return commandList;
    }

    public static class Command {
        private String command;
        private String propertyName;
        private boolean complex;
        private boolean hasControl;
        private Class<?> controlType;

        public Command(String command, String propertyName, boolean complex, boolean hasControl, Class<?> controlType) {
            this.command = command;
            this.propertyName = propertyName;
            this.complex = complex;
            this.hasControl = hasControl;
            this.controlType = controlType;
        }

        /**
         * Retrieves {@link #command}
         *
         * @return value of {@link #command}
         */
        public String getCommand() {
            return command;
        }

        /**
         * Sets {@link #command} value
         *
         * @param command new value of {@link #command}
         */
        public void setCommand(String command) {
            this.command = command;
        }

        /**
         * Retrieves {@link #propertyName}
         *
         * @return value of {@link #propertyName}
         */
        public String getPropertyName() {
            return propertyName;
        }

        /**
         * Sets {@link #propertyName} value
         *
         * @param propertyName new value of {@link #propertyName}
         */
        public void setPropertyName(String propertyName) {
            this.propertyName = propertyName;
        }

        /**
         * Retrieves {@link #complex}
         *
         * @return value of {@link #complex}
         */
        public boolean isComplex() {
            return complex;
        }

        /**
         * Sets {@link #complex} value
         *
         * @param complex new value of {@link #complex}
         */
        public void setComplex(boolean complex) {
            this.complex = complex;
        }

        /**
         * Retrieves {@link #hasControl}
         *
         * @return value of {@link #hasControl}
         */
        public boolean isHasControl() {
            return hasControl;
        }

        /**
         * Sets {@link #hasControl} value
         *
         * @param hasControl new value of {@link #hasControl}
         */
        public void setHasControl(boolean hasControl) {
            this.hasControl = hasControl;
        }

        /**
         * Retrieves {@link #controlType}
         *
         * @return value of {@link #controlType}
         */
        public Class<?> getControlType() {
            return controlType;
        }

        /**
         * Sets {@link #controlType} value
         *
         * @param controlType new value of {@link #controlType}
         */
        public void setControlType(Class<?> controlType) {
            this.controlType = controlType;
        }
    }
}
