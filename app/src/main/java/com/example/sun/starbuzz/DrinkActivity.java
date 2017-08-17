package com.example.sun.starbuzz;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.Image;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.IntegerRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DrinkActivity extends Activity {

    public static final String EXTRA_DRINKNO = "drinkNo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink);

        //Получение напитка из интента
        int drinkNo = (Integer) getIntent().getExtras().get(EXTRA_DRINKNO);

        // Drink drink = Drink.drinks[drinkNo];
        //Использовать drinkNo
        //для получения подробной информации о напитке, выбранном пользователем.

        //Cоздание курсора
        //Чтобы обновить базу данных, необходимо получить доступ к ней для чтения/записи

        try {
            SQLiteOpenHelper starbuzzDatabaseHelper = new StarbuzzDatabaseHelper(this);
            SQLiteDatabase db = starbuzzDatabaseHelper.getWritableDatabase();

            //Создать курсор для получения
            // из таблицы DRINK столбцов NAME, DESCRIPTION и IMAGE_RESOURCE_ID тех записей,
            // у которых значение _id равно drinkNo

            Cursor cursor = db.query("DRINK", new String[]{"NAME", "DESCRIPTION", "IMAGE_RESOURCE_ID", "FAVORITE"}, "_id = ?", new String[]{Integer.toString(drinkNo)}, null, null, null);
            //Курсор содержит одну запись , но и в этом случае переход необходим
            if (cursor.moveToFirst()) {
                String nameText = cursor.getString(0);
                String descriptionText = cursor.getString(1);
                int photoId = cursor.getInt(2);
                boolean isFavorite = (cursor.getInt(3) == 1);
                //Заполнение названия напитка
                TextView name = (TextView) findViewById(R.id.name);
                name.setText(nameText);
                //Заполнение описания напитка
                TextView description = (TextView) findViewById(R.id.description);
                description.setText(descriptionText);
                //Заполнение изображения напитка

                ImageView photo = (ImageView) findViewById(R.id.photo);
                photo.setImageResource(photoId);
                photo.setContentDescription(nameText);

                //Заполнение флажка любимого напитка

                CheckBox favorite = (CheckBox) findViewById(R.id.favorite);
                favorite.setChecked(isFavorite);
            }
            //Закрыть курсор и базу данных.
            cursor.close();
            db.close();
        } catch (SQLiteException e) {

            //Если будет выдано исключение SQLiteException,
            // значит, при работе с базой данных возникли проблемы.
            // В этом случае объект Toast используется для выдачи сообщения для пользователя
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }

        /** //Заполнение изображения напитка из файла Drink
         ImageView photo = (ImageView) findViewById(R.id.photo);
         // Источник данных графического поля назначается вызовом setImageResource()
         photo.setImageResource(drink.getImageResourceId());
         //Необходимо для повішения уровня доступности приложения
         photo.setContentDescription(drink.getName());
         //Заполнение названия напитка
         TextView name = (TextView) findViewById(R.id.name);
         name.setText(drink.getName());
         //Заполнение описания напитка
         TextView description = (TextView) findViewById(R.id.description);
         description.setText(drink.getDescription());*/


    }

    //Обновление базы данных по щелчку на флажке
    public void onFavoriteClicked(View view) {
        int drinkNo = (Integer) getIntent().getExtras().get("drinkNo");
//        CheckBox favorite = (CheckBox) findViewById(R.id.favorite);
//        ContentValues drinkValues = new ContentValues();
//        //Значение флажка добавляється в обьект ContentValues  с именем drinkValues
//        drinkValues.put("FAVORITE", favorite.isChecked());
//        SQLiteOpenHelper starbuzzDatabaseHelper = new StarbuzzDatabaseHelper(DrinkActivity.this);
//
//        try {
//            SQLiteDatabase db = starbuzzDatabaseHelper.getWritableDatabase();
//            //Обновить столбец Favorite текущим значением флажка
//            db.update("DRINK", drinkValues, "_id = ?", new String[]{Integer.toString(drinkNo)});
//            db.close();
//    } catch(SQLiteException e) {
//        Toast toast = Toast.makeText(this, "Database is unavaliable", Toast.LENGTH_SHORT);
//        toast.show();
//    }
        new UpdateDrinkTask().execute(drinkNo);
    }

    //Внутренний класс для обновления напитка.AsyncTask добавляется в активность в виде внутреннего класса.
    private class UpdateDrinkTask extends AsyncTask<Integer, Void, Boolean> {
        ContentValues drinkValues;

        /**
         * Прежде чем запускать код базы данных
         * на выполнение, добавить значение флажка
         * в объект ContentValues с именем drinkValues.
         */

        protected void onPreExecute() {
            CheckBox favorite = (CheckBox) findViewById(R.id.favorite);
            drinkValues = new ContentValues();
            drinkValues.put("FAVORITE", favorite.isChecked());
        }

        /**
         * Код базы данных запу-
         * скается на выполнение
         * в фоновом потоке.
         */
        protected Boolean doInBackground(Integer... drinks) {
            int drinkNo = drinks[0];
            SQLiteOpenHelper starbuzzDatabaseHelper = new StarbuzzDatabaseHelper(DrinkActivity.this);

            try {
                SQLiteDatabase db = starbuzzDatabaseHelper.getWritableDatabase();
                //Обновить столбец Favorite текущим значением флажка
                db.update("DRINK", drinkValues, "_id = ?", new String[]{Integer.toString(drinkNo)});
                db.close();
                return true;
            } catch (SQLiteException e) {
                return false;
            }
        }


        /**
         * Eсли при выполнении кода базы
         * данных возникли проблемы,
         * вывести сообщение для пользователя.
         */

        protected void onPostExecute(Boolean success) {
            if (!success) {
                Toast toast = Toast.makeText(DrinkActivity.this, "Database is unavaliable", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }
}
