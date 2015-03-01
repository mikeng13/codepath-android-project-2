package com.mike.project_2.activities;

import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.etsy.android.grid.StaggeredGridView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mike.project_2.R;
import com.mike.project_2.adapters.ImagesAdapter;
import com.mike.project_2.dialogs.ImageDetailDialog;
import com.mike.project_2.dialogs.ImageFiltersDialog;
import com.mike.project_2.helpers.EndlessScrollListener;
import com.mike.project_2.helpers.GoogleImageSearchClient;
import com.mike.project_2.helpers.Network;
import com.mike.project_2.models.Image;
import com.mike.project_2.models.SearchOptions;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class GridImageSearcherActivity extends ActionBarActivity implements ImageFiltersDialog.ImageFiltersDialogListener {
    private StaggeredGridView gvImages;
    private ArrayList<Image> images;
    private ImagesAdapter imagesAdapter;
    private SearchOptions searchOptions;
    private boolean isFetching;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_image_searcher);

        images = new ArrayList<Image>();
        imagesAdapter = new ImagesAdapter(this, images);

        gvImages = (StaggeredGridView)findViewById(R.id.gvImageGrid);
        gvImages.setAdapter(imagesAdapter);
        gvImages.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                submitQuery(false);
            }
        });
        gvImages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showImageDetailDialog(imagesAdapter.getItem(position));
            }
        });

        searchOptions = new SearchOptions("");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_grid_image_searcher, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // perform query here
                searchOptions.searchTerm = query;
                submitQuery(true);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_filters:
                showFiltersDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     *
     * @param isNewQuery
     */
    private void submitQuery(final boolean isNewQuery) {
        if (!Network.isNetworkAvailable(this)) {
            Toast.makeText(getApplicationContext(), "No internet! Try again later...", Toast.LENGTH_SHORT).show();
        }
        if (isFetching) {
            return;
        }
        GoogleImageSearchClient.searchImages(searchOptions, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            isFetching = false;
            try {
                if (isNewQuery) {
                    searchOptions.start = "0";
                    imagesAdapter.clear();
                }

                JSONObject cursorJSON = response.getJSONObject("responseData").getJSONObject("cursor");
                int currentPage = cursorJSON.getInt("currentPageIndex");

                JSONArray pages = cursorJSON.getJSONArray("pages");
                if ((pages.length() - 1) > currentPage) {
                    JSONObject page = pages.getJSONObject(currentPage + 1);
                    searchOptions.start = page.getString("start");
                } else if (!isNewQuery) {
                    // stop searching once we have reached the end
                    Toast.makeText(getApplicationContext(), "No more results for this search!", Toast.LENGTH_SHORT).show();
                    return;
                }

                JSONArray imagesJSON = response.getJSONObject("responseData").getJSONArray("results");
                for (int i = 0; i < imagesJSON.length(); i++) {
                    JSONObject imageJSON = imagesJSON.getJSONObject(i);
                    Image image = new Image();
                    image.title = imageJSON.getString("title");
                    image.content = imageJSON.getString("content");
                    image.imageUrl = imageJSON.getString("url");
                    image.thumbnailUrl = imageJSON.getString("tbUrl");
                    image.originalContextUrl = imageJSON.getString("originalContextUrl");
                    images.add(image);
                }
                imagesAdapter.notifyDataSetChanged();

                if (imagesAdapter.getCount() < 12) {
                    submitQuery(false);
                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Search failed. Try again.", Toast.LENGTH_SHORT).show();
            }
            }
        });
    }

    private void showFiltersDialog() {
        FragmentManager fm = getSupportFragmentManager();
        ImageFiltersDialog imageFiltersDialog = ImageFiltersDialog.newInstance("Advanced Filters",
                searchOptions);
        imageFiltersDialog.show(fm, "fragment_image_filters");
    }

    private void showImageDetailDialog(Image image) {
        FragmentManager fm = getSupportFragmentManager();
        ImageDetailDialog imageDetailDialog = ImageDetailDialog.newInstance(image);
        imageDetailDialog.show(fm, "frament_image_detail");
    }

    @Override
    public void onFinishImageFiltersDialog(SearchOptions searchOptions) {
        this.searchOptions = searchOptions;
        submitQuery(true);
    }
}
