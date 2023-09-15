package com.dzq.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author: daizuquan
 * @date: 2023/9/8 17:26
 * @description:
 */
public class Test01 {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
            try {
                String A = "D:\\dev\\program";
                String B = "D:\\program";
                // 创建临时文件夹
                File tempDir = new File(A);
                tempDir.mkdir();


                // 把B文件夹复制到A文件夹
                File sourceDir = new File(B);
                File destinationDir = new File(tempDir, "");
                copyDirectory(sourceDir, destinationDir);

                //把A文件夹压缩成zip包
                String AZIP = A + ".zip";
                zipFolder(A, AZIP);

                // 删除临时文件夹
//                FileUtils.deleteDirectory(tempDir);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        /**
         * 复制文件夹
         *
         * @param source      复制后文件夹路径
         * @param destination 原文件夹路径
         * @throws IOException
         */
        private static void copyDirectory(File source, File destination) throws IOException {
            File[] files = source.listFiles();
            for (File file : files) {
                if (file.isDirectory()) {
                    copyDirectory(file, new File(destination, file.getName()));
                } else {
                    if (!destination.exists()) {
                        destination.mkdirs();
                    }
                    File copy = new File(destination, file.getName());
                    copy.createNewFile();
                    FileInputStream in = new FileInputStream(file);
                    FileOutputStream out = new FileOutputStream(copy);
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = in.read(buffer)) > 0) {
                        out.write(buffer, 0, length);
                    }
                    in.close();
                    out.close();
                }
            }
        }

        /**
         * 压缩文件
         *
         * @param path    需要压缩的文件路径
         * @param zipPath 压缩的文件后路径
         * @throws Exception
         */
        public static void zipFolder(String path, String zipPath) throws Exception {
            FileOutputStream fos = null;
            ZipOutputStream zos = null;
            try {
                fos = new FileOutputStream(zipPath);
                zos = new ZipOutputStream(fos);
                addFolderToZip("", new File(path), zos);
            } finally {
                if (zos != null) {
                    zos.close();
                }
                if (fos != null) {
                    fos.close();
                }
            }
        }

        /**
         * 秭归添加压缩路径
         */
        private static void addFolderToZip(String parentPath, File folder, ZipOutputStream zos) throws Exception {
            for (File file : folder.listFiles()) {
                if (file.isDirectory()) {
                    addFolderToZip(parentPath + folder.getName() + "/", file, zos);
                } else {
                    FileInputStream fis = null;
                    try {
                        fis = new FileInputStream(file);
                        ZipEntry zipEntry = new ZipEntry(parentPath + folder.getName() + "/" + file.getName());
                        zos.putNextEntry(zipEntry);
                        byte[] bytes = new byte[1024];
                        int length;
                        while ((length = fis.read(bytes)) >= 0) {
                            zos.write(bytes, 0, length);
                        }
                    } finally {
                        if (fis != null) {
                            fis.close();
                        }
                    }
                }
            }
        }



        // 向数组中填充100内随机数
        private static void rand(int[] a) {
            for (int i = 0; i < a.length; i++) {
                a[i] = new Random().nextInt(100);
            }
        }
        // 编写自定义线程类，可以实现runable或callable，如需调用返回值，则必须实现callable
        static class sumThread implements Callable<Long> {
            // 存放的业务数据
            int[] a ;
            // 定义一个返回值，可按需求修改
            long result;
            // 初始化线程实例时，从业务数据中根据下标取出 一小段数据
            public sumThread(int[] b,int start,int end) {
                a= Arrays.copyOfRange(b,start,end);
                System.out.println("初始化一个线程实例");
            }

            // 执行方法，模拟对业务数据的处理过程，运行完成后return指定的返回值
            @Override
            public Long call() throws Exception {
                System.out.println("启动一个线程");
                // a = rand1(a);
                long beginTime = System.currentTimeMillis();
                long b = 0L;
//                for (int k = 0; k<10000 ; k++) {
                    for (int j = 0; j < a.length; j++) {
                        b += a[j];
                    }
//                }
                long endTime = System.currentTimeMillis();
                System.out.println("线程耗时"+(endTime-beginTime)+"毫秒");
                System.out.println("线程计算结果"+b);
                System.out.println("结束一个线程");
                result = b;
                return result;
            }
        }

}
