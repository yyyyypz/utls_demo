package com.xiaoyu;

import org.apache.poi.ss.usermodel.*;

import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author peizhuYu
 * 该类读取excel表中的内容，将excel指定的一个列为key，指定的另外一个列为value存储到map集合当中，将数据和properties文件中的词条
 * 根据中文替换成对应的英语或越南语言，需要遍历大量文件夹和文件，因此运用了多线程进行处理加快执行效率
 * 使用场景（编写原因）：公司项目需要进行多语言适配，人工翻译替换词条需要大量时间，因此编写了该工具类自动化批量操作
 */

public class PropertiesUtilPlus {
    // 创建线程池
    private static final ExecutorService executorService = Executors.newFixedThreadPool(10);

    public static void main(String[] args) throws Exception {
        Map<String, String> wordMap = excelReader();


        // 要扫描的路径
        String basePath = "C:\\Users\\15604\\Desktop\\yuto\\changeToVietnam2\\langlib";
        File dir = new File(basePath);
        List<File> allFileList = new ArrayList<>();
        // 判断文件夹是否存在
        if (!dir.exists()) {
            System.out.println("目录不存在");
            return;
        }
        // 获取所有文件
        getAllFile(dir, allFileList);
        // 获取properties
        ArrayList<File> propertiesList = new ArrayList<>();
        for (File file : allFileList) {
            String fileName = file.getName();
            // 如果文件后缀以properties结尾则添加到集合当中
            if (fileName.substring(fileName.length() - 10, fileName.length()).equals("properties")) {
                System.out.println(file.getName());
                System.out.println(file);
                propertiesList.add(file);
            }
        }
        System.out.println("该文件夹下共有" + propertiesList.size() + "个properties文件");
        // 使用线程池处理每个properties文件
        for (int i = 0; i < propertiesList.size(); i++) {
            int finalI = i;
            executorService.execute(() -> {
                try {
                    tempWord(propertiesList.get(finalI).toString(), wordMap);
                    System.out.println("已经遍历完第" + finalI + "个文件,总共" + propertiesList.size() + "个文件!");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        // 关闭线程池
        executorService.shutdown();
    }

    private static void getAllFile(File fileInput, List<File> allFileList) {
        // 获取文件列表
        File[] fileList = fileInput.listFiles();
        assert fileList != null;
        for (File file : fileList) {
            if (file.isDirectory()) {
                // 递归处理文件夹
                // 如果不想统计子文件夹则可以将下一行注释掉
                getAllFile(file, allFileList);
            } else {
                // 如果是文件则将其加入到文件数组中
                allFileList.add(file);
            }
        }
    }

    private static void tempWord(String absPath, Map<String, String> wordMap) {
        String filePath = absPath;
        File file = new File(filePath);
        Properties properties = new Properties();
        try {
            FileInputStream inputStream = new FileInputStream(file);
            properties.load(new InputStreamReader(inputStream, "UTF-16"));
            Set<Object> keys = properties.keySet();
            for (Object key : keys) {
                for (String wordKey : wordMap.keySet()) {
                    if (wordKey.equals(properties.get(key))) {
                        properties.setProperty(key.toString(), wordMap.get(wordKey));
                    }
                }
            }
            OutputStream out = new FileOutputStream(filePath);
            Writer writer = new OutputStreamWriter(out, "UTF-16");
            properties.store(writer, "comment");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Map<String, String> excelReader() throws Exception {
        // 读取Excel文件
        File file = new File("C:\\Users\\15604\\Desktop\\yuto\\越南语翻译词条-人力模块(1).xlsx");
        FileInputStream inputStream = new FileInputStream(file);
        Workbook workbook = WorkbookFactory.create(inputStream);

        // 读取第一个工作表
        Sheet sheet = workbook.getSheetAt(0);

        // 遍历工作表中的每一行
        Map<String, String> keyValuePairs = new HashMap<String, String>();
        for (Row row : sheet) {
            // 获取第n列单元格的值作为键
            Cell keyCell = row.getCell(1);
            String key = null;
            if (keyCell.getCellType() == CellType.ERROR) {
                continue;
            } else {
                key = keyCell.getStringCellValue();
            }


            // 获取第n列单元格的值作为值
            Cell valueCell = row.getCell(2);
            String value = null;
            if (valueCell.getCellType() == CellType.ERROR) {
                continue;
            } else if (valueCell.getCellType() == CellType.FORMULA) {
                value = valueCell.getCellFormula();
            } else {
                value = valueCell.getStringCellValue();
            }

            // 将键值对存储到Map中
            keyValuePairs.put(key, value);
        }

        // 关闭工作簿和输入流
        workbook.close();
        inputStream.close();
        return keyValuePairs;
    }
}
