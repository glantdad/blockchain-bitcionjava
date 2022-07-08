package com.undie.crash.demo.util;

import org.iq80.leveldb.*;
import org.iq80.leveldb.impl.Iq80DBFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * @Description
 * @Author glant
 * @Date 2022/7/7 19:24
 **/
public class LevelDBUtil {
    DBFactory factory = new Iq80DBFactory();
    //存储路径，默认是项目路径下
    private static final String PATH = "leveldb";
    private static final File FILE = new File(PATH);
    //编码集
    private static final Charset CHARSET = Charset.forName("UTF-8");

    /**
     * 写操作  WriteOptions可以对写操作进行一些定义，ReadOptions同样
     */
    public void put(String key, String value) {
        Options options = new Options();
        DB db = null;
        try {
            db = factory.open(FILE, options);
            WriteOptions writeOptions = new WriteOptions().sync(true);	// 线程安全
            db.put(key.getBytes(CHARSET), "value-08".getBytes(CHARSET), writeOptions);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (db != null) {
                try {
                    db.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 测试从快照中读取数据
     */
    public void get() {
        File file = new File(PATH);
        Options options = new Options();
        DB db = null;
        try {
            db = factory.open(file, options);
            // 读取当前快照，重启服务仍能读取，说明快照持久化至磁盘，
            Snapshot snapshot = db.getSnapshot();
            // 读取操作
            ReadOptions readOptions = new ReadOptions();
            //当读取一大块数据时，可以使用fillCache = false来禁止缓存被覆盖。
            readOptions.fillCache(false);
            // 默认snapshot为当前
            readOptions.snapshot(snapshot);
            DBIterator it = db.iterator(readOptions);
            //迭代访问
            while (it.hasNext()) {
                Map.Entry<byte[], byte[]> entry = it.next();
                String key = new String(entry.getKey(), CHARSET);
                String value = new String(entry.getValue(), CHARSET);
                System.out.println("key: " + key + " value: " + value);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (db != null) {
                try {
                    db.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }
}
