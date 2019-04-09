package cn.krisez.imchat.db;

import android.app.Application;
import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.db.framework.FrameworkSQLiteOpenHelperFactory;
import com.squareup.sqlbrite3.BriteDatabase;
import com.squareup.sqlbrite3.SqlBrite;
import io.reactivex.schedulers.Schedulers;

public class DbUtils {
    public static BriteDatabase db;
    public static Application context;

    public static void init(Application application) {
        context = application;
    }

    static BriteDatabase getInstance(){
        if(db == null) {
            synchronized (DbUtils.class) {
                if (db == null) {
                    SqlBrite sqlBrite = new SqlBrite.Builder().build();
                    SupportSQLiteOpenHelper.Configuration configuration = SupportSQLiteOpenHelper.Configuration.builder(context)
                            .name(DbConstant.DB_NAME)
                            .callback(new MessageDbHelper())
                            .build();
                    SupportSQLiteOpenHelper.Factory factory = new FrameworkSQLiteOpenHelperFactory();
                    SupportSQLiteOpenHelper helper = factory.create(configuration);
                    db = sqlBrite.wrapDatabaseHelper(helper, Schedulers.io());
                }
            }
        }
        return  db;
    }

}
