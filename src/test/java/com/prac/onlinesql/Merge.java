package com.prac.onlinesql;

import java.io.*;
import java.nio.channels.FileChannel;

/**
 * @Auther: Administrator
 * @Date: 2019-02-14 11:25
 * @Description:
 */
public class Merge {
    public static void main(String[] args) throws IOException {
        merge();
    }

    private static void bioMerge() throws IOException {
        String path = "G://file//";
        String filePath = "G://file//test.html";

        File dir = new File(path);
        if (dir.isDirectory()) {
            String[] list = dir.list();
            File destFile = new File(filePath);
            if (!destFile.exists()) {
                destFile.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(destFile, true);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            for (int i = 0; i < list.length; i++) {
                File temp = new File(path + i + ".html");
                if (!temp.exists()) {
                    continue;
                }
                fis = new FileInputStream(temp);
                bis = new BufferedInputStream(fis);
                byte[] bytes = new byte[1024];
                int len = -1;
                while ((len = (bis.read(bytes))) != -1) {
                    bos.write(bytes, 0, len);
                    //这里不能用bos.write(bytes);
                    //bytes每一次都是写入定义的大小(1024) 如果最后一次读取的字节数不足1024 就会使用前面的补足
                    //比如倒数第二次读取了1024大小 最后一次读取了1000大小 那么bytes存储的数据就是前一次的最后24 和后一次读取的1000
                    //将会导致部分重复
                }
            }
            bis.close();
            bos.close();
        }
    }

    private static void nioMerge() throws IOException {
        String path = "G://file//";
        String filePath = "G://file//test.mp4";

        File dir = new File(path);
        if (dir.isDirectory()) {
            String[] list = dir.list();
            File destFile = new File(filePath);
            if (!destFile.exists()) {
                destFile.createNewFile();
            }
            RandomAccessFile destAccessFile = new RandomAccessFile(destFile, "rw");
            FileChannel channel = destAccessFile.getChannel();
            for (int i = 0; i < list.length; i++) {
                File temp = new File(path + list[i]);
                if (!temp.exists()) {
                    continue;
                }
                FileChannel accessFile = new RandomAccessFile(temp, "rw").getChannel();

                channel.transferFrom(accessFile, channel.size(), accessFile.size());
                accessFile.close();
            }
            channel.close();
        }
    }

    public static void cut() {
        File file = new File("H:\\springcloud-zuull.mp4");
        int num = 10;//分割文件的数量

        long lon = file.length() / 10L + 1l;//使文件字节数+1，保证取到所有的字节
        try {
            RandomAccessFile raf1 = new RandomAccessFile(file, "r");

            byte[] bytes = new byte[1024];//值设置越小，则各个文件的字节数越接近平均值，但效率会降低，这里折中，取1024
            int len = -1;
            for (int i = 0; i < 10; i++) {
                String name = "G:\\file\\source" + i + ".tmp";
                File file2 = new File(name);
                if(!file2.exists()){
                    file2.createNewFile();
                }
                RandomAccessFile raf2 = new RandomAccessFile(file2, "rw");

                while ((len = raf1.read(bytes)) != -1){//读到文件末尾时，len返回-1，结束循环
                    raf2.write(bytes, 0, len);
                    if (raf2.length() > lon)//当生成的新文件字节数大于lon时，结束循环
                        break;
                }
                raf2.close();
            }
            raf1.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void merge() {
        File file = new File("G:\\file\\new.mp4");
        try {
            RandomAccessFile target = new RandomAccessFile(file, "rw");
            for (int i = 0; i < 10; i++) {
                File file2 = new File("G:\\file\\source" + i + ".tmp");
                RandomAccessFile src = new RandomAccessFile(file2, "r");
                byte[] bytes = new byte[1024];//每次读取字节数
                int len = -1;
                while ((len = src.read(bytes)) != -1) {
                    target.write(bytes, 0, len);//循环赋值
                }
                src.close();
            }
            target.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
