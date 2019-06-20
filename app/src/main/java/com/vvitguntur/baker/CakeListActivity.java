package com.vvitguntur.baker;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vvitguntur.baker.Model.BakingApp;
import com.vvitguntur.baker.Model.Ingredient;
import com.vvitguntur.baker.Model.Step;
import com.vvitguntur.baker.dummy.DummyContent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * An activity representing a list of Cakes. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link CakeDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class CakeListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    @BindView(R.id.ingred_tv)
    TextView ingredients;
    private boolean mTwoPane;
    BakingApp bakingApp;
    List<Ingredient> ingredientList=new ArrayList<>();
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cake_list);
        ButterKnife.bind(this);
        sharedPreferences =getSharedPreferences("ProjectConstents",MODE_PRIVATE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        bakingApp = (BakingApp) getIntent().getParcelableExtra("Recipe");
        ingredientList=bakingApp.getIngredients();
        for(int i=0;i<ingredientList.size();i++) {
            ingredients.append("\n"+ingredientList.get(i).getIngredient()+"\t\t"+ingredientList.get(i).getQuantity()+"\t"+
                    ingredientList.get(i).getMeasure());
        }
        String string=ingredients.getText().toString();
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("recipe_ingredients",string);
        editor.putString("recipeName",bakingApp.getName());
        editor.apply();
        Intent intent = new Intent(this, IngridientWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] ids = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(
                new ComponentName(getApplication(), IngridientWidget.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        sendBroadcast(intent);
        if (findViewById(R.id.cake_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        View recyclerView = findViewById(R.id.cake_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, bakingApp, mTwoPane));
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final CakeListActivity mParentActivity;
        private final BakingApp bakingApp;

        private final boolean mTwoPane;

        SimpleItemRecyclerViewAdapter(CakeListActivity parent,
                                      BakingApp bakingApp,
                                      boolean twoPane) {
            this.bakingApp = bakingApp;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.cake_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mContentView.setText(bakingApp.getSteps().get(position).getShortDescription());
        }

        @Override
        public int getItemCount() {
            return bakingApp.getSteps().size();
        }

        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            final TextView mContentView;

            ViewHolder(View view) {
                super(view);
                mContentView = (TextView) view.findViewById(R.id.content123);
                view.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putParcelable(CakeDetailFragment.ARG_ITEM_ID,bakingApp.getSteps().get(getAdapterPosition()));
                    CakeDetailFragment fragment = new CakeDetailFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.cake_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, CakeDetailActivity.class);
                    intent.putExtra(CakeDetailFragment.ARG_ITEM_ID, bakingApp.getSteps().get(getAdapterPosition()));

                    context.startActivity(intent);
                }
            }
        }
    }
}
