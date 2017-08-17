package com.example.sun.starbuzz;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class TopLevelActivity extends Activity {

    private SQLiteDatabase db;
    private Cursor favoritesCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_level);
        //Создать OnItemClickListener
        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    //Запустить DrinkCategoryActivity, если пользователь щелкнул на варианте Drinks.
                    Intent intent = new Intent(TopLevelActivity.this, DrinkCategoryActivity.class);
                    startActivity(intent);
                }
            }
        };

        //Добавить слушателя к списковому представлению
        ListView listView = (ListView) findViewById(R.id.list_options);
        //Добавление слушателя к списковому представлению
        listView.setOnItemClickListener(itemClickListener);

        //Заполнение спискового представления list_favorites данными курсораr

        ListView listFavotites = (ListView) findViewById(R.id.list_favorites);
        try {
            SQLiteOpenHelper starbuzzDatabaseHelper = new StarbuzzDatabaseHelper(this);
            db = starbuzzDatabaseHelper.getReadableDatabase();
            //Создать курсор, содержащий значения столбцов _id и name для записей которых favorite = 1
            favoritesCursor = db.query("DRINK", new String[]{"_id", "NAME"}, "FAVORITE = 1",
                    null, null, null, null);
            //вывести названия напитков в ListView
            CursorAdapter favoriteAdapter = new SimpleCursorAdapter(TopLevelActivity.this,
                    android.R.layout.simple_list_item_1, favoritesCursor,
                    new String[]{"NAME"}, new int[]{android.R.id.text1}, 0);
            listFavotites.setAdapter(favoriteAdapter);
        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(this, "Database is unavalieble", Toast.LENGTH_SHORT);
            toast.show();
        }

        //Переход к DrinkActivity при выборе напитка
        //метод вызываеться при выборе варианта спискового представления
        listFavotites.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(TopLevelActivity.this, DrinkActivity.class);

                //Если пользователь выбирает один из вариантов спискового представления любимых напитков,
                // создать интент для запуска DrinkActivity и передать id  напитка

                intent.putExtra(DrinkActivity.EXTRA_DRINKNO, (int) id);
                startActivity(intent);
            }
        });
    }

    //Закрытие курсора и базы данных в методе onDestroy()
    @Override
    public void onDestroy() {
        super.onDestroy();
        favoritesCursor.close();
        db.close();
    }

    //Метод  вызываеться при возвращении пользователя к ТОп
    @Override
    public void onRestart(){
        super.onRestart();
        try{
            SQLiteOpenHelper starbuzzDatabaseHelper = new StarbuzzDatabaseHelper(this);
            db = starbuzzDatabaseHelper.getReadableDatabase();
            Cursor newCursor = db.query("DRINK", new String[]{"_id", "NAME"}, "FAVORITE = 1",
                    null, null, null, null);
            ListView listFavorite = (ListView) findViewById(R.id.list_favorites);
            //Получить адаптер спискового представления
            CursorAdapter adapter = (CursorAdapter) listFavorite.getAdapter();
            //Заменить курсор, используемый адаптером, на новый
            adapter.changeCursor(newCursor);
            favoritesCursor = newCursor;

        }catch (SQLiteException e){
            Toast toast = Toast.makeText(this, "Database is unavalieble", Toast.LENGTH_SHORT);
            toast.show();
        }

    }
}
