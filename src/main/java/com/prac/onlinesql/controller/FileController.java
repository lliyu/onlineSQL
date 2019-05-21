package com.prac.onlinesql.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.channels.FileChannel;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Auther: Administrator
 * @Date: 2019-02-14 09:29
 * @Description:
 */
@Controller
public class FileController {

    private static String SUFFIX_FILE = ".html";

    @ResponseBody
    @RequestMapping(value = "/dbs/download", method = RequestMethod.POST)
    public String download(@RequestParam("file") MultipartFile multipartFile, HttpServletResponse response) throws IOException {
        long l = 0;
        short ls = 1;
        ls += 1;
        int len = -1;
        byte[] bytes = new byte[1024];
        FileInputStream inputStream = (FileInputStream) multipartFile.getInputStream();

//        ServletOutputStream outputStream = response.getOutputStream();
//        while((len=inputStream.read(bytes))!=-1){
//            outputStream.write(bytes,0,len);
//        }
        try {
            String originalFilename = multipartFile.getOriginalFilename();
            int index = originalFilename.lastIndexOf(".");
            String path = "G://file//" + originalFilename.substring(0,index) + "//";
            SUFFIX_FILE = originalFilename.substring(index, originalFilename.length());
            File pathF = new File(path);
            if(!pathF.isDirectory()){
                pathF.mkdirs();
            }
            String destPath = path + originalFilename;

            l = System.currentTimeMillis();

            int length = multipartFile.getBytes().length;
            int cpu = Runtime.getRuntime().availableProcessors();

            int size = cpu;
            //每个线程下载的大小 这里设置为10mb
            //如果设置过大的话会导致内存溢出
            int memery = 10 * 1024 * 1024;

            //计算需要的线程数量
            int count = length/memery + ((length%memery)==0?0:1);
            CountDownLatch countDownLatch = new CountDownLatch(count);
            ExecutorService executorService = Executors.newFixedThreadPool(size);
            if(count>1){
                DownloadThread thread1 = new DownloadThread(0, memery, inputStream, 0, path,countDownLatch);
                executorService.execute(thread1);

                for(int i=1;i<count-1;i++){
                    DownloadThread thread = new DownloadThread(i*memery+1, (i+1)*memery, inputStream, i, path,countDownLatch);
                    executorService.execute(thread);
                }
                DownloadThread thread2 = new DownloadThread((count-1)*memery+1, length, inputStream, count-1, path,countDownLatch);
                executorService.execute(thread2);
            }else {
                DownloadThread thread1 = new DownloadThread(0, length, inputStream, 0, path,countDownLatch);
                executorService.execute(thread1);
            }

            countDownLatch.await();//等待所有下载线程完成

            //整合
            File dir = new File(path);
            if(dir.isDirectory()){
                String[] list = dir.list();
                File destFile = new File(destPath);
                if(!destFile.exists()){
                    destFile.createNewFile();
                }
                RandomAccessFile destAccessFile = new RandomAccessFile(destFile, "rw");
                FileChannel channel = destAccessFile.getChannel();
                for(int i=0;i<list.length;i++){
                    File temp = new File(path + i + SUFFIX_FILE);
                    if(!temp.exists()){
                        continue;
                    }
                    RandomAccessFile randomAccessFile = new RandomAccessFile(temp, "rw");
                    FileChannel accessFile = randomAccessFile.getChannel();

                    channel.transferFrom(accessFile, channel.size(), accessFile.size());
                    accessFile.close();
                    randomAccessFile.close();
                    temp.delete();
                }
                channel.close();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return String.valueOf(System.currentTimeMillis() - l);
    }

    class DownloadThread extends Thread {
        private int start = 0;
        private int end = 0;
        private FileInputStream is;
        private int count;
        private String path;
        private CountDownLatch countDownLatch;

        public DownloadThread(int start, int end, FileInputStream is, int count, String path, CountDownLatch countDownLatch) {
            this.start = start;
            this.end = end;
            this.is = is;
            this.count = count;
            this.path = path;
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            FileChannel channel = is.getChannel();

            File file = new File(path + count + SUFFIX_FILE);
            try {
                if (!file.exists()) {
                    file.createNewFile();
                }
                System.out.println("start:"+start+"...end:"+end + "=" + (end-start));
                RandomAccessFile accessFile = new RandomAccessFile(file, "rw");
                channel.transferTo(start, end-start + 1, accessFile.getChannel());
                accessFile.close();
                countDownLatch.countDown();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
