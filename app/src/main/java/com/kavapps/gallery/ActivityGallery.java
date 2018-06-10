package com.kavapps.gallery;

import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import java.io.File;
import java.util.ArrayList;


public class ActivityGallery extends AppCompatActivity {

    private ArrayList<String> pathToPhoto = new ArrayList<>();
    private int counterPhotoInArray;
    private String pathToFolder;
    private ProgressBar progressBar;
    private TextView textViewLoading;
    private Button buttonNext;
    private Button buttonPrevious;
    private Button buttonDelete;
    private LoadPhotoInBackground loadPhotoInBackground;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        imageView = (ImageView) findViewById(R.id.imageView);
        buttonNext = (Button) findViewById(R.id.buttonNext);
        buttonPrevious = (Button)findViewById(R.id.buttonPrevious);
        buttonDelete = (Button) findViewById(R.id.buttonDelete);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        textViewLoading = (TextView) findViewById(R.id.textView);

        counterPhotoInArray=0;
        pathToFolder = Environment.getExternalStorageDirectory()+"/"+ ActivityCamera.folderForPhoto+"/";

        //Запускаем загругку фото в ArrayList
        loadPhotoInBackground = new LoadPhotoInBackground();
        loadPhotoInBackground.execute();

        //Обработчик кнопки "Next"
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pathToPhoto.size()==0){
                    Toast.makeText(ActivityGallery.this, "There are no photos in the gallery",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(counterPhotoInArray==pathToPhoto.size()-1){
                    counterPhotoInArray=0;
                    Picasso.get().load(new File(pathToPhoto.get(counterPhotoInArray))).fit().centerCrop().into(imageView);
                    return;
                }
                counterPhotoInArray++;
                    Picasso.get()
                            .load(new File(pathToPhoto.get(counterPhotoInArray)))
                            .fit()
                            .centerCrop()
                            .into(imageView);

            }
        });

        //Обработчик кнопки "Previous"
        buttonPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(pathToPhoto.size()==0){
                    Toast.makeText(ActivityGallery.this, "There are no photos in the gallery",Toast.LENGTH_SHORT).show();
                    return;
                }
                    if(counterPhotoInArray==0){
                        counterPhotoInArray=pathToPhoto.size()-1;
                        Picasso.get().load(new File(pathToPhoto.get(counterPhotoInArray))).fit().centerCrop().into(imageView);
                        return;
                    }
                counterPhotoInArray--;
                    Picasso.get()
                            .load(new File(pathToPhoto.get(counterPhotoInArray)))
                            .fit()
                            .centerCrop()
                            .into(imageView);
            }
        });
        //Обработчик кнопки "Delete"
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pathToPhoto.size()==0){
                    Toast.makeText(ActivityGallery.this, "There are no photos in the gallery",Toast.LENGTH_SHORT).show();
                    return;
                }

                File file = new File(pathToPhoto.get(counterPhotoInArray));
                file.delete();
                pathToPhoto.remove(counterPhotoInArray);
                Toast.makeText(ActivityGallery.this, "Photo delete",Toast.LENGTH_SHORT).show();

                if(pathToPhoto.size() ==counterPhotoInArray && counterPhotoInArray!=0){
                    counterPhotoInArray--;
                    Picasso.get().load(new File(pathToPhoto.get(counterPhotoInArray))).fit().centerCrop().into(imageView);
                    return;
                }
                if(pathToPhoto.size()==counterPhotoInArray){
                  imageView.setVisibility(View.GONE);
                    //  Toast.makeText(ActivityGallery.this,"delete last foto",Toast.LENGTH_SHORT).show();
                    return;
                }

                Picasso.get().load(new File(pathToPhoto.get(counterPhotoInArray))).fit().centerCrop().into(imageView);

            }
        });

    }


    class LoadPhotoInBackground extends AsyncTask<Void,Void,Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            textViewLoading.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected Void doInBackground(Void... voids) {

            //сохраняем абсолютный путь к файлам в ArrayList
            File []fList;
            File F = new File(pathToFolder);
            fList = F.listFiles();

            for(int i=0; i<fList.length; i++)
            {
                if(fList[i].isFile())
                    pathToPhoto.add(pathToFolder+ fList[i].getName());
            }


            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            textViewLoading.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
            if(pathToPhoto.size()>0){
                Picasso.get().load(new File(pathToPhoto.get(0))).fit().centerCrop().into(imageView);
            }

        }
    }

}
