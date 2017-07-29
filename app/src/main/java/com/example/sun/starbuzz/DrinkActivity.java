package com.example.sun.starbuzz;

import android.app.Activity;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class DrinkActivity extends Activity {

    public static final String EXTRA_DRINKNO = "drinkNo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink);

        int drinkNo = (Integer) getIntent().getExtras().get(EXTRA_DRINKNO);
        Drink drink = Drink.drinks[drinkNo];
        //Использовать drinkNo
        //для получения подробной информации о напитке, выбранном пользователем.


        //Заполнение изображения напитка

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
        description.setText(drink.getDescription());


    }
}
