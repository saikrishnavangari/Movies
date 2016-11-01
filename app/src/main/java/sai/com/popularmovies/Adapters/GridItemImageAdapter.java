package sai.com.popularmovies.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import sai.com.popularmovies.MainActivity;
import sai.com.popularmovies.Model.Movies.results;
import sai.com.popularmovies.R;

/**
 * Created by krrish on 1/11/2016.
 */

public class GridItemImageAdapter extends ArrayAdapter<results> {
    private Context mcontext;
    private List<results> mMoviesList;

    public GridItemImageAdapter(Context context, int resource, List<results> list) {
        super(context, resource, list);
        mcontext=context;
        mMoviesList=list;
    }


    @Override
    public int getCount() {
        return mMoviesList.size();
    }


    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View listItemView=view;
        if(listItemView==null){
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.griditemview, viewGroup, false);
        }

        ImageView moviePoster= (ImageView) listItemView.findViewById(R.id.image_id);
        //load image into imageView
        Picasso.with(mcontext).load(MainActivity.IMAGE_BASE_URL+mMoviesList.get(i).getPoster_path()).into(moviePoster);
        return listItemView;
    }
}

