package com.gurjeet.lab_a1_a2_android_gurjeetkaur_c0817568.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.gurjeet.lab_a1_a2_android_gurjeetkaur_c0817568.data.ProductDao;
import com.gurjeet.lab_a1_a2_android_gurjeetkaur_c0817568.model.Product;
import com.gurjeet.lab_a1_a2_android_gurjeetkaur_c0817568.model.Provider;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Provider.class, Product.class}, version = 1, exportSchema = false)
public abstract class ProductRoomDB  extends RoomDatabase {
    public abstract ProductDao productDao();
    private static volatile ProductRoomDB INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;

    // executor service to do tasks in background thread
    public static final ExecutorService databaseWriteExecutor
            = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    public static ProductRoomDB getInstance(final Context context){
        if(INSTANCE == null){
            synchronized (ProductRoomDB.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ProductRoomDB.class, "product_database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static final RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback() {
                @Override
                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                    super.onCreate(db);
                    databaseWriteExecutor.execute(() -> {
                        ProductDao pDao = INSTANCE.productDao();
                        pDao.deleteAll();

                       // Add demo data initially
                        Provider pr = new Provider("Apple","info@apple.com","1234567890","37.33182","-122.03118");
                        Provider pr2 = new Provider("Samsung","info@samsung.com","1234567890","43.6777","79.6248");
                        pDao.insertProvider(pr);pDao.insertProvider(pr2);
                        Product pd = new Product(1,"iPhone5","Demo details text about iPhone5. Wide camera, retina display, water resistant, privacy built in.","100","11-10-2021");
                        Product pd2 = new Product(1,"iPhoneX","Demo details text about iPhoneX, advanced dual-camera system.","200","11-10-2021");
                        Product pd3 = new Product(1,"iPad Mini","Demo details text about iPad Mini","300","11-10-2021");
                        Product pd4 = new Product(1,"Macbook Pro","Demo details text about macbookpro","1000","11-10-2021");
                        Product pd5 = new Product(1,"Apple AirPod","Demo details text about airpod","150","11-10-2021");
                        pDao.insertProduct(pd);pDao.insertProduct(pd2);pDao.insertProduct(pd3);pDao.insertProduct(pd4);pDao.insertProduct(pd5);
                        Product pd6 = new Product(2,"Samsung Galaxy","Demo details text about Samsung Galaxy, advanced dual-camera system","100","12-10-2021");
                        Product pd7 = new Product(2,"Samsung S1","Demo details text about Samsung S1 mobile with retina display, water resistant","200","12-10-2021");
                        Product pd8 = new Product(2,"Smart 4K TV","Demo details text about Samsung TV","300","12-10-2021");
                        Product pd9 = new Product(2,"Galaxy Z Flip","Demo details text about Galaxy Z Flip mobile","400","12-10-2021");
                        Product pd10 = new Product(2,"Galaxy Watch 4","Demo details text about Galaxy Watch 4","50","12-10-2021");
                        pDao.insertProduct(pd6);pDao.insertProduct(pd7);pDao.insertProduct(pd8);pDao.insertProduct(pd9);pDao.insertProduct(pd10);


                    });
                }
            };



}
