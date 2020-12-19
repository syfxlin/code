package me.ixk.days.day28;

import cn.hutool.core.io.FileUtil;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * @author Otstar Lin
 * @date 2020/12/19 下午 2:24
 */
@Slf4j
public class Day28Test {

    @Test
    void charset() {
        final File tempFile = FileUtil.createTempFile(FileUtil.getTmpDir());
        // UTF-8 用 GBK 错误读取
        // 特点：大部分是不认识的古文，并夹杂一些日韩文
        // 样例：杩欐槸涓�鍙ョ敤浜庢祴璇曢敊璇鍙栫殑鏂囧瓧
        FileUtil.writeString(
            "这是一句用于测试错误读取的文字",
            tempFile,
            "UTF-8"
        );
        log.info(FileUtil.readString(tempFile, "GBK"));
        // GBK 用 UTF-8 错误读取
        // 特点：大部分为小方块字符
        // 样例：����һ�����ڲ��Դ����ȡ������
        FileUtil.writeString("这是一句用于测试错误读取的文字", tempFile, "GBK");
        log.info(FileUtil.readString(tempFile, "UTF-8"));

        // UTF-8 用 ISO-8859-1 错误读取
        // 特点：一些不认识的符号字符
        // 样例：è¿æ¯ä¸å¥ç¨äºæµè¯éè¯¯è¯»åçæå­
        FileUtil.writeString(
            "这是一句用于测试错误读取的文字",
            tempFile,
            "UTF-8"
        );
        log.info(FileUtil.readString(tempFile, "ISO-8859-1"));

        // GBK 用 ISO-8859-1 错误读取
        // 特点：一些头顶带类似声调符号的字符
        // 样例：ÕâÊÇÒ»¾äÓÃÓÚ²âÊÔ´íÎó¶ÁÈ¡µÄÎÄ×Ö
        FileUtil.writeString("这是一句用于测试错误读取的文字", tempFile, "GBK");
        log.info(FileUtil.readString(tempFile, "ISO-8859-1"));

        // GBK 读取 UTF-8 字符，并重新用 GBK 写入，然后用 UTF-8 读取
        // 特点：部分字符出现问好或乱码
        // 样例：这是�?句用于测试错误读取的文字
        FileUtil.writeString(
            "这是一句用于测试错误读取的文字",
            tempFile,
            "UTF-8"
        );
        FileUtil.writeString(
            FileUtil.readString(tempFile, "GBK"),
            tempFile,
            "GBK"
        );
        log.info(FileUtil.readString(tempFile, "UTF-8"));

        // UTF-8 读取 GBK 字符，并重新用 UTF-8 写入，然后用 GBK 读取
        // 特点：锟斤拷码
        // 样例：锟斤拷锟斤拷一锟斤拷锟斤拷锟节诧拷锟皆达拷锟斤拷锟饺★拷锟斤拷锟斤拷锟�
        FileUtil.writeString("这是一句用于测试错误读取的文字", tempFile, "GBK");
        FileUtil.writeString(
            FileUtil.readString(tempFile, "UTF-8"),
            tempFile,
            "UTF-8"
        );
        log.info(FileUtil.readString(tempFile, "GBK"));
    }

    @Test
    void readOOM() throws IOException {
        final Path path = Paths.get(
            FileUtil.createTempFile(FileUtil.getTmpDir()).toURI()
        );
        // 当文件的大小超过堆内存时，所有的数据都存储于 List 中，会造成 OOM
        final List<String> list = Files.readAllLines(path);
        // 解决方案就是用流式按需读取
        final String str = Files
            .lines(path)
            // 限制
            .limit(2000)
            .collect(Collectors.joining("\n"));
    }

    @Test
    void tooManyFiles() throws IOException {
        final File tempFile = FileUtil.createTempFile(FileUtil.getTmpDir());
        final URI uri = tempFile.toURI();
        FileUtil.writeUtf8String("1234567890", tempFile);

        // 正确关闭
        for (int i = 0; i < 100_000; i++) {
            try (Stream<String> stream = Files.lines(Paths.get(uri))) {
                stream.forEach(s -> {});
            }
        }

        // 如果打开句柄不关闭，会出现 FileSystemException：Too many open files
        // PS: 不知道我这为什么没有报错 23333
        for (int i = 0; i < 100_000; i++) {
            Files.lines(Paths.get(uri)).forEach(s -> {});
        }
    }
}
