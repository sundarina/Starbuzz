package com.example.sun.starbuzz;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;


/**DrinkCategoryActivity представляет
 собой ListActivity — особую разновидность активности, пред-
 назначенную для работы со списковыми представлениями.*/
public class DrinkCategoryActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ListView listDrinks = getListView();
        //Списковое представление заполняется данными из массива drinks.
        ArrayAdapter<Drink> listAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, Drink.drinks);
        listDrinks.setAdapter(listAdapter);
    }

    /**ListActivity содержит реализацию слушателя щелчков по умолчанию
    ListActivity уже реализует слуша-
    теля событий щелчков. Вместо того, чтобы создавать собственного
    слушателя событий, при использовании списковой активности
    достаточно реализовать метод onListItemClick().*/

    @Override
    public void onListItemClick(ListView listView, View itemView, int position, long id) {
        Intent intent = new Intent(DrinkCategoryActivity.this, DrinkActivity.class);
        intent.putExtra(DrinkActivity.EXTRA_DRINKNO, (int) id);
        startActivity(intent);
    }

    //Метод onListItemClick() реализуется так,
    // чтобы при щелчке на варианте спискового представления запускалась активность DrinkActivity.
}
