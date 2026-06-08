package cn.holdmoral.forever.util;

import cn.holdmoral.forever.model.FixedEnvConfigDto;
import org.springframework.util.StringUtils;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.time.LocalTime;

/**
 * @author lujianhua
 * @version 1.0
 * @company holdmoral
 * @date 2021/12/23 20:14
 */
public class ByteUtil {
    public static void main(String[] args) {
        getDeviceStatus("FF3333FF120082010F0023230000000000000000000001B1");
        //System.out.println(deviceNumber);
        //String str = getContentHexString("FF3333FF12008101C100000000000000000000000000C11F",16);  01
        //FF3333FF120082010F0023230000000000000000000001B1    02
        //FF3333FF22008301DF00000000E50701000100005A000022640000000000000000000000000048B2   03
        //FF3333FF22008401DC00DC00DC00DC00DC000A141E28320000000000000000000000000000004434   04
        //FF3333FF120085010000010000000000000000000000C020    05
        //FF3333FF120086010000000000000000000000000000F29D    06
        //FF3333FF120087010000000000000000000000000000625C     07
    }

    public static void getTemperature(String hex) {
        String content = hex.replace("FF3333FF", "");
        String hexContent = content.substring(4).substring(0, 32);
        String deviceNumber = hexContent.substring(2, 4);
        int number = new BigInteger(deviceNumber, 16).intValue();
        System.out.println("设备编号 ：" + number);
        String temperatureString = hexContent.substring(4, 8);
        int temperature = localHexString2Int(temperatureString);
        System.out.println("温度：" + temperature);
        String dioxideHex = hexContent.substring(8, 12);
        int dioxide = localHexString2Int(dioxideHex);
        System.out.println("二氧化碳：" + dioxide);
        String nh3Hex = hexContent.substring(12, 16);
        int nh3 = localHexString2Int(nh3Hex);
        System.out.println("氨气：" + nh3);
        String humidityHex = hexContent.substring(16, 20);
        int humidity = localHexString2Int(humidityHex);
        System.out.println("湿度：" + humidity);
    }

    public static String getCommand(String hex) {
        return hex.replace("FF3333FF", "").substring(4).substring(0, 2);
    }

    public static void getDeviceStatus(String hex) {
        String content = hex.replace("FF3333FF", "");
        String hexContent = content.substring(4).substring(0, 32);
        String deviceNumber = hexContent.substring(2, 4);
        int number = new BigInteger(deviceNumber, 16).intValue();
        System.out.println("设备编号 ：" + number);
        String windStatus = hexContent.substring(4, 8);
//        int temperature = localHexString2Int(temperatureString);
//        System.out.println("温度："+temperature);
        String thkdString = hexContent.substring(8, 10);
        int thkd = localHexString2Int(thkdString);
        String jlkdString = hexContent.substring(10, 12);
        int jlkd = localHexString2Int(jlkdString);
    }

    public static int localHexString2Int(String hex) {
        byte[] bytes = hexString2Bytes(hex);
        if (bytes.length < 4) {
            byte[] newBytes = new byte[4];
            newBytes[0] = 0;
            newBytes[1] = 0;
            if (bytes.length == 2)
                newBytes[2] = bytes[1];
            else {
                newBytes[2] = 0;
            }
            newBytes[3] = bytes[0];
            int number = byteArrayToInt(newBytes);
            return number;
        }
        return byteArrayToInt(bytes);
    }

    public static int hex16To10(String hex) {
        BigInteger value = new BigInteger(hex, 16);
        return value.intValue();
    }

    public static String hex10To16FourBit(int value) {
        return String.format("%04X", value);
    }

    public static int getDeviceNumber(String hex) {
        String content = hex.replace("FF3333FF", "");
//        String lengthString = content.substring(0,4);
//        int length = localHexString2Int(lengthString);
        String hexContent = content.substring(4).substring(0, 32);
        String deviceNumber = hexContent.substring(2, 4);
        int number = new BigInteger(deviceNumber, 16).intValue();
        return number;
    }

    public static String getCRC2(byte[] bytes) {
//        ModBus 通信协议的 CRC ( 冗余循环校验码含2个字节, 即 16 位二进制数。
//        CRC 码由发送设备计算, 放置于所发送信息帧的尾部。
//        接收信息设备再重新计算所接收信息 (除 CRC 之外的部分）的 CRC,
//        比较计算得到的 CRC 是否与接收到CRC相符, 如果两者不相符, 则认为数据出错。
//
//        1) 预置 1 个 16 位的寄存器为十六进制FFFF(即全为 1) , 称此寄存器为 CRC寄存器。
//        2) 把第一个 8 位二进制数据 (通信信息帧的第一个字节) 与 16 位的 CRC寄存器的低 8 位相异或, 把结果放于 CRC寄存器。
//        3) 把 CRC 寄存器的内容右移一位( 朝低位)用 0 填补最高位, 并检查右移后的移出位。
//        4) 如果移出位为 0, 重复第 3 步 ( 再次右移一位); 如果移出位为 1, CRC 寄存器与多项式A001 ( 1010 0000 0000 0001) 进行异或。
//        5) 重复步骤 3 和步骤 4, 直到右移 8 次,这样整个8位数据全部进行了处理。
//        6) 重复步骤 2 到步骤 5, 进行通信信息帧下一个字节的处理。
//        7) 将该通信信息帧所有字节按上述步骤计算完成后,得到的16位CRC寄存器的高、低字节进行交换。
//        8) 最后得到的 CRC寄存器内容即为 CRC码。

        int CRC = 0x0000ffff;
        int POLYNOMIAL = 0x0000a001;

        int i, j;
        for (i = 0; i < bytes.length; i++) {
            CRC ^= (int) bytes[i];
            for (j = 0; j < 8; j++) {
                if ((CRC & 0x00000001) == 1) {
                    CRC >>= 1;
                    CRC ^= POLYNOMIAL;
                } else {
                    CRC >>= 1;
                }
            }
        }
        //高低位转换，看情况使用（譬如本人这次对led彩屏的通讯开发就规定校验码高位在前低位在后，也就不需要转换高低位)
        //CRC = ( (CRC & 0x0000FF00) >> 8) | ( (CRC & 0x000000FF ) << 8);
        return Integer.toHexString(CRC);
    }

    public static String getCRC3(byte[] data) {
        byte[] crc16_h = {
                (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40,
                (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41,
                (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41,
                (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40,
                (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41,
                (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40,
                (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40,
                (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41,
                (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41,
                (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40,
                (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40,
                (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41,
                (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40,
                (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41,
                (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41,
                (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40
        };

        byte[] crc16_l = {
                (byte) 0x00, (byte) 0xC0, (byte) 0xC1, (byte) 0x01, (byte) 0xC3, (byte) 0x03, (byte) 0x02, (byte) 0xC2, (byte) 0xC6, (byte) 0x06, (byte) 0x07, (byte) 0xC7, (byte) 0x05, (byte) 0xC5, (byte) 0xC4, (byte) 0x04,
                (byte) 0xCC, (byte) 0x0C, (byte) 0x0D, (byte) 0xCD, (byte) 0x0F, (byte) 0xCF, (byte) 0xCE, (byte) 0x0E, (byte) 0x0A, (byte) 0xCA, (byte) 0xCB, (byte) 0x0B, (byte) 0xC9, (byte) 0x09, (byte) 0x08, (byte) 0xC8,
                (byte) 0xD8, (byte) 0x18, (byte) 0x19, (byte) 0xD9, (byte) 0x1B, (byte) 0xDB, (byte) 0xDA, (byte) 0x1A, (byte) 0x1E, (byte) 0xDE, (byte) 0xDF, (byte) 0x1F, (byte) 0xDD, (byte) 0x1D, (byte) 0x1C, (byte) 0xDC,
                (byte) 0x14, (byte) 0xD4, (byte) 0xD5, (byte) 0x15, (byte) 0xD7, (byte) 0x17, (byte) 0x16, (byte) 0xD6, (byte) 0xD2, (byte) 0x12, (byte) 0x13, (byte) 0xD3, (byte) 0x11, (byte) 0xD1, (byte) 0xD0, (byte) 0x10,
                (byte) 0xF0, (byte) 0x30, (byte) 0x31, (byte) 0xF1, (byte) 0x33, (byte) 0xF3, (byte) 0xF2, (byte) 0x32, (byte) 0x36, (byte) 0xF6, (byte) 0xF7, (byte) 0x37, (byte) 0xF5, (byte) 0x35, (byte) 0x34, (byte) 0xF4,
                (byte) 0x3C, (byte) 0xFC, (byte) 0xFD, (byte) 0x3D, (byte) 0xFF, (byte) 0x3F, (byte) 0x3E, (byte) 0xFE, (byte) 0xFA, (byte) 0x3A, (byte) 0x3B, (byte) 0xFB, (byte) 0x39, (byte) 0xF9, (byte) 0xF8, (byte) 0x38,
                (byte) 0x28, (byte) 0xE8, (byte) 0xE9, (byte) 0x29, (byte) 0xEB, (byte) 0x2B, (byte) 0x2A, (byte) 0xEA, (byte) 0xEE, (byte) 0x2E, (byte) 0x2F, (byte) 0xEF, (byte) 0x2D, (byte) 0xED, (byte) 0xEC, (byte) 0x2C,
                (byte) 0xE4, (byte) 0x24, (byte) 0x25, (byte) 0xE5, (byte) 0x27, (byte) 0xE7, (byte) 0xE6, (byte) 0x26, (byte) 0x22, (byte) 0xE2, (byte) 0xE3, (byte) 0x23, (byte) 0xE1, (byte) 0x21, (byte) 0x20, (byte) 0xE0,
                (byte) 0xA0, (byte) 0x60, (byte) 0x61, (byte) 0xA1, (byte) 0x63, (byte) 0xA3, (byte) 0xA2, (byte) 0x62, (byte) 0x66, (byte) 0xA6, (byte) 0xA7, (byte) 0x67, (byte) 0xA5, (byte) 0x65, (byte) 0x64, (byte) 0xA4,
                (byte) 0x6C, (byte) 0xAC, (byte) 0xAD, (byte) 0x6D, (byte) 0xAF, (byte) 0x6F, (byte) 0x6E, (byte) 0xAE, (byte) 0xAA, (byte) 0x6A, (byte) 0x6B, (byte) 0xAB, (byte) 0x69, (byte) 0xA9, (byte) 0xA8, (byte) 0x68,
                (byte) 0x78, (byte) 0xB8, (byte) 0xB9, (byte) 0x79, (byte) 0xBB, (byte) 0x7B, (byte) 0x7A, (byte) 0xBA, (byte) 0xBE, (byte) 0x7E, (byte) 0x7F, (byte) 0xBF, (byte) 0x7D, (byte) 0xBD, (byte) 0xBC, (byte) 0x7C,
                (byte) 0xB4, (byte) 0x74, (byte) 0x75, (byte) 0xB5, (byte) 0x77, (byte) 0xB7, (byte) 0xB6, (byte) 0x76, (byte) 0x72, (byte) 0xB2, (byte) 0xB3, (byte) 0x73, (byte) 0xB1, (byte) 0x71, (byte) 0x70, (byte) 0xB0,
                (byte) 0x50, (byte) 0x90, (byte) 0x91, (byte) 0x51, (byte) 0x93, (byte) 0x53, (byte) 0x52, (byte) 0x92, (byte) 0x96, (byte) 0x56, (byte) 0x57, (byte) 0x97, (byte) 0x55, (byte) 0x95, (byte) 0x94, (byte) 0x54,
                (byte) 0x9C, (byte) 0x5C, (byte) 0x5D, (byte) 0x9D, (byte) 0x5F, (byte) 0x9F, (byte) 0x9E, (byte) 0x5E, (byte) 0x5A, (byte) 0x9A, (byte) 0x9B, (byte) 0x5B, (byte) 0x99, (byte) 0x59, (byte) 0x58, (byte) 0x98,
                (byte) 0x88, (byte) 0x48, (byte) 0x49, (byte) 0x89, (byte) 0x4B, (byte) 0x8B, (byte) 0x8A, (byte) 0x4A, (byte) 0x4E, (byte) 0x8E, (byte) 0x8F, (byte) 0x4F, (byte) 0x8D, (byte) 0x4D, (byte) 0x4C, (byte) 0x8C,
                (byte) 0x44, (byte) 0x84, (byte) 0x85, (byte) 0x45, (byte) 0x87, (byte) 0x47, (byte) 0x46, (byte) 0x86, (byte) 0x82, (byte) 0x42, (byte) 0x43, (byte) 0x83, (byte) 0x41, (byte) 0x81, (byte) 0x80, (byte) 0x40
        };

        int crc = 0x0000ffff;
        int ucCRCHi = 0x00ff;
        int ucCRCLo = 0x00ff;
        int iIndex;
        for (int i = 0; i < data.length; ++i) {
            iIndex = (ucCRCLo ^ data[i]) & 0x00ff;
            ucCRCLo = ucCRCHi ^ crc16_h[iIndex];
            ucCRCHi = crc16_l[iIndex];
        }

        crc = ((ucCRCHi & 0x00ff) << 8) | (ucCRCLo & 0x00ff) & 0xffff;
        //高低位互换，输出符合相关工具对Modbus CRC16的运算
        //crc = ( (crc & 0xFF00) >> 8) | ( (crc & 0x00FF ) << 8);
        return String.format("%04X", crc);
    }

    /*
     * 字符转换为字节
     */
    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    /*
     * 16进制字符串转字节数组
     */
    public static byte[] hexString2Bytes(String hex) {

        if ((hex == null) || (hex.equals(""))) {
            return null;
        } else if (hex.length() % 2 != 0) {
            return null;
        } else {
            hex = hex.toUpperCase();
            int len = hex.length() / 2;
            byte[] b = new byte[len];
            char[] hc = hex.toCharArray();
            for (int i = 0; i < len; i++) {
                int p = 2 * i;
                b[i] = (byte) (charToByte(hc[p]) << 4 | charToByte(hc[p + 1]));
            }
            return b;
        }

    }

    /*
     * 字节数组转16进制字符串
     */
    public static String bytes2HexString(byte[] b) {
        String r = "";

        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            r += hex.toUpperCase();
        }

        return r;
    }

    public static byte[] shortToByteArray(short s) {
        byte[] shortBuf = new byte[2];
        for (int i = 0; i < 2; i++) {
            int offset = (shortBuf.length - 1 - i) * 8;
            shortBuf[i] = (byte) ((s >>> offset) & 0xff);
        }
        return shortBuf;
    }

    public static byte[] intToByteLittle(int n) {
        byte[] b = new byte[2];
        b[0] = (byte) (n & 0xff);
        b[1] = (byte) (n >> 8 & 0xff);
        return b;
    }

    public static byte[] intToByteTransfer(int n) {
        byte[] b = new byte[1];
        b[0] = (byte) (n & 0xff);
        return b;
    }

    public static byte[] intToByteArray(int value) {
        byte[] b = new byte[4];
        for (int i = 0; i < 4; i++) {
            int offset = (b.length - 1 - i) * 8;
            b[i] = (byte) ((value >>> offset) & 0xFF);
        }
        return b;
    }

    public static final int byteArrayToShort(byte[] b) {
        return (b[0] << 8)
                + (b[1] & 0xFF);
    }

    public static final int byteArrayToInt(byte[] b) {
        return (b[0] << 24)
                + ((b[1] & 0xFF) << 16)
                + ((b[2] & 0xFF) << 8)
                + (b[3] & 0xFF);
    }

    public static byte[] mergeBytes(byte[]... values) {
        int lengthByte = 0;
        for (byte[] value : values) {
            lengthByte += value.length;
        }
        byte[] allBytes = new byte[lengthByte];
        int countLength = 0;
        for (byte[] b : values) {
            System.arraycopy(b, 0, allBytes, countLength, b.length);
            countLength += b.length;
        }
        return allBytes;
    }

    /*
     * 字节数组转字符串
     */
    public static String bytes2String(byte[] b) throws Exception {
        String r = new String(b, "UTF-8");
        return r;
    }

    public static String hex2BinaryString(String hex) {
        int intValue = new BigInteger(hex, 16).intValue();
        return Integer.toBinaryString(intValue);
    }

    public static byte[] hex2ByteArrayLittle(String hex) {
        if (hex.length() == 8) {
            int value = Integer.parseInt(hex, 16);
            ByteBuffer buffer = ByteBuffer.allocate(4);
            buffer.order(ByteOrder.BIG_ENDIAN);
            buffer.asIntBuffer().put(value);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            return buffer.array();
        }
        if (hex.length() == 4) {
            short value = Short.parseShort(hex, 16);
            ByteBuffer buffer = ByteBuffer.allocate(2);
            buffer.order(ByteOrder.BIG_ENDIAN);
            buffer.asShortBuffer().put(value);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            return buffer.array();
        }
        return null;
    }

    public static byte[] changeBytes(byte[] a){
        byte[] b = new byte[(a.length)];
        for(int i=0;i<b.length;i++){
            b[i]=a[b.length-i-1];
        }
        return b;
    }

    public static byte[] getForeverBytes(String command,String contract,int deviceNumber){
        byte[] header = ByteUtil.hexString2Bytes("AA5555AA");
        byte[] data = ByteUtil.mergeBytes(ByteUtil.changeBytes(ByteUtil.hexString2Bytes(command)),ByteUtil.int2Bytes(deviceNumber,ByteOrder.LITTLE_ENDIAN),ByteUtil.changeBytes(ByteUtil.hexString2Bytes(contract)), ByteUtil.hexString2Bytes("0000000000000000000000"));
        byte length = (byte) (data.length);
        byte[] dataLength = ByteUtil.intToByteLittle(length+2);
        byte[] crc = ByteUtil.hexString2Bytes(ByteUtil.getCRC3(ByteUtil.mergeBytes(header, dataLength, data)));
        byte[] message = ByteUtil.mergeBytes(header, dataLength, data, crc);
        return message;
    }
    public static byte[] getForeverTemperatureSetBytes(int deviceNumber,int temp){
        byte[] header = ByteUtil.hexString2Bytes("AA5555AA");
        byte[] data = ByteUtil.mergeBytes(ByteUtil.changeBytes(ByteUtil.hexString2Bytes("4003")),ByteUtil.int2Bytes(deviceNumber,ByteOrder.LITTLE_ENDIAN),ByteUtil.changeBytes(ByteUtil.hexString2Bytes("0001")),ByteUtil.intToByteLittle(temp), ByteUtil.hexString2Bytes("0000000000"));
        byte length = (byte) (data.length);
        byte[] dataLength = ByteUtil.intToByteLittle(length+2);
        byte[] crc = ByteUtil.hexString2Bytes(ByteUtil.getCRC3(ByteUtil.mergeBytes(header, dataLength, data)));
        byte[] message = ByteUtil.mergeBytes(header, dataLength, data, crc);
        return message;
    }

    public static byte[] getLightSetBytes(int deviceNumber, FixedEnvConfigDto dto){
        byte[] header = ByteUtil.hexString2Bytes("AA5555AA");
        byte[] data1 = ByteUtil.mergeBytes(ByteUtil.changeBytes(ByteUtil.hexString2Bytes("4004")),ByteUtil.int2Bytes(deviceNumber,ByteOrder.LITTLE_ENDIAN),ByteUtil.changeBytes(ByteUtil.hexString2Bytes("0001")));
        byte[] mode = ByteUtil.intToByteTransfer(dto.getLightMode());
        byte[] time1;
        if(StringUtils.isEmpty(dto.getLightTime1())){
            time1 = ByteUtil.mergeBytes(ByteUtil.intToByteTransfer(0),ByteUtil.intToByteTransfer(0),ByteUtil.intToByteTransfer(0),ByteUtil.intToByteTransfer(0));
        }else{
            String[] timeArray = dto.getLightTime1().split("-");
            LocalTime start = LocalTime.parse(timeArray[0]);
            LocalTime end = LocalTime.parse(timeArray[1]);
            time1 = ByteUtil.mergeBytes(ByteUtil.intToByteTransfer(start.getHour()),ByteUtil.intToByteTransfer(start.getMinute()),ByteUtil.intToByteTransfer(end.getHour()),ByteUtil.intToByteTransfer(end.getMinute()));
        }
        byte[] time2;
        if(StringUtils.isEmpty(dto.getLightTime2())){
            time2 = ByteUtil.mergeBytes(ByteUtil.intToByteTransfer(0),ByteUtil.intToByteTransfer(0),ByteUtil.intToByteTransfer(0),ByteUtil.intToByteTransfer(0));
        }else{
            String[] timeArray = dto.getLightTime2().split("-");
            LocalTime start = LocalTime.parse(timeArray[0]);
            LocalTime end = LocalTime.parse(timeArray[1]);
            time2 = ByteUtil.mergeBytes(ByteUtil.intToByteTransfer(start.getHour()),ByteUtil.intToByteTransfer(start.getMinute()),ByteUtil.intToByteTransfer(end.getHour()),ByteUtil.intToByteTransfer(end.getMinute()));
        }
        byte[] time3;
        if(StringUtils.isEmpty(dto.getLightTime3())){
            time3 = ByteUtil.mergeBytes(ByteUtil.intToByteTransfer(0),ByteUtil.intToByteTransfer(0),ByteUtil.intToByteTransfer(0),ByteUtil.intToByteTransfer(0));
        }else{
            String[] timeArray = dto.getLightTime3().split("-");
            LocalTime start = LocalTime.parse(timeArray[0]);
            LocalTime end = LocalTime.parse(timeArray[1]);
            time3 = ByteUtil.mergeBytes(ByteUtil.intToByteTransfer(start.getHour()),ByteUtil.intToByteTransfer(start.getMinute()),ByteUtil.intToByteTransfer(end.getHour()),ByteUtil.intToByteTransfer(end.getMinute()));
        }
        byte[] data = ByteUtil.mergeBytes(data1,mode,time1,time2,time3,ByteUtil.hexString2Bytes("000000"));
        byte length = (byte) (data.length);
        byte[] dataLength = ByteUtil.intToByteLittle(length+2);
        byte[] crc = ByteUtil.hexString2Bytes(ByteUtil.getCRC3(ByteUtil.mergeBytes(header, dataLength, data)));
        byte[] message = ByteUtil.mergeBytes(header, dataLength, data, crc);
        return message;
    }

    public static byte[] getWetWindowSetBytes(int deviceNumber, FixedEnvConfigDto dto){
        byte[] header = ByteUtil.hexString2Bytes("AA5555AA");
        byte[] data1 = ByteUtil.mergeBytes(ByteUtil.changeBytes(ByteUtil.hexString2Bytes("4005")),ByteUtil.int2Bytes(deviceNumber,ByteOrder.LITTLE_ENDIAN),ByteUtil.changeBytes(ByteUtil.hexString2Bytes("0001")));
        byte[] openTempBytes = ByteUtil.intToByteLittle((int)(dto.getWetWindowTemp()*10));
        byte[] wetMode = ByteUtil.intToByteTransfer(dto.getWetWindowMode());
        byte[] wetRun = ByteUtil.intToByteLittle(dto.getWetWindowRun());
        byte[] wetStop = ByteUtil.intToByteLittle(dto.getWetWindowStop());
        byte[] wetHumidity = ByteUtil.intToByteTransfer(dto.getWetWindowHumidity());
        byte[] showerTemp = ByteUtil.intToByteLittle((int)(dto.getShowerTemp()*10));
        byte[] showerMode = ByteUtil.intToByteTransfer(dto.getShowerMode());
        byte[] showerRun = ByteUtil.intToByteLittle(dto.getShowerRun());
        byte[] showerStop = ByteUtil.intToByteLittle(dto.getShowerStop());
        byte[] showerHumidity = ByteUtil.intToByteTransfer(dto.getShowerHumidity());
        byte[] heatTemp = ByteUtil.intToByteLittle((int)(dto.getHeatTemp()*10));
        byte[] heatMode = ByteUtil.intToByteTransfer(dto.getHeatMode());
        byte[] heatRun = ByteUtil.intToByteLittle(dto.getHeatRun());
        byte[] heatStop = ByteUtil.intToByteLittle(dto.getHeatStop());
        byte[] reserve = ByteUtil.hexString2Bytes("00");
        byte[] data = ByteUtil.mergeBytes(data1,openTempBytes,wetMode,wetRun,wetStop,wetHumidity,showerTemp,showerMode,showerRun,showerStop,showerHumidity,heatTemp,heatMode,heatRun,heatStop,reserve);
        int length = data.length;
        byte[] dataLength = ByteUtil.intToByteLittle(length+2);
        byte[] crc = ByteUtil.hexString2Bytes(ByteUtil.getCRC3(ByteUtil.mergeBytes(header, dataLength, data)));
        byte[] message = ByteUtil.mergeBytes(header, dataLength, data, crc);
        return message;
    }

    public static int littleEndian2Int(byte[] len) {
        return ByteBuffer.wrap(len).order(ByteOrder.LITTLE_ENDIAN).getInt();
    }
    public static byte[] int2Bytes(int x, ByteOrder byteOrder) {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.order(byteOrder);
        buffer.putInt(x);
        return buffer.array();
    }
    public static int littleEndian2Short(byte[] len) {
        return ByteBuffer.wrap(len).order(ByteOrder.LITTLE_ENDIAN).getShort();
    }
}
