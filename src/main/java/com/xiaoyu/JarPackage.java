package com.xiaoyu;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author peizhuYu
 * 该类主要用于批量对文件夹进行打jar包处理（通过打开多个命令行窗口执行指定指令进行打包）并将打包后的文件移动到指定目录
 * 编写原因（使用场景）：公司项目需要进行多语言适配，需要将翻译的词条编写到properties文件中并且打包成jar包，系统会读取properties词条中的
 * 词条编码将对应的语言展示到前端页面，词条数量过大，如果人工进行打包需要花费大量时间，因此编写了该工具类进行打包
 */

public class JarPackage {
    public static void main(String[] args) throws IOException, InterruptedException {
        // 获取需要打jar包的文件夹路径结构
        ArrayList<String> filePaths = findFile("C:\\Users\\15604\\Desktop\\yuto\\changeToVietnam2\\langlib");
        // 遍历所有的路径结构
        for (int i = 0; i < filePaths.size(); i++) {
            // 根据langlib切割路径获取许需要打包的文件夹名称，因此存放jar包的目录必须要有langlib目录，并且该目录的第一个子目录就必须为解压后的jar包结构
            String[] split = filePaths.get(i).toString().split("langlib");
            // jar包的结构有lang文件夹结尾，也有META-INF结尾，两个的数量和前缀一致，只需要获取其一就可，这里获取了META-INF结尾的文件路径
            String last = split[1].substring(split[1].length() - 8, split[1].length());
            //System.out.println(last);
            // 如果结尾为META-INF则对目录结构进行处理
            if (last.equals("META-INF")) {
                // 根据META-INF进行切割
                String[] split1 = split[1].toString().split("META-INF");
                //System.out.println(split1[0]);
                // 切割后获取需要打jar包的文件目录
                String directoryName = split1[0].toString().substring(1, split1[0].length() - 1);


                // 在需要打jar包的目录下的创建MANIFEST.MF文件，打jar必须要有这个文件，directoryName前面为工程文件路径
                File dir = new File("D:\\ideaproject\\util_demo\\" + directoryName);
                System.out.println(dir);
                File f = new File(dir, "MANIFEST.MF");
                f.createNewFile();


                System.out.println(directoryName);
                // 进入到需要打jar包的目录，对lang包进行打包
                Runtime.getRuntime().exec("cmd /k cd " + directoryName + " && jar cvfm  " + directoryName + ".jar MANIFEST.MF lang");
                System.out.println("cmd /k cd " + directoryName + " && jar cvfm  " + directoryName + ".jar MANIFEST.MF lang");
                // 打jar过程需要打开多个命令行窗口，让程序暂时休眠，等待一个jar包压缩完成关闭命令窗口后再继续执行程序，如果电脑配置够好可以将时间缩短
                Thread.currentThread().sleep(1500);

                // 移动复制jar包到指定路径，参数为jar包名称，不包括扩展名
                moveJar(directoryName);
            }

        }
    }

    private static ArrayList<String> findFile(String path) {
        File file = new File(path);
        File[] tempList = file.listFiles();
        System.out.println("该目录下对象个数：" + tempList.length);
        File[] temp = null;
        ArrayList<String> filePaths = new ArrayList<>();
        for (int i = 0; i < tempList.length; i++) {
            if (tempList[i].isDirectory()) {
                //读取某个文件夹下的所有文件夹
                temp = tempList[i].listFiles();
                for (int j = 0; j < temp.length; j++) {
                    System.out.println("----" + temp[j]);
                    filePaths.add(temp[j].toString());
                }
            }
        }
        return filePaths;
    }

    private static void moveJar(String directoryName) {
        // jar包路径
        File startFile = new File("D:\\ideaproject\\util_demo\\" + directoryName + "\\" + directoryName + ".jar");

        // 需要移动到的路径
        File endDirection = new File("C:\\Users\\15604\\Desktop\\yuto\\viet");
        if (!endDirection.exists()) {
            endDirection.mkdirs();
        }

        File endFile = new File(endDirection + File.separator + startFile.getName());

        try {
            if (startFile.renameTo(endFile)) {
//                System.out.println("文件移动成功！目标路径：{" + endFile.getAbsolutePath() + "}");
            } else {
                System.out.println("文件移动失败！起始路径：{" + startFile.getAbsolutePath() + "}");
            }
        } catch (
                Exception e) {
            System.out.println("文件移动出现异常！起始路径：{" + startFile.getAbsolutePath() + "}");
        }
    }
}