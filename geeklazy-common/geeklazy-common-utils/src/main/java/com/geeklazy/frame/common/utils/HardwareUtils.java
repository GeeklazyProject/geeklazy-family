package com.geeklazy.frame.common.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author heliuslee@live.cn
 * @Date 2018/12/20 10:47
 * @Description
 */
public class HardwareUtils {
    private final static String OPERATE_SYSTEM = System.getProperty("os.name");
    private final static Pattern IP_PATTERN = Pattern.compile("(?<=(0\\.){4}\\t|\\s{1,})([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}");
    private final static Pattern MAC_ADDRESS_PATTERN = Pattern.compile("((([0-9,A-Fa-f]{1,2}" + getMacSeparator(OPERATE_SYSTEM) + "){1,5})[0-9,A-Fa-f]{1,2})");

    /**
     * @param cmd
     * @return
     */
    public static String callCmd(String[] cmd) {
        StringBuilder result = new StringBuilder();
        String line;
        try {
            Process proc = Runtime.getRuntime().exec(cmd);
            InputStreamReader is = new InputStreamReader(proc.getInputStream());
            BufferedReader br = new BufferedReader(is);
            while ((line = br.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    /**
     * @param cmd     第一个命令
     * @param another 第二个命令
     * @return 第二个命令的执行结果
     */
    public static String callCmd(String[] cmd, String[] another) {
        StringBuilder result = new StringBuilder();
        String line;
        try {
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec(cmd);
            proc.waitFor(); //已经执行完第一个命令，准备执行第二个命令
            proc = rt.exec(another);
            InputStreamReader is = new InputStreamReader(proc.getInputStream());
            BufferedReader br = new BufferedReader(is);
            while ((line = br.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    public static String getMacSeparator(String os) {
        if (os != null && os.toLowerCase().contains("linux")) return ":";
        else return "-";
    }

    /**
     * 获取MAC地址
     *
     * @return 返回MAC地址
     */
    public static String getMacAddress(String ip) {
        if (OPERATE_SYSTEM != null && OPERATE_SYSTEM.toLowerCase().contains("linux")) {
            return getMacAddressOfLinux(ip).trim();
        }
        return getMacAddressOfWindows(ip).trim();
    }

    /**
     * @param ip 目标ip
     * @return Mac Address
     */
    public static String getMacAddressOfWindows(final String ip) {
        String result;
        String[] cmd = {
                "cmd",
                "/c",
                "ping " + ip
        };
        String[] another = {
                "cmd",
                "/c",
                "arp -a"
        };

        String cmdResult = callCmd(cmd, another);
        result = filterMacAddress(ip, cmdResult, getMacSeparator("windows"));
        return result;
    }

    /**
     * @param ip 目标ip
     * @return Mac Address
     */
    public static String getMacAddressOfLinux(final String ip) {
        String result;
        String[] cmd = {"sudo",
                "/bin/sh",
                "-c",
                String.format("nmap -sP %s  && arp -a %s", ip, ip)
        };
        String cmdResult = callCmd(cmd);
        result = filterMacAddress(ip, cmdResult, getMacSeparator(OPERATE_SYSTEM));
        return result.replace(getMacSeparator(OPERATE_SYSTEM), getMacSeparator("windows"));
    }

    /**
     * @param ip           目标ip,一般在局域网内
     * @param sourceString 命令处理的结果字符串
     * @param macSeparator mac分隔符号
     * @return mac地址，用上面的分隔符号表示
     */
    private static String filterMacAddress(final String ip, String sourceString, final String macSeparator) {
        String result = "";

        int index = sourceString.indexOf(ip);

        if (index == -1)
            index = 0;

        sourceString = sourceString.substring(index, sourceString.length() - 1);
        Matcher matcher = MAC_ADDRESS_PATTERN.matcher(sourceString);
        while (matcher.find()) {
            result = matcher.group(1);
            if (sourceString.indexOf(ip) <= sourceString.lastIndexOf(matcher.group(1))) {
                break; //如果有多个IP,只匹配本IP对应的Mac.
            }
        }

        return result.toUpperCase();
    }

    public static String getDefaultGateway() {
        String os = System.getProperty("os.name");
        if (os != null && os.toLowerCase().contains("linux")) {
            return getDefaultGatewayInLinux();
        }
        return getDefaultGatewayInWindows();
    }

    private static String getDefaultGatewayInWindows() {
        String[] cmd = {"cmd",
                "/c",
                "route PRINT"};
        String s = callCmd(cmd);
        Matcher matcher = IP_PATTERN.matcher(s);
        if (matcher.find())
            return matcher.group();
        return null;
    }

    private static String getDefaultGatewayInLinux() {
        String[] cmd = {"sudo", "/bin/sh",
                "-c",
                "route -n"};
        String s = callCmd(cmd);
        Matcher matcher = IP_PATTERN.matcher(s);
        if (matcher.find())
            return matcher.group();
        return null;
    }

    public static String getMacAddress() {
        try {
            InetAddress ip = null;
            if (OPERATE_SYSTEM.contains("Darwin")) {
                ip = NetworkInterface.getByName("en0").getInetAddresses().nextElement();
            } else {
                ip = InetAddress.getLocalHost();
            }
            NetworkInterface networkInterface = NetworkInterface.getByInetAddress(ip);
            return getMac(networkInterface);
        } catch (UnknownHostException | SocketException e) {
            return null;
        }
    }

    public static String getMac(String networkInterfaceName) {
        NetworkInterface networkInterface = getNetworkInterfaceByName(networkInterfaceName);
        if (networkInterface == null) return null;
        return getMac(networkInterface);
    }

    public static String getMac(NetworkInterface networkInterface) {
        try {
            return address2Mac(networkInterface.getHardwareAddress());
        } catch (SocketException e) {
            return null;
        }
    }

    public static String address2Mac(byte[] bytes) {
        if (bytes == null) return null;

        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            if (!sb.toString().equals("")) sb.append("-");
//				str.append(byteHEX(aBuf));
            sb.append(String.format("%02X", b));
        }
        return sb.toString().toUpperCase();
    }

    private static String byteHEX(byte ib) {
        char[] Digit = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a',
                'b', 'c', 'd', 'e', 'f'};
        char[] ob = new char[2];
        ob[0] = Digit[(ib >>> 4) & 0X0F];
        ob[1] = Digit[ib & 0X0F];
        return new String(ob);
    }

    public static String getLocalIP(String networkInterfaceName) {
        InetAddress inet4Address = getInet4Address(networkInterfaceName);
        if (inet4Address == null) return null;
        return inet4Address.getHostAddress();
    }

    public static Enumeration<NetworkInterface> getNetworkInterfaces() {
        try {
            return NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            return null;
        }
    }

    private static NetworkInterface getNetworkInterfaceByName(String name) {
        try {
            return NetworkInterface.getByName(name);
        } catch (SocketException e) {
            return null;
        }
    }

    public static InetAddress getInet4Address(String name) {
        NetworkInterface networkInterface = getNetworkInterfaceByName(name);
        if (networkInterface == null) return null;
        Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
        while (inetAddresses.hasMoreElements()) {
            InetAddress inetAddress = inetAddresses.nextElement();
            if (inetAddress instanceof Inet4Address)
                return inetAddress;
        }
        return null;
    }

    public static Integer getCpuTemp() {
        String os = System.getProperty("os.name");
        if (os != null && os.toLowerCase().contains("linux")) {
            String[] cmd = {"sudo", "/bin/sh",
                    "-c",
                    "cat /sys/class/thermal/thermal_zone0/temp"};
            String temp = callCmd(cmd);
            return Integer.valueOf(temp);
        }
        return null;

    }
}
