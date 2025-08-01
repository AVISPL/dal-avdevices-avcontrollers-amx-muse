/*
 * Copyright (c) 2025 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.amx.muse.communicator.data;

/**
 * Constant values storage for AMXMuseCommunicator use
 * */
public interface Constant {
    /**
     * List of HControl commands supported by the AMX devices
     * */
    interface Command {
        String DEVICE_NAME = " get {\"path\":\"/configuration/device/name\"}\n";
        String LOCATION = " get {\"path\":\"/configuration/device/location\"}\n";
        String OS_VERSION = " get {\"path\" :\"/configuration/device/version\"}\n";
        String SERIAL_NUMBER = " get {\"path\" :\"/configuration/device/serialnumber\"}\n";
        String NTP_ENABLE = " get {\"path\" :\"configuration/ntp/enable\"}\n";
        String NTP_ENABLE_COMMAND = " set {\"path\":\"/configuration/ntp/enable\",\"value\":%s}\n";
        String REBOOT = "reboot\n";
        String NTP_SERVER_HOST = " get {\"path\" :\"/configuration/ntp/server/%s/host\"}\n"; //1-10
        String NTP_SERVER_KEYID = " get {\"path\" :\"/configuration/ntp/server/%s/keyid\"}\n"; //1-10
        String NTP_KEYSEQ_KEYID = " get {\"path\" :\"configuration/ntp/keyseq/%s/keyid\"}"; //1-10
        String NTP_KEYSEQ_KEYPASSWORD = " get {\"path\" :\"configuration/ntp/keyseq/%s/keypassword\"}"; //1-10
        String NTP_KEYSEQ_ENCRYPTION = " get {\"path\" :\"configuration/ntp/keyseq/%s/encryption\"}"; //1-10
        String NETWORK_INTERFACE_IP_ADDRESS = " get {\"path\" :\"/configuration/network/interface/%s/ipv4/ip_address\"}"; //1-2
        String NETWORK_INTERFACE_SUBNET_MASK = " get {\"path\" :\"/configuration/network/interface/%s/ipv4/subnetmask\"}"; //1-2
        String NETWORK_INTERFACE_GATEWAY = " get {\"path\" :\"/configuration/network/interface/%s/ipv4/gateway\"}"; //1-2
        String NETWORK_INTERFACE_DHCP = " get {\"path\":\"configuration/network/interface/%s/ipv4/dhcp\"}"; //1-2
        String NETWORK_INTERFACE_DNS_SERVER = " get {\"path\" :\"/configuration/network/interface/%s/dnsserver/%s\"}"; //1-2, 1-3
        String NETWORK_INTERFACE_MAC = " get {\"path\" :\"/configuration/network/interface/%s/mac\"}"; //1-2
    }

    /**
     * Constant property names for AMXMuseCommunicator use
     */
    interface Property {
        String METADATA_UPTIME = "AdapterMetadata#AdapterUptime";
        String METADATA_UPTIME_MIN = "AdapterMetadata#AdapterUptime(min)";
        String METADATA_BUILD_DATE = "AdapterMetadata#AdapterBuildDate";
        String METADATA_VERSION = "AdapterMetadata#AdapterVersion";

        String DEVICE_NAME = "DeviceName";
        String LOCATION = "Location";
        String OS_VERSION = "OSVersion";
        String SERIAL_NUMBER = "SerialNumber";
        String NTP = "NTP";
        String REBOOT = "Reboot";
    }
}
