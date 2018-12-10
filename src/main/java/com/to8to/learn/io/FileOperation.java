package com.to8to.learn.io;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description: 主要用于简单的文件读写操作
 * @author: felix.fan
 * @date: 2018/3/18 20:52
 * @version: 1.0
 */
public class FileOperation {
    /**
     *
     * <b>方法说明：</b>
     * <ul>
     * 文件的读取(节点流FileInputStream读取字节流)
     * </ul>
     * @param fileName
     */
    public static void readFileByBytes(String fileName) {
        File file = null;
        FileInputStream fileInput = null;
        try {
            file = new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            byte[] buffer = new byte[1024];
            fileInput = new FileInputStream(file);
            int byteread = 0;
            while ((byteread = fileInput.read(buffer)) != -1) {
                System.out.write(buffer, 0, byteread);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileInput != null) {
                    fileInput.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * <b>方法说明：</b>
     * <ul>
     * 文件读取(节点流FileReader读取字符流)
     * </ul>
     * @param fileName
     */
    @SuppressWarnings("unused")
    public static void readFileByChars(String fileName) {
        File file = null;
        FileReader reader = null;
        try {
            file = new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            reader = new FileReader(file);
            char[] buffer = new char[1024];
            int charread = 0;
            while ((charread = reader.read(buffer)) != -1) {
                System.out.println(buffer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * <b>方法说明：</b>
     * <ul>
     * 文件的读取通过BufferedReader读取数据
     * </ul>
     * @param fileName
     */
    public static void readByBufferedReader(String fileName) {
        try {
            File file = new File(fileName);
            BufferedReader bufferReader;
            String read;
            bufferReader = new BufferedReader(new FileReader(file));
            while ((read = bufferReader.readLine()) != null) {
                System.out.println(read);
            }
            bufferReader.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     *
     * <b>方法说明：</b>
     * <ul>
     * 向文件中写入数据(字节流FileOutputStream)
     * </ul>
     */
    public void writeByFileOutputStream() {
        FileOutputStream fop = null;
        File file = null;
        String content = "This is the text content";
        try {
            file = new File("D:" + File.separator + "newFile.txt");
            fop = new FileOutputStream(file);
            if (!file.exists()) {
                file.createNewFile();
            }
            byte[] contentInBytes = content.getBytes();
            fop.write(contentInBytes);
            fop.flush();
            fop.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fop != null) {
                    fop.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     *
     * <b>方法说明：</b>
     * <ul>
     * 向文件中写数据（FileWriter）【注意使用FileWriter（“path”，true）可以往文件后面追加内容，否则就直接覆盖了】
     * </ul>
     */
    public static void writeByFileWriter() {
        try {
            String data = "This content will append to the end of the file";
            File file = new File("javaio-appendfile.txt");
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fileWriter = new FileWriter(file, true);
            fileWriter.write(data);
            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * <b>方法说明：</b>
     * <ul>
     * 采用BufferedWriter向文件中写数据
     * </ul>
     */
    public static void writeByBufferedWriter() {
        try {
            String content = "This is the content to write into file";
            File file = new File("D:" + File.separator + "filename.txt");
            if (!file.exists()) {
                file.createNewFile();
            }
            BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
            bw.write(content);
            bw.flush();
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void writeLog(String path, String content) {
        try {
            if (path == null || path.length() <= 0) {
                return;
            }
            File file = new File(path);
            if (!file.exists()) {
                file.createNewFile();
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            FileOutputStream out = new FileOutputStream(file, true);

            StringBuffer sb = new StringBuffer();
            sb.append("-----------"+sdf.format(new Date())+"------------\n");
            sb.append(content + "\n");
            out.write(sb.toString().getBytes("utf-8"));
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("resource")
    private static String readLog(String pathname) {
        StringBuffer sb = new StringBuffer();
        String tempStr = null;
        try {
            File file = new File(pathname);
            if (!file.exists())
                throw new FileNotFoundException();
            FileInputStream fis =new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            while ((tempStr = br.readLine()) != null) {
                sb.append(tempStr);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        writeLog("D:" + File.separator + "newFile.txt","this is a test log");
        writeLog("D:" + File.separator + "newFile.txt","测试中文");
        System.out.println(readLog("D:" + File.separator + "newFile.txt"));
    }
}
