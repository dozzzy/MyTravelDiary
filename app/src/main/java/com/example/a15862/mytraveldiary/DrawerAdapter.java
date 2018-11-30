package com.example.a15862.mytraveldiary;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**定义菜单项类*/
class MenuItem {
    String menuTitle ;
    int menuIcon ;

    //构造方法
    public MenuItem(String menuTitle){
        this.menuTitle = menuTitle ;
    }
    public MenuItem(String menuTitle , int menuIcon ){
        this.menuTitle = menuTitle ;
        this.menuIcon = menuIcon ;
    }

}
/**自定义设置侧滑菜单ListView的Adapter*/
public class DrawerAdapter extends BaseAdapter{

    //存储侧滑菜单中的各项的数据
    List<MenuItem> MenuItems = new ArrayList<MenuItem>( ) ;
    //构造方法中传过来的activity
    Context context ;

    //构造方法
    public DrawerAdapter( Context context ){

        this.context = context ;

        MenuItems.add(new MenuItem("History")) ;
        MenuItems.add(new MenuItem("Friends")) ;
        MenuItems.add(new MenuItem("Setting")) ;
        MenuItems.add(new MenuItem("Login")) ;
        MenuItems.add(new MenuItem("Logout")) ;
        //TODO: 要美化的话加图标
    }

    @Override
    public int getCount() {

        return MenuItems.size();

    }

    @Override
    public MenuItem getItem(int position) {

        return MenuItems.get(position) ;
    }

    @Override
    public long getItemId(int position) {

        return position ;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView ;
        if(view == null){
            view =LayoutInflater.from(context).inflate(R.layout.menudrawer_item, parent, false);
            ((TextView) view).setText(getItem(position).menuTitle) ;
            ((TextView) view).setCompoundDrawablesWithIntrinsicBounds(getItem(position).menuIcon, 0, 0, 0) ;
        }
        return view ;
    }

//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        return null;
//    }
}