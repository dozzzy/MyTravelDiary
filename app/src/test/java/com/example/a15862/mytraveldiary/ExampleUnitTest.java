package com.example.a15862.mytraveldiary;

import com.example.a15862.mytraveldiary.DAO.DBOP;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void addition_isCorrect() {
        Map<String,Object> testData = new HashMap<>();
        testData.put("test",System.currentTimeMillis());
        DBOP db=new DBOP();
        db.insertData(testData);
    }
}