package com.example.sun.starbuzz;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;


/**
 * DrinkCategoryActivity представляет
 * собой ListActivity — особую разновидность активности, пред-
 * назначенную для работы со списковыми представлениями.
 */
public class DrinkCategoryActivity extends ListActivity {

    //чтобы закрыть в методе onDestroy()
    private SQLiteDatabase db;
    private Cursor cursor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ListView listDrinks = getListView();

//        //Списковое представление заполняется данными из массива drinks.
//        ArrayAdapter<Drink> listAdapter = new ArrayAdapter<>(this,
//                android.R.layout.simple_list_item_1, Drink.drinks);
//        listDrinks.setAdapter(listAdapter);

        try {
            SQLiteOpenHelper starbuzzDatabaseHelper = new StarbuzzDatabaseHelper(this);
            //Получить ссылку на базу данных.
            db = starbuzzDatabaseHelper.getReadableDatabase();
            //Создать курсор
            cursor = db.query("DRINK", new String[]{"_id", "NAME"}, null, null, null, null, null);
            //Создать адаптер курсора.
            //Связать содержимое столбца NAME с текстом в ListView
            CursorAdapter listAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, cursor, new String[]{"NAME"}, new int[]{android.R.id.text1}, 0);
            listDrinks.setAdapter(listAdapter);

        } catch (SQLiteException e) {
            //Вывести сообщение для пользователя, если будет выдано исключение SQLiteException.
            Toast toast = Toast.makeText(this, "Database is unavaliable", Toast.LENGTH_SHORT);
            toast.show();
        }
    }


    /**
     * База данных и курсор закрываются
     * в методе onDestroy() активности.
     * Курсор остается открытым до того
     * момента, когда он перестает быть
     * нужным адаптеру.
     */

    @Override
    public void onDestroy() {
        super.onDestroy();
        cursor.close();
        db.close();
    }

    /**
     * ListActivity содержит реализацию слушателя щелчков по умолчанию
     * ListActivity уже реализует слуша-
     * теля событий щелчков. Вместо того, чтобы создавать собственного
     * слушателя событий, при использовании списковой активности
     * достаточно реализовать метод onListItemClick().
     */

    @Override
    public void onListItemClick(ListView listView, View itemView, int position, long id) {
        Intent intent = new Intent(DrinkCategoryActivity.this, DrinkActivity.class);
        intent.putExtra(DrinkActivity.EXTRA_DRINKNO, (int) id);
        startActivity(intent);
    }

    //Метод onListItemClick() реализуется так,
    // чтобы при щелчке на варианте спискового представления запускалась активность DrinkActivity.
}
