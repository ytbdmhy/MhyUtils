package com.ytbdmhy.SSM_AutoWired_Method;

// 第一种方法的注解
//@Component
public class ThreadTest implements Runnable {

    // 第一种方法
//    @Autowired
//    private TestDao testDao;
//
//    private static ThreadTest threadTest;
//
//    @PostConstruct
//    public void init() {
//        threadTest = this;
//    }
//
//    @Override
//    public void run() {
//        threadTest.testDao.test();
//        System.out.println("over");
//    }

    // 第二种方法
    private TestDao testDao;

    public TestDao getTestDao() {
        return testDao;
    }

    public void setTestDao(TestDao testDao) {
        this.testDao = testDao;
    }

    @Override
    public void run() {
        testDao.test();
        System.out.println("over");
    }
}
