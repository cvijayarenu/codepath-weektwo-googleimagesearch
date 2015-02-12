package ru.chand.googleimagesearch.adaptor;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import ru.chand.googleimagesearch.R;
import ru.chand.googleimagesearch.model.Photo;

/**
 * Created by chandrav on 2/10/15.
 */
public class PhotoAdaptor extends ArrayAdapter<Photo> {


    public PhotoAdaptor(Context context, List<Photo> photos) {
        super(context, R.layout.item_photo, photos);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        Photo photo = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent, false);
        }

        ImageView ivPhoto = (ImageView) convertView.findViewById(R.id.ivPhoto);
        TextView tvPhotoText = (TextView) convertView.findViewById(R.id.tvPhotoText);

        ivPhoto.setImageResource(0);
        tvPhotoText.setText(Html.fromHtml(photo.title));
        Picasso.with(getContext()).load(photo.tbUrl).into(ivPhoto);

        return convertView;

    }
}
