package com.undie.crash.demo;

import org.iq80.leveldb.*;
import org.iq80.leveldb.impl.Iq80DBFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * @Description
 * @Author glant
 * @Date 2022/7/7 19:10
 **/
public class LevelDBTest {
    //存储路径，默认是项目路径下
    private static final String PATH = "leveldb";
    private static final File FILE = new File(PATH);
    //编码集
    private static final Charset CHARSET = Charset.forName("UTF-8");

    public static void main(String[] args) {
        LevelDBTest test = new LevelDBTest();
//        test.putTest();
//        test.snapshotTest();

//        test.writeOptionsTest();
//        test.readFromSnapshotTest();

//        test.deleteTest();
        test.writeBatchDeleteTest();
    }
    /**
     * put操作测试
     */
    public void putTest() {
        DBFactory factory = new Iq80DBFactory();
        // 默认如果没有则创建
        Options options = new Options();
        File file = new File(PATH);
        //sync写数据后强制写入磁盘
        WriteOptions writeOptions=new WriteOptions().sync(true);
        DB db = null;
        try {
            db = factory.open(file, options);
            byte[] keyByte1 = "key-01".getBytes(CHARSET);
            byte[] keyByte2 = "key-02".getBytes(CHARSET);
            // 会写入磁盘中
            db.put(keyByte1, "value-01".getBytes(CHARSET),writeOptions);
            db.put(keyByte2, "value-02".getBytes(CHARSET));

            System.out.println(asString(db.get(keyByte1)));
            System.out.println(asString(db.get(keyByte2)));
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
    /**
     * 写操作  WriteOptions可以对写操作进行一些定义，ReadOptions同样
     */
    public void writeOptionsTest() {
        DBFactory factory = new Iq80DBFactory();
        Options options = new Options();
        DB db = null;
        try {
            db = factory.open(FILE, options);
            WriteOptions writeOptions = new WriteOptions().sync(true);	// 线程安全
            db.put("key-08".getBytes(CHARSET), "value-08".getBytes(CHARSET), writeOptions);
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
    public void readFromSnapshotTest() {
        DBFactory factory = new Iq80DBFactory();
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
    /**
     * 生成快照后写操作不影响快照读取
     */
    public void snapshotTest() {
        DBFactory factory = new Iq80DBFactory();
        Options options = new Options();
        DB db = null;
        try {
            db = factory.open(FILE, options);
            db.put("key-04".getBytes(CHARSET), "value-04".getBytes(CHARSET));
            // 只能之前到getSnapshot之前put的值，之后的无法获取，即读取期间数据的变更
            Snapshot snapshot = db.getSnapshot();
            db.put("key-05".getBytes(CHARSET), "value-05".getBytes(CHARSET));
            ReadOptions readOptions = new ReadOptions();
            readOptions.fillCache(false);
            readOptions.snapshot(snapshot);
            //可以从数据库中读取，但是快照遍历时没有
            byte[] res=db.get("key-05".getBytes(CHARSET));
            System.out.println("From the database  "+new String(res,CHARSET));
            DBIterator it = db.iterator(readOptions);
            while (it.hasNext()) {
                Map.Entry<byte[],byte[]> entry =  it.next();
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
                    e.printStackTrace();
                }
            }
        }
    }

    public void deleteTest() {
        DBFactory factory = new Iq80DBFactory();
        Options options = new Options();
        DB db = null;
        try {
            db = factory.open(FILE, options);
            // 存在会删除，之后查询不出
            db.delete("key-02".getBytes(CHARSET));
            // 不存在不会报错
            db.delete("key02".getBytes(CHARSET));

            Snapshot snapshot = db.getSnapshot();
            ReadOptions readOptions = new ReadOptions();
            readOptions.fillCache(false);
            readOptions.snapshot(snapshot);

            DBIterator it = db.iterator(readOptions);
            while (it.hasNext()) {
                Map.Entry<byte[], byte[]> entry = (Map.Entry<byte[], byte[]>) it
                        .next();
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


    /**
     * 批量写和删除
     */
    public void writeBatchDeleteTest() {
        DBFactory factory = Iq80DBFactory.factory;
        Options options = new Options();
        DB db = null;
        try {
            db = factory.open(FILE, options);
            WriteBatch writeBatch = db.createWriteBatch();
            writeBatch.put("key-10".getBytes(CHARSET), "value-10".getBytes(CHARSET));
            writeBatch.put("key-11".getBytes(CHARSET), "value-11".getBytes(CHARSET));
            // 会将key-01的value置为""
            writeBatch.delete("key-01".getBytes(CHARSET));
            db.write(writeBatch);
            writeBatch.close();
            DBIterator it = db.iterator();
            while (it.hasNext()) {
                Map.Entry<byte[], byte[]> entry = (Map.Entry<byte[], byte[]>) it
                        .next();
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

    private String asString(byte[] arr) {
        return new String(arr, CHARSET);
    }
}
