package wint.lang.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class NetworkUtil {
	
	private static final String DEFAULT_LOCALHOST = "127.0.0.1";
	
	public static String getLocationAddress() {
		String[] addresses = getAllLocalHostIP();
		if (addresses == null || addresses.length == 0) {
			return DEFAULT_LOCALHOST;
		}
		for (String address : addresses) {
			if (DEFAULT_LOCALHOST.equals(address)) {
				continue;
			}
			return address;
		}
		return DEFAULT_LOCALHOST;
	}
	
	@SuppressWarnings("unchecked")
	private static String[] getAllLocalHostIP() {  
        List<String> res = new ArrayList<String>();  
        Enumeration netInterfaces;  
        try {  
            netInterfaces = NetworkInterface.getNetworkInterfaces();  
            InetAddress ip = null;  
            while (netInterfaces.hasMoreElements()) {  
                NetworkInterface ni = (NetworkInterface) netInterfaces  
                        .nextElement();  
                Enumeration nii = ni.getInetAddresses();  
                while (nii.hasMoreElements()) {  
                    ip = (InetAddress) nii.nextElement();  
                    if (ip.getHostAddress().indexOf(":") == -1) {  
                        res.add(ip.getHostAddress());  
                    }  
                }  
            }  
        } catch (SocketException e) {  
            throw new RuntimeException(e);
        }  
        return (String[]) res.toArray(new String[0]);  
    }  
  
    public String getMacAddress(String host) {  
        String mac = "";  
        StringBuffer sb = new StringBuffer();  
        try {  
            NetworkInterface ni = NetworkInterface.getByInetAddress(InetAddress  
                    .getByName(host));  
            byte[] macs = ni.getHardwareAddress();  
            for (int i = 0; i < macs.length; i++) {  
                mac = Integer.toHexString(macs[i] & 0xFF);  
                if (mac.length() == 1) {  
                    mac = '0' + mac;  
                }  
                sb.append(mac + "-");  
            }  
        } catch (SocketException e) {  
            e.printStackTrace();  
        } catch (UnknownHostException e) {  
            e.printStackTrace();  
        }
        mac = sb.toString();  
        mac = mac.substring(0, mac.length() - 1);  
        return mac;  
    }  
  
}
