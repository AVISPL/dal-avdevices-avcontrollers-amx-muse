/*
 * Copyright (c) 2025 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.amx.muse.communicator;

import com.avispl.symphony.api.dal.control.Controller;
import com.avispl.symphony.api.dal.dto.control.AdvancedControllableProperty;
import com.avispl.symphony.api.dal.dto.control.ControllableProperty;
import com.avispl.symphony.api.dal.dto.monitor.ExtendedStatistics;
import com.avispl.symphony.api.dal.dto.monitor.Statistics;
import com.avispl.symphony.api.dal.error.CommandFailureException;
import com.avispl.symphony.api.dal.monitor.Monitorable;
import com.avispl.symphony.dal.amx.muse.communicator.data.Constant;
import com.avispl.symphony.dal.communicator.SocketCommunicator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections.CollectionUtils;

import java.util.*;

import static com.avispl.symphony.dal.util.ControllablePropertyFactory.createButton;

/**
 * HControl based communicator, targeted to AMX Muse devices.
 * Currently supported list of monitored data/functionality:
 * - Adapter metadata
 * - Device Name
 * - Location
 * - NTP Status
 * - OS Version
 * - Serial Number
 * - Reboot
 *
 * @author Maksym.Rossiitsev/Symphony Team
 */
public class AMXMuseCommunicator extends SocketCommunicator implements Monitorable, Controller {
    ObjectMapper objectMapper = new ObjectMapper();
    /**
     * Adapter metadata, collected from the version.properties
     */
    private Properties adapterProperties;
    /**
     * Device adapter instantiation timestamp.
     */
    private long adapterInitializationTimestamp;

    @Override
    protected void internalInit() throws Exception {
        adapterInitializationTimestamp = System.currentTimeMillis();
        adapterProperties = new Properties();
        adapterProperties.load(getClass().getResourceAsStream("/version.properties"));

        this.setCommandSuccessList(Arrays.asList("@get", "@set", "@exec", "@"));
        this.setCommandErrorList(Arrays.asList("@unrecognized"));
        super.internalInit();
    }

    @Override
    public void controlProperty(ControllableProperty controllableProperty) throws Exception {
        String propertyName = controllableProperty.getProperty();
        Object value = controllableProperty.getValue();

        switch (propertyName) {
            case "Reboot":
                reboot();
            break;
            case "NTP":
                updateNTPState(value);
            break;
            default:
                logger.warn(String.format("Operation %s is not supported", propertyName));
            break;
        }
    }

    @Override
    public void controlProperties(List<ControllableProperty> list) throws Exception {
        if (CollectionUtils.isEmpty(list)) {
            throw new IllegalArgumentException("Controllable properties cannot be null or empty");
        }
        for (ControllableProperty controllableProperty : list) {
            controlProperty(controllableProperty);
        }
    }

    @Override
    protected boolean doneReading(String command, String response) throws CommandFailureException {
        if (response.equals("")) {
            return true;
        }
        return super.doneReading(command, response);
    }

    @Override
    protected byte[] send(byte[] data) throws Exception {
        return super.send(data);
    }

    @Override
    public List<Statistics> getMultipleStatistics() throws Exception {
        ExtendedStatistics extendedStatistics = new ExtendedStatistics();
        Map<String, String> statistics = new HashMap<>();
        List<AdvancedControllableProperty> controls = new ArrayList<>();
//        super.send(new byte[]{'\n'});

        long adapterUptime = System.currentTimeMillis() - adapterInitializationTimestamp;
        statistics.put(Constant.Property.METADATA_UPTIME, normalizeUptime(adapterUptime/1000));
        statistics.put(Constant.Property.METADATA_UPTIME_MIN, String.valueOf(adapterUptime / (1000*60)));
        statistics.put(Constant.Property.METADATA_BUILD_DATE, adapterProperties.getProperty("adapter.build.date"));
        statistics.put(Constant.Property.METADATA_VERSION, adapterProperties.getProperty("adapter.version"));

        statistics.put(Constant.Property.DEVICE_NAME, extractResponseValue(send(Constant.Command.DEVICE_NAME.getBytes())));
        statistics.put(Constant.Property.LOCATION, extractResponseValue(send(Constant.Command.LOCATION.getBytes())));
        statistics.put(Constant.Property.OS_VERSION, extractResponseValue(send(Constant.Command.OS_VERSION.getBytes())));
        statistics.put(Constant.Property.SERIAL_NUMBER, extractResponseValue(send(Constant.Command.SERIAL_NUMBER.getBytes())));
        boolean ntpEnabled = "true".equals(extractResponseValue(send(Constant.Command.NTP_ENABLE.getBytes())));
        statistics.put(Constant.Property.NTP, ntpEnabled ? "Enabled" : "Disabled");

        statistics.put(Constant.Property.REBOOT, "");
        controls.add(createButton(Constant.Property.REBOOT, Constant.Property.REBOOT, "Rebooting...", 120000L));
//        for (int i = 1; i <= 10; i++) {
//            disconnect();
//            super.send(new byte[]{'\n'});
//            if ("true".equals(statistics.get("NTP"))) {
//                statistics.put(String.format("NTPServer[%s]#Host", i), extractResponseValue(send(String.format(Constant.Command.NTP_SERVER_HOST, i).getBytes())));
//                statistics.put(String.format("NTPServer[%s]#KeyID", i), extractResponseValue(send(String.format(Constant.Command.NTP_SERVER_KEYID, i).getBytes())));
//
//                statistics.put(String.format("NTPKeyseq[%s]#KeyID", i), extractResponseValue(send(String.format(Constant.Command.NTP_KEYSEQ_KEYID, i).getBytes())));
//                statistics.put(String.format("NTPKeyseq[%s]#Encryption", i), extractResponseValue(send(String.format(Constant.Command.NTP_KEYSEQ_ENCRYPTION, i).getBytes())));
//            }
//            if (i <= 2) {
//                statistics.put(String.format("NetworkInterface[%s]#MACAddress", i), extractResponseValue(send(String.format(Constant.Command.NETWORK_INTERFACE_MAC, i).getBytes())));
//                statistics.put(String.format("NetworkInterface[%s]#Gateway", i), extractResponseValue(send(String.format(Constant.Command.NETWORK_INTERFACE_GATEWAY, i).getBytes())));
//                statistics.put(String.format("NetworkInterface[%s]#IPAddress", i), extractResponseValue(send(String.format(Constant.Command.NETWORK_INTERFACE_IP_ADDRESS, i).getBytes())));
//                statistics.put(String.format("NetworkInterface[%s]#DHCP", i), extractResponseValue(send(String.format(Constant.Command.NETWORK_INTERFACE_DHCP, i).getBytes())));

//                for (int j = 1; j <= 3; j++) {
//                    statistics.put(String.format("NetworkInterface[%s]#DNSServer[s]", i, j), extractResponseValue(send(String.format(Constant.Command.NETWORK_INTERFACE_DNS_SERVER, i, j).getBytes())));
//                }
//                statistics.put(String.format("NetworkInterface[%s]#SubnetMask", i), extractResponseValue(send(String.format(Constant.Command.NETWORK_INTERFACE_SUBNET_MASK, i).getBytes())));
//            }
//        }
        disconnect();
        extendedStatistics.setStatistics(statistics);
        extendedStatistics.setControllableProperties(controls);
        return Arrays.asList(extendedStatistics);
    }

    /**
     * Extract the response value from device response with format
     * @get {"path":"$path","value":$value}
     *
     * @param response raw byte array response
     * @return String value of the command response
     *
     * @throws Exception if any communication error occurs
     * */
    private String extractResponseValue(byte[] response) throws Exception {
        String strResponse = new String(response);
        if (!strResponse.contains("{") || !strResponse.contains("}")) {
            throw new RuntimeException();
        }
        String jsonResponse = strResponse.substring(strResponse.indexOf("{")-1);
        JsonNode responseObject = objectMapper.readTree(jsonResponse);
        return responseObject.at("/value").asText();
    }

    /**
     * Send the reboot command to the device, preceded by the new line character for clarity,
     * drop the connection afterwards.
     *
     * @throws Exception if any communication error occurs
     * */
    private void reboot() throws Exception {
        super.send(new byte[]{'\n'});
        send(Constant.Command.REBOOT.getBytes());
        disconnect();
    }

    /**
     * Send the NTP status update command to the device, preceded by the new line character for clarity,
     * drop the connection afterwards.
     *
     * @param state new NTP state value
     * @throws Exception if any error occurs during the communication
     * */
    private void updateNTPState(Object state) throws Exception {
        super.send(new byte[]{'\n'});
        send(String.format(Constant.Command.NTP_ENABLE_COMMAND, state).getBytes());
        disconnect();
    }

    /**
     * Uptime is received in seconds, need to normalize it and make it human readable, like
     * 1 day(s) 5 hour(s) 12 minute(s) 55 minute(s)
     * Incoming parameter is may have a decimal point, so in order to safely process this - it's rounded first.
     * We don't need to add a segment of time if it's 0.
     *
     * @param uptimeSeconds value in seconds
     * @return string value of format 'x day(s) x hour(s) x minute(s) x minute(s)'
     */
    private String normalizeUptime(long uptimeSeconds) {
        StringBuilder normalizedUptime = new StringBuilder();

        long seconds = uptimeSeconds % 60;
        long minutes = uptimeSeconds % 3600 / 60;
        long hours = uptimeSeconds % 86400 / 3600;
        long days = uptimeSeconds / 86400;

        if (days > 0) {
            normalizedUptime.append(days).append(" day(s) ");
        }
        if (hours > 0) {
            normalizedUptime.append(hours).append(" hour(s) ");
        }
        if (minutes > 0) {
            normalizedUptime.append(minutes).append(" minute(s) ");
        }
        if (seconds > 0) {
            normalizedUptime.append(seconds).append(" second(s)");
        }
        return normalizedUptime.toString().trim();
    }
}
