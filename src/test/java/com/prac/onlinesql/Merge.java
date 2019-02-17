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
        nioMerge();
    }

    private static void bioMerge() throws IOException {
        String path = "G://file//";
        String filePath = "G://file//test.html";

        File dir = new File(path);
        if(dir.isDirectory()){
            String[] list = dir.list();
            File destFile = new File(filePath);
            if(!destFile.exists()){
                destFile.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(destFile, true);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            for(int i=0;i<list.length;i++){
                File temp = new File(path + i + ".html");
                if(!temp.exists()){
                    continue;
                }
                fis = new FileInputStream(temp);
                bis = new BufferedInputStream(fis);
                byte[] bytes = new byte[1024];
                int len = -1;
                while((len = (bis.read(bytes)))!=-1){
                    bos.write(bytes,0,len);
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
        String filePath = "G://file//test.html";

        File dir = new File(path);
        if(dir.isDirectory()){
            String[] list = dir.list();
            File destFile = new File(filePath);
            if(!destFile.exists()){
                destFile.createNewFile();
            }
            RandomAccessFile destAccessFile = new RandomAccessFile(destFile, "rw");
            FileChannel channel = destAccessFile.getChannel();
            for(int i=0;i<list.length;i++){
                File temp = new File(path + i + ".html");
                if(!temp.exists()){
                    continue;
                }
                FileChannel accessFile = new RandomAccessFile(temp, "rw").getChannel();

                channel.transferFrom(accessFile, channel.size(), accessFile.size());
                accessFile.close();
            }
            channel.close();

        }
    }
}
