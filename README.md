# utls_demo
该demo内有两个工具类，因公司项目需求而编写，导入demo后下载依赖，根据要求修改部分变量执行main方法。

JarPackage: 该类主要用于批量对文件夹进行打jar包处理（通过打开多个命令行窗口执行指定指令进行打包）并将打包后的文件移动到指定目录
编写原因（使用场景）：公司项目需要进行多语言适配，需要将翻译的词条编写到properties文件中并且打包成jar包，系统会读取properties
词条中的词条编码将对应的语言展示到前端页面，词条数量过大，如果人工进行打包需要花费大量时间，因此编写了该工具类进行打包



PropertiesUtilPlus:该类读取excel表中的内容，将excel指定的一个列为key，指定的另外一个列为value存储到map集合当中，将数据和properties文件中的词条
根据中文替换成对应的英语或越南语言，需要遍历大量文件夹和文件，因此运用了多线程进行处理加快执行效率
编写原因（使用场景）：公司项目需要进行多语言适配，人工翻译替换词条需要大量时间，因此编写了该工具类自动化批量操作

简历所涉及的项目内部保密没有放在GitHub上。

