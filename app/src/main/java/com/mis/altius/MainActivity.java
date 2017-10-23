package com.mis.altius;

import android.animation.Animator;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.example.historyprovider_library.WordContract;
import com.github.andreilisun.swipedismissdialog.OnSwipeDismissListener;
import com.github.andreilisun.swipedismissdialog.SwipeDismissDialog;
import com.github.andreilisun.swipedismissdialog.SwipeDismissDirection;
import com.mis.altius.adapter.CustomAdapter;
import com.mis.altius.adapter.SearchResult;
import com.mis.altius.adapter.SearchResultAdapter;
import com.mis.altius.adapter.SimpleAnimationListener;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;

import org.cryse.widget.persistentsearch.PersistentSearchView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener{
    private PersistentSearchView mSearchView;
    private View mSearchTintView;
    private RecyclerView mRecyclerView;
    private SearchResultAdapter mResultAdapter;

    private SliderLayout mDemoSlider;
    TextView t1, t2, t3, t4;
    EditText et1, et2, et3, et4;
    BoomMenuButton bmb;
    private CustomAdapter adapter;
    private ArrayList<Pair> piecesAndButtons = new ArrayList<>();
    protected ArrayList<MyData> data_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDemoSlider = (SliderLayout)findViewById(R.id.slider);
        mSearchView = (PersistentSearchView) findViewById(R.id.searchview);
        mSearchTintView = findViewById(R.id.view_search_tint);
        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerview_search_result);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mResultAdapter = new SearchResultAdapter(new ArrayList<SearchResult>());
        mRecyclerView.setAdapter(mResultAdapter);
        //Thread.setDefaultUncaughtExceptionHandler(new UncaughtException(MainActivity.this));
        mSearchTintView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchView.cancelEditing();
            }
        });
        mSearchView.setSuggestionBuilder(new SampleSuggestionsBuilder(this));
        mSearchView.setSearchListener(new PersistentSearchView.SearchListener() {
            @Override
            public void onSearchEditOpened() {
                //Use this to tint the screen
                mSearchTintView.setVisibility(View.VISIBLE);
                mSearchTintView
                        .animate()
                        .alpha(1.0f)
                        .setDuration(300)
                        .setListener(new SimpleAnimationListener())
                        .start();
            }

            @Override
            public void onSearchEditClosed() {
                mSearchTintView
                        .animate()
                        .alpha(0.0f)
                        .setDuration(300)
                        .setListener(new SimpleAnimationListener() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                mSearchTintView.setVisibility(View.GONE);
                            }
                        })
                        .start();
            }

            @Override
            public boolean onSearchEditBackPressed() {
                return false;
            }

            @Override
            public void onSearchExit() {
                mResultAdapter.clear();
                if(mRecyclerView.getVisibility() == View.VISIBLE) {
                    mRecyclerView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onSearchTermChanged(String term) {

            }

            @Override
            public void onSearch(String nama) {
                String angkatan = "";
                String kota_kantor = "";
                String perusahaan = "";
                String jabatan = "";

                String name = nama;
                ContentValues values = new ContentValues();
                values.put(WordContract.WordEntry.COLUMN_WORD_NAME,name);
                Uri newUri = getContentResolver().insert(WordContract.WordEntry.CONTENT_URI, values);

                Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
                intent.putExtra("nama", nama);
                intent.putExtra("kota_kantor", kota_kantor);
                intent.putExtra("perusahaan", perusahaan);
                intent.putExtra("jabatan", jabatan);
                intent.putExtra("angkatan", angkatan);
                startActivity(intent);
            }

//            @Override
//            public boolean onSuggestion(SearchItem searchItem) {
//                Log.d("onSuggestion", searchItem.getTitle());
//                return false;
//            }

            @Override
            public void onSearchCleared() {
                //Called when the clear button is clicked
            }

        });

        TextView tvVersion = (TextView) findViewById(R.id.tvVersion);
        String versionNumber = "";
        try {
            versionNumber = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        tvVersion.setText(String.format("Version %s", versionNumber));


        bmb = (BoomMenuButton) findViewById(R.id.bmb);
        bmb.setButtonEnum(ButtonEnum.TextOutsideCircle);
        bmb.setPiecePlaceEnum(PiecePlaceEnum.DOT_3_1);
        bmb.setButtonPlaceEnum(ButtonPlaceEnum.SC_3_3);

        for (int i = 0; i < bmb.getButtonPlaceEnum().buttonNumber(); i++) {
            bmb.addBuilder(BuilderManager.getTextOutsideCircleButtonBuilder().listener(new OnBMClickListener() {
                @Override
                public void onBoomButtonClick(int index) {
                    if (index==0){
                        Toast.makeText(MainActivity.this, "Favorite", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
                        startActivity(intent);
                    }
                    else if (index==1){
                        Toast.makeText(MainActivity.this, "History", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), HistoryActivity.class);
                        startActivity(intent);
                    }
                    else if (index==2){
                        Toast.makeText(MainActivity.this, "About App", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), AboutActivity.class);
                        startActivity(intent);
                    }
                }
            }));
        }

//        HashMap<String,String> url_maps = new HashMap<String, String>();
//        url_maps.put("Hannibal", "http://static2.hypable.com/wp-content/uploads/2013/12/hannibal-season-2-release-date.jpg");
//        url_maps.put("Big Bang Theory", "http://tvfiles.alphacoders.com/100/hdclearart-10.png");
//        url_maps.put("House of Cards", "http://cdn3.nflximg.net/images/3093/2043093.jpg");
//        url_maps.put("Game of Thrones", "http://images.boomsbeat.com/data/images/full/19640/game-of-thrones-season-4-jpg.jpg");

        HashMap<String,Integer> file_maps = new HashMap<String, Integer>();
        file_maps.put("ALTIUS",R.drawable.altius);
        file_maps.put("HMTI ITS",R.drawable.hmti);

        for(String name : file_maps.keySet()){
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra",name);
            mDemoSlider.addSlider(textSliderView);
        }
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Default);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(3000);
        mDemoSlider.addOnPageChangeListener(this);

        t1 = (TextView) findViewById(R.id.tahun);
        t2 = (TextView) findViewById(R.id.kota_kantor);
        t3 = (TextView) findViewById(R.id.kantor);
        t4 = (TextView) findViewById(R.id.jabatan);
        et1 = (EditText) findViewById(R.id.et_tahun);
        et2 = (EditText) findViewById(R.id.et_kota_kantor);
        et3 = (EditText) findViewById(R.id.et_kantor);
        et4 = (EditText) findViewById(R.id.et_jabatan);
        Button bt1 = (Button) findViewById(R.id.btn_submit);

        Typeface font= Typeface.createFromAsset(getAssets(), "helvetica-normal.ttf");
        Typeface fontet= Typeface.createFromAsset(getAssets(), "helvetica-normal.ttf");
        Typeface button= Typeface.createFromAsset(getAssets(), "helvetica-normal.ttf");

        t1.setTypeface(font);
        t2.setTypeface(font);
        t3.setTypeface(font);
        t4.setTypeface(font);
        et1.setTypeface(fontet);
        et2.setTypeface(fontet);
        et3.setTypeface(fontet);
        et4.setTypeface(fontet);
        bt1.setTypeface(button);

        data_list = new ArrayList<>();
        adapter = new CustomAdapter(this, data_list);

        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nama = "";
                String angkatan = et1.getText().toString();
                String kota_kantor = et2.getText().toString();
                String perusahaan = et3.getText().toString();
                String jabatan = et4.getText().toString();
                String name = angkatan;
                ContentValues values = new ContentValues();
                values.put(WordContract.WordEntry.COLUMN_WORD_NAME,name);
                Uri newUri = getContentResolver().insert(WordContract.WordEntry.CONTENT_URI, values);

                if( angkatan.trim().equalsIgnoreCase("") && kota_kantor.trim().equalsIgnoreCase("")&&
                        perusahaan.trim().equalsIgnoreCase("") && jabatan.trim().equalsIgnoreCase("")){
                    new SwipeDismissDialog.Builder(MainActivity.this)
                            .setLayoutResId(R.layout.dialog_reminder)
                            .setOnSwipeDismissListener(new OnSwipeDismissListener() {
                                @Override
                                public void onSwipeDismiss(View view, SwipeDismissDirection direction) {

                                }
                            })
                            .setOverlayColor(Color.parseColor("#80FF0000"))
                            .build()
                            .show();
                }
                else{
                    Context context = view.getContext();
                    Intent intent = new Intent(context, ResultActivity.class);
                    intent.putExtra("nama", nama);
                    intent.putExtra("angkatan", angkatan);
                    intent.putExtra("kota_kantor", kota_kantor);
                    intent.putExtra("perusahaan", perusahaan);
                    intent.putExtra("jabatan", jabatan);
                    context.startActivity(intent);

                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void fillResultToRecyclerView(String query) {
        List<SearchResult> newResults = new ArrayList<>();
        for(int i =0; i< 10; i++) {
            SearchResult result = new SearchResult(query, query + Integer.toString(i), "");
            newResults.add(result);
        }
        mResultAdapter.replaceWith(newResults);
    }

    @Override
    protected void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        mDemoSlider.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        Toast.makeText(this,slider.getBundle().get("extra") + "",Toast.LENGTH_SHORT).show();
        if (slider.getBundle().get("extra").equals("ALTIUS")){
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.addCategory(Intent.CATEGORY_BROWSABLE);
            intent.setData(Uri.parse("https://www.facebook.com/Altius-ITS-259336927751367/"));
            startActivity(intent);
        }
        else {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.addCategory(Intent.CATEGORY_BROWSABLE);
            intent.setData(Uri.parse("https://www.instagram.com/hmti_its/"));
            startActivity(intent);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageSelected(int position) {
        Log.d("Slider Demo", "Page Changed: " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {}

    @Override
    public void onBackPressed() {
        View dialog = LayoutInflater.from(this).inflate(R.layout.dialog_exit, null);
        final SwipeDismissDialog swipeDismissDialog = new SwipeDismissDialog.Builder(this)
                .setView(dialog)
                .build()
                .show();
        Button exitButton = dialog.findViewById(R.id.btn_exit);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swipeDismissDialog.dismiss();
                MainActivity.this.finish();
                System.exit(0);
            }
        });
    }
}