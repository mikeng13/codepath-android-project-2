package com.mike.project_2.dialogs;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.mike.project_2.R;
import com.mike.project_2.models.SearchOptions;

import java.lang.reflect.Array;

/**
 * Created by mng on 2/27/15.
 */
public class ImageFiltersDialog extends DialogFragment implements AdapterView.OnItemSelectedListener{

    private SearchOptions searchOptions;

    public interface ImageFiltersDialogListener {
        void onFinishImageFiltersDialog(SearchOptions searchOptions);
    }

    public ImageFiltersDialog() {}

    public static ImageFiltersDialog newInstance(String title, SearchOptions searchOptions) {
        ImageFiltersDialog frag = new ImageFiltersDialog();
        frag.setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_ImageFilters);
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putParcelable("searchOptions", searchOptions);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_filters, container, false);

        String title = getArguments().getString("title", "Advanced Filters");
        TextView tvImageTitle = (TextView)view.findViewById(R.id.tvImageFilterDialogTitle);
        tvImageTitle.setText(title);

        searchOptions = getArguments().getParcelable("searchOptions");

        // set up spinners and their default selected values
        Spinner spImageSize = (Spinner)view.findViewById(R.id.spImageSize);
        ArrayAdapter<CharSequence> imageSizesAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.image_size, android.R.layout.simple_spinner_item);
        imageSizesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spImageSize.setAdapter(imageSizesAdapter);
        spImageSize.setSelection(imageSizesAdapter.getPosition(searchOptions.imageSize));
        spImageSize.setOnItemSelectedListener(this);

        Spinner spColorFilter = (Spinner)view.findViewById(R.id.spColorFilter);
        ArrayAdapter<CharSequence> imageColorsAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.image_color, android.R.layout.simple_spinner_item);
        imageColorsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spColorFilter.setAdapter(imageColorsAdapter);
        spColorFilter.setSelection(imageColorsAdapter.getPosition(searchOptions.colorFilter));
        spColorFilter.setOnItemSelectedListener(this);

        Spinner spImageType = (Spinner)view.findViewById(R.id.spImageType);
        ArrayAdapter<CharSequence> imageTypes = ArrayAdapter.createFromResource(getActivity(),
                R.array.image_type, android.R.layout.simple_spinner_item);
        imageTypes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spImageType.setAdapter(imageTypes);
        spImageType.setSelection(imageTypes.getPosition(searchOptions.imageType));
        spImageType.setOnItemSelectedListener(this);

        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        // set up button click listeners
        Button btnSave = (Button)view.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageFiltersDialogListener listener = (ImageFiltersDialogListener) getActivity();
                listener.onFinishImageFiltersDialog(searchOptions);
                dismiss();
            }
        });

        Button btnCancel = (Button)view.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedValue = (String)parent.getItemAtPosition(position);
        switch (parent.getId()) {
            case R.id.spImageSize:
                searchOptions.imageSize = selectedValue;
                break;
            case R.id.spColorFilter:
                searchOptions.colorFilter = selectedValue;
                break;
            case R.id.spImageType:
                searchOptions.imageType = selectedValue;
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
