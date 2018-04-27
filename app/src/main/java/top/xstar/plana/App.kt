package top.xstar.plana

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration

/**
 * @author: xstar
 * @since: 2018-04-26.
 */
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        Realm.setDefaultConfiguration(RealmConfiguration.Builder().name("plana.db")
                .schemaVersion(1).build())

    }
}